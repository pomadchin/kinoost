package com.cyber.kinoost.adapters;

import java.util.ArrayList;
import com.cyber.kinoost.FilmActivity;
import com.cyber.kinoost.R;
import com.cyber.kinoost.img.ImageLoader;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Display;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cyber.kinoost.db.models.*;

public class ListAdapter extends BaseAdapter {
		  Activity context;
		  LayoutInflater lInflater;
		  ArrayList<Tuple<Film, Film>> filmTuples;
		  ImageLoader imageLoader; 

		  public ListAdapter(Activity context, ArrayList<Tuple<Film, Film>> filmTuples) {
		    this.context = context;
		    this.filmTuples = filmTuples;
		    lInflater = (LayoutInflater) context
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    imageLoader = new ImageLoader(context);
		    Display display = context.getWindowManager().getDefaultDisplay();
		  }

		  @Override
		  public int getCount() {
		    return filmTuples.size();
		  }

		  @Override
		  public Object getItem(int position) {
		    return filmTuples.get(position);
		  }

		  @Override
		  public long getItemId(int position) {
		    return position;
		  }
		  
		  public Tuple<Film, Film> getFilm(int position) {
			  return (Tuple<Film, Film>) getItem(position);
		  }

		  @SuppressWarnings("deprecation")
		@Override
		  public View getView(int position, View convertView, ViewGroup parent) {
		    
		    View view = convertView;
		    if (view == null) {
		      view = lInflater.inflate(R.layout.item, parent, false);
		    }

		    RelativeLayout rightTable = (RelativeLayout) view.findViewById(R.id.table_l);
		    RelativeLayout leftTable = (RelativeLayout) view.findViewById(R.id.table_r);

		    Display display = context.getWindowManager().getDefaultDisplay();
			int width = display.getWidth();
			int height = display.getHeight(); 	    

		    rightTable.getLayoutParams().height = Math.round(width/2) - 30;
		    rightTable.getLayoutParams().width = Math.round(width/2) - 30;
		    leftTable.getLayoutParams().height = Math.round(width/2) - 30;
		    leftTable.getLayoutParams().width= Math.round(width/2) - 30;

		    ImageView imgl = (ImageView) view.findViewById(R.id.image_l);
		    ImageView imgr = (ImageView) view.findViewById(R.id.image_r);


		    Tuple<Film, Film> item = getFilm(position);
		    
		    final Film fst = item.getFst();
		    final Film snd = item.getSnd();
		    
		    if(fst != null)
		    	imageLoader.DisplayImage(fst.getImgUrl(), imgl);
		    
		    if(snd != null)
		    	imageLoader.DisplayImage(snd.getImgUrl(), imgr);
		    
		    leftTable.setOnClickListener(new OnClickListener() {
		    	@Override 
		    	public void onClick(View arg0) {
		    		Intent intent = new Intent();
		    		intent.setClass(context, FilmActivity.class);
		    		intent.putExtra("img", fst.getImgUrl());
		    		intent.putExtra("film", fst);
		    		intent.putExtra("name", fst.getName());
		    		context.startActivity(intent);
		    	}
		    });
		    
		    rightTable.setOnClickListener(new OnClickListener() {
		    	@Override 
		    	public void onClick(View arg0) {
		    		Intent intent = new Intent();
		    		intent.setClass(context, FilmActivity.class);
		    		intent.putExtra("img", snd.getImgUrl());
		    		intent.putExtra("film", snd);
		    		intent.putExtra("name", snd.getName());
		    		context.startActivity(intent);
		    	}
		    });
		    
		    TextView tr = (TextView) view.findViewById(R.id.text_name_r);
		    TextView tl = (TextView) view.findViewById(R.id.text_name_l);
		    	
		    if(fst != null)
		    	tr.setText(fst.getName());
		    
		    if(snd != null)
		    	tl.setText(snd.getName());
		    
		    return view;
		  }

	}
