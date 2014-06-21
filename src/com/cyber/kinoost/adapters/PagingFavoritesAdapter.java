package com.cyber.kinoost.adapters;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cyber.kinoost.R;
import com.cyber.kinoost.adapters.OstListViewAdapter.ViewHolder;
import com.cyber.kinoost.api.ApiHelper;
import com.cyber.kinoost.db.models.*;
import com.cyber.kinoost.db.repositories.*;
import com.cyber.kinoost.paging.listview.PagingBaseAdapter;

public class PagingFavoritesAdapter extends PagingBaseAdapter<Music> {
	
	private Context context;
	final ApiHelper apiHelper = new ApiHelper();
	private FavoritesRepository favRepo;
	
	public PagingFavoritesAdapter(Context c) {
		this.context = c;
		this.favRepo = new FavoritesRepository(c);
	}
	
	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Music getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View row = convertView;
		final ViewHolder holder;
		if (row == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			row = inflater.inflate(R.layout.ost_row, parent, false);
			holder = new ViewHolder();
			holder.image = (ImageView) row.findViewById(R.id.image);
			holder.btnFav = (ImageView) row.findViewById(R.id.btnFav);
			holder.name = (TextView) row.findViewById(R.id.name);
			row.setTag(holder);
			RelativeLayout musicRow = (RelativeLayout) row;
			
			musicRow.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Log.i("ListAdapter", getItem(position).getName());
					
					String imgUrl = "";
					
					try {
						FilmRepository filmRepo = new FilmRepository(context);
						List<Film> film = filmRepo.lookupFilmsForMusic(getItem(position));
						if(film.size() > 0) {
							imgUrl = film.get(0).getImgUrl();
						}
					} catch (SQLException e) {
						imgUrl = "";
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					apiHelper.startPlayerFragment(context, getItem(position),
							imgUrl, (ArrayList<Music>) items, position);

				}
			});
			
			/**
			 * Remove song from favorites
			 **/
			holder.btnFav.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Music m = getItem(position);
					favRepo.deleteFavorites(m);
					items.remove(m);
					holder.btnFav.setImageResource(R.drawable.img_btn_fav);
				}
			});

		} else
			holder = (ViewHolder) row.getTag();		
		Music song = getItem(position);
		Performer performer = song.getPerformer();
		String performerName = (performer != null && performer.getName() != null) ? performer.getName() + " - " : "";
		holder.name.setText(performerName + song.getName());
		holder.name.setSelected(true);
		holder.image.setImageResource(R.drawable.music);
		holder.btnFav.setImageResource(R.drawable.img_btn_fav_pressed);
		
		return row;
		
	}

}
