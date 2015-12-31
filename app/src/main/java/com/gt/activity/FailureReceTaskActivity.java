package com.gt.activity;

import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.common.adapter.FailureReceiveTaskAdapter;
import com.common.base.BaseActivity;
import com.common.model.IntentExtra;
import com.common.model.MsMessage;
import com.gt.officeagent.R;

public class FailureReceTaskActivity extends BaseActivity {

	private List<String> repeatReceiveTasks;
	private int type;

	@Override
	public int bindLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.activity_failurereceivetask;
	}

	@Override
	public void initParmers() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initViewsAndValues(Bundle savedInstanceState) {
		type=intentExtra.getType();
		repeatReceiveTasks = (List<String>) intentExtra.getValue();
		viewUtils.view(R.id.lv_failReceiveTask, ListView.class).setAdapter(
				new FailureReceiveTaskAdapter(this, repeatReceiveTasks,type));
	}

	@Override
	public void releaseOnDestory() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClickable(View view) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doRequest(int requestCode, Object... needParams) {
		// TODO Auto-generated method stub

	}

	@Override
	public void requestCallBack(int requestCode, MsMessage msm,
			String failedReason) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doEmptyCheck(Object... needParams) {
		// TODO Auto-generated method stub

	}

	@Override
	public void intentCallback(int requestCode, int resultCode, IntentExtra data) {
		// TODO Auto-generated method stub

	}

}
