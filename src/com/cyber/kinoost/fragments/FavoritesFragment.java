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
import com.cyber.kinoost.adapters.PagingFavoritesAdapter;
import com.cyber.kinoost.adapters.PagingMusicAdapter;
import com.cyber.kinoost.api.tasks.SafeAsyncTask;
import com.cyber.kinoost.db.models.Favorites;
import com.cyber.kinoost.db.models.Music;
import com.cyber.kinoost.db.repositories.FavoritesRepository;
import com.cyber.kinoost.db.repositories.MusicRepository;
import com.cyber.kinoost.paging.listview.PagingListView;

public class FavoritesFragment extends Fragment {
	
	private static final String TAG = FavoritesFragment.class.getSimpleName();
    
    public FavoritesFragment() {
        // Empty constructor required for fragment subclasses
    }    
    
    private ProgressDialog loadingDialog;
    
    private PagingListView listView;
    
    private String songname = "";
    
    private int pager = 0;
    
    private boolean noMusicLeft = false;
    
    private final static int MUSIC_PER_PAGE = 15;
    
    public void createProgressDialog() {
		loadingDialog = new ProgressDialog(getActivity());
		loadingDialog.setIndeterminate(true);
		loadingDialog.setMessage(getString(R.string.loading_films));
	}
    

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
    	Bundle bundle = getArguments();   	
    	if (bundle != null && bundle.containsKey("songName"))
    		songname = getArguments().getString("songName");
    	
     	View myFragmentView = inflater.inflate(R.layout.fragment_ost, container, false);
    	listView = (PagingListView) myFragmentView.findViewById(R.id.listView);
    	
    	createProgressDialog();
    	
        listView.setHasMoreItems(true);
        listView.setPagingableListener(new PagingListView.Pagingable() {
            @Override
            public void onLoadMoreItems() {
                if (!noMusicLeft) {
                	Log.i("loader", "new films");
                    new MusicAsyncTask(false).execute();
                } else {
                    listView.onFinishLoading(false, null);
                }
            }
        });
        new MusicAsyncTask(true).execute();
        
        return myFragmentView;
    }
    
	private List<Favorites> getFavorites(int offset) {
    	FavoritesRepository favoritesRepo = new FavoritesRepository(getActivity().getBaseContext());
    	List<Favorites> favorites = null;
    	try {
			favorites = favoritesRepo.getFavorites(offset, MUSIC_PER_PAGE);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return favorites;
    } 
    
    
    private class MusicAsyncTask extends SafeAsyncTask<List<Favorites>> {
		private boolean showLoading;
		public MusicAsyncTask(boolean showLoading) {
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
		public List<Favorites> call() throws Exception {
			List<Favorites> result = getFavorites(pager * MUSIC_PER_PAGE);
			if (result.size() < MUSIC_PER_PAGE) 
				noMusicLeft = true;
			Thread.sleep(1000);
			return result;
		}

		@Override
		protected void onSuccess(List<Favorites> newItems) throws Exception {
			super.onSuccess(newItems);
			pager++;
			PagingFavoritesAdapter adapter = new PagingFavoritesAdapter(getActivity());
			if(listView.getAdapter() == null) {
				listView.setAdapter(adapter);
			}
			listView.onFinishLoading(true, newItems);
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
