package cn.yuri.wheelview;

import java.util.List;

import com.common.model.PickUpBySelf;

public class PickupBySelfWheelAdapter implements WheelAdapter {
	private List<PickUpBySelf> items;
	
	public PickupBySelfWheelAdapter(List<PickUpBySelf> items) {
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
		return items.get(index).getPickUpBySelfName();
	}

	@Override
	public int getMaximumLength() {
		return 0;
	}

}
