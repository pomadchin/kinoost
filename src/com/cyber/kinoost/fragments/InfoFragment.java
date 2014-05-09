package com.cyber.kinoost.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cyber.kinoost.R;

public class InfoFragment extends Fragment {

	public InfoFragment() {
		// Empty constructor required for fragment subclasses
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View info = inflater.inflate(R.layout.fragment_info,
				container, false);
		TextView tv = (TextView) info.findViewById(R.id.text_info);
		
		tv.setText(Html
				.fromHtml("<p>Данное приложение создано в рамках учебного проекта студентами группы K8-223</p>"
						+ "<p>Над проектом работали:</p>"
						+ "Благидзе Дмитрий<br/>"
						+ "Бушмелев Игорь<br/>"
						+ "Дьяченко Анастасия<br/>"
						+ "Закиров Хасан<br/>"
						+ "Ибрагимова Юлия<br/>"
						+ "Костяев Дмитрий<br/>"
						+ "Масгутова Екатерина<br/>"
						+ "Лещук Виталий<br/>"
						+ "Малов Игорь<br/>"
						+ "Новак Дмитрий<br/>"
						+ "Помадчин Григорий<br/>"
						+ "Скрипко Ольга<br/>"
						+ "Сурган Павел<br/>"
						+ "Пресняков Александр<br/>"
						+ "Федоров Дмитрий<br/>"
						+ "Евменков Андрей<br/>"));
		
		return info;
	}

}
