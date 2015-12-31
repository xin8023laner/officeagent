package com.common.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.base.BasicAdapter;
import com.common.model.Agent;
import com.gt.inteface.OnViewClickListener;
import com.gt.officeagent.R;

public class AgentAdapter extends BasicAdapter<Agent> {

	private OnViewClickListener onViewClickListener;
	public AgentAdapter(Context context, List<Agent> nearAgent,OnViewClickListener onViewClickListener) {
		super(context, nearAgent);
		this.onViewClickListener=onViewClickListener;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = getLayoutInflater().inflate(
					R.layout.item_searchagent, null);
			viewHolder = new ViewHolder();
			viewHolder.iv_toagentdetails = (ImageView) convertView
					.findViewById(R.id.iv_toAgentdetails);
			viewHolder.tv_agentName = (TextView) convertView
					.findViewById(R.id.tv_agentname);
			viewHolder.tv_agentAddress = (TextView) convertView
					.findViewById(R.id.tv_agentaddress);
			convertView.setTag(viewHolder);  
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		final Agent agent = getData().get(position);
		viewHolder.tv_agentName.setText(agent.getName());
		viewHolder.tv_agentAddress.setText(agent.getAddress());
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onViewClickListener.doSomeThing(v, 0, agent);
			}
		});
		return convertView;
	}

	class ViewHolder {
		TextView tv_agentName;
		TextView tv_agentAddress;
		ImageView iv_toagentdetails;
	}
}
