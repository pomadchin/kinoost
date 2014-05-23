package com.cyber.kinoost.adapters;

import java.sql.SQLException;
import java.util.*;

import android.content.Context;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;

import com.cyber.kinoost.R;
import com.cyber.kinoost.api.*;
import com.cyber.kinoost.api.vk.sources.Api;
import com.cyber.kinoost.db.models.*;
import com.cyber.kinoost.db.repositories.*;

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
		public TextView name;
	}
    
	public View getView(final int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ViewHolder holder;
		if (row == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			row = inflater.inflate(R.layout.ost_row, parent, false);
			holder = new ViewHolder();
			holder.image = (ImageView) row.findViewById(R.id.image);
			holder.name = (TextView) row.findViewById(R.id.name);
			row.setTag(holder);
			RelativeLayout musicRow = (RelativeLayout) row;
			
			musicRow.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Log.i("ListAdapter", music.get(position).getName());
					Account account = new Account(mContext);
					Api api = new Api(account);
					
					String imgUrl = "";
					
					try {
						FilmRepository filmRepo = new FilmRepository(mContext);
						List<Film> film = filmRepo.lookupFilmsForMusic(music.get(position));
						if(film.size() > 0) {
							imgUrl = film.get(0).getImgUrl();
						}
					} catch (SQLException e) {
						imgUrl = "";
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					apiHelper.getSongMusic(mContext, api, music.get(position),
							(ArrayList<Music>) music, imgUrl, position);

				}
			});

		} else
			holder = (ViewHolder) row.getTag();		
		Music song = music.get(position);
		Performer performer = song.getPerformer();
		String performerName = (performer != null && performer.getName() != null) ? performer.getName() + " - " : "";
		holder.name.setText(performerName + song.getName());
		holder.name.setSelected(true);
		holder.image.setImageResource(R.drawable.music);
		return row;
		
	}


}