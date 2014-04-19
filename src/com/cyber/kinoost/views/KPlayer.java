package com.cyber.kinoost.views;

import java.io.IOException;

import com.cyber.kinoost.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;

public class KPlayer extends Activity implements OnPreparedListener,
		OnCompletionListener {

	Context context;
	final String DATA_HTTP = "https://www.dropbox.com/s/ry6smu1k1louirn/Spiritual%20Front%20-%20Jesus%20Died%20in%20Las%20Vegas.mp3";
	final String DATA_SD = Environment
			.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
			+ "/music.mp3";

	MediaPlayer mediaPlayer;
	AudioManager am;

	public void onCreate() {
		LayoutInflater ltInflater = getLayoutInflater();
		View view = ltInflater.inflate(R.layout.buttons, null, false);

		Intent intent = getIntent();
		String action = intent.getAction();
	}

	public void onClickStart(View view) {
		releaseMP();
		try {
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setDataSource(DATA_HTTP);
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.prepare();
			mediaPlayer.start();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		mp.start();
	}

	void releaseMP() {
		if (mediaPlayer != null) {
			try {
				mediaPlayer.release();
				mediaPlayer = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void onClick(View view) {
		if (mediaPlayer == null)
			return;
		if (mediaPlayer.isPlaying())
			mediaPlayer.pause();
		else
			mediaPlayer.start();
	}

}
