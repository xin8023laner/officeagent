package com.gt.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.common.base.BaseFragment;
import com.common.model.IntentExtra;
import com.common.model.MsMessage;
import com.common.ui.Topbar.OnOtherListener;
import com.common.utils.SPUtils;
import com.gt.activity.DetailCount;
import com.gt.activity.MainActivity;
import com.gt.activity.SettingActivity;
import com.gt.officeagent.R;

/**
 * Created by Alick on 2015/7/16.
 */
public class MineFragment extends BaseFragment {

	/**
	 * 初始化变量
	 */
	@Override
	public void initParmers() {

	}

	@Override
	public void initViewsAndValues(Bundle savedInstanceState) {
		getTopbar().setOnOtherListener(new OnOtherListener() {

			@Override
			public void onOtherListener() {
				((MainActivity) getActivity()).mDrawer.toggleMenu();
			}
		});

		viewUtils.setViewText(R.id.tv_userName, TextUtils.isEmpty(SPUtils
				.getUser(getActivity()).getAgentUserName()) ? "指尖代办员" : SPUtils
				.getUser(getActivity()).getAgentUserName());
		viewUtils.setViewText(R.id.tv_phoneNum, TextUtils.isEmpty(SPUtils
				.getUser(getActivity()).getTelphone()) ? "指尖代办员" : SPUtils
						.getUser(getActivity()).getTelphone());
		viewUtils.setViewText(R.id.tv_agentName, TextUtils.isEmpty(SPUtils
				.getAgent(getActivity()).getName()) ? "指尖服务站" : SPUtils
						.getAgent(getActivity()).getName());
		
		
		viewUtils.setViewText(R.id.tv_home_paisong, SPUtils
				.getInterfaceStatics(getActivity())==null ? "0" : SPUtils
						.getInterfaceStatics(getActivity()).getSend()+"");
		viewUtils.setViewText(R.id.tv_home_lanshou, SPUtils
				.getInterfaceStatics(getActivity())==null ? "0" : SPUtils
						.getInterfaceStatics(getActivity()).getLanjian()+"");
		viewUtils.setViewText(R.id.tv_today_weituoCount, SPUtils
				.getInterfaceStatics(getActivity())==null ? "0" : SPUtils
						.getInterfaceStatics(getActivity()).getNoticeLeft()+"");
		

		viewUtils.setOnClickListener(R.id.rl_detailcount, R.id.rl_more);

	}

	@Override
	public void releaseOnDestory() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClickable(View view) {
		switch (view.getId()) {
		case R.id.rl_detailcount:
			intent(DetailCount.class, null);
			break;
		case R.id.rl_more:
			intent(SettingActivity.class, null);
			break;

		default:
			break;
		}
	}

	@Override
	public void doRequest(int requestCode, Object... needParams) {
		switch (requestCode) {

		default:
			break;
		}
	}

	@Override
	public void requestCallBack(int requestCode, MsMessage msm,
			String failedReason) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doEmptyCheck(Object... needParams) {
		// TODO Auto-generated method stub

	}

	@Override
	public int bindLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.frag_mine;
	}

	@Override
	public void intentCallback(int requestCode, int resultCode, IntentExtra data) {
		// TODO Auto-generated method stub
		
	}
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		viewUtils.setViewText(R.id.tv_home_paisong, SPUtils
				.getInterfaceStatics(getActivity())==null ? "0" : SPUtils
						.getInterfaceStatics(getActivity()).getSend()+"");
		viewUtils.setViewText(R.id.tv_home_lanshou, SPUtils
				.getInterfaceStatics(getActivity())==null ? "0" : SPUtils
						.getInterfaceStatics(getActivity()).getLanjian()+"");
		viewUtils.setViewText(R.id.tv_today_weituoCount, SPUtils
				.getInterfaceStatics(getActivity())==null ? "0" : SPUtils
						.getInterfaceStatics(getActivity()).getNoticeLeft()+"");
	}
}
