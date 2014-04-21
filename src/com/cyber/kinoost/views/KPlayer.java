package com.cyber.kinoost.views;

import java.io.IOException;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

public class KPlayer {

	private Context context;
	private MediaPlayer mediaPlayer;

	public KPlayer(Context context) {
		this.context = context;
	}

	public void play(Uri url) {
		if(url == null) return;
		
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		} else {
			this.mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

			try {
				mediaPlayer.setDataSource(context, url);
				mediaPlayer.prepare();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mediaPlayer.start();
		}

	}
}
