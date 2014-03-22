package com.cyber.kinoost.adapters;

import java.util.ArrayList;

import com.cyber.kinoost.FilmActivity;
import com.cyber.kinoost.InfoActivity;
import com.cyber.kinoost.KinoostActivity;
import com.cyber.kinoost.R;

import android.content.Context;
import android.content.Intent;
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
import android.widget.CheckBox;

public class ListAdapter extends BaseAdapter {
		  Context ctx;
		  LayoutInflater lInflater;
		  ArrayList<Film> objects;

		  public ListAdapter(Context context, ArrayList<Film> products) {
		    ctx = context;
		    objects = products;
		    lInflater = (LayoutInflater) ctx
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		  }

		  // кол-во элементов
		  @Override
		  public int getCount() {
		    return objects.size();
		  }

		  // элемент по позиции
		  @Override
		  public Object getItem(int position) {
		    return objects.get(position);
		  }

		  // id по позиции
		  @Override
		  public long getItemId(int position) {
		    return position;
		  }

		  // пункт списка
		  @Override
		  public View getView(int position, View convertView, ViewGroup parent) {
		    // используем созданные, но не используемые view
		    View view = convertView;
		    if (view == null) {
		      view = lInflater.inflate(R.layout.item, parent, false);
		    }

		    Film p = getFilm(position);

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
		    
		    rightTable.setOnClickListener(new OnClickListener() {

				@Override 
				public void onClick(View arg0) {
					Intent intent = new Intent();
					intent.setClass(ctx, FilmActivity.class);
					ctx.startActivity(intent);
					
				}} );
		    return view;
		  }

		  // товар по позиции
		  Film getFilm(int position) {
		    return ((Film) getItem(position));
		  }

		  // содержимое корзины
		  public ArrayList<Film> getBox() {
		    ArrayList<Film> box = new ArrayList<Film>();
		    for (Film p : objects) {
		      // если в корзине
		      if (p.box)
		        box.add(p);
		    }
		    return box;
		  }

		  // обработчик для чекбоксов
		  OnCheckedChangeListener myCheckChangList = new OnCheckedChangeListener() {
		    public void onCheckedChanged(CompoundButton buttonView,
		        boolean isChecked) {
		      // меняем данные товара (в корзине или нет)
		      getFilm((Integer) buttonView.getTag()).box = isChecked;
		    }
		  };
		

	}

