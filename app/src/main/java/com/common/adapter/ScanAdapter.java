package com.common.adapter;

import java.util.List;

import android.content.Context;
import android.support.annotation.VisibleForTesting;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.zjl.zxing.CaptureActivity;

import com.common.base.BasicAdapter;
import com.common.events.ScanResultEvent;
import com.common.model.AgentReceiveTask;
import com.common.model.AgentWaybill;
import com.common.utils.Constant;
import com.common.utils.ViewUtils;
import com.gt.activity.CapturePDAActivity;
import com.gt.officeagent.R;

public class ScanAdapter extends BasicAdapter<AgentReceiveTask> {

	public   boolean isPreView=false; 
	private int type;
	public ScanAdapter(Context context, List<AgentReceiveTask> data,boolean isPreView,int type) {
		super(context, data);
		this.isPreView=isPreView;
		this.type=type;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
			ViewUtils viewUtils = new ViewUtils(getContext(),
					R.layout.item_scan);
			convertView = viewUtils.getRootView();
			TextView tv_scanadapter_carriername = viewUtils.view(
					R.id.tv_scanadapter_carriername, TextView.class);
			TextView tv_scanadapter_expresscode = viewUtils.view(
					R.id.tv_scanadapter_expresscode, TextView.class);
			TextView tv_scanadapter_del = viewUtils.view(
					R.id.tv_scanadapter_del, TextView.class);
			TextView tv_scanadapter_bulu = viewUtils.view(
					R.id.tv_scanadapter_bulu, TextView.class);
			TextView tv_scanadapter_phone = viewUtils.view(
					R.id.tv_scanadapter_phone, TextView.class);
			TextView tv_scanadapter_phone2 = viewUtils.view(
					R.id.tv_scanadapter_phone2, TextView.class);
		
		final AgentReceiveTask agentReceiveTask = getData().get(position);
		if(type==Constant.SCAN_KEHUQIANSHOU_MORE){
//			tv_scanadapter_carriername.setVisibility(View.GONE);
			tv_scanadapter_bulu.setVisibility(View.GONE);
			if(TextUtils.isEmpty(agentReceiveTask.getCarrierName())){
				tv_scanadapter_expresscode.setText("单号："+agentReceiveTask.getExpressCode());
			}else{
				tv_scanadapter_expresscode.setText(agentReceiveTask.getCarrierName()+":"+agentReceiveTask.getExpressCode());
			}
		}else if(type==Constant.SCAN_PAISONG){
			if(TextUtils.isEmpty(agentReceiveTask.getCarrierName())){
				tv_scanadapter_expresscode.setText("单号："+agentReceiveTask.getExpressCode());
			}else{
				tv_scanadapter_expresscode.setText(agentReceiveTask.getCarrierName()+":"+agentReceiveTask.getExpressCode());
			}
			if(!TextUtils.isEmpty(agentReceiveTask.getReceiverPhone())){
//				tv_scanadapter_bulu.setText(agentReceiveTask.getReceiverPhone());
				tv_scanadapter_phone2.setVisibility(View.VISIBLE);
				tv_scanadapter_phone2.setText("电话："+agentReceiveTask.getReceiverPhone());
				tv_scanadapter_bulu.setVisibility(View.GONE);
			}else{
				tv_scanadapter_bulu.setVisibility(View.VISIBLE);
				tv_scanadapter_phone2.setVisibility(View.GONE);
			}
		}else{
			tv_scanadapter_bulu.setVisibility(View.VISIBLE);
//		tv_scanadapter_carriername.setText(agentReceiveTask.getCarrierName());
		tv_scanadapter_expresscode.setText(agentReceiveTask.getExpressCode());
		if(!TextUtils.isEmpty(agentReceiveTask.getReceiverPhone())){
			tv_scanadapter_bulu.setText(agentReceiveTask.getReceiverPhone());
		}
		}
		if(isPreView){
			tv_scanadapter_del.setVisibility(View.GONE);
			tv_scanadapter_bulu.setVisibility(View.GONE);
			tv_scanadapter_carriername.setVisibility(View.VISIBLE);
			tv_scanadapter_carriername.setText((position+1)+"");
			tv_scanadapter_carriername.setTextColor(getContext().getResources().getColor(R.color.color_ff6f46));
			if(!TextUtils.isEmpty(agentReceiveTask.getReceiverPhone())){
				tv_scanadapter_phone.setVisibility(View.VISIBLE);
				tv_scanadapter_phone.setText(agentReceiveTask.getReceiverPhone());
			}else{
				tv_scanadapter_phone.setVisibility(View.GONE);
			}
		}
		tv_scanadapter_del.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				remove(position);
				if(Constant.ISSCAN){
					((CapturePDAActivity)getContext()).getEventBus().post(new ScanResultEvent(agentReceiveTask,ScanResultEvent.TYPE_SCANREMOVE));
				}else{
					((CaptureActivity)getContext()).getEventBus().post(new ScanResultEvent(agentReceiveTask,ScanResultEvent.TYPE_SCANREMOVE));
				}
			}
		});
		tv_scanadapter_bulu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//补录电话号
				if(Constant.ISSCAN){
					((CapturePDAActivity)getContext()).getEventBus().post(new ScanResultEvent(agentReceiveTask,ScanResultEvent.TYPE_SCANBULU));
				}else{
					((CaptureActivity)getContext()).getEventBus().post(new ScanResultEvent(agentReceiveTask,ScanResultEvent.TYPE_SCANBULU));
				}
			}
		});
		tv_scanadapter_phone2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//补录电话号
				if(Constant.ISSCAN){
					((CapturePDAActivity)getContext()).getEventBus().post(new ScanResultEvent(agentReceiveTask,ScanResultEvent.TYPE_SCANBULU));
				}else{
					((CaptureActivity)getContext()).getEventBus().post(new ScanResultEvent(agentReceiveTask,ScanResultEvent.TYPE_SCANBULU));
				}
			}
		});
		return convertView;
	}
}
