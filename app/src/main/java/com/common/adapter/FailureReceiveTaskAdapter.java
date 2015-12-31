package com.common.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.common.base.BasicAdapter;
import com.common.model.AgentReceiveTask;
import com.common.utils.Constant;
import com.common.utils.T;
import com.gt.officeagent.R;

public class FailureReceiveTaskAdapter extends BasicAdapter<String> {

	private int type;
	public FailureReceiveTaskAdapter(Context context,
			List<String> data,int type) {
		super(context, data);
		this.type=type;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vHolder=null;
		if(convertView==null){
			convertView=getLayoutInflater().inflate(R.layout.item_failurereceivetask, null);
			vHolder=new ViewHolder();
			vHolder.tv_item_failureReceiveTask_name=(TextView) convertView.findViewById(R.id.tv_item_failureReceiveTask_name);
			vHolder.tv_item_failureReceiveTask_code=(TextView) convertView.findViewById(R.id.tv_item_failureReceiveTask_code);
			vHolder.tv_item_failureReceiveTask_refresh=(TextView) convertView.findViewById(R.id.tv_item_failureReceiveTask_refresh);
			vHolder.tv_item_failureReceiveTask_failedReason=(TextView) convertView.findViewById(R.id.tv_item_failureReceiveTask_failedReason);
			convertView.setTag(vHolder);
		}else{
			vHolder=(ViewHolder) convertView.getTag();
		}
		
		String str=getData().get(position);
		vHolder.tv_item_failureReceiveTask_name.setVisibility(View.GONE);
		vHolder.tv_item_failureReceiveTask_code.setText(str);
		if(type==Constant.SCAN_KEHUQIANSHOU_MORE||type==Constant.SCAN_PAISONG){
//			vHolder.tv_item_failureReceiveTask_failedReason.setVisibility(View.GONE);
			vHolder.tv_item_failureReceiveTask_failedReason.setText("失败原因：该快件已签收或发生异常");
		}
		vHolder.tv_item_failureReceiveTask_refresh.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				T.showShort(getContext(), "重试");
			}
		});
		return convertView;
	}

	class ViewHolder{
		TextView tv_item_failureReceiveTask_name
		,tv_item_failureReceiveTask_code
		,tv_item_failureReceiveTask_refresh//刷新按钮
		,tv_item_failureReceiveTask_failedReason;
	}
}
