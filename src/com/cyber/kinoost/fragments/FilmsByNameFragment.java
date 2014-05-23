package com.cyber.kinoost.fragments;

import java.sql.SQLException;
import java.util.List;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cyber.kinoost.R;
import com.cyber.kinoost.PagingGridView.PagingGridView;
import com.cyber.kinoost.adapters.PagingAdaper;
import com.cyber.kinoost.api.tasks.SafeAsyncTask;
import com.cyber.kinoost.db.models.Film;
import com.cyber.kinoost.db.repositories.FilmMusicRepository;

public class FilmsByNameFragment extends Fragment {
    
    public FilmsByNameFragment() {
        // Empty constructor required for fragment subclasses
    }    
    
    private ProgressDialog loadingDialog;
    
    private PagingGridView gridView;
    
    private String filmname = "";
    
    private int pager = 0;
    
    private boolean noFilmsLeft = false;
    
    private final static int FILMS_PER_PAGE = 30;
    
    public void createProgressDialog() {
		loadingDialog = new ProgressDialog(getActivity());
		loadingDialog.setIndeterminate(true);
		loadingDialog.setMessage(getString(R.string.loading_films));
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
    	Bundle bundle = getArguments();   	
    	if (bundle != null && bundle.containsKey("filmName"))
    		filmname = getArguments().getString("filmName");
    	
     	View myFragmentView = inflater.inflate(R.layout.fragment_paging_grid, container, false);
    	gridView = (PagingGridView) myFragmentView.findViewById(R.id.gridview);
    	
    	createProgressDialog();
    	
        gridView.setHasMoreItems(true);
        gridView.setPagingableListener(new PagingGridView.Pagingable() {
            @Override
            public void onLoadMoreItems() {
                if (!noFilmsLeft) {
                	Log.i("loader", "new films");
                    new FilmAsyncTask(false).execute();
                } else {
                    gridView.onFinishLoading(false, null);
                }
            }
        });
        new FilmAsyncTask(true).execute();
        
        return myFragmentView;
    }
    
    private List<Film> getFilms(int offset) {
    	FilmMusicRepository filmMusicRepo = new FilmMusicRepository(getActivity());
    	List<Film> films = null;
    	try {
			films = filmMusicRepo.findFilmByName(filmname, offset, FILMS_PER_PAGE);
			Log.d("SIZE", Integer.toString(films.size()));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return films;
    } 
    
    
    private class FilmAsyncTask extends SafeAsyncTask<List<Film>> {
		private boolean showLoading;
		public FilmAsyncTask(boolean showLoading) {
			this.showLoading = showLoading;
		}

		@Override
		protected void onPreExecute() throws Exception {
			super.onPreExecute();
			if(showLoading) {
				loadingDialog.show();
			}
		}

		@Override
		public List<Film> call() throws Exception {
			List<Film> result = getFilms(pager * FILMS_PER_PAGE);
			if (result.size() < FILMS_PER_PAGE) 
				noFilmsLeft = true;
			Thread.sleep(1000);
			return result;
		}

		@Override
		protected void onSuccess(List<Film> newItems) throws Exception {
			super.onSuccess(newItems);
			pager++;
			PagingAdaper adapter = new PagingAdaper(getActivity());
			if(gridView.getAdapter() == null) {
				gridView.setAdapter(adapter);
			}
			gridView.onFinishLoading(true, newItems);
		}

		@Override
		protected void onFinally() throws RuntimeException {
			super.onFinally();
			if(loadingDialog.isShowing()) {
				loadingDialog.dismiss();
			}
		}
	}

}
