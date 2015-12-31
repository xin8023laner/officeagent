package com.common.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.base.BasicAdapter;
import com.common.model.AppLog;
import com.common.utils.TimeUtils;
import com.gt.officeagent.R;

public class LogisticsAdapt extends BasicAdapter<AppLog> {
	public List<Item> items;

	public LogisticsAdapt(Context context, List<AppLog> data) {
		super(context, data);
		Collections.sort(getData(), new ListComparator());

		items = new ArrayList<LogisticsAdapt.Item>();
		initItems();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = getLayoutInflater().inflate(R.layout.item_logistics,
					null);

			viewHolder = new ViewHolder();
			viewHolder.iv_logistics_status = (ImageView) convertView
					.findViewById(R.id.iv_logistics_status);
			viewHolder.tv_logistics_data = (TextView) convertView
					.findViewById(R.id.tv_logistics_data);
			viewHolder.tv_logistics_time = (TextView) convertView
					.findViewById(R.id.tv_logistics_time);
			viewHolder.tv_logistics_msg = (TextView) convertView
					.findViewById(R.id.tv_logistics_msg);
			viewHolder.ll_logistics_item = (LinearLayout) convertView
					.findViewById(R.id.ll_logistics_item);
			viewHolder.ll_logistics_section = (LinearLayout) convertView
					.findViewById(R.id.ll_logistics_section);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		final Item item = items.get(position);
		if (item.type == Item.SECTION) {
			viewHolder.ll_logistics_section.setVisibility(View.VISIBLE);
			viewHolder.ll_logistics_item.setVisibility(View.GONE);

			viewHolder.tv_logistics_data.setText(TimeUtils.getData(item.inLog.getTime()));

		} else {
			viewHolder.ll_logistics_section.setVisibility(View.GONE);
			viewHolder.ll_logistics_item.setVisibility(View.VISIBLE);
			viewHolder.tv_logistics_time.setText(TimeUtils.getTime(Long
					.valueOf(item.inLog.getTime()).longValue()));
			viewHolder.tv_logistics_msg.setText(item.inLog.getContent());
		}
		if (position == 1) {
			viewHolder.iv_logistics_status
					.setImageResource(R.drawable.icon_chacunjieguo_red);
			viewHolder.tv_logistics_msg.setTextColor(Color
					.parseColor("#ff6f46"));
			viewHolder.tv_logistics_time.setTextColor(Color
					.parseColor("#ff6f46"));
			viewHolder.tv_logistics_msg.setTextSize(TypedValue.COMPLEX_UNIT_SP,
					16.5f);
			viewHolder.tv_logistics_time.setTextSize(
					TypedValue.COMPLEX_UNIT_SP, 16.5f);
		} else {
			viewHolder.iv_logistics_status
					.setImageResource(R.drawable.icon_chacunjieguo_lv);
			viewHolder.tv_logistics_msg.setTextColor(Color
					.parseColor("#909090"));
			viewHolder.tv_logistics_time.setTextColor(Color
					.parseColor("#909090"));
		}

		return convertView;
	}

	class ViewHolder {
		TextView tv_logistics_data;
		TextView tv_logistics_time;
		TextView tv_logistics_msg;
		ImageView iv_logistics_status;
		LinearLayout ll_logistics_section;
		LinearLayout ll_logistics_item;
	}

	class ListComparator implements Comparator<AppLog> {

		@Override
		public int compare(AppLog lhs, AppLog rhs) {
			if (Long.valueOf(lhs.getTime()).longValue() > Long.valueOf(
					rhs.getTime()).longValue()) {
				return 1;
			} else if (Long.valueOf(lhs.getTime()).longValue() < Long.valueOf(
					rhs.getTime()).longValue()) {
				return -1;
			} else {
				return 0;
			}
		}
	}

	class Item {
		public static final int ITEM = 0;
		public static final int SECTION = 1;
		public int type;
		public AppLog inLog;
		public int reallyPosition;

		public Item(int type, AppLog inLog, int reallyPosition) {
			this.type = type;
			this.inLog = inLog;
			this.reallyPosition = reallyPosition;
		}
	}

	private void initItems() {
		for (int i = 0; i < getData().size(); i++) {

			String time = TimeUtils.getData(
					getData().get(i).getTime().longValue());
			boolean isNewSection = true;
			for (int j = 0; j < items.size(); j++) {

				if (TimeUtils.getData(
						items.get(j).inLog.getTime().longValue())
						.equals(time)) {
					isNewSection = false;
					break;
				}
			}
			if (isNewSection) {
				Item item = new Item(Item.SECTION, getData().get(i), -1);
				items.add(item);
			}
			Item item = new Item(Item.ITEM, getData().get(i), i);
			items.add(item);
		}
	}

}
