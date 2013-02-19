package biz.remanso.redmine.util;

import java.util.List;

import biz.remanso.redmine.ui.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ListItemAdapter extends BaseAdapter {
	
	private List<CustomListItem> items;
	private Context context;
	private int numItems = 0;
	
	public ListItemAdapter( Context context, List<CustomListItem> items ) {
		 this.items = items;
		 this.context = context;
		 this.numItems = items.size();
	}

	@Override
	public int getCount() {
		return this.numItems;
	}

	@Override
	public CustomListItem getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		CustomListItem item = items.get(position);
		RelativeLayout itemLayout = (RelativeLayout) LayoutInflater.from(this.context).inflate(R.layout.list_item, parent, false);
		
		TextView tvDescription = (TextView) itemLayout.findViewById(R.id.tvDescription);
		tvDescription.setText(item.getDescription());
		
		return itemLayout;
		
	}

}
