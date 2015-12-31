package com.gt.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import cn.socks.autoload.AutoLoadListView;
import cn.socks.autoload.LoadingFooter.State;
import cn.srain.cube.views.ptr.PtrDefaultHandler;
import cn.srain.cube.views.ptr.PtrFrameLayout;
import cn.srain.cube.views.ptr.PtrHandler;

import com.common.adapter.ReceivePickupTaskAdapter;
import com.common.base.BaseActivity;
import com.common.events.AutoRefreshPickupEvent;
import com.common.model.ApiCode;
import com.common.model.IntentExtra;
import com.common.model.MsMessage;
import com.common.model.TaskWaybill;
import com.common.utils.AppUtils;
import com.common.utils.Constant;
import com.common.utils.SPUtils;
import com.common.utils.T;
import com.common.utils.ViewUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gt.inteface.AdapterItemClickEvent;
import com.gt.officeagent.R;

public class ReceivePickupTaskActivity extends BaseActivity {

	public static int list_type = Constant.LIST_PICKUP_UNPICAUP;
//	private int pageNo = 1;// 默认第一页
	private PtrFrameLayout ptr_receivepickuptask;
	private AutoLoadListView allv_receivepickuptask;
	private ReceivePickupTaskAdapter receivePickupTaskAdapter = null;
	private List<TaskWaybill> taskWaybills = new ArrayList<TaskWaybill>();
	private int index=-1;
	private ViewUtils vDialogUtils;
	private Dialog dialog;
	private int type;//是退回操作还是接受任务操作
	
	@Override
	public int bindLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.activity_receivepickuptask;
	}

	@Override
	public void initParmers() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initViewsAndValues(Bundle savedInstanceState) {
		
		getEventBus().register(this);

		receivePickupTaskAdapter = new ReceivePickupTaskAdapter(this, taskWaybills);

		viewUtils.setOnClickListener(R.id.btn_empty_data);

		ptr_receivepickuptask = initRefreshListener(R.id.ptr_receivepickuptask, new PtrHandler() {
			@Override
			public void onRefreshBegin(final PtrFrameLayout frame) {
//				pageNo = 1;
				allv_receivepickuptask.setState(State.Idle);
				frame.refreshComplete();
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
						allv_receivepickuptask, header);
			}
		});
		allv_receivepickuptask=(AutoLoadListView) viewUtils.view(R.id.allv_receivepickuptask);
//		allv_receivepickuptask = initAutoloadMoreListener(R.id.allv_receivepickuptask,
//				new OnLoadNextListener() {
//					@Override
//					public void onLoadNext() {
//						pageNo++;
//						// if(CommonUtils.isLogin(activity)){
//						// doRequest(RequestCode_LoadMore);
//						// }else{
//						// doEmptyCheck();
//						ptr_receivepickuptask.refreshComplete();
//						// }
//					}
//				});

		allv_receivepickuptask.setAdapter(receivePickupTaskAdapter);
		doEmptyCheck();
		doRequest(Constant.queryReadySendTask);
		
	}

	@Override
	public void releaseOnDestory() {
		// TODO Auto-generated method stub
		getEventBus().unregister(this);
	}

	@Override
	public void onClickable(View view) {
		switch(view.getId()){
		case R.id.btn_empty_data://点击重试
			doRequest(Constant.queryReadySendTask);
			break;
		}
		
	}

	@Override
	public void doRequest(int requestCode, Object... needParams) {
		showLoadingDialog(requestCode);

		switch (requestCode) {
		case Constant.queryReadySendTask:
			Gson gson = new Gson();
			Map<String, String> maps = new HashMap<String, String>();
			maps.put("agentId", SPUtils.getAgent(this).getAgentId()+"");
			String paramsValues = gson.toJson(maps);
			requestServer(Constant.SENDTASK_SERVER,
					Constant.queryReadySendTask, AppUtils.createParams(
							ApiCode.queryReadySendTask.code, paramsValues));
			break;
		case Constant.returnSendTask:
			Gson gson1 = new Gson();
			TaskWaybill taskWaybill=(TaskWaybill)needParams[0];
			Map<String, String> maps1 = new HashMap<String, String>();
			maps1.put("taskId", taskWaybill.getTaskId()+"");
			maps1.put("agentUserId", SPUtils.getUser(this).getAgentUserId()+"");
			maps1.put("agentUserCode", SPUtils.getUser(this).getAgentUserCode());
			maps1.put("agentUserName", SPUtils.getUser(this).getAgentUserName());
			maps1.put("remark", (String)needParams[1]);
			String paramsValues1 = gson1.toJson(maps1);
			requestServer(Constant.SENDTASK_SERVER,
					Constant.returnSendTask, AppUtils.createParams(
							ApiCode.returnSendTask.code, paramsValues1));
			break;
		case Constant.takeSendTask:
			Gson gson2 = new Gson();
			TaskWaybill taskWaybill2=(TaskWaybill)needParams[0];
			Map<String, String> maps2 = new HashMap<String, String>();
			maps2.put("taskId", taskWaybill2.getTaskId()+"");
			maps2.put("agentUserId", SPUtils.getUser(this).getAgentUserId()+"");
			maps2.put("agentUserCode", SPUtils.getUser(this).getAgentUserCode());
			maps2.put("agentUserName", SPUtils.getUser(this).getAgentUserName());
			String paramsValues2 = gson2.toJson(maps2);
			requestServer(Constant.SENDTASK_SERVER,
					Constant.takeSendTask, AppUtils.createParams(
							ApiCode.takeSendTask.code, paramsValues2));
			break;
		}

		
	}

	@Override
	public void requestCallBack(int requestCode, MsMessage msm,
			String failedReason) {
		hideLoadingDialog(requestCode);
		switch (requestCode) {
		case Constant.queryReadySendTask:
			if (msm.getResult() == 0) {// 成功
				taskWaybills = new Gson().fromJson(msm.getData().toString(),
						new TypeToken<List<TaskWaybill>>() {
						}.getType());
				receivePickupTaskAdapter.updateAll(taskWaybills);
				doEmptyCheck();
			} else {//测试
				T.showShort(this, msm.getMessage());
//				for (int i = 0; i < 10; i++) {
//
//					TaskWaybill taskWaybill = new TaskWaybill();
//					taskWaybill.setSenderAddress("北京海淀海兴大厦B座一层西塔网络1002号567室");
//					taskWaybill.setSenderName("猫哭考考啊");
//					taskWaybill.setSenderTelphone("18500608270");
//					taskWaybill.setSendTime(System.currentTimeMillis());
//					taskWaybill.setAppointmentTime("8月7日 9：00-6：00");
//					taskWaybills.add(taskWaybill);
//					receivePickupTaskAdapter.updateAll(taskWaybills);
//				}
			}
			break;
		case Constant.takeSendTask:
		case Constant.returnSendTask:
			if (msm.getResult() == 0) {// 成功
				receivePickupTaskAdapter.remove(index);
				T.showShort(this, msm.getMessage());
//				switch (type) {
//				case AdapterItemClickEvent.RECEIVETASKTYPE_BACK:
//					break;
//				case AdapterItemClickEvent.RECEIVETASKTYPE_SUBMIT:
//					getEventBus().post(new AutoRefreshPickupEvent());
//					break;
//				}
			}
			getEventBus().post(new AutoRefreshPickupEvent());
			break;
		}
		getTopbar().setTitleText("待取件揽收任务（"+taskWaybills.size()+"）");
	}

	@Override
	public void doEmptyCheck(Object... needParams) {
		if (taskWaybills != null && taskWaybills.size() > 0) {
			viewUtils.setGone(R.id.nodata);
		} else {
			viewUtils.setVisiable(R.id.nodata);
			viewUtils.setViewText(R.id.tv_nodata_msg, "暂无内容");
		}
		
	}

	@Override
	public void intentCallback(int requestCode, int resultCode, IntentExtra data) {
		
		
	}

	public void onEvent(AdapterItemClickEvent adapterItemClickEvent){
		type=adapterItemClickEvent.getType();
		index=adapterItemClickEvent.getPosition();
		switch (adapterItemClickEvent.getType()) {
		case AdapterItemClickEvent.RECEIVETASKTYPE_SUBMIT:
			if(adapterItemClickEvent.getObject()!=null){
				doRequest(Constant.takeSendTask,adapterItemClickEvent.getObject());
			}
			break;
		case AdapterItemClickEvent.RECEIVETASKTYPE_BACK:
			editRUKUDialog();
			break;

		default:
			break;
		}
//		getEventBus().post(new AutoRefreshEvent());
	}
	private void editRUKUDialog() {
		vDialogUtils = showRUKUDialog(this);
		vDialogUtils.setGone(R.id.et_dialog_reason);
		vDialogUtils.setVisiable(R.id.tv_dialog_text);
		vDialogUtils.setViewText(R.id.tv_dialog_text,"是否要退回？");
		vDialogUtils.view(R.id.btn_dialog_cancel).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// 取消
						dialog.dismiss();
					}
				});
		vDialogUtils.view(R.id.btn_dialog_ok).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
//						vDialogUtils.getViewText(R.id.et_dialog_reason);
						//if(TextUtils.isEmpty(vDialogUtils.getViewText(R.id.et_dialog_reason))){
						//	T.showShort(ReceivePickupTaskActivity.this, "请填写退回原因！");
					//	}else{
						doRequest(Constant.returnSendTask,taskWaybills.get(index),vDialogUtils.getViewText(R.id.et_dialog_reason));//退回任务
//						doRequest(Constant.takeSendTask,taskWaybills.get(index));
//						receivePickupTaskAdapter.remove(index);
//						getTopbar().setTitleText("待取件揽收任务（"+taskWaybills.size()+"）");
						dialog.dismiss();
						//}
					}
				});

	}

	/**
	 * 快递入库弹框
	 * 
	 * @param context
	 * @param requesxxxtCode
	 */
	private ViewUtils showRUKUDialog(Context context) {

		dialog = new Dialog(context, R.style.Dialog);
		ViewUtils viewUtils = new ViewUtils(context, R.layout.dialog_ruku);

		dialog.setContentView(viewUtils.getRootView());

		dialog.show();
		return viewUtils;
	}
}