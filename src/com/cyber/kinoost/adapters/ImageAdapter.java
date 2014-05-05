package com.cyber.kinoost.adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyber.kinoost.R;
import com.cyber.kinoost.db.models.Film;
import com.cyber.kinoost.img.ImageLoader;

public class ImageAdapter extends BaseAdapter {
    
	private Context mContext;
    
	private List<Film> films;
    
	private ImageLoader imageLoader;

    public ImageAdapter(Context c, List<Film> films) {
        mContext = c;
        this.films = films;
        this.imageLoader = new ImageLoader(c);
    }

    public int getCount() {
        return films.size();
    }

    public Object getItem(int position) {
        return films.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    // create a new ImageView for each item referenced by the Adapter
//    public View getView(int position, View convertView, ViewGroup parent) {
//        ImageView imageView;
//        if (convertView == null) {  // if it's not recycled, initialize some attributes
//            imageView = new ImageView(mContext);
//            imageView.setLayoutParams(new GridView.LayoutParams(650, 650));
//            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            imageView.setPadding(8, 8, 8, 8);
//        } else {
//            imageView = (ImageView) convertView;
//        }
//
//        imageView.setImageResource(mThumbIds[position]);
//        return imageView;
//    }
    
    public static class ViewHolder
	{
		public ImageView image;
		public TextView name;
	}
    
    public View getView(int position, View convertView, ViewGroup parent) {
    	View row = convertView; 
    	ViewHolder holder = null;
    	
    	if (row == null) { 
    		LayoutInflater inflater = LayoutInflater.from(mContext);
    		row = inflater.inflate(R.layout.row_grid, parent, false); 
    		holder = new ViewHolder(); 
    		holder.name = (TextView) row.findViewById(R.id.item_text); 
    		holder.image = (ImageView) row.findViewById(R.id.item_image); 
    		row.setTag(holder); 
    		} 
    	else { 
    		holder = (ViewHolder) row.getTag(); 
    	} 
    	Film film = films.get(position); 
    	holder.name.setText(film.getName()); 
    	holder.image.setImageResource(R.drawable.sample_2);
    	//imageLoader.DisplayImage(film.getImgUrl(), holder.image);
    	return row;

    }

}