package com.gt.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.View;

import cn.socks.autoload.AutoLoadListView;
import cn.socks.autoload.AutoLoadListView.OnLoadNextListener;
import cn.socks.autoload.LoadingFooter.State;
import cn.srain.cube.views.ptr.PtrDefaultHandler;
import cn.srain.cube.views.ptr.PtrFrameLayout;
import cn.srain.cube.views.ptr.PtrHandler;

import com.common.adapter.GLOrdersAdapter;
import com.common.base.BaseActivity;
import com.common.model.ApiCode;
import com.common.model.IntentExtra;
import com.common.model.MsMessage;
import com.common.model.Order;
import com.common.utils.AppUtils;
import com.common.utils.Constant;
import com.common.utils.SPUtils;
import com.common.utils.T;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gt.officeagent.R;

public class GLOrdersActivity extends BaseActivity {

	private PtrFrameLayout ptr_GLOrders;
	private AutoLoadListView allv_GLOrders;
	private int pageNo;
	private List<Order> orders = new ArrayList<Order>();
	private GLOrdersAdapter glOrderAdapter;
	
	@Override
	public int bindLayoutId() {
		return R.layout.activity_glorders;
	}

	@Override
	public void initParmers() {

	}

	@Override
	public void initViewsAndValues(Bundle savedInstanceState) {
		ptr_GLOrders = initRefreshListener(R.id.ptr_GLOrders, new PtrHandler() {
			@Override
			public void onRefreshBegin(final PtrFrameLayout frame) {
				pageNo = 1;
				doRequest(Constant.queryGLOrders);
			}

			@Override
			public boolean checkCanDoRefresh(PtrFrameLayout frame,
					View content, View header) {
				return PtrDefaultHandler.checkContentCanBePulledDown(frame,
						allv_GLOrders, header);
			}
		});

		allv_GLOrders = initAutoloadMoreListener(R.id.allv_GLOrders,
				new OnLoadNextListener() {
					@Override
					public void onLoadNext() {
						pageNo++;
						ptr_GLOrders.refreshComplete();
					}
				});
		
		ptr_GLOrders.autoRefresh();
	}

	@Override
	public void releaseOnDestory() {

	}

	@Override
	public void onClickable(View view) {
		switch (view.getId()) {
		case R.id.btn_empty_data:
			ptr_GLOrders.autoRefresh();
			break;

		default:
			break;
		}
	}

	@Override
	public void doRequest(int requestCode, Object... needParams) {
		showLoadingDialog(requestCode);
		switch (requestCode) {
		case Constant.queryGLOrders:
			Gson gson = new Gson();
			Map<String, String> maps = new HashMap<String, String>();
			maps.put("agentId", SPUtils.getAgent(this).getAgentId()+ "");
			String paramsValues = gson.toJson(maps);
			requestServer(Constant.GLORDER_SERVER, Constant.queryGLOrders,
					AppUtils.createParams(ApiCode.queryGLOrders.code,
							paramsValues));
			break;

		default:
			break;
		}
	}

	@Override
	public void requestCallBack(int requestCode, MsMessage msm,
			String failedReason) {
		hideLoadingDialog(requestCode);
		switch (requestCode) {
		case Constant.queryGLOrders:
			try{
			if (msm.getResult() == 0) {// 成功
				orders = new Gson().fromJson(msm.getData().toString(),
						new TypeToken<List<Order>>() {
						}.getType());
				glOrderAdapter = new GLOrdersAdapter(this,orders);
				allv_GLOrders.setAdapter(glOrderAdapter);
				allv_GLOrders.setState(State.TheEnd);
			}else{
				T.showShort(this, "数据获取失败！");
			}
			doEmptyCheck();
			ptr_GLOrders.refreshComplete();
			}catch(Exception e){
				e.printStackTrace();
			}
			break;

		default:
			break;
		}

	}

	@Override
	public void doEmptyCheck(Object... needParams) {
		if (orders != null && orders.size() > 0) {
			viewUtils.setGone(R.id.nodata);
			viewUtils.setVisiable(R.id.ptr_GLOrders);
		} else {
			viewUtils.setVisiable(R.id.nodata);
			viewUtils.setGone(R.id.ptr_GLOrders);
			viewUtils.setViewText(R.id.tv_nodata_msg, "暂无内容");
			viewUtils.setOnClickListener(R.id.btn_empty_data);
		}
	}

	@Override
	public void intentCallback(int requestCode, int resultCode, IntentExtra data) {
		// TODO Auto-generated method stub

	}

}
