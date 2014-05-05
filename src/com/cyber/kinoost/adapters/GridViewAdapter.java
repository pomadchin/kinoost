package com.cyber.kinoost.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyber.kinoost.R;
import com.cyber.kinoost.db.models.Film;
import com.cyber.kinoost.img.ImageLoader;

public class GridViewAdapter extends BaseAdapter {
    
	private Context mContext;
    
	private List<Film> films;
    
	private ImageLoader imageLoader;

    public GridViewAdapter(Context c, List<Film> films) {
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
    
    public static class ViewHolder
	{
		public ImageView image;
		public TextView name;
	}
    
    public View getView(final int position, View convertView, ViewGroup parent) {
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
    	//holder.image.setImageResource(R.drawable.sample_2);
    	imageLoader.DisplayImage(film.getImgUrl(), holder.image);
    	
//    	Button button = (Button) row.findViewById(R.layout.row_grid);
//    	button.setOnClickListener(new OnClickListener() {
//
//             @Override
//             public void onClick(View v) {
//                 startFilmFragment(position);
//             }
//         });
    	
    	//imageLoader.DisplayImage(film.getImgUrl(), holder.image);
    	return row;

    }
    
    private void startFilmFragment(int position) {
    	
    }

}