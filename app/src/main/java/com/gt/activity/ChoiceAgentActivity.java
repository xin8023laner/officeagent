package com.gt.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.common.adapter.AgentAdapter;
import com.common.base.BaseActivity;
import com.common.model.Agent;
import com.common.model.ApiCode;
import com.common.model.IntentExtra;
import com.common.model.MsMessage;
import com.common.utils.AppUtils;
import com.common.utils.Constant;
import com.common.utils.T;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gt.inteface.OnViewClickListener;
import com.gt.officeagent.R;

public class ChoiceAgentActivity extends BaseActivity implements OnViewClickListener{
	private List<Agent> nearAgents = new ArrayList<Agent>();// 附近代办点
	private List<Agent> searchAgents = new ArrayList<Agent>();// 搜索结果
	private AgentAdapter nearAgentAdapter = null;
	private AgentAdapter searchAgentAdapter = null;
	private LocationClient mLocationClient;
	private LocationClientOption mOption;
	private double lng, lat;

	@Override
	public int bindLayoutId() {
		return R.layout.activity_choiceagent;
	}

	@Override
	public void initParmers() {
	}

	@Override
	public void initViewsAndValues(Bundle savedInstanceState) {
		
		viewUtils.setOnClickListener(R.id.btn_search_agent);
		mLocationClient = new LocationClient(getApplicationContext());
		mOption = new LocationClientOption();
		getMyLatlng();

		nearAgentAdapter = new AgentAdapter(this, nearAgents,this);
		searchAgentAdapter = new AgentAdapter(this, searchAgents,this);

		viewUtils.view(R.id.lv_choiseagent_near, ListView.class).setAdapter(
				nearAgentAdapter);
		viewUtils.view(R.id.lv_choiseagent_searchagent, ListView.class)
				.setAdapter(searchAgentAdapter);
		//TODO
//		if(Constant.ISSCAN){
//			KeyBoardUtils.closeKeybordShowCursor(this,viewUtils.view(R.id.edt_search_agent,EditText.class));
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
	}

	@Override
	public void releaseOnDestory() {
	}

	@Override
	public void onClickable(View view) {
		switch (view.getId()) {
		case R.id.btn_search_agent:
			if (!TextUtils.isEmpty(viewUtils.getViewText(R.id.edt_search_agent))) {
				doRequest(Constant.queryAgentsByName);
			} else {
				T.showShort(this, "请输入要搜索的代办点名称");
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void doRequest(int requestCode, Object... needParams) {
		switch (requestCode) {
		case Constant.queryAgentsByCoord:
			showLoadingDialog(Constant.queryAgentsByCoord);
			Gson gsonfornear = new Gson();
			Map<String, String> mapsfornear = new HashMap<String, String>();
			mapsfornear.put("lat", lat + "");
			mapsfornear.put("lng", lng + "");
			mapsfornear.put("is_school",
					"1");
			String paramsValues = gsonfornear.toJson(mapsfornear);
			requestServer(Constant.BASIC_SERVER, Constant.queryAgentsByCoord,
					AppUtils.createParams(ApiCode.queryAgentsByCoord.code,
							paramsValues));
			break;
		case Constant.queryAgentsByName:
			Gson gsonforsearch = new Gson();
			Map<String, String> mapsforsearch = new HashMap<String, String>();
			mapsforsearch.put("agentName",
					viewUtils.getViewText(R.id.edt_search_agent));
			mapsforsearch.put("is_school",
					"1");
			String params = gsonforsearch.toJson(mapsforsearch);
			requestServer(Constant.BASIC_SERVER, Constant.queryAgentsByName,
					AppUtils.createParams(ApiCode.queryAgentsByName.code,
							params));
			break;

		default:
			break;
		}
	}

	@Override
	public void requestCallBack(int requestCode, MsMessage msm,
			String failedReason) {
		switch (requestCode) {
		case Constant.queryAgentsByCoord:
			hideLoadingDialog(Constant.queryAgentsByCoord);
			if (msm.getResult() == 0) {

				nearAgents = new Gson().fromJson(msm.getData().toString(),
						new TypeToken<List<Agent>>() {
						}.getType());
				nearAgentAdapter.updateAll(nearAgents);
			} else {
				T.showShort(this, "获取附近数据失败" + failedReason);
			}
			break;

		case Constant.queryAgentsByName:
			if (msm.getResult() == 0) {

				searchAgents = new Gson().fromJson(msm.getData().toString(),
						new TypeToken<List<Agent>>() {
						}.getType());

				if (searchAgents.size() == 0) {
					viewUtils.setGone(R.id.ll_choiseagent_search);
				} else {
					viewUtils.setVisiable(R.id.ll_choiseagent_search);
					searchAgentAdapter.updateAll(searchAgents);
				}
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

	}

	@Override
	public void intentCallback(int requestCode, int resultCode, IntentExtra data) {

	}


	@Override
	public void OnViewClicked(View v, int type, Object obj) {
		
		
	}

	@Override
	public void doSomeThing(View v, int type, Object obj) {
		result(0, new IntentExtra(0,(Agent)obj));
		finish();
		
	}
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (Constant.ISSCAN) {
		if (event.getKeyCode()==KeyEvent.KEYCODE_DPAD_CENTER) {
			if (viewUtils.getViewText(R.id.edt_search_agent) != "") {
				doRequest(Constant.queryAgentsByName);
			} else {
				T.showShort(this, "请输入要搜索的代办点名称");
			}
		}
		}
		return super.onKeyUp(keyCode, event);
	}
}
