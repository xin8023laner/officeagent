package com.gt.activity;

import android.os.Bundle;
import android.view.View;

import com.common.base.BaseActivity;
import com.common.model.IntentExtra;
import com.common.model.MsMessage;
import com.gt.officeagent.R;

/***
 * 意见反馈
 * 
 * @author zrt
 * 
 */

public class FeedbookActivity extends BaseActivity{





	@Override
	public void initParmers() {

	}


	@Override
	public int bindLayoutId() {
		return R.layout.activity_feedbook;
	}

	@Override
	public void initViewsAndValues(Bundle savedInstanceState) {
//		if(Constant.ISSCAN){
//			KeyBoardUtils.closeKeybordShowCursor(this,viewUtils.view(R.id.et_feedbookContent,EditText.class));
//		 }
		
	}

	@Override
	public void releaseOnDestory() {
		
		
	}

	@Override
	public void onClickable(View v) {
//		switch (v.getId()) {
//		case R.id.ll_topbar_backLayout:
//			finish();
//			break;
//		case R.id.btn_feedbookSubmit:
//			if(TextUtils.isEmpty(et_feedbookContent.getText())){
//				T.showShort(this, "反馈内容不能为空");
//				return;
//			}
//			doRequest(GT.SUBMIT_SUGGESS);
//			break;
//		default:
//			break;
//		}
		
	}

	@Override
	public void doRequest(int requestCode, Object... needParams) {
//		AgentUser user = SharedPrefUtils.getUser(this);
//		RequestParams params = new RequestParams();
//		params.addBodyParameter("suggestionType", "1");
//		params.addBodyParameter("telphone", user.getTelphone());
//		params.addBodyParameter("email",user.getEmail());
//		params.addBodyParameter("content",et_feedbookContent.getText().toString());
//		params.addBodyParameter("callPhone","Android");
//		requestServer(GT.SUGGESTION_SERVER, requestCode, true, false, params);
		
	}

	@Override
	public void requestCallBack(int requestCode, MsMessage msm,
			String failedReason) {
//		if(isSucess){
//			T.showShort(this, "感谢您宝贵的建议，我们会及时与您联系");
//			finish();
//		}
		
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
