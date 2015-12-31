package com.gt.activity;

import java.util.HashMap;
import java.util.Map;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;

import com.common.base.BaseActivity;
import com.common.model.ApiCode;
import com.common.model.IntentExtra;
import com.common.model.MsMessage;
import com.common.model.Software;
import com.common.service.UpdateService;
import com.common.utils.AppUtils;
import com.common.utils.Constant;
import com.common.utils.ViewUtils;
import com.google.gson.Gson;
import com.gt.officeagent.R;
import com.zltd.industry.ScannerManager;

/**
 * 程序启动页
 * 
 * @author mcx
 * 
 */
public class SplashActivity extends BaseActivity {

	private Handler handler = new Handler();

	private Runnable intentActivity = new Runnable() {
		@Override
		public void run() {
			if (AppUtils.isLogined(SplashActivity.this)) {

				intent(MainActivity.class, null);

			} else {
				intent(LoginActivity.class, null);
			}
			finish();
		}
	};

	@Override
	public void initParmers() {
		doRequest(Constant.softwareUpdate);
		
		try {
			ScannerManager.getInstance().scannerEnable(true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Constant.ISSCAN=false;
		}
	}

	@Override
	public int bindLayoutId() {
		return R.layout.activity_splash;
	}

	@Override
	public void initViewsAndValues(Bundle savedInstanceState) {
	}

	@Override
	public void releaseOnDestory() {

	}

	@Override
	public void onClickable(View view) {

	}

	@Override
	public void doRequest(int requestCode, Object... needParams) {
		switch (requestCode) {
		case Constant.softwareUpdate:
			Gson gson = new Gson();
			Map<String, String> map = new HashMap<String, String>();
			map.put("versionType", "1");
			map.put("is_school","1");
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
				if (msm == null || msm.getData() == null) {
					handler.postDelayed(intentActivity, 1000);
				}
				Software software = new Gson().fromJson(msm.getData()
						.toString(), Software.class);
				if (AppUtils.getVersionCode(SplashActivity.this) < software
						.getVersionNumber()) {
					// 需要更新
					ViewUtils vUtils = new ViewUtils(SplashActivity.this,
							R.layout.dialog_update_app);

					final Dialog dialog = new Dialog(SplashActivity.this,
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
											SplashActivity.this,
											UpdateService.class);
									if(Constant.ISSCAN){
										intent.putExtra("url", Constant.PDAAPK_URL);
									}else{
										intent.putExtra("url", Constant.MobileAPK_URL);
									}
									startService(intent);
									dialog.dismiss();
									//TODO 修改表结构,第五版的数据库与此不同，之后的这里注掉即可
//							    	try {
//										getDbUtils().deleteAll(AgentReceiveTask.class);
//										getDbUtils().dropTable(AgentReceiveTask.class);
//										SplashActivity.this.finish();
//									} catch (DbException e) {
//										// TODO Auto-generated catch block
//										e.printStackTrace();
//									}

//									handler.postDelayed(intentActivity, 1000);
								}
							});
//					vUtils.view(R.id.dialog_net_cancel).setOnClickListener(
//							new OnClickListener() {
//
//								@Override
//								public void onClick(View v) {
////									dialog.dismiss();
////									handler.postDelayed(intentActivity, 1000);
//								}
//							});
					vUtils.view(R.id.dialog_net_cancel).setOnClickListener(null);
				} else {
					// 不需要更新
					handler.postDelayed(intentActivity, 1000);
				}
			} else {
				handler.postDelayed(intentActivity, 1000);
			}
			break;
		}

	}

	@Override
	public void doEmptyCheck(Object... needParams) {

	}

	@Override
	public void intentCallback(int requestCode, int resultCode, IntentExtra data) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onResume() {

		super.onResume();

		// JPushInterface.onResume(this);
	}

	@Override
	protected void onPause() {

		super.onPause();

		// JPushInterface.onPause(this);
	}

}
