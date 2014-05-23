package com.cyber.kinoost.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cyber.kinoost.R;
import com.cyber.kinoost.adapters.OstListViewAdapter.ViewHolder;
import com.cyber.kinoost.api.Account;
import com.cyber.kinoost.api.ApiHelper;
import com.cyber.kinoost.api.vk.sources.Api;
import com.cyber.kinoost.db.models.Music;
import com.cyber.kinoost.db.models.Performer;
import com.cyber.kinoost.paging.listview.PagingBaseAdapter;

public class PagingMusicAdapter extends PagingBaseAdapter<Music> {
	
	private Context context;
	
	final ApiHelper apiHelper = new ApiHelper();
	
	public PagingMusicAdapter(Context c) {
		this.context = c;
	}
	
	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Music getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ViewHolder holder;
		if (row == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			row = inflater.inflate(R.layout.ost_row, parent, false);
			holder = new ViewHolder();
			holder.image = (ImageView) row.findViewById(R.id.image);
			holder.name = (TextView) row.findViewById(R.id.name);
			row.setTag(holder);
			RelativeLayout musicRow = (RelativeLayout) row;
			musicRow.setOnClickListener(new OnClickListener() {
	             @Override
	             public void onClick(View v) {
					Log.i("ListAdapter", getItem(position).getName());
					Account account = new Account(context);
					Api api = new Api(account);

					apiHelper.getSongMusic(context, api, getItem(position), "");

	             }
	         });

		} else
			holder = (ViewHolder) row.getTag();		
		Music song = getItem(position);
		Performer performer = song.getPerformer();
		String performerName = (performer != null && performer.getName() != null) ? performer.getName() + " - " : "";
		holder.name.setText(performerName + song.getName());
		holder.name.setSelected(true);
		holder.image.setImageResource(R.drawable.music);
		return row;
		
	}

}
