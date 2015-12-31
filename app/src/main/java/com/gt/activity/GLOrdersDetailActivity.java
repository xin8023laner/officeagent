package com.gt.activity;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.common.base.BaseActivity;
import com.common.events.AutoRefreshEvent;
import com.common.model.IntentExtra;
import com.common.model.MsMessage;
import com.common.model.Order;
import com.common.model.TaskWaybill;
import com.common.utils.ViewUtils;
import com.gt.officeagent.R;

/***
 * 快件详情
 * 
 * @author
 * 
 */
public class GLOrdersDetailActivity extends BaseActivity {

	private TaskWaybill taskWaybill = null;
	private ViewUtils vDialogUtils = null;
	private Dialog dialog;
	private int type;
	private Order order;

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void initParmers() {

	}

	@Override
	public int bindLayoutId() {
		return R.layout.activity_glorderdetail;
	}

	@Override
	public void initViewsAndValues(Bundle savedInstanceState) {
		order = (Order) intentExtra.getValue();
		type = intentExtra.getType();
		if(order.getPayStatus()==1){//已付
			viewUtils.setViewText(R.id.tv_glorderDetail_payStatus,"已付");
		}else{
			viewUtils.setViewText(R.id.tv_glorderDetail_payStatus,"未付");
		}
		switch (order.getServiceStatus()) {
		case 1://待派送
			viewUtils.setViewText(R.id.tv_glorderDetail_status,"待派送");
			break;
		case 2://配送中
			viewUtils.setViewText(R.id.tv_glorderDetail_status,"配送中");
			break;
		case 3://已完成
			viewUtils.setViewText(R.id.tv_glorderDetail_status,"已完成");
			break;

		default:
			break;
		}
		
		viewUtils.setViewText(R.id.tv_glorderDetail_orderCode,
				TextUtils.isEmpty(order.getOrderCode()) ? "未知"
						: order.getOrderCode());
		viewUtils.setViewText(R.id.tv_glorderDetail_userName,
				TextUtils.isEmpty(order.getReceiverName()) ? "未知"
						: order.getReceiverName());

		viewUtils.setViewText(R.id.tv_glorderDetail_userPhone, TextUtils
				.isEmpty(order.getReceierPhone()) ? "未知"
				: order.getReceierPhone());
		viewUtils.setViewText(R.id.tv_glorderDetail_userAddress, TextUtils
				.isEmpty(order.getReceiverAddress()) ? "未知"
				: order.getReceiverAddress());
		viewUtils.setViewText(R.id.tv_glorderDetail_orderTime,
				order.getCreateTime()==0 ? "未知"
						: "下单时间："+new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(order.getCreateTime())));
		

		viewUtils.setViewText(R.id.tv_glorderDetail_Name, TextUtils
				.isEmpty(order.getGoodsName()+"") ? "未知"
				: (order.getGoodsName()+""));
		viewUtils.setViewText(R.id.tv_glorderDetail_sumNum, TextUtils
				.isEmpty(order.getGoodsCount()+"") ? "0件"
						: (order.getGoodsCount()+"件"));
		viewUtils.setViewText(R.id.tv_glorderDetail_sumPrice, TextUtils
				.isEmpty(order.getOrderPrice()+"") ? "0元"
						: (order.getOrderPrice()+"元"));
		viewUtils.setViewText(R.id.tv_glorderDetail_disPrice, TextUtils
				.isEmpty(order.getPreferentialPrice()+"") ? "0元"
						: (order.getPreferentialPrice()+"元"));
		
	}

	@Override
	public void releaseOnDestory() {
	}

	@Override
	public void onClickable(View view) {

	}

	@Override
	public void doRequest(int requestCode, Object... needParams) {

		
	}

	@Override
	public void requestCallBack(int requestCode, MsMessage msm,
			String failedReason) {
		
	}

	@Override
	public void doEmptyCheck(Object... needParams) {

	}

	@Override
	public void intentCallback(int requestCode, int resultCode, IntentExtra data) {
	}

	public void onEvent(AutoRefreshEvent event) {

	}

}
