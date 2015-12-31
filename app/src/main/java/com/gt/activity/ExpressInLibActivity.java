package com.gt.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.TextView;
import cn.zjl.zxing.CaptureActivity;

import com.common.adapter.CommonCarrierAdapter;
import com.common.base.BaseActivity;
import com.common.events.ChooseSendCompanyEvent;
import com.common.events.CloseEvent;
import com.common.model.AgentCarrier;
import com.common.model.AgentReceiveTask;
import com.common.model.ApiCode;
import com.common.model.IntentExtra;
import com.common.model.MsMessage;
import com.common.utils.AppUtils;
import com.common.utils.Constant;
import com.common.utils.SPUtils;
import com.common.utils.T;
import com.common.utils.ViewUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gt.officeagent.R;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

public class ExpressInLibActivity extends BaseActivity {

	private List<AgentCarrier> agentCarriers = new ArrayList<AgentCarrier>();
	private CommonCarrierAdapter commonCarrierAdapter = null;
	public static AgentCarrier agentCarrier = null;
	private Dialog dialog;
	private ViewUtils vDialogUtils = null;

	@Override
	public int bindLayoutId() {
		return R.layout.activity_expressinlib1;
	}

	@Override
	public void initParmers() {
		getEventBus().register(this);
		agentCarrier = null;
	}

	@Override
	public void initViewsAndValues(Bundle savedInstanceState) {
		viewUtils.setOnClickListener(R.id.btn_expressinlib_submit,
				R.id.tv_expressinlib_enter);

		commonCarrierAdapter = new CommonCarrierAdapter(this, agentCarriers,
				Constant.TYPE_COMPANY);
		viewUtils.view(R.id.gv_expressinlib, GridView.class).setAdapter(
				commonCarrierAdapter);
//		List<AgentCarrier> list=SPUtils.getAgentCarrierList(this);
//		if(list!=null&&list.size()>0){
//			for(int i=0;i<list.size();i++){
//				if(i==10){
//					break;
//				}
//				agentCarriers.add(list.get(i));
//			}
//			commonCarrierAdapter.updateAll(agentCarriers);
//		}else{
		doRequest(Constant.queryCommonCarriers);
//		}
	}

	@Override
	public void releaseOnDestory() {
		getEventBus().unregister(this);
	}

	@Override
	public void onClickable(View view) {

		switch (view.getId()) {
		case R.id.btn_expressinlib_submit:
			if (agentCarrier == null) {
				T.showShort(this, "请选择快递公司!");
				return;
			} else {
				try {
					AgentCarrier dbAgentCarrier=getDbUtils().findFirst(AgentCarrier.class);
					if (dbAgentCarrier == null) {
						getDbUtils().save(agentCarrier);
						if(Constant.ISSCAN){
							intent(CapturePDAActivity.class, new IntentExtra(
									Constant.SCAN_KUAIJIANRUKU, null));
						}else{
							intent(CaptureActivity.class, new IntentExtra(
									Constant.SCAN_KUAIJIANRUKU, null));
						}
					} else if (dbAgentCarrier.getParentCarrierName().equals(this.agentCarrier.getParentCarrierName())) {
						if(Constant.ISSCAN){
							intent(CapturePDAActivity.class, new IntentExtra(
									Constant.SCAN_KUAIJIANRUKU, null));
						}else{
							intent(CaptureActivity.class, new IntentExtra(
									Constant.SCAN_KUAIJIANRUKU, null));
						}
						
					} else {
						List<AgentReceiveTask> list=getDbUtils().findAll(Selector.from(AgentReceiveTask.class).where("type", "=", Constant.SCAN_KUAIJIANRUKU));
						if(list==null||list.size()==0){
							getDbUtils().deleteAll(AgentCarrier.class);
							getDbUtils().save(agentCarrier);
							if(Constant.ISSCAN){
								intent(CapturePDAActivity.class, new IntentExtra(
										Constant.SCAN_KUAIJIANRUKU, null));
							}else{
								intent(CaptureActivity.class, new IntentExtra(
										Constant.SCAN_KUAIJIANRUKU, null));
							}
						}else{
						editRUKUDialog(dbAgentCarrier,list);}
					}
				} catch (DbException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				

			}
			break;
		case R.id.tv_expressinlib_enter:

			intentForResult(CarrierBookActivity2.class, null, 0);
			break;

		default:
			break;
		}

	}

	private void editRUKUDialog(final AgentCarrier dbAgentCarrier,final List<AgentReceiveTask> list) {
		String carrierName="当前有未完成的"+"<font color='red'>" + dbAgentCarrier.getParentCarrierName() + "</font>"+"快递扫描记录，是否重新扫描？";
		vDialogUtils = showRUKUDialog(this, Html.fromHtml(carrierName));
//		vDialogUtils = showRUKUDialog(this, "当前有未完成的快递扫描记录，是否重新扫描？");
		vDialogUtils.setViewText(R.id.btn_dialog_cancel, "继续扫描");
		vDialogUtils.setViewText(R.id.btn_dialog_ok, "重新扫描");
		vDialogUtils.view(R.id.btn_dialog_cancel).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// 取消
						if(Constant.ISSCAN){
							intent(CapturePDAActivity.class, new IntentExtra(
									Constant.SCAN_KUAIJIANRUKU, null));
						}else{
							intent(CaptureActivity.class, new IntentExtra(
									Constant.SCAN_KUAIJIANRUKU, null));
						}
						dialog.cancel();
					}
				});
		vDialogUtils.view(R.id.btn_dialog_ok).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						try {
							getDbUtils().deleteAll(AgentCarrier.class);
							getDbUtils().save(agentCarrier);
							getDbUtils().deleteAll(list);
//							intent(CaptureActivity.class, new IntentExtra(
//									Constant.SCAN_KUAIJIANRUKU, null));
							dialog.cancel();
							if(Constant.ISSCAN){
								intent(CapturePDAActivity.class, new IntentExtra(
										Constant.SCAN_KUAIJIANRUKU, null));
							}else{
								intent(CaptureActivity.class, new IntentExtra(
										Constant.SCAN_KUAIJIANRUKU, null));
							}
						} catch (DbException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});

	}

	@Override
	public void doRequest(int requestCode, Object... needParams) {
		showLoadingDialog(requestCode);

		switch (requestCode) {
		case Constant.queryCommonCarriers:
			Map<String, String> maps = new HashMap<String, String>();
			maps.put("agentId", SPUtils.getAgent(this).getAgentId() + "");

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
			List<AgentCarrier> carries= new Gson().fromJson(msm.getData().toString(),
					new TypeToken<List<AgentCarrier>>() {
					}.getType());
			for(int i=0;i<carries.size();i++){
				if(i==10){
					break;
				}
				agentCarriers.add(carries.get(i));
			}
			SPUtils.setAgentCarrierList(this, carries);
		} else {
//			agentCarriers = AppUtils.getAgentCarriers(this);
			List<AgentCarrier> list=SPUtils.getAgentCarrierList(this);
			if(list!=null&&list.size()>0){
				for(int i=0;i<list.size();i++){
					if(i==10){
						break;
					}
					agentCarriers.add(list.get(i));
				}
			}
		}
		commonCarrierAdapter.updateAll(agentCarriers);

	}

	@Override
	public void doEmptyCheck(Object... needParams) {

	}

	@Override
	public void intentCallback(int requestCode, int resultCode, IntentExtra data) {
		agentCarrier = (AgentCarrier) data.getValue();
		// InLibPreviewActivity.agentCarrier=agentCarrier;
		viewUtils.setViewText(R.id.tv_expressinlib_enter,
				agentCarrier.getParentCarrierName());
		commonCarrierAdapter.updateAll(agentCarriers);
	}

	public void onEvent(CloseEvent event) {
		finish();
	}

	public void onEvent(ChooseSendCompanyEvent event) {
		agentCarrier = event.getAgentCarrier();
		// InLibPreviewActivity.agentCarrier=event.getAgentCarrier();
		viewUtils.setViewText(R.id.tv_expressinlib_enter,
				agentCarrier.getParentCarrierName());
	}

	/**
	 * 快递入库弹框
	 * 
	 * @param context
	 * @param requesxxxtCode
	 */
	private ViewUtils showRUKUDialog(Context context, Spanned text) {

		dialog = new Dialog(context, R.style.Dialog);
		ViewUtils viewUtils = new ViewUtils(context, R.layout.dialog_ruku);

		dialog.setContentView(viewUtils.getRootView());

//		viewUtils.setViewText(R.id.tv_dialog_text, text);
		viewUtils.view(R.id.tv_dialog_text,TextView.class).setText(text);

		dialog.show();
		return viewUtils;
	}
}