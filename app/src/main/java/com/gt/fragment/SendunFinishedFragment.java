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

import com.common.adapter.SendAdapter;
import com.common.base.BaseFragment;
import com.common.events.AutoRefreshEvent;
import com.common.events.ChangeTabEvent;
import com.common.events.GetSearchTextEvent;
import com.common.events.SendTabEvent;
import com.common.model.AgentReceiveTask;
import com.common.model.ApiCode;
import com.common.model.IntentExtra;
import com.common.model.MsMessage;
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
public class SendunFinishedFragment extends BaseFragment {

	public static int list_type = Constant.LIST_SEND_FAILED;
	private int pageNo = 1;// 默认第一页
	private PtrFrameLayout ptr_send;
	private AutoLoadListView allv_send;

	private SendAdapter sendAdapter = null;

	private List<AgentReceiveTask> agentReceiveTasks = new ArrayList<AgentReceiveTask>();
	private List<AgentReceiveTask> searchAgentReceiveTasks = new ArrayList<AgentReceiveTask>();
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


		

		ptr_send = initRefreshListener(R.id.ptr_send, new PtrHandler() {
			@Override
			public void onRefreshBegin(final PtrFrameLayout frame) {
				pageNo = 1;
//				showLoadingDialog(Constant.queryReceiveTaskList);
				// frame.refreshComplete();
				doRequest(Constant.queryReceiveTaskList);
				Log.w("356", "65466543565");
				// if(CommonUtils.isLogin(activity)){
				// doRequest(Constant.ORDER_LIST);
				// }else{
				// doEmptyCheck();
				// frame.refreshComplete();
				// }
			}

			@Override
			public boolean checkCanDoRefresh(PtrFrameLayout frame,
					View content, View header) {
				return PtrDefaultHandler.checkContentCanBePulledDown(frame,
						allv_send, header);
			}
		});

		allv_send = initAutoloadMoreListener(R.id.allv_send,
				new OnLoadNextListener() {
					@Override
					public void onLoadNext() {
						pageNo++;
						// if(CommonUtils.isLogin(activity)){
						// doRequest(RequestCode_LoadMore);
						// }else{
						// doEmptyCheck();
						// ptr_send.refreshComplete();
						// }
					}
				});
	
		ptr_send.autoRefresh();
	}

	@Override
	public void releaseOnDestory() {
		getEventBus().unregister(this);

	}

	@Override
	public void onClickable(View view) {
		switch (view.getId()) {
		case R.id.btn_empty_data:
			ptr_send.autoRefresh();
			break;

		default:
			break;
		}
	}

	@Override
	public void doRequest(int requestCode, Object... needParams) {

		switch (requestCode) {
		case Constant.queryReceiveTaskList:
			if(!isRefreshing){
			showLoadingDialog(requestCode);
			}
			Gson gson = new Gson();
			Map<String, String> maps = new HashMap<String, String>();

			maps.put("agentId", SPUtils.getAgent(getActivity()).getAgentId()
					+ "");
			maps.put("agentUserCode", SPUtils.getUser(getActivity())
					.getAgentUserCode());
			maps.put("taskStatus", list_type + "");
			String paramsValues = gson.toJson(maps);
			requestServer(Constant.RECEIVETASK_SERVER,
					Constant.queryReceiveTaskList, AppUtils.createParams(
							ApiCode.queryReceiveTaskList.code, paramsValues));
			break;

		default:
			break;
		}

	}

	@Override
	public void requestCallBack(int requestCode, MsMessage msm,
			String failedReason) {
		switch (requestCode) {
		case Constant.queryReceiveTaskList:
			
			if (msm.getResult() == 0) {// 成功
				agentReceiveTasks = new Gson().fromJson(msm.getData()
						.toString(), new TypeToken<List<AgentReceiveTask>>() {
				}.getType());
				getEventBus().post(new SendTabEvent(2, agentReceiveTasks.size()));
				sendAdapter = new SendAdapter(getActivity(), agentReceiveTasks,Constant.LIST_SEND_FAILED);
				allv_send.setAdapter(sendAdapter);
				allv_send.setState(State.TheEnd);

			}else{
				T.showShort(activity, msm.getMessage());
			}
			doEmptyCheck();
			ptr_send.refreshComplete();
			hideLoadingDialog(requestCode);
			isRefreshing=false;
			break;

		default:
			break;
		}

	}

	@Override
	public void doEmptyCheck(Object... needParams) {
		if (agentReceiveTasks != null && agentReceiveTasks.size() > 0) {
			viewUtils.setGone(R.id.nodata);
		} else {
			viewUtils.setVisiable(R.id.nodata);
			viewUtils.setViewText(R.id.tv_nodata_msg, "暂无内容");
			viewUtils.setOnClickListener(R.id.btn_empty_data);
		}

	}

	@Override
	public int bindLayoutId() {
		return R.layout.frag_send_task;
	}

	public void onEvent(GetSearchTextEvent event) {
		if (event.getResult().equals("")) {
			sendAdapter.updateAll(agentReceiveTasks);
		} else {
			searchAgentReceiveTasks.clear();
			for (AgentReceiveTask agentReceiveTask : agentReceiveTasks) {
				if (agentReceiveTask.getReceiverPhone().contains(
						event.getResult())||agentReceiveTask.getExpressCode().contains(event.getResult())) {
					searchAgentReceiveTasks.add(agentReceiveTask);
				}
			}
			sendAdapter.updateAll(searchAgentReceiveTasks);
			getEventBus().post(new ChangeTabEvent(Constant.TAB_SEND_UNFINISHED));
		}
	}
	public void onEvent(AutoRefreshEvent event){
		isRefreshing=true;
		ptr_send.autoRefresh();
	}

	@Override
	public void intentCallback(int requestCode, int resultCode, IntentExtra data) {
		// TODO Auto-generated method stub
		
	}
}
