package com.cyber.kinoost.adapters;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cyber.kinoost.R;
import com.cyber.kinoost.api.Account;
import com.cyber.kinoost.api.ApiHelper;
import com.cyber.kinoost.api.vk.sources.Api;
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

					apiHelper.getSongMusic(mContext, api, music.get(position), "");

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