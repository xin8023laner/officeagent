package com.common.adapter;

import java.util.List;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.base.BaseFragmentActivity;
import com.common.base.BasicAdapter;
import com.common.events.CallEvent;
import com.common.model.IntentExtra;
import com.common.model.PickupCallEvent;
import com.common.model.TaskWaybill;
import com.common.utils.CommonUtils;
import com.common.utils.Constant;
import com.common.utils.T;
import com.common.utils.TimeUtils;
import com.common.utils.ViewUtils;
import com.gt.activity.MainActivity;
import com.gt.activity.PickupExpressDetailActivity;
import com.gt.officeagent.R;

public class PickupAdapter extends BasicAdapter<TaskWaybill> {

	private int type =Constant.LIST_PICKUP_PICKUPED;
	private TelephonyManager telephonyManager;
	public PickupAdapter(Context context, List<TaskWaybill> data,int type) {
		super(context, data);
		this.type=type;
		telephonyManager=(TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			ViewUtils viewUtils = new ViewUtils(getContext(),
					R.layout.item_pickup);
			convertView = viewUtils.getRootView();
			viewHolder = new ViewHolder();
			viewHolder.tv_pickup_wishtime = viewUtils.view(
					R.id.tv_pickup_wishtime, TextView.class);
			viewHolder.tv_pickup_ordertime = viewUtils.view(
					R.id.tv_pickup_ordertime, TextView.class);
			viewHolder.tv_pickup_name = viewUtils.view(R.id.tv_pickup_name,
					TextView.class);
			viewHolder.tv_pickup_phone = viewUtils.view(R.id.tv_pickup_phone,
					TextView.class);
			viewHolder.tv_pickup_addr = viewUtils.view(R.id.tv_pickup_addr,
					TextView.class);
			viewHolder.iv_pickup_call = viewUtils.view(R.id.iv_pickup_call,
					ImageView.class);
			viewHolder.tv_pickup_status = viewUtils.view(R.id.tv_pickup_status,
					TextView.class);
			viewHolder.ll_pickup_wishtime = viewUtils.view(
					R.id.ll_pickup_wishtime, LinearLayout.class);
			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		final TaskWaybill taskWaybill = getData().get(position);
		//T.showShort(getContext(), taskWaybill.getExpressCode());
		viewHolder.tv_pickup_ordertime.setText(TimeUtils
				.getTime(taskWaybill.getSendTime()));

		viewHolder.tv_pickup_name.setText(taskWaybill.getSenderName());
		viewHolder.tv_pickup_phone.setText(TextUtils.isEmpty(taskWaybill
				.getSenderTelphone()) ? taskWaybill.getSenderPhone()
				: taskWaybill.getSenderTelphone());
		viewHolder.tv_pickup_addr.setText(taskWaybill.getSenderAddress());

		if (type == Constant.LIST_PICKUP_UNPICAUP) {
			viewHolder.ll_pickup_wishtime.setVisibility(View.VISIBLE);
			viewHolder.tv_pickup_wishtime.setText("期望取件时间："
					+ taskWaybill.getAppointmentTime());

			viewHolder.tv_pickup_status.setVisibility(View.GONE);
		} else {
			viewHolder.ll_pickup_wishtime.setVisibility(View.GONE);
			viewHolder.tv_pickup_status.setVisibility(View.VISIBLE);
			//根据状态值判断,TODO
			if(taskWaybill.getStatus()==2){//已入库
				viewHolder.tv_pickup_status.setText("揽件已入库");
			}else{
			viewHolder.tv_pickup_status.setText("揽收成功");
			}
		}

		viewHolder.iv_pickup_call.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(telephonyManager.getSimState()==TelephonyManager.SIM_STATE_ABSENT){
					T.showShort(getContext(), "无SIM卡！");
				}else{
				CommonUtils.call(getContext(), TextUtils.isEmpty(taskWaybill
						.getSenderTelphone()) ? taskWaybill.getSenderPhone()
								: taskWaybill.getSenderTelphone());
				}
			}
		});
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				intent(PickupExpressDetailActivity.class, new IntentExtra(type,taskWaybill));
				//T.showShort(getContext(), taskWaybill.getExpressCode());
			}
		});
		return convertView;
	}

	class ViewHolder {
		TextView tv_pickup_ordertime;
		TextView tv_pickup_wishtime;
		TextView tv_pickup_name;
		TextView tv_pickup_phone;
		TextView tv_pickup_addr;
		TextView tv_pickup_status;
		LinearLayout ll_pickup_wishtime;
		ImageView iv_pickup_call;
	}
}
