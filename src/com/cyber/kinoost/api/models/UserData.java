package com.cyber.kinoost.api.models;


import java.util.List;
import com.cyber.kinoost.db.models.*;

public class UserData {
    
	private List<Favorites> favorites;
   
    private List<MusicRating> musicrating;
    
    public List<Favorites> getFavorites() {
		return favorites;
	}
	public void setFavorites(List<Favorites> favorites) {
		this.favorites = favorites;
	}
	public List<MusicRating> getmMusicRating() {
		return musicrating;
	}
	public void setMusicRating(List<MusicRating> MusicRating) {
		this.musicrating = MusicRating;
	}
	
	@Override
	public String toString() {
		return "JsonUpdate [favorites=" + favorites + ", musicrating=" + musicrating
				+ "]";
	}
}