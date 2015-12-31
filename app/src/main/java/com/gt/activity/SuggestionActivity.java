package com.gt.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;

import com.common.base.BaseActivity;
import com.common.model.AgentSuggestion;
import com.common.model.ApiCode;
import com.common.model.IntentExtra;
import com.common.model.MsMessage;
import com.common.utils.AppUtils;
import com.common.utils.Constant;
import com.common.utils.SPUtils;
import com.common.utils.T;
import com.google.gson.Gson;
import com.gt.officeagent.R;

public class SuggestionActivity extends BaseActivity {

	public static AgentSuggestion as = new AgentSuggestion();
	public static List<AgentSuggestion> lists = new ArrayList<AgentSuggestion>();

	@Override
	public int bindLayoutId() {
		return R.layout.activity_suggestion;
	}

	@Override
	public void initParmers() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initViewsAndValues(Bundle savedInstanceState) {
		viewUtils.setOnClickListener(R.id.btn_suggestion_submit);
		//TODO
//		if(Constant.ISSCAN){
//			KeyBoardUtils.closeKeybordShowCursor(this,viewUtils.view(R.id.edt_suggestion_Content,EditText.class));
//		 }
	}

	@Override
	public void releaseOnDestory() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClickable(View view) {
		switch (view.getId()) {
		case R.id.btn_suggestion_submit:
			if(!TextUtils.isEmpty(viewUtils.getViewText(R.id.edt_suggestion_Content))){
				doRequest(Constant.suggestion);
			}else{
				T.showShort(this, "请输入对我们的意见！");
			}
			
			break;

		default:
			break;
		}
	}

	@Override
	public void doRequest(int requestCode, Object... needParams) {
		switch (requestCode) {
		case Constant.suggestion:
			Gson gson = new Gson();
			Map<String, String> map = new HashMap<String, String>();
			map.put("agentId",SPUtils.getAgent(this).getAgentId()+"");
			map.put("agentUserId", SPUtils.getUser(this).getAgentUserId()+"");
			map.put("suggestionType", "1");
			map.put("suggestionContent",viewUtils.getViewText(R.id.edt_suggestion_Content));
			map.put("telphone", SPUtils.getUser(this).getTelphone());
			String paramsValue = gson.toJson(map);

			
			requestServer(Constant.BASIC_SERVER, Constant.suggestion,
					AppUtils.createParams(ApiCode.suggestion.code, paramsValue));
			break;
		default:
			break;
		}
	}

	@Override
	public void requestCallBack(int requestCode, MsMessage msm,
			String failedReason) {
		if (msm.getResult() == 0) {
			T.showShort(this, msm.getMessage());
			finish();
		} else {
			T.showShort(this, msm.getMessage());
		}
	}

	@Override
	public void doEmptyCheck(Object... needParams) {

	}

	@Override
	public void intentCallback(int requestCode, int resultCode, IntentExtra data) {

	}
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (Constant.ISSCAN) {
			if(!TextUtils.isEmpty(viewUtils.getViewText(R.id.edt_suggestion_Content))){
				doRequest(Constant.suggestion);
			}else{
				T.showShort(this, "请输入对我们的意见！");
			}
			}
		return super.onKeyUp(keyCode, event);

	
	}
}
