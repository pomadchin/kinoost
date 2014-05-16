package com.cyber.kinoost.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyber.kinoost.R;
import com.cyber.kinoost.api.ApiHelper;
import com.cyber.kinoost.db.models.Music;
import com.cyber.kinoost.db.models.Performer;

public class OstListViewAdapter extends BaseAdapter {
    
	private Context mContext;
    
	private List<Music> music;
	
	final ApiHelper apiHelper = new ApiHelper();
	
    public OstListViewAdapter(Context c, List<Music> sounds) {
        mContext = c;
        this.music = sounds;
    }

    public int getCount() {
        return music.size();
    }

    public Object getItem(int position) {
        return music.get(position);
    }

    public long getItemId(int position) {
        return position;
    }
    
    public static class ViewHolder
	{
		public ImageView image;
		public TextView performer;
		public TextView songName;
	}
    
	public View getView(final int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ViewHolder holder;
		if (row == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			row = inflater.inflate(R.layout.ost_row, parent, false);
			holder = new ViewHolder();
			holder.image = (ImageView) row.findViewById(R.id.image);
			holder.songName = (TextView) row.findViewById(R.id.song_name);
			holder.performer = (TextView) row.findViewById(R.id.performer);
			row.setTag(holder);

		} else
			holder = (ViewHolder) row.getTag();		
		Music song = music.get(position);
		Performer performer = song.getPerformer();
		String performerName = (performer != null) ? performer.getName() : "";
		performerName = (performerName == null) ? "Performer" : performerName; 
		holder.songName.setText(song.getName());
		holder.performer.setText(performerName);
		holder.image.setImageResource(R.drawable.music);
		return row;
		
	}


}