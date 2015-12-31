package com.common.adapter;

import java.util.List;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.zjl.zxing.CaptureActivity;

import com.common.base.BasicAdapter;
import com.common.events.ChangeContactPhoneEvent;
import com.common.model.AgentContactCustomer;
import com.common.model.AgentReceiveTask;
import com.common.utils.CommonUtils;
import com.common.utils.Constant;
import com.common.utils.T;
import com.common.utils.ViewUtils;
import com.gt.activity.CapturePDAActivity;
import com.gt.officeagent.R;

public class ScanContactAdapter extends BasicAdapter<AgentContactCustomer> {

	private TelephonyManager telephonyManager;
	private boolean isShow=false;
	public ScanContactAdapter(Context context,List<AgentContactCustomer> data) {
		super(context, data);
		telephonyManager=(TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
			ViewUtils viewUtils;
				viewUtils = new ViewUtils(getContext(),
						R.layout.item_contact);
				convertView = viewUtils.getRootView();
				final LinearLayout ll_ScanContactAdapter_btnCompel=viewUtils.view(R.id.ll_ScanContactAdapter_btnCompel,LinearLayout.class);
				LinearLayout ll_item_contact=viewUtils.view(R.id.ll_item_contact,LinearLayout.class);
				TextView tv_ScanContactAdapter_carriername = viewUtils.view(
						R.id.tv_ScanContactAdapter_carriername, TextView.class);
				TextView tv_ScanContactAdapter_expresscode = viewUtils.view(
						R.id.tv_ScanContactAdapter_expresscode, TextView.class);
				TextView tv_ScanContactAdapter_phone = viewUtils.view(
						R.id.tv_ScanContactAdapter_phone, TextView.class);
				TextView tv_ScanContactAdapter_bulu=viewUtils.view(R.id.tv_ScanContactAdapter_bulu, Button.class);
				final AgentContactCustomer agentContactCustomer=getData().get(position);
				tv_ScanContactAdapter_carriername.setText(agentContactCustomer.getCarrierName());
				tv_ScanContactAdapter_expresscode.setText(agentContactCustomer.getExpressCode());
				if(TextUtils.isEmpty(agentContactCustomer.getReceiverPhone())){
					tv_ScanContactAdapter_phone.setVisibility(View.GONE);
					tv_ScanContactAdapter_bulu.setVisibility(View.VISIBLE);
					ll_ScanContactAdapter_btnCompel.setVisibility(View.GONE);
				}else{
					tv_ScanContactAdapter_phone.setVisibility(View.VISIBLE);
					tv_ScanContactAdapter_phone.setText(agentContactCustomer.getReceiverPhone());
					tv_ScanContactAdapter_bulu.setVisibility(View.GONE);
					ll_ScanContactAdapter_btnCompel.setVisibility(View.VISIBLE);
				}
				viewUtils.view(R.id.btn_ScanContactAdapter_excep).setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO 异常件处理
						if(Constant.ISSCAN){
							((CapturePDAActivity)getContext()).getEventBus().post(new ChangeContactPhoneEvent(position,agentContactCustomer,ChangeContactPhoneEvent.TYPE_CONSTANTS_EXCEP));
						}else{
							((CaptureActivity)getContext()).getEventBus().post(new ChangeContactPhoneEvent(position,agentContactCustomer,ChangeContactPhoneEvent.TYPE_CONSTANTS_EXCEP));
						}
					}
				});
				viewUtils.view(R.id.btn_ScanContactAdapter_editCall).setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO 编辑电话
						if(Constant.ISSCAN){
							((CapturePDAActivity)getContext()).getEventBus().post(new ChangeContactPhoneEvent(position,agentContactCustomer,ChangeContactPhoneEvent.TYPE_CONSTANTS_EDITPHONE));
						}else{
							((CaptureActivity)getContext()).getEventBus().post(new ChangeContactPhoneEvent(position,agentContactCustomer,ChangeContactPhoneEvent.TYPE_CONSTANTS_EDITPHONE));
						}
					}
				});
				viewUtils.view(R.id.btn_ScanContactAdapter_call).setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO 拨打电话
						if(telephonyManager.getSimState()==TelephonyManager.SIM_STATE_ABSENT){
							T.showShort(getContext(), "无SIM卡！");
						}else{
							if(Constant.ISSCAN){
								CommonUtils.call(((CapturePDAActivity)getContext()),agentContactCustomer.getReceiverPhone());
							}else{
								CommonUtils.call(((CaptureActivity)getContext()),agentContactCustomer.getReceiverPhone());
							}
						}
					}
				});
				tv_ScanContactAdapter_bulu.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO 补录
						if(Constant.ISSCAN){
							((CapturePDAActivity)getContext()).getEventBus().post(new ChangeContactPhoneEvent(position,agentContactCustomer,ChangeContactPhoneEvent.TYPE_CONSTANTS_SUPPLEINFO));
						}else{
							((CaptureActivity)getContext()).getEventBus().post(new ChangeContactPhoneEvent(position,agentContactCustomer,ChangeContactPhoneEvent.TYPE_CONSTANTS_SUPPLEINFO));
						}
					}
				});
//				ll_item_contact.setOnClickListener(new OnClickListener() {
//					
//					@Override
//					public void onClick(View v) {
//						// TODO Auto-generated method stub
//						if(TextUtils.isEmpty(agentReceiveTask.getReceiverPhone())){
//							isShow=false;
//							ll_ScanContactAdapter_btnCompel.setVisibility(View.GONE);
//						}else{
//						if(isShow){
//							ll_ScanContactAdapter_btnCompel.setVisibility(View.GONE);
//							isShow=false;
//						}else{
//							ll_ScanContactAdapter_btnCompel.setVisibility(View.VISIBLE);
//							isShow=true;
//						}
//						}
//					}
//				});
		return convertView;
	}
}
