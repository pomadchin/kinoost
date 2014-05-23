package com.cyber.kinoost.fragments;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.json.JSONException;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.cyber.kinoost.KinoostActivity;
import com.cyber.kinoost.R;
import com.cyber.kinoost.api.Account;
import com.cyber.kinoost.api.vk.sources.Api;
import com.cyber.kinoost.api.vk.sources.Audio;
import com.cyber.kinoost.api.vk.sources.KException;
import com.cyber.kinoost.db.models.Film;
import com.cyber.kinoost.db.models.Music;
import com.cyber.kinoost.db.repositories.FilmRepository;
import com.cyber.kinoost.db.repositories.MusicRepository;
import com.cyber.kinoost.listeners.OnSwipeTouchListener;
import com.cyber.kinoost.mediaplayer.SongsManager;
import com.cyber.kinoost.mediaplayer.Utilities;
import com.squareup.picasso.Picasso;

public class KPlayerFragment extends Fragment implements OnCompletionListener,
		SeekBar.OnSeekBarChangeListener {

	RelativeLayout menuContainer;
	RelativeLayout listContainer;
	RelativeLayout imgContainer;
	RelativeLayout headContainer;
	View myFragmentView;
	private final String MUSIC_CLASS_NAME = "com.cyber.kinoost.db.models.Music";
	private final String MUSIC_LIST_CLASS_NAME = "List<com.cyber.kinoost.db.models.Music>";
	private final String FILM_IMG_URL = "filmImgUrl";
	private final String CURRENT_SONG_INDEX = "currentSongIndex";
	private Music music;
	private List<Music> musicList;
	private String imgUrl;
	private ImageButton btnPlay;
	private ImageButton btnNext;
	private ImageButton btnPrevious;
	private SeekBar songProgressBar;
	private TextView songTitleLabel;
	private TextView songCurrentDurationLabel;
	private TextView songTotalDurationLabel;
	private ImageView image;
	// Media Player
	private static MediaPlayer mp = new MediaPlayer();
	// Handler to update UI timer, progress bar etc,.
	private Handler mHandler = new Handler();
	private SongsManager songManager;
	private Utilities utils;
	private int seekForwardTime = 5000; // 5000 milliseconds
	private int seekBackwardTime = 5000; // 5000 milliseconds
	private int currentSongIndex;
	private boolean isShuffle = false;
	private boolean isRepeat = false;
	// api
	private Api api;

	public KPlayerFragment() {
		// Empty constructor required for fragment subclasses
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		getActivity().getActionBar().show();			
	}
	
	private void startFilmFragment() {
		Bundle bundle = new Bundle();
		bundle.putSerializable("music", music);
		Fragment newFragment = new FilmsByMusicFragment();
		newFragment.setArguments(bundle);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        transaction.replace(R.id.content_frame, newFragment);
        transaction.addToBackStack("player fragment");
        transaction.commit();
    }

	@SuppressWarnings("unchecked")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		getActivity().getActionBar().hide();
		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		// Get music info
		music = (Music) getArguments().getSerializable(MUSIC_CLASS_NAME);
		musicList = (List<Music>) getArguments().getSerializable(MUSIC_LIST_CLASS_NAME);
		imgUrl = getArguments().getString(FILM_IMG_URL);
		currentSongIndex = getArguments().getInt(CURRENT_SONG_INDEX);
		
		// api
		api = new Api(new Account(getActivity()));
		
		myFragmentView = inflater.inflate(R.layout.player, container, false);
		myFragmentView.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
		    @Override
		    public void onSwipeLeft() {
		        startFilmFragment();
		    }
		});

		// All player buttons
		btnPlay = (ImageButton) myFragmentView.findViewById(R.id.btnPlay);
		btnNext = (ImageButton) myFragmentView.findViewById(R.id.btnNext);
		btnPrevious = (ImageButton) myFragmentView.findViewById(R.id.btnPrevious);
		songProgressBar = (SeekBar) myFragmentView.findViewById(R.id.songProgressBar);
		songTitleLabel = (TextView) myFragmentView.findViewById(R.id.songTitle);
		songCurrentDurationLabel = (TextView) myFragmentView.findViewById(R.id.songCurrentDurationLabel);
		songTotalDurationLabel = (TextView) myFragmentView.findViewById(R.id.songTotalDurationLabel);
		image = (ImageView) myFragmentView.findViewById(R.id.image);
		
		if (imgUrl.length() > 0)
			Picasso.with(getActivity()).load(imgUrl)
					.placeholder(R.drawable.placeholder).fit().into(image);

		// Mediaplayer
		songManager = new SongsManager();
		utils = new Utilities();

		// Listeners
		songProgressBar.setOnSeekBarChangeListener(this); // Important
		mp.setOnCompletionListener(this); // Important
		
		// By play passed song (default -- first song)
		new Thread(new Runnable() {
			public void run() {
				if (currentSongIndex >= 0 && currentSongIndex < musicList.size()) {
					refreshMusicList(currentSongIndex);
					playSong(currentSongIndex);
				}
			}
		}).start();

		/**
		 * Play button click event plays a song and changes button to pause
		 * image pauses a song and changes button to play image
		 * */
		btnPlay.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// check for already playing
				if (mp.isPlaying()) {
					if (mp != null) {
						mp.pause();
						// Changing button image to play button
						btnPlay.setImageResource(R.drawable.btn_play);
					}
				} else {
					// Resume song
					if (mp != null) {
						mp.start();
						// Changing button image to pause button
						btnPlay.setImageResource(R.drawable.btn_pause);
					}
				}

			}
		});

		/**
		 * Next button click event
		 * Plays next song by taking currentSongIndex + 1
		 * */
		btnNext.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				new Thread(new Runnable() {
					public void run() {
						// check if next song is there or not
						if (currentSongIndex < (musicList.size() - 1)) {
							refreshMusicList(currentSongIndex + 1);
							playSong(currentSongIndex + 1);
							currentSongIndex = currentSongIndex + 1;
						} else {
							// play first song
							refreshMusicList(0);
							playSong(0);
							currentSongIndex = 0;
						}
					}
				}).start();
			}
			
		});
		
		/**
		 * Back button click event
		 * Plays previous song by currentSongIndex - 1
		 * */
		btnPrevious.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				new Thread(new Runnable() {
					public void run() {
						if (currentSongIndex > 0) {
							refreshMusicList(currentSongIndex - 1);
							playSong(currentSongIndex - 1);
							currentSongIndex = currentSongIndex - 1;
						} else {
							// play last song
							refreshMusicList(musicList.size() - 1);
							playSong(musicList.size() - 1);
							currentSongIndex = musicList.size() - 1;
						}
					}
				}).start();
			}
		});

		return myFragmentView;
	}

	/**
	 * Receiving song index from playlist view and play the song
	 * */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 100) {
			currentSongIndex = data.getExtras().getInt("songIndex");
			// play selected song
			playSong(currentSongIndex);
		}

	}

	/**
	 * Function to play a song
	 * 
	 * @param songIndex - index of song
	 * */
	public void playSong(final int songIndex) {

		// Play song
		try {
			mp.reset();
			mp.setDataSource(musicList.get(songIndex).getFileName());
			mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mp.setOnPreparedListener(new OnPreparedListener() {
				public void onPrepared(MediaPlayer mp) {
					mp.start();
					
					Music music = musicList.get(songIndex);

					// Displaying Song title
					String songTitle = music.getName();
					
					if(music.getPerformer() != null && music.getPerformer().getName() != null) 
						songTitle = music.getPerformer() + " - " + songTitle;
					
					songTitleLabel.setText(songTitle);

					// Changing Button Image to pause image
					btnPlay.setImageResource(R.drawable.btn_pause);

					// set Progress bar values
					songProgressBar.setProgress(0);
					songProgressBar.setMax(100);

					// Updating progress bar
					updateProgressBar();
				}
			});
			mp.prepareAsync();
			// mp.start();

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			mp.reset();
			playSong(songIndex);
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Update timer on seekbar
	 * */
	public void updateProgressBar() {
		mHandler.postDelayed(mUpdateTimeTask, 100);
	}

	/**
	 * Background Runnable thread
	 * */
	private Runnable mUpdateTimeTask = new Runnable() {
		public void run() {
			if (mp.isPlaying()) {
				long totalDuration = 0;
				long currentDuration = 0;
				try {
					totalDuration = mp.getDuration();
					currentDuration = mp.getCurrentPosition();
				} catch (Exception e) {

				}

				// Displaying Total Duration time
				songTotalDurationLabel.setText(""
						+ utils.milliSecondsToTimer(totalDuration));
				// Displaying time completed playing
				songCurrentDurationLabel.setText(""
						+ utils.milliSecondsToTimer(currentDuration));

				// Updating progress bar
				int progress = (int) (utils.getProgressPercentage(
						currentDuration, totalDuration));
				// Log.d("Progress", ""+progress);
				songProgressBar.setProgress(progress);
			}

			// Running this thread after 100 milliseconds
			mHandler.postDelayed(this, 100);
		}
	};

	/**
	 * 
	 * */
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromTouch) {

	}

	/**
	 * When user starts moving the progress handler
	 * */
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// remove message Handler from updating progress bar
		mHandler.removeCallbacks(mUpdateTimeTask);
	}

	/**
	 * When user stops moving the progress hanlder
	 * */
	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		mHandler.removeCallbacks(mUpdateTimeTask);
		int totalDuration = mp.getDuration();
		int currentPosition = utils.progressToTimer(seekBar.getProgress(),
				totalDuration);

		// forward or backward to certain seconds
		mp.seekTo(currentPosition);

		// update timer progress again
		updateProgressBar();
	}

	/**
	 * On Song Playing completed if repeat is ON play same song again if shuffle
	 * is ON play random song
	 * */
	@Override
	public void onCompletion(MediaPlayer arg0) {
		new Thread(new Runnable() {
			public void run() {
				// check for repeat is ON or OFF
				if (isRepeat) {
					// repeat is on play same song again
					playSong(currentSongIndex);
				} else if (isShuffle) {
					// shuffle is on - play a random song
					Random rand = new Random();
					currentSongIndex = rand.nextInt((musicList.size() - 1) - 0 + 1) + 0;
					refreshMusicList(currentSongIndex);
					playSong(currentSongIndex);
				} else {
					// no repeat or shuffle ON - play next song
					if (currentSongIndex < (musicList.size() - 1)) {
						refreshMusicList(currentSongIndex + 1);
						playSong(currentSongIndex + 1);
						currentSongIndex = currentSongIndex + 1;
					} else {
						// play first song
						refreshMusicList(0);
						playSong(0);
						currentSongIndex = 0;
					}
				}
			}
		}).start();
	}
	
	public void refreshMusicList(int position) {
		if (position < 0 || musicList.size() < position)
			return;

		Music music = musicList.get(position);
		
		if (music.getName() == null || music.getName().length() == 0)
			return;

		try {
			ArrayList<Audio> songsVkList = api.searchAudio(music.getName(),
					"2", "0", (long) 1, (long) 0, null, null);

			MusicRepository musicRepository = new MusicRepository(getActivity());
			music.setFileName(songsVkList.get(0).url);
			musicRepository.editMusic(music);
			musicList.set(position, music);
			
			List<Film> films = new FilmRepository(getActivity()).lookupFilmsForMusic(music);
			if(films.size() > 0)
				imgUrl = films.get(0).getImgUrl();
			
			if (imgUrl.length() > 0)
				Picasso.with(getActivity()).load(imgUrl)
						.placeholder(R.drawable.placeholder).fit().into(image);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (KException e) {
			Log.i(this.getClass().getName(), e.getMessage());
			if (e.getMessage().contains("authorization failed")) {
				startLoginFragment();
			}

			// e.printStackTrace();
		} catch (Exception e) {
			// e.printStackTrace();
		}

	}
	
	private void startLoginFragment() {
		KinoostActivity activity = (KinoostActivity) getActivity();
		Fragment newFragment = new LoginFragment();
		FragmentManager fragmentManager = activity.getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.replace(R.id.content_frame, newFragment);
		transaction.commit();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mp.reset();
		mp.pause();
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		mp.reset();
		mp.pause();
	}
}
