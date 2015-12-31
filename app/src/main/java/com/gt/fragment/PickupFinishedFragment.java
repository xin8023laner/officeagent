package com.gt.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import cn.socks.autoload.AutoLoadListView;
import cn.socks.autoload.AutoLoadListView.OnLoadNextListener;
import cn.socks.autoload.LoadingFooter.State;
import cn.srain.cube.views.ptr.PtrDefaultHandler;
import cn.srain.cube.views.ptr.PtrFrameLayout;
import cn.srain.cube.views.ptr.PtrHandler;

import com.common.adapter.PickupAdapter;
import com.common.base.BaseFragment;
import com.common.events.AutoRefreshPickupEvent;
import com.common.events.ChangeTabEvent;
import com.common.events.GetSearchTextPickupEvent;
import com.common.events.TabEvent;
import com.common.model.ApiCode;
import com.common.model.IntentExtra;
import com.common.model.MsMessage;
import com.common.model.TaskWaybill;
import com.common.utils.AppUtils;
import com.common.utils.Constant;
import com.common.utils.SPUtils;
import com.common.utils.T;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gt.officeagent.R;

/**
 * Created by Alick on 2015/7/16.
 */
public class PickupFinishedFragment extends BaseFragment {

	public static int list_type = Constant.LIST_PICKUP_PICKUPED;
	private int pageNo = 1;// 默认第一页
	private PtrFrameLayout ptr_pickup;
	private AutoLoadListView allv_pickup;

	private PickupAdapter pickupAdapter;
	private List<TaskWaybill> taskWaybills = new ArrayList<TaskWaybill>();
	private List<TaskWaybill> searchTaskWaybills = new ArrayList<TaskWaybill>();

	private boolean isRefreshing;
	/**
	 * 初始化变量
	 */
	@Override
	public void initParmers() {

	}

	@Override
	public void initViewsAndValues(Bundle savedInstanceState) {

		getEventBus().register(this);

		ptr_pickup = initRefreshListener(R.id.ptr_pickup, new PtrHandler() {
			@Override
			public void onRefreshBegin(final PtrFrameLayout frame) {
				pageNo = 1;
				doRequest(Constant.querySendTaskList);
			}

			@Override
			public boolean checkCanDoRefresh(PtrFrameLayout frame,
					View content, View header) {
				return PtrDefaultHandler.checkContentCanBePulledDown(frame,
						allv_pickup, header);
			}
		});

		allv_pickup = initAutoloadMoreListener(R.id.allv_pickup,
				new OnLoadNextListener() {
					@Override
					public void onLoadNext() {
						pageNo++;
						ptr_pickup.refreshComplete();
					}
				});
		ptr_pickup.autoRefresh();

	}

	@Override
	public void releaseOnDestory() {
		getEventBus().unregister(this);

	}

	@Override
	public void onClickable(View view) {
		switch (view.getId()) {
		case R.id.btn_empty_data:
			ptr_pickup.autoRefresh();
			break;

		default:
			break;
		}
	}

	@Override
	public void doRequest(int requestCode, Object... needParams) {

		switch (requestCode) {
		case Constant.querySendTaskList:
			if(!isRefreshing){
				showLoadingDialog(requestCode);
			}
			Gson gson = new Gson();
			Map<String, String> maps = new HashMap<String, String>();
			maps.put("agentId", SPUtils.getAgent(getActivity()).getAgentId()+ "");
			maps.put("agentUserId", SPUtils.getUser(getActivity()).getAgentUserId() + "");
			maps.put("agentUserCode", SPUtils.getUser(getActivity()).getAgentUserCode());
			maps.put("agentUserName", SPUtils.getUser(getActivity()).getAgentUserName());
			maps.put("type", list_type + "");
			String paramsValues = gson.toJson(maps);
			requestServer(Constant.SENDTASK_SERVER, Constant.querySendTaskList,AppUtils.createParams(ApiCode.querySendTaskList.code,paramsValues));
			break;

		default:
			break;
		}

	}

	@Override
	public void requestCallBack(int requestCode, MsMessage msm,
			String failedReason) {
		
		switch (requestCode) {
		case Constant.querySendTaskList:
//			T.showShort(activity, msm.getMessage());
			Log.w("$%^&", msm.toString());
			if (msm.getResult() == 0) {// 成功
				taskWaybills = new Gson().fromJson(msm.getData().toString(),
						new TypeToken<List<TaskWaybill>>() {
						}.getType());
				getEventBus().post(new TabEvent(1, taskWaybills.size()));
					pickupAdapter = new PickupAdapter(getActivity(),taskWaybills, Constant.LIST_PICKUP_PICKUPED);
					allv_pickup.setAdapter(pickupAdapter);
					allv_pickup.setState(State.TheEnd);

			}else{
				T.showShort(activity, msm.getMessage());
			}
			doEmptyCheck();
			ptr_pickup.refreshComplete();
			hideLoadingDialog(requestCode);
			isRefreshing=false;
			break;

		default:
			break;
		}

	}

	@Override
	public void doEmptyCheck(Object... needParams) {
		if (taskWaybills != null && taskWaybills.size() > 0) {
			viewUtils.setGone(R.id.nodata);
		} else {
			viewUtils.setVisiable(R.id.nodata);
			viewUtils.setViewText(R.id.tv_nodata_msg, "暂无内容");
			viewUtils.setOnClickListener(R.id.btn_empty_data);
		}

	}

	@Override
	public int bindLayoutId() {
		return R.layout.frag_pickup_task;
	}

	public void onEvent(GetSearchTextPickupEvent event) {
		if (event.getResult().equals("")) {
			pickupAdapter.updateAll(taskWaybills);
		} else {
			searchTaskWaybills.clear();
			for (TaskWaybill taskWaybill : taskWaybills) {
				if (taskWaybill.getSenderPhone().contains(event.getResult())) {
					searchTaskWaybills.add(taskWaybill);
				}
			}
			pickupAdapter.updateAll(searchTaskWaybills);
			getEventBus().post(new ChangeTabEvent(Constant.TAB_PICKUP_FINISHED));
		}

	}
	public void onEvent(AutoRefreshPickupEvent event){
		isRefreshing=true;
		ptr_pickup.autoRefresh();
	}

	@Override
	public void intentCallback(int requestCode, int resultCode, IntentExtra data) {
		// TODO Auto-generated method stub
		
	}
}
