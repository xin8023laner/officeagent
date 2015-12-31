package com.gt.activity;

import android.os.Bundle;
import android.view.View;

import com.common.base.BaseActivity;
import com.common.model.IntentExtra;
import com.common.model.MsMessage;
import com.gt.officeagent.R;

/***
 * 关于
 * 
 * @author
 * 
 */
public class AboutActivity extends BaseActivity {


	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	


	@Override
	public void initParmers() {

	}

	@Override
	public int bindLayoutId() {
		return R.layout.activity_about;
	}

	@Override
	public void initViewsAndValues(Bundle savedInstanceState) {
		
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
