package com.gt.activity;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.common.base.BaseActivity;
import com.common.model.AgentUser;
import com.common.model.ApiCode;
import com.common.model.IntentExtra;
import com.common.model.MsMessage;
import com.common.utils.AppUtils;
import com.common.utils.Constant;
import com.common.utils.SPUtils;
import com.common.utils.T;
import com.google.gson.Gson;
import com.gt.officeagent.R;

public class UpdatePwdActivity extends BaseActivity {

	private EditText edt_oldpwd;
	private EditText edt_newpwd;
	private EditText edt_repeatpwd;

	@Override
	public int bindLayoutId() {
		return R.layout.activity_updatepwd;
	}

	@Override
	public void initParmers() {

	}

	@Override
	public void initViewsAndValues(Bundle savedInstanceState) {
		setEnable(R.id.btn_submit_update, false);
		edt_oldpwd = (EditText) viewUtils.view(R.id.edt_oldpwd);
		edt_newpwd = (EditText) viewUtils.view(R.id.edt_newpwd);
		edt_repeatpwd = (EditText) viewUtils.view(R.id.edt_querenpwd);
		viewUtils.setOnClickListener(R.id.btn_submit_update);

		edt_oldpwd.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.length() == 0) {
					buttonChange();
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		edt_newpwd.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.length() > 5 && s.length() < 17) {
					buttonChange();
				} else {

				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		edt_repeatpwd.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.length() == 0) {
					T.showShort(UpdatePwdActivity.this, "请再次输入新密码");
					buttonChange();
				} else {
					buttonChange();
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		//TODO
//		if(Constant.ISSCAN){
//			KeyBoardUtils.closeKeybordShowCursor(this,edt_oldpwd,edt_newpwd,edt_repeatpwd);
//		 }
	}

	@Override
	public void releaseOnDestory() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClickable(View view) {
		switch (view.getId()) {
		case R.id.btn_submit_update:
			if (TextUtils.isEmpty(viewUtils.getViewText(R.id.edt_newpwd))
					|| TextUtils
							.isEmpty(viewUtils.getViewText(R.id.edt_oldpwd))
					|| TextUtils.isEmpty(viewUtils
							.getViewText(R.id.edt_querenpwd))) {
				T.showShort(this, "旧密码、新密码、确认密码均不能为空！");
			}

			if (!viewUtils.getViewText(R.id.edt_newpwd).equals(
					viewUtils.getViewText(R.id.edt_querenpwd))) {
				T.showShort(UpdatePwdActivity.this, "两次密码输入不一致");
				buttonChange();
			} else {
				doRequest(Constant.editPassword);
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void doRequest(int requestCode, Object... needParams) {
		switch (requestCode) {
		case Constant.editPassword:
			Gson gson = new Gson();
			Map<String, String> map = new HashMap<String, String>();
			AgentUser user = new AgentUser();
			map.put("agentId", SPUtils.getAgent(this).getAgentId() + "");
			map.put("agentUserCode", SPUtils.getUser(this).getAgentUserCode()
					+ "");
			map.put("userId", SPUtils.getUser(this).getAgentUserId() + "");
			map.put("oldPassword", SPUtils.getUser(this).getAgentUserPswd()
					+ "");
			map.put("newPassword", viewUtils.getViewText(R.id.edt_newpwd));
			String paramValues = gson.toJson(map);
			requestServer(Constant.USER_SERVER, Constant.editPassword,
					AppUtils.createParams(ApiCode.editPassword.code,
							paramValues));
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
		} else {
			T.showShort(this, msm.getMessage());
		}
	}

	// 非空检查（按钮是否可点击）
	private void buttonChange() {
		if (!TextUtils.isEmpty(viewUtils.getViewText(R.id.edt_oldpwd))
				&& !TextUtils.isEmpty(viewUtils.getViewText(R.id.edt_newpwd))
				&& !TextUtils
						.isEmpty(viewUtils.getViewText(R.id.edt_querenpwd))
				&& viewUtils.getViewText(R.id.edt_newpwd).length() > 5
				&& viewUtils.getViewText(R.id.edt_newpwd).length() < 17
				&& viewUtils.getViewText(R.id.edt_newpwd).length() > 5
				&& viewUtils.getViewText(R.id.edt_newpwd).length() < 17) {
			setEnable(R.id.btn_submit_update, true);
		} else {
			setEnable(R.id.btn_submit_update, false);
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
			if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER) {
				if (TextUtils.isEmpty(viewUtils.getViewText(R.id.edt_newpwd))
						|| TextUtils.isEmpty(viewUtils
								.getViewText(R.id.edt_oldpwd))
						|| TextUtils.isEmpty(viewUtils
								.getViewText(R.id.edt_querenpwd))) {
					T.showShort(this, "旧密码、新密码、确认密码均不能为空！");
				}

				if (!viewUtils.getViewText(R.id.edt_newpwd).equals(
						viewUtils.getViewText(R.id.edt_querenpwd))) {
					T.showShort(UpdatePwdActivity.this, "两次密码输入不一致");
					buttonChange();
				} else {
					doRequest(Constant.editPassword);
				}
			}
		}
		return super.onKeyUp(keyCode, event);
	}
}
