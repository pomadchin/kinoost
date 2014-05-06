package com.cyber.kinoost.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyber.kinoost.R;
import com.cyber.kinoost.db.models.Film;
import com.cyber.kinoost.db.models.Music;
import com.cyber.kinoost.img.ImageLoader;

public class ListViewAdapter extends BaseAdapter {
    
	private Context mContext;
    
	private List<Music> sounds;
	
	private Film film;
    
	private ImageLoader imageLoader;

    public ListViewAdapter(Context c, Film film, List<Music> sounds) {
        mContext = c;
        this.film = film;
        this.sounds = sounds;
        this.imageLoader = new ImageLoader(c);
    }

    public int getCount() {
        return sounds.size();
    }

    public Object getItem(int position) {
        return sounds.get(position);
    }

    public long getItemId(int position) {
        return position;
    }
    
    public static class FilmInfoHolder
	{
		public ImageView image;
		public TextView name;
	}
    
	public View getView(final int position, View convertView, ViewGroup parent) {
		if(position == 0)
			return getImageView(convertView, parent);
		else
			return getTextView(position, convertView, parent);
	}
	
	public View getImageView(View convertView, ViewGroup parent) {
		View row = convertView;
		FilmInfoHolder filmInfoHolder;
		if (row == null || row.getTag() instanceof TextView) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			row = inflater.inflate(R.layout.film_info, parent, false);
			filmInfoHolder = new FilmInfoHolder();
			filmInfoHolder.image = (ImageView) row.findViewById(R.id.film_image);
			filmInfoHolder.name = (TextView) row.findViewById(R.id.film_desc);
			row.setTag(filmInfoHolder);

		} else
			filmInfoHolder = (FilmInfoHolder) row.getTag();
		
		filmInfoHolder.name.setText(film.getName());
		imageLoader.DisplayImage(film.getImgUrl(), filmInfoHolder.image);
		return row;
	}
	
	public View getTextView(final int position, View convertView, ViewGroup parent) {
		View row = convertView;
		TextView musicHolder;
		if (row == null || row.getTag() instanceof FilmInfoHolder) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			row = inflater.inflate(R.layout.row_list, parent, false);
			musicHolder = (TextView) row
					.findViewById(R.id.film_name);
			musicHolder.setText(sounds.get(position - 1).getName());
			row.setTag(musicHolder);
		} else
			musicHolder = (TextView) row.getTag();
		musicHolder.setText(sounds.get(position).getName());
		return row;
	}

}