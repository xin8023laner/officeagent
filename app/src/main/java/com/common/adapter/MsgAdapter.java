package com.common.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.base.BasicAdapter;
import com.common.db.PushModel;
import com.common.utils.TimeUtils;
import com.common.utils.ViewUtils;
import com.gt.officeagent.R;

public class MsgAdapter extends BasicAdapter<PushModel> {

	public MsgAdapter(Context context, List<PushModel> data) {
		super(context, data);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			ViewUtils viewUtils = new ViewUtils(getContext(),
					R.layout.item_msg);
			convertView = viewUtils.getRootView();
			viewHolder = new ViewHolder();
			viewHolder.tv_msg_msg = viewUtils.view(
					R.id.tv_msg_msg, TextView.class);
			viewHolder.tv_msg_time = viewUtils.view(
					R.id.tv_msg_time, TextView.class);
			viewHolder.iv_msg_new = viewUtils.view(
					R.id.iv_msg_new, ImageView.class);
			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		PushModel msg = getData().get(position);
		
		
		if(msg.isRead()){
			viewHolder.iv_msg_new.setVisibility(View.GONE);
		}else{
			viewHolder.iv_msg_new.setVisibility(View.VISIBLE);
		}
		viewHolder.tv_msg_msg.setText(msg.getContent());
		viewHolder.tv_msg_time.setText(TimeUtils.getChatTime(msg.getDate()));
		
		return convertView;
	}

	class ViewHolder {
		TextView tv_msg_msg;
		TextView tv_msg_time;
		ImageView iv_msg_new;
	}
}
