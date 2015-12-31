package com.gt.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.common.adapter.CommonCarrierAdapter2;
import com.common.base.BaseActivity;
import com.common.model.AgentCarrier;
import com.common.model.ApiCode;
import com.common.model.IntentExtra;
import com.common.model.MsMessage;
import com.common.utils.AppUtils;
import com.common.utils.Constant;
import com.common.utils.SPUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gt.officeagent.R;

public class CarrierBookActivity2 extends BaseActivity{
	private CommonCarrierAdapter2 commonCarrierAdapter = null;
	private ListView lv_activity_carrierbook;
	private List<AgentCarrier> agentCarriers = new ArrayList<AgentCarrier>();

	@Override
	public int bindLayoutId() {
		return R.layout.activity_carrierbook1;
	}

	@Override
	public void initParmers() {
		
	}

	@Override
	public void initViewsAndValues(Bundle savedInstanceState) {
		lv_activity_carrierbook=(ListView) viewUtils.view(R.id.lv_activity_carrierbook);
		commonCarrierAdapter = new CommonCarrierAdapter2(this, agentCarriers,Constant.TYPE_COMPANY_TEXT);
		lv_activity_carrierbook.setAdapter(commonCarrierAdapter);
		
		doRequest(Constant.queryCommonCarriers);
	}

	@Override
	public void releaseOnDestory() {
	}

	@Override
	public void onClickable(View view) {
		switch (view.getId()) {
		case R.id.btn_empty_data:
			doRequest(Constant.queryCommonCarriers);
			break;

		default:
			break;
		}
	}

	@Override
	public void doRequest(int requestCode, Object... needParams) {
		showLoadingDialog(requestCode);

		switch (requestCode) {
		case Constant.queryCommonCarriers:
			Map<String, String> maps = new HashMap<String, String>();
			maps.put("agentId", SPUtils.getAgent(this).getAgentId()+"");
			
			String paramsValues = new Gson().toJson(maps);
			requestServer(Constant.RECEIVETASK_SERVER,
					Constant.queryCommonCarriers, AppUtils.createParams(
							ApiCode.queryCommonCarriers.code, paramsValues));
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
			agentCarriers = new Gson().fromJson(msm.getData().toString(),
					new TypeToken<List<AgentCarrier>>() {
					}.getType());
			
		}else{
			agentCarriers=AppUtils.getAgentCarriers(this);
//			T.showLong(this, "请检查网络！");
		}
		commonCarrierAdapter.updateAll(agentCarriers);
		doEmptyCheck();
	}

	@Override
	public void doEmptyCheck(Object... needParams) {
		if (agentCarriers != null && agentCarriers.size() > 0) {
			viewUtils.setGone(R.id.nodata);
		} else {
			viewUtils.setVisiable(R.id.nodata);
			viewUtils.setViewText(R.id.tv_nodata_msg, "暂无内容");
			viewUtils.setOnClickListener(R.id.btn_empty_data);
		}
	}

	@Override
	public void intentCallback(int requestCode, int resultCode, IntentExtra data) {
		
	}
	
}
