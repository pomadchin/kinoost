package com.cyber.kinoost.views;

import java.io.IOException;

import com.cyber.kinoost.R;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class Player2 extends Activity implements OnPreparedListener,
OnCompletionListener{

	final String DATA_HTTP = "https://www.dropbox.com/s/ry6smu1k1louirn/Spiritual%20Front%20-%20Jesus%20Died%20in%20Las%20Vegas.mp3";
	final String DATA_SD = Environment
		      .getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
		      + "/music.mp3";
	MediaPlayer mediaPlayer;
	AudioManager am;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
        setContentView(R.layout.buttons);
        
        am = (AudioManager) getSystemService(AUDIO_SERVICE);
        
        Button start_stop = (Button)findViewById(R.id.start_stop);

        start_stop.setOnClickListener(new View.OnClickListener() {
    		public void onClick(View view) {
    			  if (mediaPlayer == null)
    			      return;
    		      if (mediaPlayer.isPlaying()){
    		    	Log.d("play", "start HTTP");
    		        mediaPlayer.pause();
    		        }
    		      else {
    		    	Log.d("stop", "start HTTP");
    		        mediaPlayer.start();
    		      }
    		}
    	});
   }

	public void onClickStart(View view) {
		releaseMP();

		try {
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setDataSource(DATA_HTTP);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.prepareAsync();
		} catch (IOException e) {
		      e.printStackTrace();
		    }
	}

	private void releaseMP() {
	    if (mediaPlayer != null) {
	      try {
	        mediaPlayer.release();
	        mediaPlayer = null;
	      } catch (Exception e) {
	        e.printStackTrace();
	      }
	    }
	}

    

	@Override
	public void onCompletion(MediaPlayer arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPrepared(MediaPlayer arg0) {
		Log.d("no", "onPrepared");
		mediaPlayer.start();
	}

	@Override
	  protected void onDestroy() {
	    super.onDestroy();
	    releaseMP();
	  }
}