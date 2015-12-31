package com.gt.activity;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;

import com.common.base.BaseActivity;
import com.common.events.CloseEvent;
import com.common.model.AgentReceiveTask;
import com.common.model.ApiCode;
import com.common.model.IntentExtra;
import com.common.model.MsMessage;
import com.common.utils.AppUtils;
import com.common.utils.Constant;
import com.common.utils.SPUtils;
import com.common.utils.SoundUtils;
import com.common.utils.T;
import com.google.gson.Gson;
import com.gt.officeagent.R;
import com.zltd.industry.ScannerManager;
import com.zltd.industry.ScannerManager.IScannerStatusListener;

public class InputInfoActivity extends BaseActivity {
	private ScannerManager mScannerManager;
	private SoundUtils mSoundUtils;
	private boolean continousScanMode = false;
	// 扫描模式
	public static final int SINGLE_SCAN_MODE = 1;
	public static final int CONTINUOUS_SCAN_MODE = 2;
	private Handler mHandler = new Handler();

	@Override
	public int bindLayoutId() {
		return R.layout.activity_inputinfo;
	}

	@Override
	public void initParmers() {
		mSoundUtils = SoundUtils.getInstance();
		mSoundUtils.init(this);
		setScan();
	}

	@Override
	public void initViewsAndValues(Bundle savedInstanceState) {
		getEventBus().register(this);
		viewUtils.setOnClickListener(R.id.btn_inputinfo_submit);
	}

	@Override
	public void releaseOnDestory() {
		getEventBus().unregister(this);
		SoundUtils.getInstance().release();
	}

	@Override
	public void onClickable(View view) {
		switch (view.getId()) {
		case R.id.btn_inputinfo_submit:
			if (!TextUtils.isEmpty(viewUtils
					.getViewText(R.id.edt_inputinfo_expresscode))) {
				if (viewUtils.getViewText(
						R.id.edt_expressdetail_expresscode).length() < 6
						|| viewUtils.getViewText(
								R.id.edt_expressdetail_expresscode)
								.contains("http")) {
					T.showShort(this, "您的单号格式不正确！");
					return;
				}
				doRequest(Constant.queryReceiveTask);
			} else {
				T.showShort(this, "请输入或扫描单号");
			}
			break;
		}

	}

	@Override
	public void doRequest(int requestCode, Object... needParams) {
		showLoadingDialog(requestCode);

		switch (requestCode) {
		case Constant.queryReceiveTask:
			Gson gson = new Gson();
			Map<String, String> maps = new HashMap<String, String>();
			maps.put("agentId", SPUtils.getAgent(this).getAgentId() + "");
			maps.put("expressCode",
					viewUtils.getViewText(R.id.edt_inputinfo_expresscode));
			String paramsValues = gson.toJson(maps);
			requestServer(Constant.RECEIVETASK_SERVER,
					Constant.queryReceiveTask, AppUtils.createParams(
							ApiCode.queryReceiveTask.code, paramsValues));
			break;

		default:
			break;
		}

	}

	@Override
	public void requestCallBack(int requestCode, MsMessage msm,
			String failedReason) {
		hideLoadingDialog(requestCode);
		try {
			if (msm.getResult() == 0) {// 成功
				AgentReceiveTask agentReceiveTask = new Gson().fromJson(msm
						.getData().toString(), AgentReceiveTask.class);
				intent(InputInfoDetailActivity.class, new IntentExtra(0,
						agentReceiveTask));
			} else {
				T.showShort(this, msm.getMessage());
			}
		} catch (Exception e) {
			T.showShort(this, "获得个人数据异常！");
		}
	}

	@Override
	public void doEmptyCheck(Object... needParams) {

	}
	public void onEvent(CloseEvent closeEvent) {
		finish();

	}

	private void setScan() {
		// 获取扫描控制器实例
		mScannerManager = ScannerManager.getInstance();
		// 打开扫描头开关
		scanSwitch(true);
		continuousScanMode(false);
	}

	/**
	 * 扫描开关
	 * 
	 * @param context
	 * @param message
	 */

	private void scanSwitch(boolean scanSwitch) {
		mScannerManager.scannerEnable(scanSwitch);
	}

	/**
	 * 连扫控制
	 * 
	 * @param context
	 * @param message
	 */
	private void continuousScanMode(boolean continuousScan) {
		if (continuousScan
				&& mScannerManager.getScanMode() == InputInfoActivity.SINGLE_SCAN_MODE) {
			mScannerManager.setScanMode(ScannerManager.SCAN_CONTINUOUS_MODE);
			mScannerManager.startContinuousScan();
			continousScanMode = true;
		} else if (!continuousScan
				&& mScannerManager.getScanMode() == InputInfoActivity.CONTINUOUS_SCAN_MODE) {
			mScannerManager.setScanMode(ScannerManager.SCAN_SINGLE_MODE);
			mScannerManager.stopContinuousScan();
			continousScanMode = false;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 添加扫描监听
		mScannerManager.addScannerStatusListener(mIScannerStatusListener);
	}

	// 创建扫描监听
	private IScannerStatusListener mIScannerStatusListener = new IScannerStatusListener() {

		@Override
		public void onScannerStatusChanage(int arg0) {

		}

		@Override
		public void onScannerResultChanage(final byte[] arg0) {
			mHandler.post(new Runnable() {

				@Override
				public void run() {
					String s = null;
					try {
						s = new String(arg0, "UTF-8");
						onScan(s);
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
			});
		}
	};

	protected void onScan(String barcode) {
		if (barcode != null) {
			mSoundUtils.success();
			viewUtils.setViewText(R.id.edt_inputinfo_expresscode, barcode);
			if (!TextUtils.isEmpty(viewUtils
					.getViewText(R.id.edt_inputinfo_expresscode))) {
				doRequest(Constant.queryReceiveTask);
			} else {
				T.showShort(this, "请输入或扫描单号");
			}
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		// 移除扫描监听
		mScannerManager
				.removeScannerStatusListener(mIScannerStatusListener);
		if (mScannerManager.getScanMode() == InputInfoActivity.CONTINUOUS_SCAN_MODE) {
			mScannerManager.stopContinuousScan();
		}

	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER) {
			if (!TextUtils.isEmpty(viewUtils
					.getViewText(R.id.edt_inputinfo_expresscode))) {
				doRequest(Constant.queryReceiveTask);
			} else {
				T.showShort(this, "请输入或扫描单号");
			}
		}
		return super.onKeyUp(keyCode, event);
	}

	@Override
	public void intentCallback(int requestCode, int resultCode, IntentExtra data) {
		
	}
}