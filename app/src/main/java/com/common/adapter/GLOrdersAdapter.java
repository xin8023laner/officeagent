package com.common.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.base.BasicAdapter;
import com.common.model.IntentExtra;
import com.common.model.Order;
import com.common.model.TaskWaybill;
import com.common.utils.CommonUtils;
import com.common.utils.Constant;
import com.common.utils.TimeUtils;
import com.common.utils.ViewUtils;
import com.gt.activity.GLOrdersDetailActivity;
import com.gt.activity.MainActivity;
import com.gt.activity.PickupExpressDetailActivity;
import com.gt.officeagent.R;

public class GLOrdersAdapter extends BasicAdapter<Order> {

	public GLOrdersAdapter(Context context, List<Order> data) {
		super(context, data);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			ViewUtils viewUtils = new ViewUtils(getContext(),
					R.layout.item_glorder);
			convertView = viewUtils.getRootView();
			viewHolder = new ViewHolder();
			viewHolder.tv_orderCode = viewUtils.view(
					R.id.tv_orderCode, TextView.class);
			viewHolder.tv_serviceStatus = viewUtils.view(R.id.tv_serviceStatus,
					TextView.class);
			viewHolder.goodsName = viewUtils.view(R.id.goodsName,
					TextView.class);
			viewHolder.tv_goodsCount = viewUtils.view(R.id.tv_goodsCount,
					TextView.class);
			viewHolder.tv_realPrcie = viewUtils.view(R.id.tv_realPrcie,
					TextView.class);
			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		final Order order = getData().get(position);

		switch (order.getServiceStatus()) {
		case 1://待派送
			viewHolder.tv_serviceStatus.setText("待派送");
			break;
		case 2://配送中
			viewHolder.tv_serviceStatus.setText("配送中");
			break;
		case 3://已完成
			viewHolder.tv_serviceStatus.setText("已完成");
			break;

		default:
			break;
		}
		viewHolder.tv_orderCode.setText("订单编号："+order.getOrderCode());

		viewHolder.goodsName.setText(order.getGoodsName()+"");
		viewHolder.tv_goodsCount.setText("共"+order.getGoodsCount()+"件");

		viewHolder.tv_realPrcie.setText(order.getOrderPrice()+"元");


		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				intent(GLOrdersDetailActivity.class, new IntentExtra(0,order));
			}
		});
		return convertView;
	}

	class ViewHolder {
		
		TextView tv_orderCode,tv_serviceStatus,goodsName,tv_goodsCount,tv_realPrcie;
	}
}
