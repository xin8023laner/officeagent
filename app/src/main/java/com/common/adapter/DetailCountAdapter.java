package com.common.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.common.base.BasicAdapter;
import com.common.model.MyWorkStatistics;
import com.common.utils.ViewUtils;
import com.gt.officeagent.R;

public class DetailCountAdapter extends BasicAdapter<MyWorkStatistics> {

	public DetailCountAdapter(Context context, List<MyWorkStatistics> data) {
		super(context, data);

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			ViewUtils viewUtils = new ViewUtils(getContext(),
					R.layout.item_detailcount_everyday);
			convertView = viewUtils.getRootView();
			viewHolder = new ViewHolder();
			viewHolder.mtv_detailcount_date = viewUtils.view(
					R.id.tv_detailcount_date, TextView.class);
			viewHolder.mtv_detailcount_send = viewUtils.view(
					R.id.tv_detailcount_send, TextView.class);
			viewHolder.mtvdetailcount_receive = viewUtils.view(
					R.id.tv_detailcount_receive, TextView.class);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		final MyWorkStatistics myWorkStatistic = getData().get(position);
		viewHolder.mtv_detailcount_date.setText((position+1)+"日");
		viewHolder.mtv_detailcount_send.setText(myWorkStatistic.getOut());
		viewHolder.mtvdetailcount_receive.setText(myWorkStatistic.getTotal());
		
		
		

		return convertView;
	}

	class ViewHolder {
		TextView mtv_detailcount_date;
		TextView mtv_detailcount_send;
		TextView mtvdetailcount_receive;
	}
}
