package cn.yuri.wheelview;

import java.util.List;

import com.common.model.WheelBaseItem;





public class BaseWheelAdapter implements WheelAdapter {
	private List<WheelBaseItem> items;
	public BaseWheelAdapter(List<WheelBaseItem> items) {
		this.items = items;
	}

	@Override
	public int getItemsCount() {
		return items==null?0:items.size();
	}

	@Override
	public String getItem(int index) {
		return items.get(index).getName();
	}

	@Override
	public int getMaximumLength() {
		return 0;
	}

}
