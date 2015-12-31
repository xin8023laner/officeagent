package com.gt.activity;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import cn.zjl.zxing.CaptureActivity;

import com.common.base.BaseActivity;
import com.common.events.AutoRefreshEvent;
import com.common.events.AutoRefreshPickupEvent;
import com.common.model.ApiCode;
import com.common.model.IntentExtra;
import com.common.model.MsMessage;
import com.common.model.TaskWaybill;
import com.common.utils.AppUtils;
import com.common.utils.CommonUtils;
import com.common.utils.Constant;
import com.common.utils.KeyBoardUtils;
import com.common.utils.SPUtils;
import com.common.utils.SoundUtils;
import com.common.utils.T;
import com.common.utils.ViewUtils;
import com.google.gson.Gson;
import com.gt.officeagent.R;
import com.zltd.industry.ScannerManager;
import com.zltd.industry.ScannerManager.IScannerStatusListener;

/***
 * 快件详情
 * 
 * @author
 * 
 */
public class PickupExpressDetailActivity extends BaseActivity {

	private TaskWaybill taskWaybill = null;
	private ViewUtils vDialogUtils;
	private Dialog dialog;
	private TelephonyManager telephonyManager;
	private ScannerManager mScannerManager;
	private SoundUtils mSoundUtils;
	private boolean continousScanMode = false;
	// 扫描模式
	public static final int SINGLE_SCAN_MODE = 1;
	public static final int CONTINUOUS_SCAN_MODE = 2;
	private Handler mHandler = new Handler();
	private boolean isPrint;//是否是小标签

	private String remark;

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void initParmers() {
		if (Constant.ISSCAN) {
			mSoundUtils = SoundUtils.getInstance();
			mSoundUtils.init(this);
			setScan();
		}
	}

	@Override
	public int bindLayoutId() {
		return R.layout.activity_pickup_expressdetail;
	}

	@Override
	public void initViewsAndValues(Bundle savedInstanceState) {
		getEventBus().register(this);
		taskWaybill = (TaskWaybill) intentExtra.getValue();

		viewUtils.setViewText(R.id.tv_expressdetail_sendname, TextUtils
				.isEmpty(taskWaybill.getSenderName()) ? "寄件人:未知"
				: ("寄件人:" + taskWaybill.getSenderName()));
		viewUtils.setViewText(R.id.tv_expressdetail_sendphone,
				TextUtils.isEmpty(taskWaybill.getSenderPhone()) ? "未知"
						: taskWaybill.getSenderPhone());
		viewUtils.setViewText(R.id.tv_expressdetail_sendaddr,
				TextUtils.isEmpty(taskWaybill.getSenderAddress()) ? "未知"
						: taskWaybill.getSenderAddress());

		viewUtils.setViewText(R.id.tv_expressdetail_receivename, TextUtils
				.isEmpty(taskWaybill.getReceiverName()) ? "收件人:未知"
				: ("收件人:" + taskWaybill.getReceiverName()));
		viewUtils.setViewText(R.id.tv_expressdetail_receivephone, TextUtils
				.isEmpty(taskWaybill.getReceiverTelphone()) ? "未知"
				: taskWaybill.getReceiverTelphone());
		viewUtils.setViewText(R.id.tv_expressdetail_receiveaddr,
				TextUtils.isEmpty(taskWaybill.getReceiverAddress()) ? "未知"
						: taskWaybill.getReceiverAddress());

		viewUtils.setViewText(R.id.tv_expressdetail_carriername, TextUtils
				.isEmpty(taskWaybill.getCarrierName()) ? "快递公司:未知"
				: ("快递公司:" + taskWaybill.getCarrierName()));
		// TODO 此处应该设置用户写入的重量
		viewUtils.setViewText(R.id.tv_expressdetail_weight, TextUtils
				.isEmpty(taskWaybill.getWeight()) ? "物品重量:暂无" : ("物品重量:"
				+ taskWaybill.getWeight() + "KG"));
		viewUtils.setViewText(R.id.tv_expressdetail_time, TextUtils
				.isEmpty(taskWaybill.getAppointmentTime()) ? "期望取件时间:暂无"
				: ("期望取件时间:" + taskWaybill.getAppointmentTime()));
		if (taskWaybill.getFreight() != 0) {
			viewUtils.setGone(R.id.ll_btn_submit,
					R.id.llpickup_expressdetail_code);
			viewUtils.setVisiable(R.id.tv_expressdetail_carriercodegone);
			String carrierDes = TextUtils.isEmpty(taskWaybill.getExpressCode()) ? TextUtils
					.isEmpty(taskWaybill.getGtLabel()) ? "快递单号："
					: ("贯通标签：" + taskWaybill.getGtLabel())
					: ("快递单号：" + taskWaybill.getExpressCode());
			viewUtils.setViewText(R.id.tv_expressdetail_carriercodegone,
					carrierDes);
			// if (TextUtils.isEmpty(taskWaybill.getExpressCode())) {
			// viewUtils.setViewText(R.id.tv_expressdetail_carriercodegone,"快递单号：");
			// } else {
			// viewUtils.setViewText(R.id.tv_expressdetail_carriercodegone,"快递单号："+taskWaybill.getExpressCode());
			// }
		}
		viewUtils.setViewText(R.id.edt_expressdetail_money, taskWaybill
				.getFreight() == 0 ? "" : (taskWaybill.getFreight() + "元"));
		double weight = Double.parseDouble(TextUtils.isEmpty(taskWaybill.getWeight())?0+"":taskWaybill.getWeight());
		viewUtils.setViewText(R.id.edt_expressdetail_weight, weight == 0 ? ""
				: taskWaybill.getWeight());

		viewUtils.setViewText(R.id.edt_expressdetail_type,
				TextUtils.isEmpty(taskWaybill.getCommodityName()) ? ""
						: taskWaybill.getCommodityName());

		viewUtils.setViewText(
				R.id.edt_expressdetail_remark,
				TextUtils.isEmpty(taskWaybill.getRemark()) ? "" : taskWaybill
						.getRemark());

		viewUtils.setViewText(
				R.id.edt_expressdetail_expresscode,
				TextUtils.isEmpty(taskWaybill.getExpressCode()) ? TextUtils
						.isEmpty(taskWaybill.getGtLabel()) ? "" : taskWaybill
						.getGtLabel() : taskWaybill.getExpressCode());

		setEditStatus(R.id.edt_expressdetail_money);
		setEditStatus(R.id.edt_expressdetail_weight);
		setEditStatus(R.id.edt_expressdetail_type);
		setEditStatus(R.id.edt_expressdetail_remark);
		setEditStatus(R.id.edt_expressdetail_expresscode);

		if (!TextUtils.isEmpty(taskWaybill.getExpressCode())
				|| !TextUtils.isEmpty(taskWaybill.getGtLabel())) {
			viewUtils.setGone(R.id.iv_expressdetail_scan);
			viewUtils.setBackgroundResource(R.id.ll_expressdetail_code,
					R.color.transparent);
		} else {
			viewUtils.setViewText(R.id.tv_expressdetail_carriercodegone,
					"快递单号：");
		}
		viewUtils.setOnClickListener(R.id.btn_expressdetail_submit,
				R.id.iv_expressdetail_scan, R.id.iv_expressdetail_call,
				R.id.btn_expressdetail_failure, R.id.btn_expressdetail_return);

		telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		// TODO
		if (Constant.ISSCAN) {
//			KeyBoardUtils.closeKeybordShowCursor(this, viewUtils.view(
//					R.id.edt_expressdetail_expresscode, EditText.class));
			viewUtils.setGone(R.id.iv_expressdetail_scan);// 不显示扫描图标，因为本页面就可扫描
		}

	}

	@Override
	public void releaseOnDestory() {
		getEventBus().unregister(this);
	}

	@Override
	public void onClickable(View view) {
		switch (view.getId()) {
		case R.id.btn_expressdetail_submit:

			if (TextUtils.isEmpty(viewUtils
					.getViewText(R.id.edt_expressdetail_money))) {
				T.showShort(this, "请填写揽收价格");
			} else if (TextUtils.isEmpty(viewUtils
					.getViewText(R.id.edt_expressdetail_weight))) {
				T.showShort(this, "请填写实际重量");
			} else if (TextUtils.isEmpty(viewUtils
					.getViewText(R.id.edt_expressdetail_expresscode))) {
				T.showShort(this, "请填写单号");
			} else {
				// TODO判断是不是小标签，如果是小标签，则根据服务器传递过来的字段判断此快递公司是否支持小标签；如果不是小标签则直接请求服务器
				// 判断是不是小标签
				if (viewUtils.getViewText(R.id.edt_expressdetail_expresscode)
						.contains("GT")) {// 贯通标签
					// 请求接口，获得是否支持小标签
					doRequest(Constant.queryIsPrint);
				} else {// 正常快递单号
					String code = viewUtils
							.getViewText(R.id.edt_expressdetail_expresscode);
					if (viewUtils.getViewText(
							R.id.edt_expressdetail_expresscode).length() < 6
							|| viewUtils.getViewText(
									R.id.edt_expressdetail_expresscode)
									.contains("http")) {
						T.showShort(this, "您的单号格式不正确！");
						return;
					}
					if ("韵达快递".equals(taskWaybill.getCarrierName())
							&& code.length() > 13) {
						code = TextUtils.substring(code, 0, 13);
					}
					viewUtils.setViewText(R.id.edt_expressdetail_expresscode,
							code);
					editRUKUDialog(view.getId());
				}

			}
			break;
		case R.id.btn_expressdetail_failure:
		case R.id.btn_expressdetail_return:
			editRUKUDialog(view.getId());
			break;
		// doRequest(Constant.takeMySendTaskReturn);

		// break;
		case R.id.iv_expressdetail_scan:
			// if(Constant.ISSCAN){
			// intentForResult(CapturePDAActivity.class, new IntentExtra(
			// Constant.SCAN_PICKUPDETAIL, null), 0);
			// }else{
			intentForResult(CaptureActivity.class, new IntentExtra(
					Constant.SCAN_NORMAL, null), 0);
			// }
			break;
		case R.id.iv_expressdetail_call:
			if (telephonyManager.getSimState() == TelephonyManager.SIM_STATE_ABSENT) {
				T.showShort(this, "无SIM卡！");
			} else {
				CommonUtils.call(this, taskWaybill.getSenderPhone());
				// doRequest(Constant.addCustomer);
			}
			break;

		}

	}

	@Override
	public void doRequest(int requestCode, Object... needParams) {

		showLoadingDialog(requestCode);

		switch (requestCode) {
		case Constant.takeMySendTaskSuccess:
			Gson gson = new Gson();
			Map<String, String> maps = new HashMap<String, String>();
			maps.put("taskId", taskWaybill.getTaskId() + "");
			// if (!TextUtils.isEmpty(viewUtils
			// .getViewText(R.id.edt_expressdetail_money))) {
			maps.put(
					"freight",
					taskWaybill.getFreight() == 0 ? viewUtils
							.getViewText(R.id.edt_expressdetail_money)
							: taskWaybill.getFreight() + "");
			// }
			// if (!TextUtils.isEmpty(viewUtils
			// .getViewText(R.id.edt_expressdetail_weight))) {
			maps.put("weight",
					viewUtils.getViewText(R.id.edt_expressdetail_weight));
			// }
			maps.put("commodityName",
					viewUtils.getViewText(R.id.edt_expressdetail_type));
			maps.put("remark",
					viewUtils.getViewText(R.id.edt_expressdetail_remark));
			// if (!TextUtils.isEmpty(viewUtils
			// .getViewText(R.id.edt_expressdetail_expresscode))) {
			// TODO 判断是存入小标签还是单号
			if(isPrint){//是小标签
				maps.put("gtLabel", viewUtils.getViewText(R.id.edt_expressdetail_expresscode));
			}else{
				maps.put("expressCode",viewUtils.getViewText(R.id.edt_expressdetail_expresscode));
			}

			String paramsValues = gson.toJson(maps);
			requestServer(Constant.SENDTASK_SERVER,
					Constant.takeMySendTaskSuccess, AppUtils.createParams(
							ApiCode.takeMySendTaskSuccess.code, paramsValues));
			break;
		case Constant.takeMySendTaskFailure:
			Gson gson2 = new Gson();
			Map<String, String> maps2 = new HashMap<String, String>();
			maps2.put("taskId", taskWaybill.getTaskId() + "");
			maps2.put("remark", (String) needParams[0]);
			maps2.put("agentUserId", SPUtils.getUser(this).getAgentUserId()
					+ "");
			maps2.put("agentUserCode", SPUtils.getUser(this).getAgentUserCode());
			maps2.put("agentUserName", SPUtils.getUser(this).getAgentUserName());
			String paramsValues2 = gson2.toJson(maps2);
			requestServer(Constant.SENDTASK_SERVER,
					Constant.takeMySendTaskFailure, AppUtils.createParams(
							ApiCode.takeMySendTaskFailure.code, paramsValues2));
			break;
		case Constant.returnSendTask:
			Gson gson1 = new Gson();
			Map<String, String> maps1 = new HashMap<String, String>();
			maps1.put("taskId", taskWaybill.getTaskId() + "");
			maps1.put("remark", (String) needParams[0]);
			maps1.put("agentUserId", SPUtils.getUser(this).getAgentUserId()
					+ "");
			maps1.put("agentUserCode", SPUtils.getUser(this).getAgentUserCode());
			maps1.put("agentUserName", SPUtils.getUser(this).getAgentUserName());
			String paramsValues1 = gson1.toJson(maps1);
			requestServer(Constant.SENDTASK_SERVER, Constant.returnSendTask,
					AppUtils.createParams(ApiCode.returnSendTask.code,
							paramsValues1));
			break;
		case Constant.addCustomer:
			Gson gson3 = new Gson();
			Map<String, String> maps3 = new HashMap<String, String>();
			maps3.put("taskId", taskWaybill.getTaskId() + "");
			maps3.put("agentUserId", taskWaybill.getAgentUserId() + "");
			maps3.put("receiverName", taskWaybill.getReceiverName());
			maps3.put("receiverPhone", TextUtils.isEmpty(taskWaybill
					.getSenderTelphone()) ? taskWaybill.getSenderPhone()
					: taskWaybill.getSenderTelphone());
			maps3.put("expressCode", taskWaybill.getExpressCode());
			maps3.put("carrierName", taskWaybill.getCarrierName());
			// maps.put("carrierId", taskWaybill.getCarrierId()+"");
			maps3.put("agentId", SPUtils.getAgent(this).getAgentId() + "");
			requestServer(
					Constant.RECEIVETASK_SERVER,
					Constant.addCustomer,
					AppUtils.createParams(ApiCode.addCustomer.code,
							gson3.toJson(maps3)));
			break;
		case Constant.queryIsPrint:
			Gson gson4 = new Gson();
			Map<String, String> maps4 = new HashMap<String, String>();
			maps4.put("agentId", SPUtils.getAgent(this).getAgentId() + "");
			maps4.put("parentCarrierId", taskWaybill.getCarrierId() + "");
			requestServer(Constant.BASIC_SERVER,Constant.queryIsPrint,AppUtils.createParams(ApiCode.queryIsPrint.code,
							gson4.toJson(maps4)));
			break;
		default:
			break;
		}
	}

	@Override
	public void requestCallBack(int requestCode, MsMessage msm,
			String failedReason) {
		hideLoadingDialog(requestCode);
		T.showShort(this, msm.getMessage());
		switch (requestCode) {
		case Constant.queryIsPrint:
			if (msm.getResult() == 0) {
				if(msm.getData()==null){
					msm.setData(0);
				}
				if (Integer.parseInt((String) msm.getData())==1) {// 1-支持小标签,2-不支持小标签，0-没有此功能
					isPrint=true;
					editRUKUDialog(R.id.btn_expressdetail_submit);
				}else if(Integer.parseInt((String) msm.getData())==2){
					T.showLong(this, "抱歉，小标签功能已关闭！");
				}else{// 0
					T.showLong(this, "抱歉，请开启该快递公司小标签功能！");
				}
			}
			break;

		default:
			if (msm.getResult() == 0) {// 成功
				getEventBus().post(new AutoRefreshPickupEvent());
				if (requestCode == Constant.takeMySendTaskFailure) {
					editLanshouFailDialog(remark);
				} else {
					finish();
				}
			}
			break;
		}
	}

	@Override
	public void doEmptyCheck(Object... needParams) {

	}

	@Override
	public void intentCallback(int requestCode, int resultCode, IntentExtra data) {
		String code = (String) data.getValue();
		if (TextUtils.isEmpty(code)) {
			T.showShort(this, "单号不能为空！");
			return;
		} else if (code.length() < 6 || code.contains("http")) {
			T.showShort(this, "您的单号格式不正确！");
			return;
		}
		// 判断是不是小标签
		if (code.contains("GT")) {// 贯通标签
			// 请求接口，获得是否支持小标签
			doRequest(Constant.queryIsPrint);
		} else {// 正常快递单号
			if ("韵达快递".equals(taskWaybill.getCarrierName())
					&& code.length() > 13) {
				code = TextUtils.substring(code, 0, 13);
			}
			viewUtils.setViewText(R.id.edt_expressdetail_expresscode, code);
		}

	}

	public void onEvent(AutoRefreshEvent event) {

	}

	/*
	 * 揽收失败/退回
	 */
	private void editRUKUDialog(int id) {
		vDialogUtils = showRUKUDialog(this);
		vDialogUtils.setGone(R.id.tv_dialog_text);
		// TODO
		vDialogUtils.view(R.id.btn_dialog_cancel).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// 取消
						dialog.cancel();
					}
				});
		switch (id) {
		case R.id.btn_expressdetail_failure:
			vDialogUtils.setVisiable(R.id.et_dialog_reasonFailure);
			vDialogUtils.view(R.id.btn_dialog_ok).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							if (TextUtils.isEmpty(vDialogUtils
									.getViewText(R.id.et_dialog_reasonFailure))) {
								T.showShort(PickupExpressDetailActivity.this,
										"请输入揽收失败原因！");
							} else {
								remark = vDialogUtils
										.getViewText(R.id.et_dialog_reasonFailure);
								doRequest(Constant.takeMySendTaskFailure,
										remark);// 揽收失败任务
								dialog.cancel();
							}
						}
					});
			break;
		case R.id.btn_expressdetail_return:
			// vDialogUtils = showRUKUDialog(this);
			// TODO退回原因，setvisible
			vDialogUtils.setVisiable(R.id.tv_dialog_text);
			vDialogUtils.setViewText(R.id.tv_dialog_text,
					"退回操作会将订单从服务站系统中退回至总公司，是否继续");
			vDialogUtils.setViewText(R.id.btn_dialog_cancel, "取消");
			vDialogUtils.setViewText(R.id.btn_dialog_ok, "确定");
			vDialogUtils.view(R.id.btn_dialog_ok).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							doRequest(Constant.returnSendTask, "");// 退回任务
							dialog.cancel();
						}
					});
			break;
		case R.id.btn_expressdetail_submit:
			vDialogUtils.setGone(R.id.et_dialog_reason);
			vDialogUtils.setVisiable(R.id.tv_dialog_text);
			vDialogUtils
					.setViewText(
							R.id.tv_dialog_text,
							"请确认输入的信息是否有误：\n快递收费："
									+ viewUtils
											.getViewText(R.id.edt_expressdetail_money)
									+ "\n实际重量："
									+ viewUtils
											.getViewText(R.id.edt_expressdetail_weight)
									+ "\n物品名称："
									+ viewUtils
											.getViewText(R.id.edt_expressdetail_type)
									+ "\n备注信息："
									+ viewUtils
											.getViewText(R.id.edt_expressdetail_remark));
			vDialogUtils.setViewText(R.id.btn_dialog_cancel, "修改");
			vDialogUtils.view(R.id.btn_dialog_ok).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// vDialogUtils.getViewText(R.id.et_dialog_reason);

							doRequest(Constant.takeMySendTaskSuccess);
							dialog.cancel();

						}
					});
			vDialogUtils.view(R.id.btn_dialog_cancel).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							dialog.cancel();
						}
					});
			break;

		}

	}

	/**
	 * 快递入库弹框
	 * 
	 * @param context
	 * @param requesxxxtCode
	 */
	private ViewUtils showRUKUDialog(Context context) {

		dialog = new Dialog(context, R.style.Dialog);
		ViewUtils viewUtils = new ViewUtils(context, R.layout.dialog_ruku);

		dialog.setContentView(viewUtils.getRootView());

		dialog.show();
		return viewUtils;
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (Constant.ISSCAN) {
			if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER) {

				if (TextUtils.isEmpty(viewUtils
						.getViewText(R.id.edt_expressdetail_money))) {
					T.showShort(this, "请填写揽收价格");
				} else if (TextUtils.isEmpty(viewUtils
						.getViewText(R.id.edt_expressdetail_weight))) {
					T.showShort(this, "请填写实际重量");
				} else {
					doRequest(Constant.takeMySendTaskSuccess);
				}
			}
		}
		return super.onKeyUp(keyCode, event);

	}

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
			// TODO判断是不是小标签，如果公司支持小标签。。。

			viewUtils.setViewText(R.id.edt_expressdetail_expresscode, barcode);
			// if (TextUtils.isEmpty(viewUtils
			// .getViewText(R.id.edt_expressdetail_money))) {
			// T.showShort(this, "请填写揽收价格");
			// } else if (TextUtils.isEmpty(viewUtils
			// .getViewText(R.id.edt_expressdetail_weight))) {
			// T.showShort(this, "请填写实际重量");
			// } else if (TextUtils.isEmpty(viewUtils
			// .getViewText(R.id.edt_expressdetail_expresscode))) {
			// T.showShort(this, "请填写单号");
			// } else {
			// editRUKUDialog(viewUtils.view(R.id.btn_expressdetail_submit,Button.class).getId());
			// }
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
			if (mScannerManager.getScanMode() == InputInfoActivity.CONTINUOUS_SCAN_MODE) {
				mScannerManager.stopContinuousScan();
			}
		}

	}

	private void editLanshouFailDialog(String remark) {
		vDialogUtils = showRUKUDialog(this);
		vDialogUtils.view(R.id.tv_dialog_text, TextView.class).setText(
				Html.fromHtml("由于    " + "<font color='red'>" + remark
						+ "</font>" + "    原因揽收失败！"));
		vDialogUtils.setViewText(R.id.btn_dialog_cancel, "再看看");
		vDialogUtils.setViewText(R.id.btn_dialog_ok, "我知道啦");
		vDialogUtils.view(R.id.btn_dialog_cancel).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// 取消
						dialog.cancel();
					}
				});
		vDialogUtils.view(R.id.btn_dialog_ok).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						finish();
						dialog.cancel();
					}
				});

	}

}
