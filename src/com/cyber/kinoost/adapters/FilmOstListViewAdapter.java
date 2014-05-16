package com.cyber.kinoost.adapters;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyber.kinoost.R;
import com.cyber.kinoost.api.Account;
import com.cyber.kinoost.api.ApiHelper;
import com.cyber.kinoost.api.vk.sources.Api;
import com.cyber.kinoost.db.models.Film;
import com.cyber.kinoost.db.models.Music;
import com.cyber.kinoost.img.ImageLoader;

public class FilmOstListViewAdapter extends BaseAdapter {
    
	private Context mContext;
    
	private List<Music> music;
	
	private Film film;
    
	private ImageLoader imageLoader;
	
	final ApiHelper apiHelper = new ApiHelper();
	
    public FilmOstListViewAdapter(Context c, Film film, List<Music> sounds) {
        mContext = c;
        this.film = film;
        this.music = sounds;
        this.imageLoader = new ImageLoader(c);
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
    
	public View getView(final int position, View convertView, ViewGroup parent) {
		if(position == 0)
			return getImageView(convertView, parent);
		else
			return getTextView(position-1, convertView, parent);
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
			row = inflater.inflate(R.layout.film_ost_row, parent, false);
			musicHolder = (TextView) row
					.findViewById(R.id.film_name);
			musicHolder.setText(music.get(position).getName());
			row.setTag(musicHolder);
			FrameLayout musicRow = (FrameLayout) row;
			musicRow.setOnClickListener(new OnClickListener() {
	             @Override
	             public void onClick(View v) {
					Log.i("ListAdapter", music.get(position).getName());
					Account account = new Account(mContext);
					Api api = new Api(account);

					apiHelper.getSongMusic(mContext, api, music.get(position));

	             }
	         });
		} else
			musicHolder = (TextView) row.getTag();
		musicHolder.setText(music.get(position).getName());
    	return row;
	}

}