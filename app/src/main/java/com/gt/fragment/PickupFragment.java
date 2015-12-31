package com.gt.fragment;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import cn.greenrobot.eventbus.EventBus;
import cn.zjl.zxing.CaptureActivity;

import com.common.base.BaseFragment;
import com.common.base.FragmentTabHost;
import com.common.events.ChangeTabPickupEvent;
import com.common.events.GetSearchTextEvent;
import com.common.events.GetSearchTextPickupEvent;
import com.common.events.TabEvent;
import com.common.model.ApiCode;
import com.common.model.IntentExtra;
import com.common.model.MsMessage;
import com.common.model.TaskWaybill;
import com.common.ui.Topbar.OnControlTwoListener;
import com.common.ui.Topbar.OnOtherListener;
import com.common.utils.AppUtils;
import com.common.utils.Constant;
import com.common.utils.SPUtils;
import com.common.utils.SoundUtils;
import com.common.utils.T;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gt.activity.InputInfoActivity;
import com.gt.activity.MainActivity;
import com.gt.officeagent.R;
import com.zltd.industry.ScannerManager;
import com.zltd.industry.ScannerManager.IScannerStatusListener;

/**
 * Created by Alick on 2015/7/16.
 */
public class PickupFragment extends BaseFragment{

	private FragmentTabHost tabHost;
	/** 我的订单页面,两个tab页Fragment数组 */
	private Class<?> fragmentArray[] = { PickupunFinishedFragment.class,PickupFinishedFragment.class };
	/** 我的订单页面,两个tab页名称数组 */
	private String mTextviewArray[] = { Constant.TAB_PICKUP_UNFINISHED,Constant.TAB_PICKUP_FINISHED };

	private Fragment currentOrderFragment;
	private PopupWindow popupWindow;
	private TextView textView;
	
	private ScannerManager mScannerManager;
	private SoundUtils mSoundUtils;
	private boolean continousScanMode = false;
	// 扫描模式
	public static final int SINGLE_SCAN_MODE = 1;
	public static final int CONTINUOUS_SCAN_MODE = 2;
	private Handler mHandler = new Handler();
	/**
	 * 初始化变量
	 */
	@Override
	public void initParmers() {
		if (Constant.ISSCAN) {
			 mSoundUtils = SoundUtils.getInstance();
			 mSoundUtils.init(getActivity());
			setScan();
		}
		
	}

	@Override
	public void initViewsAndValues(Bundle savedInstanceState) {

		getEventBus().register(this);

		tabHost = viewUtils.view(android.R.id.tabhost, FragmentTabHost.class);
		tabHost.setup(activity, getChildFragmentManager(),
				R.id.fl_pickup_realcontent);
		/* 循环添加tab页 */
		for (int i = 0; i < fragmentArray.length; i++) {
			// 为每一个Tab按钮设置图标、文字和内容
			TabHost.TabSpec tabSpec = tabHost.newTabSpec(mTextviewArray[i]+"0）")
					.setIndicator(getTabItemView(i));
			// 将Tab按钮添加进Tab选项卡中
			tabHost.addTab(tabSpec, fragmentArray[i], null);
			
		}
		/* 设置tab页切换时的监听 */
		tabHost.setOnTabChangedListener(new OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {
				currentOrderFragment = getChildFragmentManager()
						.findFragmentByTag(tabId);
			}
		});
		tabHost.initAllTabView();
		getTopbar().setOnOtherListener(new OnOtherListener() {

			@Override
			public void onOtherListener() {
				((MainActivity) getActivity()).mDrawer.toggleMenu();
			}
		});
		getTopbar().setOnControlTwoListener(new OnControlTwoListener() {

			@Override
			public void onControlTwoListener() {
				viewUtils.setVisiable(R.id.ll_frag_search);
			}
		});
		
		//搜索框初始化
		viewUtils.setOnClickListener(R.id.tv_search_cancel);
		
		
		viewUtils.view(R.id.edt_search, EditText.class).addTextChangedListener(
				new TextWatcher() {
					@Override
					public void onTextChanged(CharSequence arg0, int arg1,
							int arg2, int arg3) {
						EventBus.getDefault().post(
								new GetSearchTextPickupEvent(arg0.toString()));
					}

					@Override
					public void beforeTextChanged(CharSequence arg0, int arg1,
							int arg2, int arg3) {
						

					}

					@Override
					public void afterTextChanged(Editable arg0) {
						

					}
				});
//		doRequest(Constant.querySendTaskList);
		if(!Constant.ISSCAN){
			viewUtils.setVisiable(R.id.iv_search_saomiao);
			viewUtils.setOnClickListener(R.id.iv_search_saomiao);
		}
	}

	@Override
	public void releaseOnDestory() {
		getEventBus().unregister(this);

	}

	@Override
	public void onClickable(View view) {

		switch (view.getId()) {
		case R.id.tv_search_cancel:
			viewUtils.setGone(R.id.ll_frag_search);
			viewUtils.setViewText(R.id.edt_search, "");
			break;
		case R.id.iv_search_saomiao:
			intentForResult(CaptureActivity.class, new IntentExtra(Constant.SCAN_NORMAL,null), 0);
			break;
		default:
			break;
		}
	}

	@Override
	public void doRequest(int requestCode, Object... needParams) {
		switch (requestCode) {
		case Constant.querySendTaskList:
			Gson gson = new Gson();
			Map<String, String> maps = new HashMap<String, String>();
			maps.put("agentId", SPUtils.getAgent(getActivity()).getAgentId()+ "");
			maps.put("agentUserId", SPUtils.getUser(getActivity()).getAgentUserId() + "");
			maps.put("agentUserCode", SPUtils.getUser(getActivity()).getAgentUserCode());
			maps.put("agentUserName", SPUtils.getUser(getActivity()).getAgentUserName());
			maps.put("type", PickupFinishedFragment.list_type + "");
			String paramsValues = gson.toJson(maps);
			requestServer(Constant.SENDTASK_SERVER, Constant.querySendTaskList,
					AppUtils.createParams(ApiCode.querySendTaskList.code,
							paramsValues));
			break;
		default:
			break;
		}
	}

	@Override
	public void requestCallBack(int requestCode, MsMessage msm,
			String failedReason) {
		switch (requestCode) {
		case Constant.querySendTaskList:
			List<TaskWaybill> taskWaybills = new ArrayList<TaskWaybill>();
			if (msm.getResult() == 0) {// 成功
				taskWaybills = new Gson().fromJson(msm.getData().toString(),
						new TypeToken<List<TaskWaybill>>() {
						}.getType());
			}
			onEvent(new TabEvent(1, taskWaybills.size()));
			break;
		case Constant.addCustomer://新增
			if(msm.getResult()==0){
				T.showShort(getActivity(), "已经加到您的联系客户列表中了哦！");
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
	public int bindLayoutId() {
		return R.layout.frag_pickup;
	}

	/**
	 * 获得Tab视图
	 * 
	 * @param index
	 * @return
	 * @since 2015-7-21下午2:24:15
	 * @author cuixingwang
	 */
	private View getTabItemView(int index) {
		View view = inflater.inflate(R.layout.item_task_tabhost, null);
		textView = (TextView) view
				.findViewById(R.id.item_tabhost_bottomText);
		textView.setText(mTextviewArray[index]+"0）");
		return view;
	}

	public void onEvent(TabEvent event) {
		TextView childAt = (TextView) tabHost.getTabWidget().getChildAt(event.getIndex()).findViewById(R.id.item_tabhost_bottomText);
		childAt.setText(mTextviewArray[event.getIndex()]+event.getSize()+"）");
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
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// 移除扫描监听
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
			mSoundUtils.init(getActivity());
			mSoundUtils.success();
			viewUtils.setVisiable(R.id.ll_frag_search);
			viewUtils.setViewText(R.id.edt_search, barcode);
			EventBus.getDefault().post(new GetSearchTextEvent(barcode));
		}
	}

	@Override
	public void onPause() {
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

	@Override
	public void intentCallback(int requestCode, int resultCode, IntentExtra data) {
		// TODO Auto-generated method stub
		viewUtils.setVisiable(R.id.ll_frag_search);
		viewUtils.setViewText(R.id.edt_search, (String)data.getValue());
		EventBus.getDefault().post(new GetSearchTextPickupEvent((String)data.getValue()));
	}
	public void onEvent(ChangeTabPickupEvent event){
		tabHost.setCurrentTabByTag(event.getTab());
	}
}
