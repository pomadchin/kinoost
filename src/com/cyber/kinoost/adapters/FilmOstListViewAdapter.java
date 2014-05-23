package com.cyber.kinoost.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;

import com.cyber.kinoost.R;
import com.cyber.kinoost.api.*;
import com.cyber.kinoost.db.models.*;
import com.squareup.picasso.Picasso;

public class FilmOstListViewAdapter extends BaseAdapter {
    
	private Context mContext;
    
	private List<Music> music;
	
	private Film film;
	
	final ApiHelper apiHelper = new ApiHelper();
	
    public FilmOstListViewAdapter(Context c, Film film, List<Music> sounds) {
        mContext = c;
        this.film = film;
        this.music = sounds;
    }

    public int getCount() {
        return music.size() + 1;
    }

    public Object getItem(int position) {
        return music.get(position);
    }

    public long getItemId(int position) {
        return position;
    }
    
    public static class FilmInfoHolder
	{
		public ImageView image;
		public TextView name;
	}
    
    public static class MusicInfoHolder
   	{
   		public ImageView image;
   		public TextView name;
   		public TextView performer;
   	}
    
	public View getView(final int position, View convertView, ViewGroup parent) {
		if(position == 0)
			return getImageView(convertView, parent);
		else
			return getMusicView(position-1, convertView, parent);
	}
	
	public View getImageView(View convertView, ViewGroup parent) {
		View row = convertView;
		FilmInfoHolder filmInfoHolder;
		if (row == null || row.getTag() instanceof MusicInfoHolder) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			row = inflater.inflate(R.layout.film_info, parent, false);
			filmInfoHolder = new FilmInfoHolder();
			filmInfoHolder.image = (ImageView) row.findViewById(R.id.film_image);
			filmInfoHolder.name = (TextView) row.findViewById(R.id.film_desc);
			row.setTag(filmInfoHolder);

		} else
			filmInfoHolder = (FilmInfoHolder) row.getTag();
		
		filmInfoHolder.name.setText(film.getName() + " (" + String.valueOf(film.getYear()) + ")");
		
		Picasso.with(mContext).load(film.getImgUrl())
				.placeholder(R.drawable.placeholder).fit()
				.into(filmInfoHolder.image);
		
		return row;
	}
	
	public View getMusicView(final int position, View convertView, ViewGroup parent) {
		View row = convertView;
		MusicInfoHolder musicHolder;
		if (row == null || row.getTag() instanceof FilmInfoHolder) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			row = inflater.inflate(R.layout.ost_row, parent, false);
			musicHolder = new MusicInfoHolder();
			musicHolder.image = (ImageView) row.findViewById(R.id.image);
			musicHolder.name =  (TextView) row.findViewById(R.id.name);
			row.setTag(musicHolder);
			RelativeLayout musicRow = (RelativeLayout) row;
			musicRow.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Log.i("ListAdapter", music.get(position).getName());

					apiHelper.startPlayerFragment(mContext,
							music.get(position), film.getImgUrl(),
							(ArrayList<Music>) music, position);
				}
			});
		} else
			musicHolder = (MusicInfoHolder) row.getTag();
		
		Music song = music.get(position);
		Performer performer = song.getPerformer();
		String performerName = (performer != null && performer.getName() != null) ? performer.getName() + " - " : "";
		musicHolder.name.setText(performerName + song.getName());
		musicHolder.name.setSelected(true);
		musicHolder.image.setImageResource(R.drawable.music);
    	return row;
	}

}