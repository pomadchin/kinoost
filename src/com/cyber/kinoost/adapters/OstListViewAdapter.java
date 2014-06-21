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
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cyber.kinoost.R;
import com.cyber.kinoost.api.ApiHelper;
import com.cyber.kinoost.db.models.Film;
import com.cyber.kinoost.db.models.Music;
import com.cyber.kinoost.db.models.Performer;
import com.cyber.kinoost.db.models.User;
import com.cyber.kinoost.db.repositories.FavoritesRepository;
import com.cyber.kinoost.db.repositories.FilmRepository;
import com.cyber.kinoost.db.repositories.UserRepository;

public class OstListViewAdapter extends BaseAdapter {
    
	private Context mContext;
	private List<Music> music;
	final ApiHelper apiHelper = new ApiHelper();
	private FavoritesRepository favRepo;
	private UserRepository userRepo;
	private List<String> musicFavoritesIds;
	private User user;
	
    public OstListViewAdapter(Context c, List<Music> sounds) {
        this.mContext = c;
        this.music = sounds;
        this.favRepo = new FavoritesRepository(c);
        this.userRepo = new UserRepository(c);
        this.user = userRepo.getUser();
        
        try {
			this.musicFavoritesIds = favRepo.getFavoritesIds(this.music);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public int getCount() {
        return music.size();
    }

    public Object getItem(int position) {
        return music.get(position);
    }

    public long getItemId(int position) {
        return position;
    }
    
    public static class ViewHolder
	{
		public ImageView image;
		public ImageView btnFav;
		public TextView performer;
		public TextView name;
	}
    
	public View getView(final int position, View convertView, ViewGroup parent) {
		View row = convertView;
		final ViewHolder holder;
		if (row == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
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
					FilmRepository filmRepo = new FilmRepository(mContext);
					List<Film> film = filmRepo.lookupFilmsForMusic(music.get(position));
					if(film.size() > 0) {
						imgUrl = film.get(0).getImgUrl();
					}
				} catch (SQLException e) {
					imgUrl = "";
					e.printStackTrace();
				}
				
				apiHelper.startPlayerFragment(mContext,
						music.get(position), imgUrl,
						(ArrayList<Music>) music, position);
			}
		});
		
		/**
		 * Add or remove song from favorites
		 **/
		holder.btnFav.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Music m = music.get(position);
				boolean isFavorite = false;
				
				try {
					isFavorite = favRepo.isFavorite(m);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
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

		
		Music song = music.get(position);
		Performer performer = song.getPerformer();
		String performerName = (performer != null && performer.getName() != null) ? performer.getName() + " - " : "";
		holder.name.setText(performerName + song.getName());
		holder.name.setSelected(true);
		holder.image.setImageResource(R.drawable.music);
		
		if(musicFavoritesIds.contains(Integer.toString(song.getId())))
			holder.btnFav.setImageResource(R.drawable.img_btn_fav_pressed);
		
		return row;
		
	}


}