package com.gt.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.common.adapter.LogisticsAdapt;
import com.common.base.BaseActivity;
import com.common.model.ApiCode;
import com.common.model.AppLog;
import com.common.model.IntentExtra;
import com.common.model.MsMessage;
import com.common.utils.AppUtils;
import com.common.utils.Constant;
import com.common.utils.KeyBoardUtils;
import com.common.utils.SPUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gt.officeagent.R;

/***
 * 快件查询
 * 
 * @author
 * 
 */
public class QueryExpressActivity extends BaseActivity {

	private List<AppLog> appLogs = new ArrayList<AppLog>();

	LogisticsAdapt logisticsAdapt = null;

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void initParmers() {
		// TODO Auto-generated method stub

	}

	@Override
	public int bindLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.activity_queryexpress;
	}

	@Override
	public void initViewsAndValues(Bundle savedInstanceState) {

		viewUtils.setOnClickListener(R.id.btn_queryexpress_submit,
				R.id.iv_queryexpress_scan);

		// for (int i = 0; i < 10; i++) {
		// AppLog inLog = new AppLog();
		// inLog.setTime(System.currentTimeMillis() + i * 24 * 60 * 60 * 1000);
		// inLog.setContent("快件已到中关村额额额额额 额额额额");
		// inLogs.add(inLog);
		//
		// }
		logisticsAdapt = new LogisticsAdapt(this, appLogs);
		viewUtils.view(R.id.lv_queryexpress, ListView.class).setAdapter(
				logisticsAdapt);
		doEmptyCheck();
		if(Constant.ISSCAN){
			KeyBoardUtils.closeKeybordShowCursor(this,viewUtils.view(R.id.edt_queryexpress_expresscode,EditText.class));
		 }
	}

	@Override
	public void releaseOnDestory() {

	}

	@Override
	public void onClickable(View view) {
		switch (view.getId()) {
		case R.id.btn_queryexpress_submit:
			doRequest(Constant.queryTask);
			break;
		default:
			break;
		}
	}

	@Override
	public void doRequest(int requestCode, Object... needParams) {
		switch (requestCode) {
		case Constant.queryTask:
			Gson gson = new Gson();
			Map<String, String> maps = new HashMap<String, String>();
			maps.put("agentId", SPUtils.getAgent(this).getAgentId() + "");
			maps.put("expressCode",
					viewUtils.getViewText(R.id.edt_queryexpress_expresscode));
			String paramsValues = gson.toJson(maps);
			requestServer(Constant.BASIC_SERVER, Constant.queryTask,
					AppUtils.createParams(ApiCode.queryTask.code, paramsValues));
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
		case Constant.queryTask:
			if (msm.getResult() == 0) {
				appLogs = new Gson().fromJson(msm.getData().toString(),
						new TypeToken<List<AppLog>>() {
						}.getType());
				logisticsAdapt.updateAll(appLogs);
				doEmptyCheck();

			} else {
				// T.showShort(this, msm.getMessage());
			}
			break;

		}
	}

	@Override
	public void doEmptyCheck(Object... needParams) {
		if (appLogs != null && appLogs.size() > 0) {
			viewUtils.setGone(R.id.nodata);
		} else {
			viewUtils.setVisiable(R.id.nodata);
			viewUtils.setViewText(R.id.tv_nodata_msg, "暂无内容");
		}

	}

	@Override
	public void intentCallback(int requestCode, int resultCode, IntentExtra data) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (Constant.ISSCAN) {
			if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER) {
				doRequest(Constant.queryTask);
			}
		}
		return super.onKeyUp(keyCode, event);
	}
}
