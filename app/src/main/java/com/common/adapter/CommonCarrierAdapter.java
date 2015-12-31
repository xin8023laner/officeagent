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
import com.gt.activity.ExpressInLibActivity;
import com.gt.activity.InLibPreviewActivity;
import com.gt.inteface.OnViewClickListener;
import com.gt.officeagent.R;

public class CommonCarrierAdapter extends BasicAdapter<AgentCarrier> {

	private int type;
	public CommonCarrierAdapter(Context context, List<AgentCarrier> data,
			int type) {
		super(context, data);
		this.type=type;
	}

	@Override
	public View getView(final int position, View convertView, final ViewGroup parent) {
		
		ViewUtils viewUtils;
		
		
		TextView tv_item_carrierbook_Name = null,tv_item_carrierbook_Phone = null;
			if (type == Constant.TYPE_COMPANY) {
				viewUtils = new ViewUtils(getContext(),
						R.layout.item_expressinlib_carrier);
			} else {// 有edittext显示的快递公司
				viewUtils = new ViewUtils(getContext(),
						R.layout.item_carrierbook);
				tv_item_carrierbook_Name = viewUtils.view(
						R.id.tv_item_carrierbook_Name, TextView.class);
				tv_item_carrierbook_Phone = viewUtils.view(
						R.id.tv_item_carrierbook_Phone, TextView.class);
			}
			convertView = viewUtils.getRootView();
			final CircleImageView civ_expressinlib_carrier_icon = viewUtils.view(
					R.id.civ_expressinlib_carrier_icon, CircleImageView.class);
			final ImageView iv_item_selector = viewUtils.view(
					R.id.iv_item_selector, ImageView.class);
			TextView tv_expressinlib_carrier = viewUtils.view(
					R.id.tv_expressinlib_carrier, TextView.class);
		
		final AgentCarrier carrier = getData().get(position);
		if (ExpressInLibActivity.agentCarrier!=null) {
			if (ExpressInLibActivity.agentCarrier.getParentCarrierName().equals(carrier.getParentCarrierName())) {
				
			
			civ_expressinlib_carrier_icon.setBorderColor(getContext().getResources().getColor(R.color.topbar_background));
			  iv_item_selector.setVisibility(View.VISIBLE);
		     } else {
		  	   civ_expressinlib_carrier_icon.setBorderColor(getContext().getResources().getColor(R.color.bai_ff));
		  	   iv_item_selector.setVisibility(View.GONE);
		    

				}}
		BaseApplication.baseApplication.getBitmapUtils().display(
			civ_expressinlib_carrier_icon,
				Constant.CARRIERLOGO_SERVER + carrier.getParentCarrierCode()
						+ ".png");
		tv_expressinlib_carrier.setText(carrier.getAgentCarrierName());
		if (type == Constant.TYPE_COMPANY_TEXT) {// 有edittext显示的快递公司
			
			tv_item_carrierbook_Name.setText(carrier
					.getParentCarrierName());
			tv_item_carrierbook_Phone.setText("官方客服电话："
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
					((CarrierBookActivity1) getContext()).result(0, new IntentExtra(0,carrier));
					((CarrierBookActivity1) getContext()).finish();
				} else {
					for (int j = 0; j < getData().size(); j++) {
						View v=parent.getChildAt(j);
						  if (position == j) {//当前选中的Item改变背景颜色
							  civ_expressinlib_carrier_icon.setBorderColor(getContext().getResources().getColor(R.color.topbar_background));
							  iv_item_selector.setVisibility(View.VISIBLE);
				               } else {
				            	   CircleImageView civ_expressinlib_carrier_icon= (CircleImageView) v.findViewById(R.id.civ_expressinlib_carrier_icon);
				            	   ImageView iv_item_selector= (ImageView) v.findViewById(R.id.iv_item_selector);
				            	   civ_expressinlib_carrier_icon.setBorderColor(getContext().getResources().getColor(R.color.bai_ff));
				            	   iv_item_selector.setVisibility(View.GONE);
				               }
					}
					
					//civ_expressinlib_carrier_icon.setBorderColor(getContext().getResources().getColor(R.color.topbar_background));
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
	}
}
