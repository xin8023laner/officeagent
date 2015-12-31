package com.gt.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import cn.jpush.android.api.JPushInterface;

import com.common.base.BaseActivity;
import com.common.events.LogoutEvent;
import com.common.model.AgentCarrier;
import com.common.model.AgentReceiveTask;
import com.common.model.ApiCode;
import com.common.model.IntentExtra;
import com.common.model.MsMessage;
import com.common.model.Software;
import com.common.service.UpdateService;
import com.common.utils.AppUtils;
import com.common.utils.Constant;
import com.common.utils.DialogUtils;
import com.common.utils.SPUtils;
import com.common.utils.T;
import com.common.utils.ViewUtils;
import com.google.gson.Gson;
import com.gt.officeagent.R;
import com.lidroid.xutils.exception.DbException;

public class SettingActivity extends BaseActivity {

	

	@Override
	public int bindLayoutId() {
		return R.layout.activity_setting;
	}

	@Override
	public void initViewsAndValues(Bundle savedInstanceState) {
		viewUtils.setOnClickListener(R.id.rl_updatepwd_setting,
				R.id.btn_exitLogin,R.id.rl_checkupdate_text,R.id.rl_suggestion_setting);
		
		
		viewUtils.setViewText(R.id.tv_setting_version, AppUtils.getVersionName(this));
	}

	@Override
	public void releaseOnDestory() {

	}

	@Override
	public void onClickable(View view) {
		switch (view.getId()) {
		// case R.id.ll_topbar_backLayout:
		// finish();
		// break;
		// case R.id.rl_setting_help:
		// // 使用帮助
		// break;
		 case R.id.rl_suggestion_setting:
		 // 意见反馈
		 intent(SuggestionActivity.class,null);
		 break;
		 case R.id.rl_checkupdate_text:
		 // 版本更新
		 doRequest(Constant.softwareUpdate);
		 break;
		// case R.id.rl_setting_about:
		// // 关于我们
		// intent(AboutActivity.class);
		// break;
		case R.id.rl_updatepwd_setting:
			// 修改密码
			intent(UpdatePwdActivity.class, null);
			break;
		case R.id.btn_exitLogin:
			// 注销
			AgentCarrier agentCarrier;
			SPUtils.clear(this);
			try {
				agentCarrier = getDbUtils().findFirst(AgentCarrier.class);
				List<AgentReceiveTask> list=getDbUtils().findAll(AgentReceiveTask.class);
				if(agentCarrier!=null&&list!=null&&list.size()>0){
					ViewUtils viewUtils=DialogUtils.showLoginOutDialog(this, "您有未入库/未签收的快递单号，退出将清空了哦！");
					viewUtils.view(R.id.btn_dialog_ok).setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							try {
								getDbUtils().deleteAll(AgentReceiveTask.class);
								getDbUtils().deleteAll(AgentCarrier.class);
								JPushInterface.stopPush(SettingActivity.this);//停止极光推送
								AppUtils.loginOut(SettingActivity.this);
								intent(LoginActivity.class,null);
								getEventBus().post(new LogoutEvent("logout"));
								finish();
							} catch (DbException e) {
								e.printStackTrace();
							}
						}
					});
				}else{
					AppUtils.loginOut(SettingActivity.this);
					intent(LoginActivity.class,null);
					getEventBus().post(new LogoutEvent("logout"));
					finish();
				}
			} catch (DbException e) {
				e.printStackTrace();
			}
			break;
		}

	}

	@Override
	public void doRequest(int requestCode, Object... needParams) {
		switch (requestCode) {
		case Constant.softwareUpdate:
			Gson gson = new Gson();
			Map<String, String> map = new HashMap<String, String>();
			map.put("versionType", "1");
			map.put("is_school",
					"1");
			String paramsValue = gson.toJson(map);
			requestServer(Constant.BASIC_SERVER, Constant.softwareUpdate,
					AppUtils.createParams(ApiCode.softwareUpdate.code,
							paramsValue));
			break;
		default:
			break;
		}

	}

	@Override
	public void requestCallBack(int requestCode, MsMessage msm,
			String failedReason) {
		switch (requestCode) {
		case Constant.softwareUpdate:
			if (msm.getResult()==0) {
			
				Software software = new Gson().fromJson(msm.getData()
						.toString(), Software.class);
				if (AppUtils.getVersionCode(SettingActivity.this) < software
						.getVersionNumber()) {
					// 需要更新
					ViewUtils vUtils = new ViewUtils(SettingActivity.this,
							R.layout.dialog_update_app);

					final Dialog dialog = new Dialog(SettingActivity.this,
							R.style.Dialog);
				

					vUtils.setViewText(R.id.dialog_net_message,
							software.getVersionName());
					
					dialog.setContentView(vUtils.getRootView());
					dialog.setCancelable(false);
					dialog.setCanceledOnTouchOutside(false);
					
					dialog.show();
					vUtils.view(R.id.dialog_net_resure).setOnClickListener(
							new OnClickListener() {

								@Override
								public void onClick(View v) {
									// 开启Service执行后台下载操作
									Intent intent = new Intent(
											SettingActivity.this,
											UpdateService.class);
								if(Constant.ISSCAN){
									intent.putExtra("url", Constant.PDAAPK_URL);
								}else{
									intent.putExtra("url", Constant.MobileAPK_URL);
								}
//									intent.putExtra("url", Constant.MobileAPK_URL);
									startService(intent);
									dialog.dismiss();

								}
							});
					vUtils.view(R.id.dialog_net_cancel).setOnClickListener(
							new OnClickListener() {

								@Override
								public void onClick(View v) {
									dialog.dismiss();
								}
							});
				}else{
					T.showShort(this, "当前已经是最新版本");
				}
			}else {
				T.showShort(this, "不需要更新");
			}
			break;
		}

	}

	@Override
	public void doEmptyCheck(Object... needParams) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initParmers() {
		// TODO Auto-generated method stub

	}

	@Override
	public void intentCallback(int requestCode, int resultCode, IntentExtra data) {
		// TODO Auto-generated method stub

	}
}
