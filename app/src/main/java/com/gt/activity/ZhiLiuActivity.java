package com.gt.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.common.base.BaseActivity;
import com.common.events.CloseEvent;
import com.common.events.UpdateZhiLiuList;
import com.common.model.AgentReceiveTask;
import com.common.model.ApiCode;
import com.common.model.IntentExtra;
import com.common.model.MsMessage;
import com.common.utils.AppUtils;
import com.common.utils.CommonUtils;
import com.common.utils.Constant;
import com.common.utils.DialogUtils;
import com.common.utils.KeyBoardUtils;
import com.common.utils.T;
import com.common.utils.ViewUtils;
import com.google.gson.Gson;
import com.gt.officeagent.R;

public class ZhiLiuActivity extends BaseActivity {
	private AgentReceiveTask agentReceiveTask = null;
	private PopupWindow popupWindow;

	@Override
	public int bindLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.activity_zhiliu;
	}

	@Override
	public void initParmers() {
		getEventBus().register(this);
	}

	@Override
	public void initViewsAndValues(Bundle savedInstanceState) {
		agentReceiveTask = (AgentReceiveTask) intentExtra.getValue();
		
		viewUtils.setViewText(R.id.tv_zhiliu_carry, TextUtils
				.isEmpty(agentReceiveTask.getCarrierName())&&TextUtils
						.isEmpty(agentReceiveTask.getExpressCode()) ? "未知"
				: agentReceiveTask.getCarrierName()+"        "+agentReceiveTask.getExpressCode());
		viewUtils.setViewText(R.id.tv_zhiliu_telephone, TextUtils
				.isEmpty(agentReceiveTask.getReceiverPhone()) ? "请补录手机号"
				: agentReceiveTask.getReceiverPhone());
		viewUtils.setViewText(R.id.tv_zhiliu_msg, agentReceiveTask.getSuccessMsg()==0 ? "短信/APP通知：成功"
				: "短信/APP通知：失败");
		viewUtils.setGone(R.id.rl_zhiliu_yuanyinyin,R.id.tv_zhiliu_msg);
		if (agentReceiveTask.getTaskStatus()==3) {//异常
			viewUtils.setVisiable(R.id.rl_zhiliu_yuanyinyin);
			//viewUtils.setGone(R.id.btn_zhiliu_submit);
			viewUtils.view(R.id.rad_yichang,RadioButton.class).setChecked(true);
			//viewUtils.view(R.id.rad_zhiliu,RadioButton.class).setEnabled(false);
			try {
				JSONArray array = new JSONArray(agentReceiveTask.getStatusDesc());
				 for(int i=0 ; i < array.length() ;i++) {
		               //获取每一个JsonObject对象
		               JSONObject myjObject = array.getJSONObject(i);
		               String numString = myjObject.getString("desc");
		               viewUtils.setViewText(R.id.tv_zhiliu_yuanyin, numString);
				 }
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else if (agentReceiveTask.getTaskStatus()==72) {//联系失败
			viewUtils.setVisiable(R.id.rl_zhiliu_yuanyinyin);
			//viewUtils.setGone(R.id.btn_zhiliu_submit);
			viewUtils.view(R.id.rad_shibai,RadioButton.class).setChecked(true);
			try {
				JSONArray array = new JSONArray(agentReceiveTask.getStatusDesc());
				 for(int i=0 ; i < array.length() ;i++) {
		               //获取每一个JsonObject对象
		               JSONObject myjObject = array.getJSONObject(i);
		               String numString = myjObject.getString("desc");
		               viewUtils.setViewText(R.id.tv_zhiliu_yuanyin, numString);
				 }
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		viewUtils.view(R.id.radgr_zhiliu,RadioGroup.class).setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				if (arg1==R.id.rad_zhiliu) {
					viewUtils.setGone(R.id.rl_zhiliu_yuanyinyin);
				}else if (arg1==R.id.rad_shibai) {
					viewUtils.setVisiable(R.id.rl_zhiliu_yuanyinyin);
					viewUtils.setViewText(R.id.tv_zhiliu_yuanyin, "无人接听");
					viewUtils.setOnClickListener(R.id.rl_zhiliu_yuanyin);
				
				} else {//异常
					viewUtils.setVisiable(R.id.rl_zhiliu_yuanyinyin);
					viewUtils.setOnClickListener(R.id.rl_zhiliu_yuanyin);
					viewUtils.setViewText(R.id.tv_zhiliu_yuanyin, "错分件");
				}
			}
		});
		viewUtils.setOnClickListener(R.id.btn_zhiliu_submit,R.id.btn_zhiliu_edit,R.id.btn_zhiliu_call);
	}

	@Override
	public void releaseOnDestory() {
		// TODO Auto-generated method stub
		getEventBus().unregister(this);
	}

	@Override
	public void onClickable(View view) {
		switch (view.getId()) {
		case R.id.rl_zhiliu_yuanyin:
			initPopWindow();
			break;
		case R.id.btn_zhiliu_edit:
			ViewUtils view1 = DialogUtils.showTelephoneDialog(
					this, agentReceiveTask.getExpressCode());
			Button ll_dialog_ruku_queren = view1.view(
					R.id.ll_dialog_ruku_queren, Button.class);
			final EditText tv_dialog_rukutelephone_text = view1
					.view(R.id.tv_dialog_rukutelephone_text,
							EditText.class);
			if(Constant.ISSCAN){
				KeyBoardUtils.closeKeybordShowCursor(this, view1
					.view(R.id.tv_dialog_rukutelephone_text,
							EditText.class));
			}
			tv_dialog_rukutelephone_text.addTextChangedListener(new TextWatcher() {
				
				@Override
				public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
					// TODO Auto-generated method stub
					if (arg0.toString().endsWith("#")) {
						agentReceiveTask.setReceiverPhone(tv_dialog_rukutelephone_text
								.getText().toString().substring(0, tv_dialog_rukutelephone_text
								.getText().toString().length()-1));
						viewUtils.setViewText(R.id.tv_zhiliu_telephone, 
								tv_dialog_rukutelephone_text
								.getText().toString().substring(0, tv_dialog_rukutelephone_text
										.getText().toString().length()-1));
//							DialogUtils.hideTelephoneDialog();
							DialogUtils.cancelDialog();
					}
				}
				
				@Override
				public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
						int arg3) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void afterTextChanged(Editable arg0) {
					// TODO Auto-generated method stub
					
				}
			});
				tv_dialog_rukutelephone_text.setText(agentReceiveTask.getReceiverPhone());
				tv_dialog_rukutelephone_text.setSelection(agentReceiveTask.getReceiverPhone().length());
			ll_dialog_ruku_queren
					.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							agentReceiveTask.setReceiverPhone(tv_dialog_rukutelephone_text
									.getText().toString());
							viewUtils.setViewText(R.id.tv_zhiliu_telephone, 
									tv_dialog_rukutelephone_text
									.getText().toString());
								DialogUtils.cancelDialog();
							
						}
					});
		
			break;
		case R.id.btn_zhiliu_call:
			CommonUtils.call(this, viewUtils.getViewText(R.id.tv_zhiliu_telephone));
			break;
		case R.id.btn_zhiliu_submit:
			if (viewUtils.getViewText(R.id.tv_zhiliu_yuanyin).equals("请选择异常原因")&&viewUtils.view(R.id.rad_yichang,RadioButton.class).isChecked()) {
				T.showShort(this, "请选择异常原因");
				return;
			}
			doRequest(Constant.zljcl);
			break;
		default:
			break;
		}

	}

	private void initPopWindow() {
		final ArrayList<String> list = new ArrayList<String>();
		
		if (viewUtils.view(R.id.rad_shibai, RadioButton.class).isChecked()) {
			list.add("无人接听");
			list.add("电话停机");
			
		} else {
			list.add("错分件");
			list.add("电话是空号");
			list.add("电话看不清");
			list.add("用户拒收");
			list.add("其他");
		}
				
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.item_pop_expressdetail,
				R.id.tv_pop_expressdetail_item, list);
		ViewUtils vUtils = new ViewUtils(this, R.layout.popup_unfinish_reason);

		vUtils.view(R.id.lv_pop_expressdetail, ListView.class).setAdapter(
				adapter);
		vUtils.view(R.id.lv_pop_expressdetail, ListView.class)
		.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				viewUtils.setViewText(
						R.id.tv_zhiliu_yuanyin,
						list.get(arg2));
				popupWindow.dismiss();
			}
		});
		popupWindow = new PopupWindow(vUtils.getRootView(),
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable(getResources()));
		popupWindow.setFocusable(true);

		popupWindow.showAsDropDown(
				viewUtils.view(R.id.rl_zhiliu_yuanyin), 0, 0);
	}

	@Override
	public void doRequest(int requestCode, Object... needParams) {
		showLoadingDialog(requestCode);

		Gson gson = new Gson();
		Map<String, String> maps = new HashMap<String, String>();
		switch (requestCode) {
		case Constant.zljcl:
			if (viewUtils.view(R.id.rad_zhiliu,RadioButton.class).isChecked()) {
				agentReceiveTask.setTaskStatus((short) 71);
				agentReceiveTask.setStatusDesc("");
			}else if (viewUtils.view(R.id.rad_shibai,RadioButton.class).isChecked()) {
				agentReceiveTask.setTaskStatus((short) 72);
				agentReceiveTask.setStatusDesc(viewUtils.getViewText(R.id.tv_zhiliu_yuanyin));
			
			} else {
				agentReceiveTask.setTaskStatus((short) 3);
				agentReceiveTask.setStatusDesc(viewUtils.getViewText(R.id.tv_zhiliu_yuanyin));
			}
			requestServer(Constant.RECEIVETASK_SERVER,
					Constant.zljcl, AppUtils.createParams(
							ApiCode.chuliZhiLiu.code,
							gson.toJson(agentReceiveTask)));
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
		case Constant.zljcl:
			T.showShort(this, msm.getMessage());
			if (msm.getResult()==0) {
				getEventBus().post(new UpdateZhiLiuList(agentReceiveTask));
				finish();
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
		// TODO Auto-generated method stub

	}
	public void onEvent(CloseEvent event){
		
	}
}
