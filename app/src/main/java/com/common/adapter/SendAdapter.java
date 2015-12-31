package com.common.adapter;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.base.BaseFragmentActivity;
import com.common.base.BasicAdapter;
import com.common.events.CallEvent;
import com.common.model.AgentReceiveTask;
import com.common.model.AgentWaybill;
import com.common.model.IntentExtra;
import com.common.model.PickupCallEvent;
import com.common.model.SendCallEvent;
import com.common.utils.AppUtils;
import com.common.utils.CommonUtils;
import com.common.utils.Constant;
import com.common.utils.T;
import com.common.utils.TimeUtils;
import com.common.utils.ViewUtils;
import com.gt.activity.MainActivity;
import com.gt.activity.SendExpressDetailActivity;
import com.gt.fragment.SendFragment;
import com.gt.officeagent.R;

public class SendAdapter extends BasicAdapter<AgentReceiveTask> {

	private int type = Constant.LIST_SEND_FAILED;
	private TelephonyManager telephonyManager;
	public SendAdapter(Context context, List<AgentReceiveTask> data, int type) {
		super(context, data);
		this.type = type;
		telephonyManager=(TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			ViewUtils viewUtils = new ViewUtils(getContext(),
					R.layout.item_send);
			convertView = viewUtils.getRootView();
			viewHolder = new ViewHolder();
			viewHolder.tv_send_carriername = viewUtils.view(
					R.id.tv_send_carriername, TextView.class);
			viewHolder.tv_send_expresscode = viewUtils.view(
					R.id.tv_send_expresscode, TextView.class);
			viewHolder.tv_send_time = viewUtils.view(R.id.tv_send_time,
					TextView.class);
			viewHolder.tv_send_name = viewUtils.view(R.id.tv_send_name,
					TextView.class);
			viewHolder.tv_send_phone = viewUtils.view(R.id.tv_send_phone,
					TextView.class);
			viewHolder.tv_send_addr = viewUtils.view(R.id.tv_send_addr,
					TextView.class);
			viewHolder.iv_send_call = viewUtils.view(R.id.iv_send_call,
					ImageView.class);
			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		final AgentReceiveTask agentReceiveTask = getData().get(position);
		viewHolder.tv_send_carriername.setText(agentReceiveTask
				.getCarrierName());
		viewHolder.tv_send_expresscode.setText(agentReceiveTask
				.getExpressCode());
		viewHolder.tv_send_time.setText(TimeUtils.getTime(agentReceiveTask
				.getUpdateTime()));

		if (type == Constant.LIST_SEND_FAILED) {
			viewHolder.tv_send_name.setVisibility(View.VISIBLE);
			try {// 拒收原因
				String json = agentReceiveTask.getStatusDesc().replaceAll("”",
						"\"");
				String endJson = json.replaceAll("“", "\"");
				JSONArray array = new JSONArray(endJson);
				JSONObject object = new JSONObject(array.getString(array
						.length() - 1));
				viewHolder.tv_send_name.setText(object.getString("desc"));

			} catch (Exception e) {
				viewHolder.tv_send_name.setText("暂无失败原因");
				e.printStackTrace();
			}

			viewHolder.tv_send_addr.setVisibility(View.GONE);
			viewHolder.tv_send_phone.setVisibility(View.GONE);
			viewHolder.iv_send_call.setVisibility(View.GONE);
		} else if(type == Constant.LIST_SEND_SUCCESS){
			viewHolder.iv_send_call.setVisibility(View.GONE);
			viewHolder.tv_send_name.setText(TextUtils.isEmpty(agentReceiveTask
					.getReceiverName()) ? "姓名不详" : agentReceiveTask
					.getReceiverName());
			if (TextUtils.isEmpty(agentReceiveTask.getReceiverPhone())
					&& TextUtils.isEmpty(agentReceiveTask.getReceiverPhone())) {
				viewHolder.tv_send_phone.setVisibility(View.GONE);
//				viewHolder.iv_send_call.setVisibility(View.GONE);
			} else {
				viewHolder.tv_send_phone.setVisibility(View.VISIBLE);
//				viewHolder.iv_send_call.setVisibility(View.VISIBLE);
				viewHolder.tv_send_phone
						.setText(TextUtils.isEmpty(agentReceiveTask
								.getReceiverPhone()) ? agentReceiveTask
								.getReceiverPhone() : agentReceiveTask
								.getReceiverPhone());
			}

			if (TextUtils.isEmpty(agentReceiveTask.getReceiverAddress())) {
				if (TextUtils.isEmpty(agentReceiveTask.getReceiverPhone())
						&& TextUtils.isEmpty(agentReceiveTask.getReceiverPhone())) {
					viewHolder.tv_send_addr.setVisibility(View.GONE);
				}else{
					viewHolder.tv_send_addr.setVisibility(View.INVISIBLE);
				}
				
			} else {
				viewHolder.tv_send_addr.setVisibility(View.VISIBLE);
				viewHolder.tv_send_addr.setText(agentReceiveTask
						.getReceiverAddress());
			}
		}else {
			

			viewHolder.tv_send_name.setText(TextUtils.isEmpty(agentReceiveTask
					.getReceiverName()) ? "姓名不详" : agentReceiveTask
					.getReceiverName());
			if (TextUtils.isEmpty(agentReceiveTask.getReceiverPhone())
					&& TextUtils.isEmpty(agentReceiveTask.getReceiverPhone())) {
				viewHolder.tv_send_phone.setVisibility(View.GONE);
				viewHolder.iv_send_call.setVisibility(View.GONE);
			} else {
				viewHolder.tv_send_phone.setVisibility(View.VISIBLE);
				viewHolder.iv_send_call.setVisibility(View.VISIBLE);
				viewHolder.tv_send_phone
						.setText(TextUtils.isEmpty(agentReceiveTask
								.getReceiverPhone()) ? agentReceiveTask
								.getReceiverPhone() : agentReceiveTask
								.getReceiverPhone());
			}

			if (TextUtils.isEmpty(agentReceiveTask.getReceiverAddress())) {
				if (TextUtils.isEmpty(agentReceiveTask.getReceiverPhone())
						&& TextUtils.isEmpty(agentReceiveTask.getReceiverPhone())) {
					viewHolder.tv_send_addr.setVisibility(View.GONE);
				}else{
					viewHolder.tv_send_addr.setVisibility(View.INVISIBLE);
				}
				
			} else {
				viewHolder.tv_send_addr.setVisibility(View.VISIBLE);
				viewHolder.tv_send_addr.setText(agentReceiveTask
						.getReceiverAddress());
			}
		}

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				intent(SendExpressDetailActivity.class, new IntentExtra(type,
						agentReceiveTask));
			}
		});
		viewHolder.iv_send_call.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(telephonyManager.getSimState()==TelephonyManager.SIM_STATE_ABSENT){
					T.showShort(getContext(), "无SIM卡！");
				}else{
				CommonUtils.call(
						getContext(),
						TextUtils.isEmpty(agentReceiveTask.getReceiverPhone()) ? agentReceiveTask
								.getReceiverPhone() : agentReceiveTask
								.getReceiverPhone());
				if(type == Constant.LIST_SEND_PAISONG){
					((BaseFragmentActivity)getContext()).getEventBus().post(new SendCallEvent(agentReceiveTask));
				}
				}
			}
		});
		return convertView;
	}

	class ViewHolder {
		TextView tv_send_carriername;
		TextView tv_send_expresscode;
		TextView tv_send_time;
		TextView tv_send_name;
		TextView tv_send_phone;
		TextView tv_send_addr;
		ImageView iv_send_call;
	}
}
