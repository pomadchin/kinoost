package com.cyber.kinoost;

import com.cyber.kinoost.views.MenuView;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;


public class InfoActivity extends Activity{
	
	MenuView menu;
	RelativeLayout menuContainer;
	RelativeLayout listContainer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);
        TextView tv = (TextView) findViewById(R.id.text_info);
        //СДЕЛАЙТЕ ХТМЛЬКУ ПЛЗ, МНЕ ВЛОМ!
        tv.setText(Html.fromHtml("<p>Данное приложение создано в рамках учебного проекта студентами группы к8-223</p>" +
        		"<br>Над проектом работали:</br>" +
        		"<br>Благидзе Дмитрий</br>" +
        		"<br>Бушмелев Игорь</br>" +
        		"<br>Дьяченко Анастасия</br>" +
        		"<br>Закиров Хасан</br>" +
        		"<br>Ибрагимова Юлия</br>" +
        		"<br>Костяев Дмитрий</br>" +
        		"<br>Лисина Екатерина</br>" +
        		"<br>Лещук Виталий</br>" +
        		"<br>Малов Игорь</br>" +
        		"<br>Новак Дмитрий</br>" +
        		"<br>Помадчин Григорий</br>" +
        		"<br>Скрипко Ольга</br>" +
        		"<br>Сурган Павел</br>" +
        		"<br>Пресняков Александр</br>" +
        		"<br>Федоров Дмитрий</br>"));
		menu = new MenuView(this);
		Log.v(Integer.toString(menu.getChildCount()), "asd");
		menuContainer = (RelativeLayout) findViewById(R.id.menu_container);
		listContainer = (RelativeLayout) findViewById(R.id.list_container);
		menuContainer.addView(menu);
		ToggleButton toogleButton = (ToggleButton) findViewById(R.id.main_button);
		toogleButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {

@Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if(buttonView.isChecked()){
            	   FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(listContainer.getWidth(), LayoutParams.MATCH_PARENT);   
            	   lp.setMargins(menuContainer.getWidth(), 0, 0, 0);
            	   listContainer.setLayoutParams(lp);
               }else{
            	   FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            	   lp.setMargins(0, 0, 0, 0);
            	   listContainer.setLayoutParams(lp);
               }
            }
        });
	}
}