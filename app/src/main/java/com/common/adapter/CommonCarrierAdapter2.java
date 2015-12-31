package com.common.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import cn.zjl.zxing.CaptureActivity;

import com.common.base.BaseActivity;
import com.common.base.BaseApplication;
import com.common.base.BasicAdapter;
import com.common.events.ChooseSendCompanyEvent;
import com.common.model.AgentCarrier;
import com.common.model.IntentExtra;
import com.common.ui.CircleImageView;
import com.common.utils.Constant;
import com.common.utils.T;
import com.common.utils.ViewUtils;
import com.gt.activity.CarrierBookActivity1;
import com.gt.activity.CarrierBookActivity2;
import com.gt.activity.ExpressInLibActivity;
import com.gt.activity.InLibPreviewActivity;
import com.gt.inteface.OnViewClickListener;
import com.gt.officeagent.R;

public class CommonCarrierAdapter2 extends BasicAdapter<AgentCarrier> {
	ViewHolder viewHolder = null;
	private int type;
	public CommonCarrierAdapter2(Context context, List<AgentCarrier> data,
			int type) {
		super(context, data);
		this.type=type;
	}

	@Override
	public View getView(final int position, View convertView, final ViewGroup parent) {
	
		ViewUtils viewUtils;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			if (type == Constant.TYPE_COMPANY) {
				viewUtils = new ViewUtils(getContext(),
						R.layout.item_expressinlib_carrier);
			} else {// 有edittext显示的快递公司
				viewUtils = new ViewUtils(getContext(),
						R.layout.item_carrierbook);
				viewHolder.tv_item_carrierbook_Name = viewUtils.view(
						R.id.tv_item_carrierbook_Name, TextView.class);
				viewHolder.tv_item_carrierbook_Phone = viewUtils.view(
						R.id.tv_item_carrierbook_Phone, TextView.class);
			}
			convertView = viewUtils.getRootView();
			viewHolder.civ_expressinlib_carrier_icon = viewUtils.view(
					R.id.civ_expressinlib_carrier_icon, CircleImageView.class);
			viewHolder.iv_item_selector = viewUtils.view(
					R.id.iv_item_selector, ImageView.class);
			
			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final AgentCarrier carrier = getData().get(position);
		BaseApplication.baseApplication.getBitmapUtils().display(
				viewHolder.civ_expressinlib_carrier_icon,
				Constant.CARRIERLOGO_SERVER + carrier.getParentCarrierCode()
						+ ".png");
		if (type == Constant.TYPE_COMPANY_TEXT) {// 有edittext显示的快递公司
			
			viewHolder.tv_item_carrierbook_Name.setText(carrier
					.getParentCarrierName());
			viewHolder.tv_item_carrierbook_Phone.setText("官方客服电话："
					+ carrier.getAgentCarrierPhone());

			// if(position==0){
			// viewUtils.view(R.id.ll_item_carrierbook).setBackgroundColor(getContext().getResources().getColor(R.color.bai_ff));
			// }
		}
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (type == Constant.TYPE_COMPANY_TEXT) {
//					onViewClickListener.doSomeThing(arg0, 0, carrier);
					((CarrierBookActivity2) getContext()).result(0, new IntentExtra(0,carrier));
					((CarrierBookActivity2) getContext()).finish();
				} else {
					for (int j = 0; j < getData().size(); j++) {
						View v=parent.getChildAt(j);
						  if (position == j) {//当前选中的Item改变背景颜色
							  viewHolder.civ_expressinlib_carrier_icon.setBorderColor(getContext().getResources().getColor(R.color.topbar_background));
							  viewHolder.iv_item_selector.setVisibility(View.VISIBLE);
				               } else {
				            	   CircleImageView civ_expressinlib_carrier_icon= (CircleImageView) v.findViewById(R.id.civ_expressinlib_carrier_icon);
				            	   ImageView iv_item_selector= (ImageView) v.findViewById(R.id.iv_item_selector);
				            	   civ_expressinlib_carrier_icon.setBorderColor(getContext().getResources().getColor(R.color.bai_ff));
				            	   iv_item_selector.setVisibility(View.GONE);
				               }
					}
					((ExpressInLibActivity) getContext()).getEventBus().post(
							new ChooseSendCompanyEvent(carrier));
				}
			}
		});

		return convertView;
	}

	class ViewHolder {

		CircleImageView civ_expressinlib_carrier_icon;
		TextView tv_item_carrierbook_Name, tv_item_carrierbook_Phone;
		ImageView iv_item_selector;
	}
}
