package com.cyber.kinoost.adapters;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
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
import com.cyber.kinoost.db.models.Film;
import com.cyber.kinoost.db.models.Music;
import com.cyber.kinoost.db.models.Performer;
import com.cyber.kinoost.db.models.User;
import com.cyber.kinoost.db.repositories.FavoritesRepository;
import com.cyber.kinoost.db.repositories.FilmRepository;
import com.cyber.kinoost.db.repositories.UserRepository;
import com.cyber.kinoost.paging.listview.PagingBaseAdapter;

public class PagingMusicAdapter extends PagingBaseAdapter<Music> {
	
	private Context context;
	final ApiHelper apiHelper = new ApiHelper();
	private FavoritesRepository favRepo;
	private UserRepository userRepo;
	private List<String> musicFavoritesIds;
	private User user;
	
	public PagingMusicAdapter(Context c) {
		this.context = c;
		
		this.favRepo = new FavoritesRepository(c);
		this.userRepo = new UserRepository(c);
        this.user = userRepo.getUser();
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
			try {
				this.musicFavoritesIds = favRepo.getFavoritesIds(items);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			LayoutInflater inflater = LayoutInflater.from(context);
			row = inflater.inflate(R.layout.ost_row, parent, false);
			holder = new ViewHolder();
			holder.image = (ImageView) row.findViewById(R.id.image);
			holder.btnFav = (ImageView) row.findViewById(R.id.btnFav);
			holder.name = (TextView) row.findViewById(R.id.name);
			row.setTag(holder);
			
		} else
			holder = (ViewHolder) row.getTag();
		
		RelativeLayout musicRow = (RelativeLayout) row;		
		musicRow.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String imgUrl = "";				
				try {
					FilmRepository filmRepo = new FilmRepository(context);
					List<Film> film = filmRepo.lookupFilmsForMusic(getItem(position));
					if(film.size() > 0) {
						imgUrl = film.get(0).getImgUrl();
					}
				} catch (SQLException e) {
					imgUrl = "";
					e.printStackTrace();
				}
				
				apiHelper.startPlayerFragment(context, getItem(position),
						imgUrl, (ArrayList<Music>) items, position);

			}
		});
		
		holder.btnFav.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Music m = getItem(position);
				boolean isFavorite = false;
				
				try {
					isFavorite = favRepo.isFavorite(m);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				if(isFavorite) {
					favRepo.deleteFavorites(m);
					holder.btnFav.setImageResource(R.drawable.img_btn_fav);
					musicFavoritesIds.remove(Integer.toString(m.getId()));
				} else {
					favRepo.createFavorites(user, m);
					holder.btnFav.setImageResource(R.drawable.img_btn_fav_pressed);
					musicFavoritesIds.add(Integer.toString(m.getId()));
				}
			}
		});	
		
		Music song = getItem(position);
		Performer performer = song.getPerformer();
		String performerName = (performer != null && performer.getName() != null) ? performer.getName() + " - " : "";
		holder.name.setText(performerName + song.getName());
		holder.name.setSelected(true);
		holder.image.setImageResource(R.drawable.music);
	
		if(musicFavoritesIds.contains(Integer.toString(song.getId())))
			holder.btnFav.setImageResource(R.drawable.img_btn_fav_pressed);
		else
			holder.btnFav.setImageResource(R.drawable.img_btn_fav);
		
		return row;
		
	}

}
