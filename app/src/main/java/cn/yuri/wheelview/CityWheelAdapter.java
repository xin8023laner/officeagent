package cn.yuri.wheelview;

import java.util.List;

import com.common.model.CityListItem;





public class CityWheelAdapter implements WheelAdapter {
	private List<CityListItem> items;
	public CityWheelAdapter(List<CityListItem> items) {
		this.items = items;
	}

	@Override
	public int getItemsCount() {
		return items==null?0:items.size();
	}

	@Override
	public String getItem(int index) {
		if (index>=items.size()) {
			return "";
		}
		return items.get(index).getName();
	}

	@Override
	public int getMaximumLength() {
		return 0;
	}

}
