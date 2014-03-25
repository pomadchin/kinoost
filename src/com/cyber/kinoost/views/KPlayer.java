package com.cyber.kinoost.views;

import java.io.IOException;

import com.cyber.kinoost.R;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

public class KPlayer extends RelativeLayout implements OnPreparedListener,
OnCompletionListener{
	
	Context cont;
	final String DATA_SD = Environment
		      .getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
		      + "/music.mp3";

	  MediaPlayer mediaPlayer;
	  AudioManager am;

	public KPlayer(final Context ctx) {
		super(ctx);
		cont = ctx;
        LayoutInflater layoutInflater = LayoutInflater.from(ctx);
        layoutInflater.inflate(R.layout.buttons, this);
        
		  final String DATA_SD = Environment
			      .getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
			      + "/music.mp3";

		  MediaPlayer mediaPlayer;
		  AudioManager am;
	}
 
		  public void onClickStart(View view) {
			    releaseMP();
			    try {
			          mediaPlayer = new MediaPlayer();
			          mediaPlayer.setDataSource(DATA_SD);
			          mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			          mediaPlayer.prepare();
			          mediaPlayer.start();


			    }
		  catch (IOException e) {
		      e.printStackTrace();}
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
