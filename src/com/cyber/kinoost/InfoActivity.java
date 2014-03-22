package com.cyber.kinoost;

import com.cyber.kinoost.views.MenuView;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
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
        tv.setText(Html.fromHtml("<h2>Описание</h2><br><p>Дичайшее, например</p>"));
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
            	   FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(listContainer.getWidth(), FrameLayout.LayoutParams.MATCH_PARENT);   
            	   lp.setMargins(menuContainer.getWidth(), 0, 0, 0);
            	   listContainer.setLayoutParams(lp);
               }else{
            	   FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            	   lp.setMargins(0, 0, 0, 0);
            	   listContainer.setLayoutParams(lp);
               }
            }
        });
	}
}