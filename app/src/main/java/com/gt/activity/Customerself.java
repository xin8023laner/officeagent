package com.gt.activity;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import cn.zjl.zxing.CaptureActivity;

import com.common.base.BaseActivity;
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

public class Customerself extends BaseActivity {
	private Handler mHandler = new Handler();
	private AgentReceiveTask agentReceiveTask = null;
	private EditText edt_input_expresscode;
	private ScannerManager mScannerManager;
	private SoundUtils mSoundUtils;
	private boolean continousScanMode = false;
	// 扫描模式
	public static final int SINGLE_SCAN_MODE = 1;
	public static final int CONTINUOUS_SCAN_MODE = 2;

	@Override
	public int bindLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.activity_userself;
	}

	@Override
	public void initParmers() {

	}

	@Override
	public void initViewsAndValues(Bundle savedInstanceState) {
		viewUtils.view(R.id.edt_input_querenma, EditText.class).setFilters(
				new InputFilter[] { new InputFilter.LengthFilter(4) });
		edt_input_expresscode = (EditText) viewUtils
				.view(R.id.edt_inputinfo_expresscode);
		viewUtils.setOnClickListener(R.id.btn_select, R.id.iv_userself_scan,
				R.id.btn_sureuserself, R.id.iv_userself_clearall);
		if (Constant.ISSCAN) {
			mSoundUtils = SoundUtils.getInstance();
			mSoundUtils.init(this);
			setScan();
			//TODO
//			KeyBoardUtils.closeKeybordShowCursor(this,edt_input_expresscode);
		}

		edt_input_expresscode.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.length() == 0) {
					viewUtils.setGone(R.id.iv_userself_clearall);
				} else {
					viewUtils.setVisiable(R.id.iv_userself_clearall);
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
		viewUtils.setGone(R.id.ll_userself_success);
	}

	@Override
	public void releaseOnDestory() {
		SoundUtils.getInstance().release();
	}

	@Override
	public void onClickable(View view) {
		switch (view.getId()) {
		case R.id.iv_userself_clearall:
			viewUtils.setViewText(R.id.edt_inputinfo_expresscode, "");
			viewUtils.view(R.id.ll_userself_content).setVisibility(view.GONE);
			viewUtils.view(R.id.btn_sureuserself).setVisibility(view.GONE);
			break;
		case R.id.btn_select:
			if (viewUtils.getViewText(R.id.edt_inputinfo_expresscode)
					.equals("")) {
				T.showShort(this, "请输入或扫描调出订单号");
			} else {
				// viewUtils.view(R.id.ll_userself_content).setVisibility(
				// view.VISIBLE);
				// viewUtils.view(R.id.btn_sureuserself).setVisibility(
				// view.VISIBLE);
				doRequest(Constant.queryReceiveTask);
			}
			break;
		case R.id.iv_userself_scan:
			if(Constant.ISSCAN){
				intentForResult(CapturePDAActivity.class, new IntentExtra(
						Constant.SCAN_KEHUZITI, agentReceiveTask),
						Constant.queryReceiveTask);
			}else{
				intentForResult(CaptureActivity.class, new IntentExtra(
						Constant.SCAN_KEHUZITI, agentReceiveTask),
						Constant.queryReceiveTask);
			}
			break;
		case R.id.btn_sureuserself:
			try {
				if (viewUtils.getViewText(R.id.edt_input_querenma).equals("")) {
					T.showShort(this, "请输入确认码");
				} else if (viewUtils.getViewText(R.id.edt_input_querenma)
						.equals(agentReceiveTask.getReceiverInsureCode())
						|| viewUtils.getViewText(R.id.edt_input_querenma)
								.equals("0000")) {
					doRequest(Constant.editReceiveTaskFinishSelf);
				} else {
					T.showShort(this, "您输入的确认码不正确！");
				}
			} catch (Exception e) {
				e.printStackTrace();
				if (viewUtils.getViewText(R.id.edt_input_querenma).equals("")) {
					T.showShort(this, "请输入确认码");
				} else {
					T.showShort(this, "确认码输入有误或快件已签收");
				}
			}
			break;
		default:
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

		case Constant.editReceiveTaskFinishSelf:
			Gson gsonself = new Gson();
			Map<String, String> map = new HashMap<String, String>();
			map.put("agentId", SPUtils.getAgent(this).getAgentId() + "");
			map.put("agentUserName", SPUtils.getUser(this).getAgentUserName());
			map.put("agentUserId", SPUtils.getUser(this).getAgentUserId() + "");
			map.put("agentUserCode", SPUtils.getUser(this).getAgentUserCode());
			map.put("taskId", agentReceiveTask.getTaskId() + "");
			map.put("expressCode", agentReceiveTask.getExpressCode());
			map.put("carrierId", agentReceiveTask.getCarrierId() + "");
			map.put("receiveName",
					viewUtils.getViewText(R.id.edt_receiver_name));
			map.put("phone", viewUtils.getViewText(R.id.edt_receiver_phone));
			map.put("receiverAddress",
					viewUtils.getViewText(R.id.edt_receiver_address));
			String paramsValue = gsonself.toJson(map);
			requestServer(
					Constant.RECEIVETASK_SERVER,
					Constant.editReceiveTaskFinishSelf,
					AppUtils.createParams(
							ApiCode.editReceiveTaskFinishSelf.code, paramsValue));
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
		case Constant.queryReceiveTask:
			try {
				if (msm.getResult() == 0) {// 成功
					agentReceiveTask = new Gson().fromJson(msm.getData()
							.toString(), AgentReceiveTask.class);
					viewUtils.setViewText(R.id.tv_gtexpresscode,
							agentReceiveTask.getWaybillCode());
					viewUtils.setViewText(R.id.tv_companyName,
							agentReceiveTask.getCarrierName());
					viewUtils.setViewText(R.id.tv_expresscode,
							agentReceiveTask.getExpressCode());
					viewUtils.view(R.id.btn_sureuserself).setVisibility(
							View.VISIBLE);
					viewUtils.view(R.id.ll_userself_success).setVisibility(
							View.GONE);
					viewUtils.view(R.id.ll_userself_content).setVisibility(
							View.VISIBLE);
					//TODO
//					if(Constant.ISSCAN){
//						KeyBoardUtils.closeKeybordShowCursor(this,viewUtils.view(R.id.edt_receiver_name,EditText.class)
//								,viewUtils.view(R.id.edt_receiver_phone,EditText.class)
//								,viewUtils.view(R.id.edt_receiver_address,EditText.class));
//					 }
					// viewUtils.view(R.id.btn_sureuserself).setVisibility(View.VISIBLE);
				} else {// 测试用
					// AgentReceiveTask agentReceiveTask = new
					// AgentReceiveTask();
					//
					// agentReceiveTask.setCarrierName("顺丰快递");
					// agentReceiveTask.setExpressCode("38178728128");
					// agentReceiveTask.setReceiverName("猫哭");
					// agentReceiveTask.setReceiverPhone("18500608270");
					// agentReceiveTask.setReceiverAddress("海兴大厦B座一层西塔网络");
					// agentReceiveTask
					// .setTaskStatus((short) Constant.TASKSTATUS_OK);
					//
					// intent(SendExpressDetailActivity.class, new IntentExtra(
					// Constant.SCAN_KEHUQIANSHOU, agentReceiveTask));
					// finish();
					T.showShort(this, msm.getMessage());

				}
			} catch (Exception e) {
				T.showShort(this, "获得个人数据异常！");
			}
			break;

		case Constant.editReceiveTaskFinishSelf:
			SimpleDateFormat sdf = new SimpleDateFormat(
					"yyyy-MM-dd     hh:mm:ss");
			Date currentDate = new Date(System.currentTimeMillis());
			String date = sdf.format(currentDate);
			if (msm.getResult() == 0) {
				T.showShort(this, msm.getMessage());
				viewUtils.setViewText(R.id.edt_receiver_name,
						agentReceiveTask.getReceiverName());
				viewUtils.setViewText(R.id.edt_receiver_phone,
						agentReceiveTask.getReceiverPhone());
				viewUtils.setViewText(R.id.edt_receiver_address,
						agentReceiveTask.getReceiverAddress());
				setEditStatus(R.id.edt_receiver_name);
				setEditStatus(R.id.edt_receiver_phone);
				setEditStatus(R.id.edt_receiver_address);
				viewUtils.view(R.id.ll_input_querenma).setVisibility(View.GONE);
				viewUtils.view(R.id.btn_sureuserself).setVisibility(View.GONE);
				viewUtils.view(R.id.ll_success_querenma).setVisibility(
						View.VISIBLE);
				viewUtils.view(R.id.ll_userself_success).setVisibility(
						View.VISIBLE);

				viewUtils.setViewText(R.id.tv_success_querenma,
						agentReceiveTask.getReceiverInsureCode());
				viewUtils.setViewText(R.id.tv_userself_date, date);
				viewUtils.setViewText(R.id.tv_agentUserName,
						SPUtils.getUser(this).getAgentUserName() + "" + "   "
								+ SPUtils.getUser(this).getTelphone() + "");
			} else {
				T.showShort(this, msm.getMessage());
			}
			break;
		default:
			break;
		}

	}

	@Override
	public void doEmptyCheck(Object... needParams) {
		// TODO Auto-generated method stub

	}

	@Override
	public void intentCallback(int requestCode, int resultCode, IntentExtra data) {
		viewUtils.setViewText(R.id.edt_inputinfo_expresscode,
				(String) data.getValue());
		if (edt_input_expresscode.getText() == null) {
			T.showShort(this, "请输入或扫描调出订单号");
			return;
		} else {
			// viewUtils.view(R.id.ll_userself_content)
			// .setVisibility(View.VISIBLE);
			// viewUtils.view(R.id.btn_sureuserself).setVisibility(View.VISIBLE);
			doRequest(Constant.queryReceiveTask);
		}

	}

	/**
	 * 扫描初始设置
	 * 
	 * @param context
	 * @param message
	 */
	private void setScan() {

		// TODO Auto-generated method stub
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

		// TODO Auto-generated method stub
		mScannerManager.scannerEnable(scanSwitch);
	}

	/**
	 * 连扫控制
	 * 
	 * @param context
	 * @param message
	 */
	private void continuousScanMode(boolean continuousScan) {
		// TODO Auto-generated method stub
		if (continuousScan
				&& mScannerManager.getScanMode() == Customerself.SINGLE_SCAN_MODE) {
			mScannerManager.setScanMode(ScannerManager.SCAN_CONTINUOUS_MODE);
			mScannerManager.startContinuousScan();
			continousScanMode = true;
		} else if (!continuousScan
				&& mScannerManager.getScanMode() == Customerself.CONTINUOUS_SCAN_MODE) {
			mScannerManager.setScanMode(ScannerManager.SCAN_SINGLE_MODE);
			mScannerManager.stopContinuousScan();
			continousScanMode = false;
		}
	}

	@Override
	protected void onResume() {

		// TODO Auto-generated method stub
		super.onResume();
		// 添加扫描监听
		if (Constant.ISSCAN) {
			mScannerManager.addScannerStatusListener(mIScannerStatusListener);
		}

	}

	// 创建扫描监听
	private IScannerStatusListener mIScannerStatusListener = new IScannerStatusListener() {

		@Override
		public void onScannerStatusChanage(int arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onScannerResultChanage(final byte[] arg0) {
			// TODO Auto-generated method stub
			// mSoundUtils.success();
			mHandler.post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					String s = null;
					try {
						s = new String(arg0, "UTF-8");
						onScan(s);
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
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
			if (edt_input_expresscode.getText() == null) {
				T.showShort(this, "请输入或扫描调出订单号");
				return;
			} else {
				// viewUtils.view(R.id.ll_userself_content).setVisibility(
				// View.VISIBLE);
				doRequest(Constant.queryReceiveTask);
			}
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// 移除扫描监听
		if (Constant.ISSCAN) {
			mScannerManager
					.removeScannerStatusListener(mIScannerStatusListener);
			if (mScannerManager.getScanMode() == Customerself.CONTINUOUS_SCAN_MODE) {
				mScannerManager.stopContinuousScan();
			}
		}

	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (Constant.ISSCAN) {
			if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER) {
				try {
					if (viewUtils.view(R.id.btn_sureuserself, Button.class) == null) {
						if (viewUtils.getViewText(
								R.id.edt_inputinfo_expresscode).equals("")) {
							T.showShort(this, "请输入或扫描调出订单号");
						} else {
							// viewUtils.view(R.id.ll_userself_content).setVisibility(
							// View.VISIBLE);
							// viewUtils.view(R.id.btn_sureuserself).setVisibility(
							// View.VISIBLE);
							doRequest(Constant.queryReceiveTask);
						}
					} else if (viewUtils.view(R.id.btn_sureuserself,
							Button.class) != null) {
						if (viewUtils.getViewText(R.id.edt_input_querenma)
								.equals(agentReceiveTask
										.getReceiverInsureCode())
								|| viewUtils.getViewText(
										R.id.edt_input_querenma).equals("0000")) {
							doRequest(Constant.editReceiveTaskFinishSelf);
						} else if (viewUtils.getViewText(
								R.id.edt_input_querenma).equals("")) {
							T.showShort(this, "请输入确认码");
						}

					}
				} catch (Exception e) {
					e.printStackTrace();
					if (viewUtils.getViewText(R.id.edt_input_querenma).equals(
							"")) {
						T.showShort(this, "请输入确认码");
					}
					T.showShort(this, "确认码输入有误或快件已签收");

				}
			}
		}
		return super.onKeyUp(keyCode, event);
	}
}
