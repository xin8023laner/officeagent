package com.gt.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import cn.jpush.android.api.JPushInterface;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.common.base.BaseActivity;
import com.common.model.Agent;
import com.common.model.AgentUser;
import com.common.model.ApiCode;
import com.common.model.IntentExtra;
import com.common.model.MsMessage;
import com.common.utils.AppUtils;
import com.common.utils.CommonUtils;
import com.common.utils.Constant;
import com.common.utils.DialogUtils;
import com.common.utils.SPUtils;
import com.common.utils.T;
import com.common.utils.ViewUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gt.officeagent.R;

public class LoginActivity extends BaseActivity{

	private EditText edt_phone_login;
	private EditText edt_pwd_login;
	private Agent agent = null;
	private AgentUser user = null;
	private LocationClient mLocationClient;
	private LocationClientOption mOption;
	private double lng, lat;//
	private ViewUtils vUtils;
//	private int locType;//定位结束后的结果码

	@Override
	public int bindLayoutId() {
		return R.layout.activity_login;
	}

	@Override
	public void initParmers() {

	}

	@Override
	public void initViewsAndValues(Bundle savedInstanceState) {
		mLocationClient = new LocationClient(getApplicationContext());
		mOption = new LocationClientOption();
		getMyLatlng();

		// setEnable(R.id.btn_submit_login, false);
		edt_phone_login = (EditText) viewUtils.view(R.id.edt_shoujihao);
		edt_pwd_login = (EditText) viewUtils.view(R.id.edt_mima);
		viewUtils.setOnClickListener(R.id.tv_resetpwd_login,
				R.id.btn_submit_login, R.id.rl_daibandian);

		edt_phone_login.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.length() == 0) {
					viewUtils.setGone(R.id.iv_phoneclear_login);
					// buttonChange();
				} else {
					viewUtils.setVisiable(R.id.iv_phoneclear_login);
					viewUtils.setOnClickListener(R.id.rl_clear_phone);
					// buttonChange();
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		edt_pwd_login.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.length() > 5 && s.length() < 17) {
					viewUtils.setVisiable(R.id.iv_pwd_login);
					// buttonChange();
				} else {
					viewUtils.setGone(R.id.iv_pwd_login);
					// buttonChange();
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		//TODO
//		if(Constant.ISSCAN){
//			KeyBoardUtils.closeKeybordShowCursor(this, edt_phone_login,edt_pwd_login);
//		 }
	}

	private void getMyLatlng() {
		LocationClientOption.LocationMode tempMode = LocationClientOption.LocationMode.Hight_Accuracy;
		String tempcoor = "bd09ll";
		mOption.setLocationMode(tempMode);
		// 设置选项
		mOption.setOpenGps(true);
		mOption.setCoorType(tempcoor);
		mOption.setIsNeedAddress(true);
//		mOption.disableCache(false);//禁止启用缓存定位  
		// 本地取址Client 端设置 Option选项
		mLocationClient.setLocOption(mOption);
		// 设置监听器，监听服务器发送过来的地址信息
		mLocationClient.registerLocationListener(new BDLocationListener() {

			@Override
			public void onReceiveLocation(BDLocation bdLocation) {
				if (bdLocation == null) {
					return;
				}
				// 获取经纬度
				else {
					lat = bdLocation.getLatitude();
					lng = bdLocation.getLongitude();
					doRequest(Constant.queryAgentsByCoord);
				}
			}
		});
		mLocationClient.start();
		
//		mLocationClient = new LocationClient(this);
//		LocationClientOption option = new LocationClientOption();
//        option.setLocationMode(LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
//        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
//        int span=0;
//        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
//        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
//        option.setOpenGps(true);//可选，默认false,设置是否使用gps
//        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
//        option.setIgnoreKillProcess(false);//可选，默认false，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认杀死
//        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
//        mLocationClient.setLocOption(option);
//		mLocationClient.registerLocationListener(new BDLocationListener() {
//
//			@Override
//			public void onReceiveLocation(BDLocation bdLocation) {
//				
//				// 获取经纬度
//				locType=bdLocation.getLocType();
//				lat = bdLocation.getLatitude();
//				lng = bdLocation.getLongitude();
//				if(isLocationSuccessful()){
//					doRequest(Constant.queryAgentsByCoord);
//				}else {
//					T.showShort(LoginActivity.this, "定位失败！");
//				}
//			}
//		});
//		mLocationClient.start();

	}

	@Override
	public void releaseOnDestory() {
		if (mLocationClient!=null&&mLocationClient.isStarted()){
            mLocationClient.stop();
            mLocationClient=null;
        }
	}

	@Override
	public void onClickable(View view) {
		switch (view.getId()) {
		case R.id.rl_clear_phone:
			viewUtils.setViewText(R.id.edt_shoujihao, "");
			break;
		case R.id.tv_resetpwd_login:
			vUtils = DialogUtils.showForgetPwd(this, Constant.forgetPassword);
			vUtils.view(R.id.btn_forgetpwd_submit).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if (agent!=null&&!TextUtils.isEmpty(vUtils.getViewText(R.id.edt_forgetpwd_shoujihao))) {
								doRequest(Constant.forgetPassword);
								}else {
									T.showShort(LoginActivity.this, "请输入电话号码并选择代办点");
								}
						}
					});
			break;
		case R.id.btn_submit_login:

			if (agent == null) {
				T.showShort(this, "请选择代办点！");
				return;
			}
			if (TextUtils.isEmpty(viewUtils.getViewText(R.id.edt_shoujihao))) {
				T.showShort(this, "请输入手机号！");
				return;
			}
			if (TextUtils.isEmpty(viewUtils.getViewText(R.id.edt_mima))) {
				T.showShort(this, "请输入密码！");
				return;
			}

			if (!CommonUtils.isPhoneNum(viewUtils
					.getViewText(R.id.edt_shoujihao))) {
				DialogUtils.showLoginFail(this, 0);
				return;
			}
			doRequest(Constant.login);

			break;

		case R.id.rl_daibandian:
			intentForResult(ChoiceAgentActivity.class, null, 0);
			break;
		default:
			break;
		}

	}

	@Override
	public void doRequest(int requestCode, Object... needParams) {
		showLoadingDialog(requestCode);
		switch (requestCode) {
		case Constant.login:
			Gson gsonlogin = new Gson();
			Map<String, String> maplogin = new HashMap<String, String>();
			maplogin.put("agentId", agent.getAgentId() + "");
			maplogin.put("account", viewUtils.getViewText(R.id.edt_shoujihao));
			maplogin.put("password", viewUtils.getViewText(R.id.edt_mima));
			maplogin.put("RegistrationID", JPushInterface.getRegistrationID(this));
			String paramsValue = gsonlogin.toJson(maplogin);
			requestServer(Constant.USER_SERVER, Constant.login,
					AppUtils.createParams(ApiCode.login.code, paramsValue));
			break;
		case Constant.forgetPassword:
			Gson gsonfp = new Gson();
			Map<String, String> mapfp = new HashMap<String, String>();
			mapfp.put("agentId", agent.getAgentId() + "");
			mapfp.put("telphone",
					vUtils.getViewText(R.id.edt_forgetpwd_shoujihao));
			String paramfp = gsonfp.toJson(mapfp);
			requestServer(Constant.USER_SERVER, Constant.forgetPassword,
					AppUtils.createParams(ApiCode.forgetPassword.code, paramfp));
			break;
		case Constant.queryAgentsByCoord:
			Gson gsonfornear = new Gson();
			Map<String, String> mapsfornear = new HashMap<String, String>();
			mapsfornear.put("lat", lat + "");
			mapsfornear.put("lng", lng + "");
			mapsfornear.put("is_school", "1");
			String paramsValues = gsonfornear.toJson(mapsfornear);
			requestServer(Constant.BASIC_SERVER, Constant.queryAgentsByCoord,
					AppUtils.createParams(ApiCode.queryAgentsByCoord.code,
							paramsValues));
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
		case Constant.login:
			if (msm.getResult() == 0) {
				Gson gson = new Gson();
				user = gson.fromJson(msm.getData().toString(), AgentUser.class);
				if(JPushInterface.isPushStopped(this)){
					JPushInterface.resumePush(this);//开启极光推送
				}
				AppUtils.login(this, user, agent);
				intent(MainActivity.class, null);
				finish();
			} else {
				DialogUtils.showLoginFail(this, 0);
			}
			break;
		case Constant.forgetPassword:
			if (msm.getResult() == 0) {
				T.showShort(this, msm.getMessage());
				DialogUtils.cancelDialog();
				if (msm.getMessage().equals("发送成功")) {
					Gson gson = new Gson();
					user = gson.fromJson(msm.getData().toString(),
							AgentUser.class);
					SPUtils.put(this, Constant.USER_SERVER, user);
				} else {
					T.showShort(this, "发送失败");
				}

			}
			break;
		case Constant.queryAgentsByCoord:
			if (msm.getResult() == 0) {
				List<Agent> nearAgents = new Gson().fromJson(msm.getData()
						.toString(), new TypeToken<List<Agent>>() {
				}.getType());

				if (nearAgents != null) {
					agent = nearAgents.get(0);
					viewUtils.setViewText(R.id.tv_choicedaibandian, nearAgents
							.get(0).getName());
				} else {
					T.showShort(this, "请手动选择代办点");
				}
			} else {
				T.showShort(this, "获取附近数据失败" + failedReason);
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void doEmptyCheck(Object... needParams) {

	}

	@Override
	public void intentCallback(int requestCode, int resultCode, IntentExtra data) {
		agent = (Agent) data.getValue();
		viewUtils.setViewText(R.id.tv_choicedaibandian, agent.getName());
	}
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (Constant.ISSCAN) {
				if (viewUtils.view(R.id.edt_mima, EditText.class).isFocused()) {
					return false;
				}
				
			
		if (event.getKeyCode()==KeyEvent.KEYCODE_DPAD_CENTER) {
			if (agent == null) {
				T.showShort(this, "请选择代办点！");
			}
			if (TextUtils.isEmpty(viewUtils.getViewText(R.id.edt_shoujihao))) {
				T.showShort(this, "请输入手机号！");
			}
			if (TextUtils.isEmpty(viewUtils.getViewText(R.id.edt_mima))) {
				T.showShort(this, "请输入密码！");
			}

			if (!CommonUtils.isPhoneNum(viewUtils
					.getViewText(R.id.edt_shoujihao))) {
				DialogUtils.showLoginFail(this, 0);
			}
			doRequest(Constant.login);
		}
		}
		return super.onKeyUp(keyCode, event);
	}
//	//是否定位成功
//	public boolean isLocationSuccessful(){
//		if(locType == 61){
//			return true;
//		}else if(locType == 161){
//			return true;
//		}else{
//			return false;
//		}
//	}
}
