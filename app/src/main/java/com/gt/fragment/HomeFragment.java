package com.gt.fragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import cn.zjl.zxing.CaptureActivity;

import com.common.base.BaseFragment;
import com.common.events.PublicNewMsgEvent;
import com.common.model.ApiCode;
import com.common.model.IntentExtra;
import com.common.model.MsMessage;
import com.common.model.TaskWaybill;
import com.common.ui.Topbar.OnControlTwoListener;
import com.common.ui.Topbar.OnOtherListener;
import com.common.utils.AppUtils;
import com.common.utils.Constant;
import com.common.utils.SPUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gt.activity.CapturePDAActivity;
import com.gt.activity.ExpressInLibActivity;
import com.gt.activity.MainActivity;
import com.gt.activity.MsgActivity;
import com.gt.activity.ReceivePickupTaskActivity;
import com.gt.officeagent.R;

/**
 * Created by Alick on 2015/7/16.
 */
public class HomeFragment extends BaseFragment {

	/**
	 * 初始化变量
	 */
	@Override
	public void initParmers() {

	}

	@Override
	public void releaseOnDestory() {
		getEventBus().unregister(this);

	}

	@Override
	public void onClickable(View view) {
		switch (view.getId()) {
		case R.id.iv_home_guangboclean:
			viewUtils.setGone(R.id.rl_home_guangbo);
			break;
		case R.id.rl_home_guangbo:
			intent(MsgActivity.class, null);
			break;
		case R.id.ll_home_kehuqianshou:
			if(Constant.ISSCAN){
				intent(CapturePDAActivity.class, new IntentExtra(
						Constant.SCAN_KEHUQIANSHOU, null));
			}else{
				intent(CaptureActivity.class, new IntentExtra(
						Constant.SCAN_KEHUQIANSHOU, null));
			}
			break;
		case R.id.ll_home_lanshou:
			intent(ReceivePickupTaskActivity.class, null);
			break;
		case R.id.ll_home_paisong:
			if(Constant.ISSCAN){
				intent(CapturePDAActivity.class, new IntentExtra(Constant.SCAN_PAISONG, 0));
			}else{
				intent(CaptureActivity.class, new IntentExtra(Constant.SCAN_PAISONG, 0));
			}
			break;
		case R.id.ll_home_kuaidiruku:
			intent(ExpressInLibActivity.class, new IntentExtra(
					Constant.SCAN_KUAIJIANRUKU, null));
			break;

		default:
			break;
		}

	}

	@Override
	public void doRequest(int requestCode, Object... needParams) {
		Gson gson;
		switch(requestCode){
		case Constant.queryReadySendTask:
			gson = new Gson();
			Map<String, String> maps = new HashMap<String, String>();
			maps.put("agentId", SPUtils.getAgent(getActivity()).getAgentId()+"");
			String paramsValues = gson.toJson(maps);
			requestServer(Constant.SENDTASK_SERVER,
					Constant.queryReadySendTask, AppUtils.createParams(
							ApiCode.queryReadySendTask.code, paramsValues));
			break;
		}
	
	}

	@Override
	public void requestCallBack(int requestCode, MsMessage msm,
			String failedReason) {
		switch(requestCode){
		case Constant.queryReadySendTask:
			if (msm.getResult() == 0) {// 成功
				List<TaskWaybill> taskWaybills = new Gson().fromJson(msm.getData().toString(),
						new TypeToken<List<TaskWaybill>>() {
						}.getType());
				if(taskWaybills!=null&&taskWaybills.size()>0){
					viewUtils.setVisiable(R.id.tv_home_lanshouCount);
					viewUtils.setViewText(R.id.tv_home_lanshouCount, taskWaybills.size()+"");
				}else{
					viewUtils.setGone(R.id.tv_home_lanshouCount);
				}
			} 
			break;
		}
		

	}

	@Override
	public void doEmptyCheck(Object... needParams) {
		// TODO Auto-generated method stub

	}

	@Override
	public int bindLayoutId() {
		return R.layout.frag_home1;
	}

	@Override
	public void initViewsAndValues(Bundle savedInstanceState) {
		getEventBus().register(this);
		getTopbar().setOnOtherListener(new OnOtherListener() {
			@Override
			public void onOtherListener() {
				((MainActivity) getActivity()).mDrawer.toggleMenu();
			}
		});
		getTopbar().setOnControlTwoListener(new OnControlTwoListener() {

			@Override
			public void onControlTwoListener() {
				intent(MsgActivity.class, null);

			}
		});
		getTopbar().setTitleText(SPUtils.getAgent(getActivity()).getName());

		viewUtils.setOnClickListener(R.id.iv_home_guangboclean,
				R.id.ll_home_kehuqianshou, R.id.ll_home_lanshou,
				R.id.ll_home_kuaidiruku, R.id.ll_home_paisong,
				R.id.rl_home_guangbo);
		
//		doRequest(Constant.queryReadySendTask);//请求服务器返回“待揽收任务数”
	}

	public void onEvent(PublicNewMsgEvent publicNewMsgEvent) {
		viewUtils.setVisiable(R.id.rl_home_guangbo);
		viewUtils.setViewText(R.id.tv_home_guangbo, publicNewMsgEvent
				.getPushModel().getContent());
		doRequest(Constant.queryReadySendTask);//请求服务器返回“待揽收任务数”
	}
	@Override
	public void onResume() {
		doRequest(Constant.queryReadySendTask);//请求服务器返回“待揽收任务数”
		super.onResume();
	}

	@Override
	public void intentCallback(int requestCode, int resultCode, IntentExtra data) {
		// TODO Auto-generated method stub
		
	}
}
