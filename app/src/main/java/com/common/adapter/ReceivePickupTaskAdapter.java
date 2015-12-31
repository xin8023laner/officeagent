package com.common.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.greenrobot.eventbus.EventBus;

import com.common.base.BasicAdapter;
import com.common.model.TaskWaybill;
import com.common.utils.T;
import com.common.utils.ViewUtils;
import com.gt.activity.ReceivePickupTaskActivity;
import com.gt.inteface.AdapterItemClickEvent;
import com.gt.officeagent.R;

public class ReceivePickupTaskAdapter extends BasicAdapter<TaskWaybill> {

	public ReceivePickupTaskAdapter(Context context, List<TaskWaybill> data) {
		super(context, data);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			ViewUtils viewUtils = new ViewUtils(getContext(),
					R.layout.item_receivepickuptask);
			convertView = viewUtils.getRootView();
			viewHolder = new ViewHolder();
			viewHolder.tv_receivepickup_name = viewUtils.view(
					R.id.tv_receivepickup_name, TextView.class);
			viewHolder.tv_receivepickup_phone = viewUtils.view(
					R.id.tv_receivepickup_phone, TextView.class);
			viewHolder.tv_receivepickup_addr = viewUtils.view(
					R.id.tv_receivepickup_addr, TextView.class);
			viewHolder.tv_receivepickup_time = viewUtils.view(
					R.id.tv_receivepickup_time, TextView.class);
			viewHolder.ll_receivepickup_submit = viewUtils.view(
					R.id.ll_receivepickup_submit, LinearLayout.class);
			viewHolder.ll_receivepickup_back = viewUtils.view(
					R.id.ll_receivepickup_back, LinearLayout.class);
			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		final TaskWaybill taskWaybill = getData().get(position);
		viewHolder.tv_receivepickup_name.setText(taskWaybill.getSenderName());
		viewHolder.tv_receivepickup_phone.setText(taskWaybill
				.getSenderTelphone());
		viewHolder.tv_receivepickup_addr.setText(taskWaybill
				.getSenderAddress());
		viewHolder.tv_receivepickup_time.setText("期望取件时间："
				+ taskWaybill.getAppointmentTime());
		viewHolder.ll_receivepickup_submit
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						EventBus.getDefault()
								.post(new AdapterItemClickEvent(taskWaybill,position,
										AdapterItemClickEvent.RECEIVETASKTYPE_SUBMIT));
					}
				});
		viewHolder.ll_receivepickup_back
		.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				EventBus.getDefault()
				.post(new AdapterItemClickEvent(taskWaybill,position,
						AdapterItemClickEvent.RECEIVETASKTYPE_BACK));
			}
		});
		return convertView;
	}

	class ViewHolder {
		TextView tv_receivepickup_name;
		TextView tv_receivepickup_phone;
		TextView tv_receivepickup_addr;
		TextView tv_receivepickup_time;
		LinearLayout ll_receivepickup_submit,ll_receivepickup_back;
	}
}
