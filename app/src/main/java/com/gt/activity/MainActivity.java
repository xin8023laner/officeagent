package com.gt.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;

import cn.zjl.zxing.CaptureActivity;

import com.common.base.BaseFragmentActivity;
import com.common.base.FragmentTabHost;
import com.common.events.ChangeMainTabByTagEvent;
import com.common.events.ChangeTabEvent;
import com.common.events.LogoutEvent;
import com.common.model.ApiCode;
import com.common.model.IntentExtra;
import com.common.model.InterfaceStatics;
import com.common.model.MsMessage;
import com.common.utils.AppUtils;
import com.common.utils.Constant;
import com.common.utils.SPUtils;
import com.common.utils.ScreenUtils;
import com.common.utils.ViewUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gt.fragment.HomeFragment;
import com.gt.fragment.MineFragment;
import com.gt.fragment.PickupFragment;
import com.gt.fragment.SendFragment;
import com.gt.officeagent.R;

public class MainActivity extends BaseFragmentActivity {

	public MenuDrawer mDrawer;

	// 定义FragmentTabHost对象
	private FragmentTabHost mTabHost;

	private Class<?> fragmentArray[] = { HomeFragment.class,
			SendFragment.class, PickupFragment.class, MineFragment.class };

	private String mTextviewArray[] = { Constant.TAB_HOME, Constant.TAB_SEND,
			Constant.TAB_PICKUP, Constant.TAB_MINE };

	private Fragment fragment;
	private String tabId = Constant.TAB_HOME;
	
	//上次按back键的时间、
	private long lastClickBackTime;

	@Override
	public void initParmers() {
	}

	@Override
	public void initViewsAndValues(Bundle savedInstanceState) {
		getEventBus().register(this);

		mDrawer = MenuDrawer.attach(this, MenuDrawer.MENU_DRAG_WINDOW,
				Position.LEFT);
		mDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_BEZEL);
		mDrawer.setContentView(R.layout.activity_main);
		mDrawer.setMenuView(R.layout.activity_menu);
		mDrawer.setHardwareLayerEnabled(false);
		mDrawer.setDropShadowEnabled(true);
		mDrawer.setDropShadowColor(getResources().getColor(R.color.hui_90));
		mDrawer.setMenuSize((int) (ScreenUtils.getScreenWidth(this) * (3.0 / 5.0)));

		// 重置viewutils
		viewUtils.resetRootView(getWindow().getDecorView());

		mTabHost = viewUtils.view(android.R.id.tabhost, FragmentTabHost.class);

		mTabHost.setup(this, getSupportFragmentManager(),
				R.id.fl_main_realcontent);
		mTabHost.getTabWidget().setDividerDrawable(null);

		for (int i = 0; i < fragmentArray.length; i++) {
			// 为每一个Tab按钮设置图标、文字和内容
			TabHost.TabSpec tabSpec = mTabHost.newTabSpec(mTextviewArray[i])
					.setIndicator(getTabItemView(i));
			// 将Tab按钮添加进Tab选项卡中
			mTabHost.addTab(tabSpec, fragmentArray[i], null);
		}
		mTabHost.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String tabId) {
				MainActivity.this.tabId = tabId;
				fragment = getSupportFragmentManager().findFragmentByTag(tabId);
			}
		});

		viewUtils.setOnClickListener(R.id.rl_menu_kuaijianchaxun,
				R.id.rl_menu_kehuziti, R.id.tv_menu_telphone,
				R.id.civ_menu_head,R.id.rl_menu_GLOrders,R.id.rl_menu_contact,R.id.rl_menu_luru);
		viewUtils
				.setViewText(R.id.tv_menu_nickname, TextUtils.isEmpty(SPUtils
						.getUser(this).getAgentUserName()) ? "指尖代办员" : SPUtils
						.getUser(this).getAgentUserName());
		viewUtils.setViewText(R.id.tv_menu_telphone, TextUtils.isEmpty(SPUtils
				.getUser(this).getTelphone()) ? "指尖代办员" : SPUtils.getUser(this)
				.getTelphone());

//		doRequest(Constant.queryStatistics);

	}

	@Override
	public void onAttachFragment(Fragment fragment) {
		super.onAttachFragment(fragment);

		this.fragment = fragment;
	}

	private View getTabItemView(int index) {
		ViewUtils viewUtils = new ViewUtils(this, R.layout.item_main_tabhost);
		viewUtils.setViewText(R.id.item_tabhost_bottomText,
				mTextviewArray[index]);
		return viewUtils.getRootView();
	}

	@Override
	public void releaseOnDestory() {
		getEventBus().unregister(this);
	}

	public void onClickable(View view) {
		switch (view.getId()) {
		case R.id.rl_menu_kuaijianchaxun:
			intent(QueryExpressActivity.class, null);
			break;
		case R.id.rl_menu_kehuziti:
			intent(Customerself.class, null);
			break;
		case R.id.civ_menu_head:
		case R.id.tv_menu_telphone:

			changeTab(Constant.TAB_MINE);
			mDrawer.closeMenu();
			doRequest(Constant.queryStatistics);
			break;
		case R.id.rl_menu_GLOrders://查看够啦的所有订单
			intent(GLOrdersActivity.class, null);
			break;
		case R.id.rl_menu_contact://联系客户
			if(Constant.ISSCAN){
				intent(CapturePDAActivity.class, new IntentExtra(Constant.SCAN_CONTACT, null));
			}else{
				intent(CaptureActivity.class, new IntentExtra(Constant.SCAN_CONTACT, null));
			}
			break;
		case R.id.rl_menu_luru://录入信息
			if(Constant.ISSCAN){
				intent(InputInfoActivity.class, null);
			}else{
				intent(CaptureActivity.class, new IntentExtra(Constant.SCAN_INPUTINFO, 0));
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void doRequest(int requestCode, Object... needParams) {
		switch (requestCode) {
		case Constant.queryStatistics:
			Gson gson = new Gson();
			Map<String, String> map = new HashMap<String, String>();
			map.put("agentId", SPUtils.getAgent(this).getAgentId() + "");
			map.put("agentUserId", SPUtils.getUser(this).getAgentUserId() + "");
			String paramsValues = gson.toJson(map);
			requestServer(Constant.STATISTICS_SERVER, Constant.queryStatistics,
					AppUtils.createParams(ApiCode.queryStatistics.code,
							paramsValues));
			break;
		}
	}

	@Override
	public void requestCallBack(int requestCode, MsMessage msm,
			String failedReason) {
		switch (requestCode) {
		case Constant.queryStatistics:
		try{
			if (msm.getResult() == 0) {
				//T.showShort(this, msm.getMessage());
				List<InterfaceStatics> interfaceStatics = new Gson().fromJson(msm
						.getData().toString(),
						new TypeToken<List<InterfaceStatics>>() {
						}.getType());

				if (interfaceStatics != null) {
					SPUtils.setInterfaceStatics(this, interfaceStatics.get(0));
					viewUtils.setViewText(R.id.tv_home_lanshou, interfaceStatics
							.get(0).getLanjian() + "");
					viewUtils.setViewText(R.id.tv_home_paisong, interfaceStatics
							.get(0).getSend() + "");
					viewUtils.setViewText(R.id.tv_today_weituoCount, interfaceStatics
							.get(0).getNoticeLeft()+"");
				}

			} else {
//				T.showShort(this, msm.getMessage());
			}
		}catch(Exception e){
//			T.showShort(this, "获得个人数据异常");
		}
		break;
		}
		
	}

	@Override
	public void doEmptyCheck(Object... needParams) {

	}

	@Override
	public int bindLayoutId() {
		return R.layout.activity_main;
	}
	@Override
	public void onResume() {
		doRequest(Constant.queryStatistics);
		super.onResume();
	}
	/**
	 * 变化tab
	 * 
	 * @param tag
	 */
	public void changeTab(String tag) {
		mTabHost.setCurrentTabByTag(tag);
	}

	public void onEvent(ChangeMainTabByTagEvent changeMainTabByTagEvent) {
		changeTab(changeMainTabByTagEvent.getTag());
		mDrawer.closeMenu();
		if(changeMainTabByTagEvent.getTag().equals(Constant.TAB_SEND)){
			getEventBus().post(new ChangeTabEvent(Constant.TAB_SEND_SHEDING));
		}
	}

	public void onEvent(LogoutEvent logoutEvent) {
		finish();

	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK){
			long currentTime=System.currentTimeMillis();
			if(currentTime-lastClickBackTime<1500){
//				getEventBus().post(new CloseEvent("close"));
//				return super.onKeyDown(keyCode, event);
				Intent i=new Intent(Intent.ACTION_MAIN);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				i.addCategory(Intent.CATEGORY_HOME);
				startActivity(i);
				return false;
			}else{
				lastClickBackTime=currentTime;
				Toast.makeText(this, "再按一次返回键退出", Toast.LENGTH_SHORT).show();
				return false;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}
