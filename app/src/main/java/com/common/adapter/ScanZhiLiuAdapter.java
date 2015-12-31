package com.common.adapter;


import java.text.Collator;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import cn.zjl.zxing.CaptureActivity;

import com.common.base.BaseApplication;
import com.common.base.BasicAdapter;
import com.common.model.AgentReceiveTask;
import com.common.model.IntentExtra;
import com.common.utils.Constant;
import com.common.utils.DialogUtils;
import com.common.utils.ViewUtils;

import com.gt.activity.SendExpressDetailActivity;
import com.gt.activity.ZhiLiuActivity;
import com.gt.officeagent.R;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

public class ScanZhiLiuAdapter extends BasicAdapter<AgentReceiveTask> {
	
	

	public ScanZhiLiuAdapter(Context context, List<AgentReceiveTask> data
			) {
		super(context, data);
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		
			ViewUtils viewUtils = new ViewUtils(getContext(),
					R.layout.item_scan);
			convertView = viewUtils.getRootView();
			
			final TextView tv_scanadapter_carriername = viewUtils.view(
					R.id.tv_scanadapter_carriername, TextView.class);
			TextView tv_scanadapter_expresscode = viewUtils.view(
					R.id.tv_scanadapter_expresscode, TextView.class);
			TextView tv_scanadapter_del = viewUtils.view(
					R.id.tv_scanadapter_del, TextView.class);
			
		


		final AgentReceiveTask agentReceiveTask = getData().get(position);
		/*
		 * if (agentReceiveTask.getReceiverPhone() != null) {
		 * viewHolder.tv_scanadapter_carriername.setText(agentReceiveTask
		 * .getReceiverPhone());
		 * viewHolder.tv_scanadapter_carriername.setBackground(null); } else {
		 * 
		 * }
		 */
		tv_scanadapter_expresscode.setText(agentReceiveTask
				.getExpressCode());
				tv_scanadapter_carriername.setText("滞留处理");
//		tv_scanadapter_del.setVisibility(View.GONE);
				tv_scanadapter_del.setText(agentReceiveTask.getCarrierName());
				tv_scanadapter_del.setTextColor(Color.BLACK);
		convertView
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						intent(ZhiLiuActivity.class, new IntentExtra(
								Constant.SCAN_ZHILIUJIANCHULI, agentReceiveTask));
						//((CaptureActivity)getContext()).finish();
					}
				});

		return convertView;
	}

	class ViewHolder {
		TextView tv_scanadapter_carriername;
		TextView tv_scanadapter_expresscode;
		TextView tv_scanadapter_del;
	}
}
