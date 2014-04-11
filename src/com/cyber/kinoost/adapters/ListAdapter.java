package com.cyber.kinoost.adapters;

import java.util.ArrayList;
import java.util.List;

import com.cyber.kinoost.FilmActivity;
import com.cyber.kinoost.R;
import com.cyber.kinoost.img.ImageLoader;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.cyber.kinoost.db.models.*;


public class ListAdapter extends BaseAdapter {
		  Context context;
		  LayoutInflater lInflater;
		  ArrayList<Tuple<Film, Film>> filmTuples;
		  ImageLoader imageLoader; 

		  public ListAdapter(Context context, ArrayList<Tuple<Film, Film>> filmTuples) {
		    this.context = context;
		    this.filmTuples = filmTuples;
		    lInflater = (LayoutInflater) context
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    imageLoader = new ImageLoader(context);
		  }

		  // кол-во элементов
		  @Override
		  public int getCount() {
		    return filmTuples.size();
		  }

		  // элемент по позиции
		  @Override
		  public Object getItem(int position) {
		    return filmTuples.get(position);
		  }

		  // id по позиции
		  @Override
		  public long getItemId(int position) {
		    return position;
		  }
		  
		  public Tuple<Film, Film> getFilm(int position) {
			  return (Tuple<Film, Film>) getItem(position);
		  }

		  // пункт списка
		  @Override
		  public View getView(int position, View convertView, ViewGroup parent) {
		    // используем созданные, но не используемые view
		    View view = convertView;
		    if (view == null) {
		      view = lInflater.inflate(R.layout.item, parent, false);
		    }

		    // заполняем View в пункте списка данными из товаров: наименование, цена
		    // и картинка
	/*	    ((TextView) view.findViewById(R.id.text_name_l)).setText(p.name);
		    ((TextView) view.findViewById(R.id.tvPrice)).setText(p.price + "");
		    ((ImageView) view.findViewById(R.id.ivImage)).setImageResource(p.image);

		    CheckBox cbBuy = (CheckBox) view.findViewById(R.id.cbBox);
		    // присваиваем чекбоксу обработчик
		    cbBuy.setOnCheckedChangeListener(myCheckChangList);
		    // пишем позицию
		    cbBuy.setTag(position);
		    // заполняем данными из товаров: в корзине или нет
		    cbBuy.setChecked(p.box);*/

		    RelativeLayout rightTable = (RelativeLayout) view.findViewById(R.id.table_r);
		    RelativeLayout leftTable = (RelativeLayout) view.findViewById(R.id.table_l);
		    
		    ImageView imgl = (ImageView) view.findViewById(R.id.image_l);
		    ImageView imgr = (ImageView) view.findViewById(R.id.image_r);

		    imgr.getLayoutParams().height = 170;
		    imgl.getLayoutParams().width = 170;
		    imgl.getLayoutParams().height = 170;
		    imgr.getLayoutParams().width = 170;
		    
		    Tuple<Film, Film> item = getFilm(position);
		    
		    // подгрузим картинки
		    Film fst = item.getFst();
		    Film snd = item.getSnd();
		    
		    if(fst != null)
		    	imageLoader.DisplayImage(fst.getImgUrl(), imgl);
		    
		    if(snd != null)
		    	imageLoader.DisplayImage(snd.getImgUrl(), imgr);
		    
		    Log.d("ListAdapter", item.toString());
		    
		    leftTable.setOnClickListener(new OnClickListener() {
		    	@Override 
		    	public void onClick(View arg0) {
		    		Intent intent = new Intent();
		    		intent.setClass(context, FilmActivity.class);
		    		context.startActivity(intent);
		    	}
		    });
		    
		    rightTable.setOnClickListener(new OnClickListener() {
		    	@Override 
		    	public void onClick(View arg0) {
		    		Intent intent = new Intent();
		    		intent.setClass(context, FilmActivity.class);
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

