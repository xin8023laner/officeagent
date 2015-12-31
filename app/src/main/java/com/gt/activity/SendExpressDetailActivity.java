package com.gt.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.common.base.BaseActivity;
import com.common.events.AutoRefreshEvent;
import com.common.model.AgentReceiveTask;
import com.common.model.ApiCode;
import com.common.model.IntentExtra;
import com.common.model.MsMessage;
import com.common.utils.AppUtils;
import com.common.utils.CommonUtils;
import com.common.utils.Constant;
import com.common.utils.DialogUtils;
import com.common.utils.KeyBoardUtils;
import com.common.utils.SPUtils;
import com.common.utils.T;
import com.common.utils.ViewUtils;
import com.google.gson.Gson;
import com.gt.officeagent.R;

/***
 * 快件详情
 * 
 * @author
 * 
 */
public class SendExpressDetailActivity extends BaseActivity{

	private PopupWindow popupWindow;

	private AgentReceiveTask agentReceiveTask = null;

	private ViewUtils vDialogUtils;
//	private SoundUtils mSoundUtils;
	private Dialog dialog;
	private TelephonyManager telephonyManager;
	private String unFinishReason;
	private int type;
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void initParmers() {
//		if(Constant.ISSCAN){
//		mSoundUtils = SoundUtils.getInstance();
//		mSoundUtils.init(this);
//		}
	}

	@Override
	public int bindLayoutId() {
		return R.layout.activity_send_expressdetail;
	}

	@Override
	public void initViewsAndValues(Bundle savedInstanceState) {
		type=intentExtra.getType();
		// 设置edittext可输入大小
		viewUtils.view(R.id.edt_expressdetail_finishedsubmitCode,
				EditText.class).setFilters(
				new InputFilter[] { new InputFilter.LengthFilter(4) });
		agentReceiveTask = (AgentReceiveTask) intentExtra.getValue();

		viewUtils.setViewText(R.id.tv_expressdetail_carriername, TextUtils
				.isEmpty(agentReceiveTask.getCarrierName()) ? "未知"
				: agentReceiveTask.getCarrierName());

		viewUtils.setViewText(R.id.tv_expressdetail_expresscode, TextUtils
				.isEmpty(agentReceiveTask.getExpressCode()) ? "未知"
				: agentReceiveTask.getExpressCode());
		viewUtils.setViewText(R.id.edt_expressdetail_name, TextUtils
				.isEmpty(agentReceiveTask.getReceiverName()) ? ""
				: agentReceiveTask.getReceiverName());

		viewUtils.setViewText(R.id.edt_expressdetail_phone, TextUtils
				.isEmpty(agentReceiveTask.getReceiverPhone()) ? ""
				: agentReceiveTask.getReceiverPhone());

		viewUtils.setViewText(R.id.edt_expressdetail_addr, TextUtils
				.isEmpty(agentReceiveTask.getReceiverAddress()) ? ""
				: agentReceiveTask.getReceiverAddress());

		viewUtils.view(R.id.rg_expressdetail_status, RadioGroup.class)
		.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {

				switch (arg1) {
				case R.id.rb_expressdetail_finish:
					viewUtils
							.setGone(R.id.ll_expressdetail_unfinishedReasen);
					viewUtils
							.setVisiable(R.id.ll_expressdetail_finishedsubmitCode);
					break;
				case R.id.rb_expressdetail_unfinish:
					viewUtils
							.setVisiable(R.id.ll_expressdetail_unfinishedReasen);
					viewUtils
							.setGone(R.id.ll_expressdetail_finishedsubmitCode);
					break;

				default:
					break;
				}
			}
		});
		if (type==Constant.LIST_SEND_SUCCESS) {
			viewUtils.view(R.id.rg_expressdetail_status, RadioGroup.class)
					.check(R.id.rb_expressdetail_finish);
			viewUtils.setViewText(R.id.tv_expressdetail_statustxt, "妥投成功");

			setEditStatus(R.id.edt_expressdetail_addr);

			viewUtils.setGone(R.id.ll_expressdetail_unfinishedReasen,
					R.id.rg_expressdetail_status,
					R.id.ll_expressdetail_finishedsubmitCode);
			if (!TextUtils.isEmpty(agentReceiveTask.getReceiverName())
					&& !TextUtils.isEmpty(agentReceiveTask.getReceiverPhone())
					&& !TextUtils.isEmpty(agentReceiveTask.getReceiverAddress())) {
				viewUtils.setGone(R.id.btn_expressdetail_submit);
				setEditStatus(R.id.edt_expressdetail_name);
				setEditStatus(R.id.edt_expressdetail_phone);
				setEditStatus(R.id.edt_expressdetail_addr);
			}
		} 
//		else if (agentReceiveTask.getTaskStatus() == Constant.TASKSTATUS_INLIB) {
//			viewUtils.view(R.id.rg_expressdetail_status, RadioGroup.class)
//					.check(R.id.rb_expressdetail_finish);
//			viewUtils.setViewText(R.id.tv_expressdetail_statustxt, "已入库");
//
//			viewUtils.setGone(R.id.ll_expressdetail_unfinishedReasen);
//		} 
		else if (type==Constant.LIST_SEND_PAISONG) {
//			viewUtils.view(R.id.rg_expressdetail_status, RadioGroup.class)
//			.check(R.id.rb_expressdetail_finish);
			viewUtils.setViewText(R.id.tv_expressdetail_statustxt, "在派送");

			viewUtils.setGone(R.id.ll_expressdetail_unfinishedReasen,
					R.id.rg_expressdetail_status,
					R.id.ll_expressdetail_finishedsubmitCode);
		} 
//		else if (agentReceiveTask.getTaskStatus() == Constant.TASKSTATUS_EXCEPTION) {
//			viewUtils.view(R.id.rg_expressdetail_status, RadioGroup.class)
//					.check(R.id.rb_expressdetail_unfinish);
//
//			unFinishReason(R.id.tv_expressdetail_statustxt);
//
//			viewUtils.setVisiable(R.id.ll_expressdetail_unfinishedReasen);
//			viewUtils.setGone(R.id.rg_expressdetail_status,
//					R.id.iv_expressdetail_select,
//					R.id.ll_expressdetail_unfinishedReasen,
//					R.id.ll_expressdetail_finishedsubmitCode);
//		} 
		else if (type==Constant.LIST_SEND_FAILED) {// 未妥投
//			viewUtils.view(R.id.rg_expressdetail_status, RadioGroup.class)
//					.check(R.id.rb_expressdetail_unfinish);
			viewUtils.setViewText(R.id.tv_expressdetail_statustxt, "未妥投");
//			viewUtils.setVisiable(R.id.ll_expressdetail_unfinishedReasen,
//					R.id.rg_expressdetail_status);
			viewUtils.setVisiable(R.id.ll_expressdetail_unfinishedReasen);
			viewUtils.setGone(R.id.ll_expressdetail_finishedsubmitCode,R.id.rg_expressdetail_status);
			unFinishReason(R.id.tv_expressdetail_unfinishedReasen);
//			viewUtils.view(R.id.rb_expressdetail_finish,
//					RadioButton.class).setEnabled(false);
			viewUtils.setEnable(R.id.ll_expressdetail_unfinishedReasen, false);
		}else if(type==Constant.SCAN_KEHUQIANSHOU){//从“单签”入口进入
			switch (agentReceiveTask.getTaskStatus()) {
			case Constant.TASKSTATUS_INLIB://入库
				viewUtils.setViewText(R.id.tv_expressdetail_statustxt, "已入库");
				viewUtils.view(R.id.rg_expressdetail_status, RadioGroup.class)
				.check(R.id.rb_expressdetail_finish);
				viewUtils.setGone(R.id.ll_expressdetail_unfinishedReasen);
				break;
			case Constant.TASKSTATUS_EXCEPTION://异常
				viewUtils.setViewText(R.id.tv_expressdetail_statustxt, "异常");
				viewUtils.view(R.id.rg_expressdetail_status, RadioGroup.class)
				.check(R.id.rb_expressdetail_finish);
				viewUtils.setGone(R.id.ll_expressdetail_unfinishedReasen);
				break;
			case Constant.TASKSTATUS_PAISONG://派送
				viewUtils.setViewText(R.id.tv_expressdetail_statustxt, "在派送");
				viewUtils.view(R.id.rg_expressdetail_status, RadioGroup.class)
				.check(R.id.rb_expressdetail_finish);
				viewUtils.setGone(R.id.ll_expressdetail_unfinishedReasen);
				break;
			case Constant.TASKSTATUS_UNFINISH://未妥投
//				viewUtils.view(R.id.rg_expressdetail_status, RadioGroup.class).check(R.id.rb_expressdetail_unfinish);
				viewUtils.setViewText(R.id.tv_expressdetail_statustxt, "未妥投");
				viewUtils.setVisiable(R.id.ll_expressdetail_unfinishedReasen);
				viewUtils.setGone(R.id.ll_expressdetail_finishedsubmitCode,R.id.rg_expressdetail_status);
				unFinishReason(R.id.tv_expressdetail_unfinishedReasen);
//				viewUtils.view(R.id.rb_expressdetail_finish,RadioButton.class).setEnabled(false);
				viewUtils.setEnable(R.id.ll_expressdetail_unfinishedReasen, false);
				break;
			case Constant.TASKSTATUS_OK://已妥投
				viewUtils.view(R.id.rg_expressdetail_status, RadioGroup.class).check(R.id.rb_expressdetail_finish);
				viewUtils.setViewText(R.id.tv_expressdetail_statustxt, "妥投成功");

				setEditStatus(R.id.edt_expressdetail_addr);

				viewUtils.setGone(R.id.ll_expressdetail_unfinishedReasen,R.id.rg_expressdetail_status,R.id.ll_expressdetail_finishedsubmitCode);
				if (!TextUtils.isEmpty(agentReceiveTask.getReceiverName())
						&& !TextUtils.isEmpty(agentReceiveTask.getReceiverPhone())
						&& !TextUtils.isEmpty(agentReceiveTask.getReceiverAddress())) {
					viewUtils.setGone(R.id.btn_expressdetail_submit);
					setEditStatus(R.id.edt_expressdetail_name);
					setEditStatus(R.id.edt_expressdetail_phone);
					setEditStatus(R.id.edt_expressdetail_addr);
				}
			   break;
			}
//			viewUtils.view(R.id.rg_expressdetail_status, RadioGroup.class)
//			.check(R.id.rb_expressdetail_finish);
//			viewUtils.setGone(R.id.ll_expressdetail_unfinishedReasen);
		}
		viewUtils.setOnClickListener(R.id.btn_expressdetail_submit,
				R.id.ll_expressdetail_unfinishedReasen, R.id.iv_expressdetail_call);
		
		viewUtils.view(R.id.edt_expressdetail_phone, EditText.class).setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				if (arg1) {
					viewUtils.view(R.id.edt_expressdetail_phone, EditText.class).setInputType(InputType.TYPE_CLASS_PHONE);
				}
			}
		});
		telephonyManager=(TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		if(Constant.ISSCAN){
			KeyBoardUtils.closeKeybordShowCursor(this,viewUtils.view(R.id.edt_expressdetail_finishedsubmitCode,EditText.class)
					,viewUtils.view(R.id.edt_expressdetail_phone,EditText.class));
		 }
	}

	private void unFinishReason(int id) {
		try {// 拒收原因
			String json = agentReceiveTask.getStatusDesc()
					.replaceAll("”", "\"");
			String endJson = json.replaceAll("“", "\"");
			JSONArray array = new JSONArray(endJson);
			JSONObject object = new JSONObject(
					array.getString(array.length() - 1));
			switch (id) {
			case R.id.tv_expressdetail_statustxt:
				viewUtils.setViewText(R.id.tv_expressdetail_statustxt, "妥投失败（"
						+ object.getString("desc") + "）");
				break;
			case R.id.tv_expressdetail_unfinishedReasen:
				viewUtils.setViewText(R.id.tv_expressdetail_unfinishedReasen,
						object.getString("desc"));
				unFinishReason=object.getString("desc");
				break;
			}
		} catch (Exception e) {
			switch (id) {
			case R.id.tv_expressdetail_statustxt:
				viewUtils.setViewText(R.id.tv_expressdetail_statustxt,
						"妥投失败（暂无拒绝原因" + "）");
				break;
			case R.id.tv_expressdetail_unfinishedReasen:
				viewUtils.setViewText(R.id.tv_expressdetail_unfinishedReasen,
						"（暂无拒绝原因" + "）");
				break;
			}
			e.printStackTrace();
		}
	}

	private void initPopWindow() {
		final ArrayList<String> list = new ArrayList<String>();
		list.add("客户拒收");
		list.add("订单分发错误");
		list.add("无法联系到客户");

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.item_pop_expressdetail,
				R.id.tv_pop_expressdetail_item, list);
		ViewUtils vUtils = new ViewUtils(this, R.layout.popup_unfinish_reason);

		vUtils.view(R.id.lv_pop_expressdetail, ListView.class).setAdapter(
				adapter);
		vUtils.view(R.id.lv_pop_expressdetail, ListView.class)
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {

						viewUtils.setViewText(
								R.id.tv_expressdetail_unfinishedReasen,
								list.get(arg2));
						unFinishReason=list.get(arg2);
						popupWindow.dismiss();
					}
				});

		popupWindow = new PopupWindow(vUtils.getRootView(),
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable(getResources()));
		popupWindow.setFocusable(true);

		popupWindow.showAsDropDown(
				viewUtils.view(R.id.tv_expressdetail_unfinishedReasen), 0, 0);

	}

	@Override
	public void releaseOnDestory() {

	}

	@Override
	public void onClickable(View view) {
		switch (view.getId()) {
		case R.id.btn_expressdetail_submit:

			if (agentReceiveTask.getTaskStatus() == Constant.TASKSTATUS_OK
			||type==Constant.LIST_SEND_PAISONG
			||agentReceiveTask.getTaskStatus() == Constant.TASKSTATUS_UNFINISH) {// 已妥投/派送/未妥投
				doRequest(Constant.editReceiveTask);
			} else {
				if (viewUtils.view(R.id.rb_expressdetail_finish,
						RadioButton.class).isChecked()) {// 妥投
					if (viewUtils
							.view(R.id.ll_expressdetail_finishedsubmitCode)
							.isShown()) {
						String inSureCode = viewUtils
								.getViewText(R.id.edt_expressdetail_finishedsubmitCode);
//						 if ("".equals(inSureCode)|| "0000".equals(inSureCode)|| inSureCode.equals(agentReceiveTask.getReceiverInsureCode())) {
						if ("".equals(inSureCode)) {
							editRUKUDialog();
						} else if ("0000".equals(inSureCode)
								|| inSureCode.equals(agentReceiveTask
										.getReceiverInsureCode())) {
							doRequest(Constant.editReceiveTaskFinish);
						} else {
							T.showShort(this, "您输入的确认码不正确！");
						}
//					}
					}
				} else {
					if (!TextUtils.isEmpty(unFinishReason)) {
						doRequest(Constant.editReceiveTaskUnFinish);
					} else {
						T.showShort(this, "请填写未妥投原因");
					}

				}
			}
			break;
		case R.id.ll_expressdetail_unfinishedReasen:
			initPopWindow();
			break;
		case R.id.iv_expressdetail_call:
			if(telephonyManager.getSimState()==TelephonyManager.SIM_STATE_ABSENT){
				T.showShort(this, "无SIM卡！");
			}else{
			if (!TextUtils.isEmpty(agentReceiveTask.getReceiverPhone())) {
				DialogUtils
						.showConfirmDialog(this, "拨打电话",
								"拨打" + agentReceiveTask.getReceiverPhone())
						.view(R.id.btn_dialog_ok)
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								CommonUtils.call(
										SendExpressDetailActivity.this,
										agentReceiveTask.getReceiverPhone());
								//TODO 请求服务器，将其定为“已联系”
								doRequest(Constant.addCustomer);
							}
						});
			} else {

				final ViewUtils vUtils = DialogUtils.showEditCallDialog(this,"添加联系方式");
				vUtils.setViewText(R.id.edt_dialog,
						viewUtils.getViewText(R.id.edt_expressdetail_phone));
				if(Constant.ISSCAN){
					KeyBoardUtils.closeKeybordShowCursor(this, vUtils.view(
							R.id.edt_dialog, EditText.class));
				}
				vUtils.view(R.id.btn_dialog_call).setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								if (!TextUtils.isEmpty(vUtils
										.getViewText(R.id.edt_dialog))) {
									CommonUtils
											.call(SendExpressDetailActivity.this,
													vUtils.getViewText(R.id.edt_dialog));
									//TODO 请求服务器，将其定为“已联系”,除去已妥投、未妥投的件
									agentReceiveTask.setReceiverPhone(vUtils
										.getViewText(R.id.edt_dialog));
									doRequest(Constant.addCustomer);
								} else {
									T.showShort(SendExpressDetailActivity.this,
											"请输入联系方式！");
								}

							}
						});
				}
			}
			break;

		}

	}

	@Override
	public void doRequest(int requestCode, Object... needParams) {

		showLoadingDialog(requestCode);

		Gson gson = new Gson();
		Map<String, String> maps = new HashMap<String, String>();
		switch (requestCode) {
		case Constant.editReceiveTaskFinish:
			maps.clear();

			maps.put("agentId", SPUtils.getAgent(this).getAgentId() + "");
			maps.put("taskId", agentReceiveTask.getTaskId() + "");
			maps.put("agentUserId", SPUtils.getUser(this).getAgentUserId() + "");
			maps.put("agentUserName", SPUtils.getUser(this).getAgentUserName());
			maps.put("agentUserCode", SPUtils.getUser(this).getAgentUserCode());
			maps.put("expressCode", agentReceiveTask.getExpressCode());
			maps.put("carrierId", agentReceiveTask.getCarrierId()+"");
			maps.put("receiverName",
					viewUtils.getViewText(R.id.edt_expressdetail_name));
			maps.put("receiverPhone",
					viewUtils.getViewText(R.id.edt_expressdetail_phone));
			maps.put("receiverAddress",
					viewUtils.getViewText(R.id.edt_expressdetail_addr));
			String paramsValues = gson.toJson(maps);
			requestServer(Constant.RECEIVETASK_SERVER,
					Constant.editReceiveTaskFinish, AppUtils.createParams(
							ApiCode.editReceiveTaskFinish.code, paramsValues));
			break;
		case Constant.editReceiveTaskUnFinish:
			maps.clear();
			maps.put("taskId", agentReceiveTask.getTaskId() + "");
			maps.put("agentId", SPUtils.getAgent(this).getAgentId() + "");
			maps.put("type", Constant.TASKSTATUS_EDIT + "");
			maps.put("statusDesc", viewUtils
					.getViewText(R.id.tv_expressdetail_unfinishedReasen));
			maps.put("carrierName", agentReceiveTask.getCarrierName());
			maps.put("agentUserId", SPUtils.getUser(this).getAgentUserId() + "");
			maps.put("agentUserName", SPUtils.getUser(this).getAgentUserName());
			maps.put("agentUserCode", SPUtils.getUser(this).getAgentUserCode());
			maps.put("expressCode", agentReceiveTask.getExpressCode());
			maps.put("carrierId", agentReceiveTask.getCarrierId()+"");
			maps.put("receiverName",
					viewUtils.getViewText(R.id.edt_expressdetail_name));
			maps.put("receiverPhone",
					viewUtils.getViewText(R.id.edt_expressdetail_phone));
			maps.put("receiverAddress",
					viewUtils.getViewText(R.id.edt_expressdetail_addr));
			requestServer(Constant.RECEIVETASK_SERVER,
					Constant.editReceiveTaskUnFinish, AppUtils.createParams(
							ApiCode.editReceiveTaskUnFinish.code,
							gson.toJson(maps)));
			break;

		case Constant.editReceiveTask:
			maps.clear();
			maps.put("agentId", SPUtils.getAgent(this).getAgentId() + "");
			maps.put("taskId", agentReceiveTask.getTaskId() + "");
			maps.put("receiverName",
					viewUtils.getViewText(R.id.edt_expressdetail_name));
			maps.put("receiverPhone",
					viewUtils.getViewText(R.id.edt_expressdetail_phone));
			maps.put("receiverAddress",
					viewUtils.getViewText(R.id.edt_expressdetail_addr));
			maps.put("sendType", (agentReceiveTask.getSendType()==0?Constant.SENDTYPE_SHANGMENPAISONG:agentReceiveTask.getSendType())+ "");// 默认为派送
			requestServer(
					Constant.RECEIVETASK_SERVER,
					Constant.editReceiveTask,
					AppUtils.createParams(ApiCode.editReceiveTask.code,
							gson.toJson(maps)));
			break;
		case Constant.addCustomer:
			maps.clear();
			maps.put("taskId", agentReceiveTask.getTaskId()+"");
			maps.put("agentUserId", SPUtils.getUser(this).getAgentUserId() + "");
			maps.put("receiverName", agentReceiveTask.getReceiverName());
			maps.put("receiverPhone", agentReceiveTask.getReceiverPhone());
			maps.put("expressCode", agentReceiveTask.getExpressCode());
			maps.put("carrierName", agentReceiveTask.getCarrierName());
			maps.put("carrierId", agentReceiveTask.getCarrierId()+"");
			maps.put("agentId", SPUtils.getAgent(this).getAgentId()+"");
			requestServer(Constant.RECEIVETASK_SERVER, Constant.addCustomer,
					AppUtils.createParams(ApiCode.addCustomer.code, gson.toJson(maps)));
			break;
		default:
			break;
		}
	}

	@Override
	public void requestCallBack(int requestCode, MsMessage msm,
			String failedReason) {
		hideLoadingDialog(requestCode);
		if (msm.getResult() == 0) {// 成功

			T.showShort(this, msm.getMessage());
			switch (requestCode) {
			case Constant.editReceiveTaskFinish:
			case Constant.editReceiveTaskUnFinish:
				getEventBus().post(new AutoRefreshEvent());
				finish();
				break;
			case Constant.editReceiveTask:
				viewUtils.setViewText(R.id.edt_expressdetail_phone, TextUtils
						.isEmpty(agentReceiveTask.getReceiverPhone()) ? ""
						: agentReceiveTask.getReceiverPhone());
				setEditStatus(R.id.edt_expressdetail_phone);
				getEventBus().post(new AutoRefreshEvent());
//				T.showShort(this, msm.getMessage());
				finish();
				break;
			case Constant.addCustomer://新增
//				T.showShort(this, "已经加到您的联系客户列表中了哦！");
				DialogUtils.cancelDialog();
			break;
			}
		}

	}

	@Override
	public void doEmptyCheck(Object... needParams) {

	}

	@Override
	public void intentCallback(int requestCode, int resultCode, IntentExtra data) {

	}

	private void editRUKUDialog() {
		vDialogUtils = showRUKUDialog(this);
		// vDialogUtils.setVisiable(R.id.et_dialog_reason);
		// vDialogUtils.setGone(R.id.tv_dialog_text);
		// 设置edittext可输入大小
		vDialogUtils.view(R.id.et_dialog_insurecode,
						EditText.class).setFilters(
						new InputFilter[] { new InputFilter.LengthFilter(4) });
		//TODO
//		if(Constant.ISSCAN){
//			KeyBoardUtils.closeKeybordShowCursor(this,vDialogUtils.view(R.id.et_dialog_insurecode,EditText.class));
//		 }
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
						String insureCode=vDialogUtils.getViewText(R.id.et_dialog_insurecode);
						if (TextUtils.isEmpty(insureCode)) {
							T.showShort(SendExpressDetailActivity.this, "请输入确认码！");
						} else {
							if ("0000".equals(insureCode)|| insureCode.equals(agentReceiveTask.getReceiverInsureCode())) {
								viewUtils.setViewText(R.id.edt_expressdetail_finishedsubmitCode,insureCode);
								doRequest(Constant.editReceiveTaskFinish);
								dialog.cancel();
							} else {
								T.showShort(SendExpressDetailActivity.this, "您输入的确认码不正确！");
							}
						}
						
					}
				});

	}

	/**
	 * 快递入库弹框
	 * 
	 * @param context
	 * @param requesxxxtCode
	 */
	private ViewUtils showRUKUDialog(Context context) {

		dialog = new Dialog(context, R.style.Dialog);
		ViewUtils viewUtils = new ViewUtils(context, R.layout.dialog_insurecode);

		dialog.setContentView(viewUtils.getRootView());
//		dialog.setOnKeyListener(this);
		dialog.show();
		return viewUtils;
	}
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (Constant.ISSCAN) {
		if (event.getKeyCode()==KeyEvent.KEYCODE_DPAD_CENTER) {
			if (agentReceiveTask.getTaskStatus() == Constant.TASKSTATUS_OK) {// 已妥投
				doRequest(Constant.editReceiveTask);
			} else {
				if (viewUtils.view(R.id.rb_expressdetail_finish,
						RadioButton.class).isChecked()) {// 妥投
					if (viewUtils
							.view(R.id.ll_expressdetail_finishedsubmitCode)
							.isShown()) {
						String inSureCode = viewUtils
								.getViewText(R.id.edt_expressdetail_finishedsubmitCode);
						// if ("".equals(inSureCode)
						// || "0000".equals(inSureCode)
						// || inSureCode.equals(agentReceiveTask
						// .getReceiverInsureCode())) {
						if ("".equals(inSureCode)) {
							editRUKUDialog();
						} else if ("0000".equals(inSureCode)
								|| inSureCode.equals(agentReceiveTask
										.getReceiverInsureCode())) {
							doRequest(Constant.editReceiveTaskFinish);
						} else {
							T.showShort(this, "您输入的确认码不正确！");
						}
					}
				} else {
					if (!TextUtils.isEmpty(viewUtils.getViewText(R.id.tv_expressdetail_unfinishedReasen))) {
						doRequest(Constant.editReceiveTaskUnFinish);
					} else {
						T.showShort(this, "请填写未妥投原因");
					}

				}}}
			}
		return super.onKeyUp(keyCode, event);
	}
//	@Override
//	public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//		if (event.getAction() == KeyEvent.ACTION_DOWN&&Constant.ISSCAN) {
//			Log.w("asdasd", String.valueOf(keyCode));
//				switch (event.getKeyCode()) {
//				case KeyEvent.KEYCODE_0:
//					mSoundUtils.playOther(0);
//					break;
//				case KeyEvent.KEYCODE_1:
//					mSoundUtils.playOther(1);
//					break;
//				case KeyEvent.KEYCODE_2:
//					mSoundUtils.playOther(2);
//					break;
//				case KeyEvent.KEYCODE_3:
//					mSoundUtils.playOther(3);
//					break;
//				case KeyEvent.KEYCODE_4:
//					mSoundUtils.playOther(4);
//					break;
//				case KeyEvent.KEYCODE_5:
//					mSoundUtils.playOther(5);
//					break;
//				case KeyEvent.KEYCODE_6:
//					mSoundUtils.playOther(6);
//					break;
//				case KeyEvent.KEYCODE_7:
//					mSoundUtils.playOther(7);
//					break;
//				case KeyEvent.KEYCODE_8:
//					mSoundUtils.playOther(8);
//					break;
//				case KeyEvent.KEYCODE_9:
//					mSoundUtils.playOther(9);
//					break;
//				
//				default:
//					break;
//				}
//		}
//	return false;
//	}
}
