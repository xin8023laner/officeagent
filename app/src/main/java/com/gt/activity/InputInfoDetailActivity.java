package com.gt.activity;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.common.base.BaseActivity;
import com.common.events.CloseEvent;
import com.common.model.AgentReceiveTask;
import com.common.model.ApiCode;
import com.common.model.IntentExtra;
import com.common.model.MsMessage;
import com.common.utils.AppUtils;
import com.common.utils.Constant;
import com.common.utils.SPUtils;
import com.common.utils.T;
import com.google.gson.Gson;
import com.gt.officeagent.R;

public class InputInfoDetailActivity extends BaseActivity {

	private AgentReceiveTask agentReceiveTask = null;
	private int type = Constant.SENDTYPE_SHANGMENPAISONG;

	@Override
	public int bindLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.activity_inputinfo_detail;
	}

	@Override
	public void initParmers() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initViewsAndValues(Bundle savedInstanceState) {

		getEventBus().register(this);
		agentReceiveTask = (AgentReceiveTask) intentExtra.getValue();

		viewUtils.setViewText(R.id.tv_inputinfodetail_waybillcode,
				agentReceiveTask.getWaybillCode());
		viewUtils.setViewText(R.id.tv_inputinfodetail_carriername,
				agentReceiveTask.getCarrierName());
		viewUtils.setViewText(R.id.tv_inputinfodetail_expresscode,
				agentReceiveTask.getExpressCode());
		viewUtils.setViewText(R.id.edt_inputinfodetail_name,

				agentReceiveTask.getReceiverName());
		viewUtils.setViewText(R.id.edt_inputinfodetail_phone,
				agentReceiveTask.getReceiverPhone());
		viewUtils.setViewText(R.id.edt_inputinfodetail_name,
				agentReceiveTask.getReceiverName());
		viewUtils.setViewText(R.id.edt_inputinfodetail_addr,
				agentReceiveTask.getReceiverAddress());

		setEditStatus(R.id.edt_inputinfodetail_phone);
		setEditStatus(R.id.edt_inputinfodetail_name);
		setEditStatus(R.id.edt_inputinfodetail_addr);

		viewUtils.setOnClickListener(R.id.btn_inputinfodetail_submit,
				R.id.btn_inputinfodetail_reset);

		if (agentReceiveTask.getSendType() == Constant.SENDTYPE_KEHUZITI) {
			viewUtils.view(R.id.rg_inputinfodetail_type, RadioGroup.class)
					.check(R.id.rb_inputinfodetail_kehuziyi);
		} else {
			viewUtils.view(R.id.rg_inputinfodetail_type, RadioGroup.class)
					.check(R.id.rb_inputinfodetail_shangmenpaisong);
		}

		viewUtils.view(R.id.rg_inputinfodetail_type, RadioGroup.class)
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup arg0, int arg1) {

						switch (arg1) {
						case R.id.rb_inputinfodetail_kehuziyi:
							type = Constant.SENDTYPE_KEHUZITI;
							break;
						case R.id.rb_inputinfodetail_shangmenpaisong:
							type = Constant.SENDTYPE_SHANGMENPAISONG;
							break;

						default:
							break;
						}
					}
				});
		//TODO
//		if(Constant.ISSCAN){
//			KeyBoardUtils.closeKeybordShowCursor(this,viewUtils.view(R.id.edt_inputinfodetail_phone,EditText.class)
//					,viewUtils.view(R.id.edt_inputinfodetail_name,EditText.class)
//					,viewUtils.view(R.id.edt_inputinfodetail_addr,EditText.class));
//		 }
	}

	@Override
	public void releaseOnDestory() {
		getEventBus().unregister(this);

	}

	@Override
	public void onClickable(View view) {
		switch (view.getId()) {
		case R.id.btn_inputinfodetail_submit:
			doRequest(Constant.editReceiveTask);
			break;
		case R.id.btn_inputinfodetail_reset:
			viewUtils.setViewText(R.id.edt_inputinfodetail_phone, "");
			viewUtils.setViewText(R.id.edt_inputinfodetail_name, "");
			viewUtils.setViewText(R.id.edt_inputinfodetail_addr, "");
			setEnable(R.id.edt_inputinfodetail_phone, true);
			setEnable(R.id.edt_inputinfodetail_name, true);
			setEnable(R.id.edt_inputinfodetail_addr, true);
			break;

		default:
			break;
		}
	}

	@Override
	public void doRequest(int requestCode, Object... needParams) {
		showLoadingDialog(requestCode);

		switch (requestCode) {
		case Constant.editReceiveTask:
			Gson gson = new Gson();
			Map<String, String> maps = new HashMap<String, String>();
			maps.put("agentId", SPUtils.getAgent(this).getAgentId() + "");
			maps.put("taskId", agentReceiveTask.getTaskId() + "");
			if (!TextUtils.isEmpty(viewUtils
					.getViewText(R.id.edt_inputinfodetail_name))) {
				maps.put("receiverName",
						viewUtils.getViewText(R.id.edt_inputinfodetail_name));
			}
			maps.put("receiverPhone",
						viewUtils.getViewText(R.id.edt_inputinfodetail_phone));
			if (!TextUtils.isEmpty(viewUtils
					.getViewText(R.id.edt_inputinfodetail_addr))) {
				maps.put("receiverAddress",
						viewUtils.getViewText(R.id.edt_inputinfodetail_addr));
			}
			maps.put("sendType", type + "");
			String paramsValues = gson.toJson(maps);
			requestServer(Constant.RECEIVETASK_SERVER,
					Constant.editReceiveTask, AppUtils.createParams(
							ApiCode.editReceiveTask.code, paramsValues));
			break;

		default:
			break;
		}

	}

	@Override
	public void requestCallBack(int requestCode, MsMessage msm,
			String failedReason) {
		hideLoadingDialog(requestCode);
		T.showShort(this, msm.getMessage());
		if (msm.getResult() == 0) {// 成功
			finish();
		}

	}

	@Override
	public void doEmptyCheck(Object... needParams) {
		// TODO Auto-generated method stub

	}

	@Override
	public void intentCallback(int requestCode, int resultCode, IntentExtra data) {
		// TODO Auto-generated method stub

	}

	public void onEvent(CloseEvent closeEvent) {
		finish();

	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (Constant.ISSCAN) {
			if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER) {
				doRequest(Constant.editReceiveTask);
			}
		}
		return super.onKeyUp(keyCode, event);
	}
}