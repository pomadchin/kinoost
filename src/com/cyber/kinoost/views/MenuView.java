package com.cyber.kinoost.views;

import com.cyber.kinoost.ChosenActivity;
import com.cyber.kinoost.InfoActivity;
import com.cyber.kinoost.KinoostActivity;
import com.cyber.kinoost.R;
import com.cyber.kinoost.RateActivity;
import com.cyber.kinoost.SavedActivity;
import com.cyber.kinoost.SearchActivity;
import com.cyber.kinoost.SettingsActivity;
import com.cyber.kinoost.TopActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MenuView  extends RelativeLayout {

	public MenuView(final Context ctx) {
		super(ctx);
        LayoutInflater layoutInflater = LayoutInflater.from(ctx);
        layoutInflater.inflate(R.layout.menu, this);
        

        TextView info = (TextView) this.findViewById(R.id.menu_text_info);
	    info.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(ctx, InfoActivity.class);
				ctx.startActivity(intent);
				
			}} );        

        TextView rate = (TextView) this.findViewById(R.id.menu_text_rating);
        rate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(ctx, RateActivity.class);
				ctx.startActivity(intent);
				
			}} );        

        TextView saved = (TextView) this.findViewById(R.id.menu_text_saved);
        saved.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(ctx, SavedActivity.class);
				ctx.startActivity(intent);
				
			}} );        

        TextView search = (TextView) this.findViewById(R.id.menu_text_find);
        search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(ctx, SearchActivity.class);
				ctx.startActivity(intent);
				
			}} );        

        TextView settings = (TextView) this.findViewById(R.id.menu_text_settings);
        settings.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(ctx, SettingsActivity.class);
				ctx.startActivity(intent);
				
			}} );        

        TextView top = (TextView) this.findViewById(R.id.menu_text_top);
        top.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(ctx, TopActivity.class);
				ctx.startActivity(intent);
				
			}} );        

        TextView chosen = (TextView) this.findViewById(R.id.menu_text_chosen);
        chosen.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(ctx, ChosenActivity.class);
				ctx.startActivity(intent);
				
			}} );
        TextView main = (TextView) this.findViewById(R.id.menu_text_main);
        main.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(ctx, KinoostActivity.class);
				ctx.startActivity(intent);
				
			}} );
	}
 

}