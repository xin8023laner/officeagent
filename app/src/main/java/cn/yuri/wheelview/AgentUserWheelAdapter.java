package cn.yuri.wheelview;

import java.util.List;

import com.common.model.AgentUser;

public class AgentUserWheelAdapter implements WheelAdapter {
	private List<AgentUser> items;
	
	public AgentUserWheelAdapter(List<AgentUser> items) {
		super();
		this.items = items;
	}

	@Override
	public int getItemsCount() {
		// TODO Auto-generated method stub
		return items==null?0:items.size();
	}

	@Override
	public String getItem(int index) {
		if(index>=items.size()){
			return "";
		}
		return items.get(index).getAgentUserName();
	}

	@Override
	public int getMaximumLength() {
		return 0;
	}

}
