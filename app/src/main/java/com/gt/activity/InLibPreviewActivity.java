package com.gt.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.common.adapter.ScanAdapter;
import com.common.base.BaseActivity;
import com.common.events.CloseEvent;
import com.common.model.AgentCarrier;
import com.common.model.AgentReceiveTask;
import com.common.model.ApiCode;
import com.common.model.IntentExtra;
import com.common.model.MsMessage;
import com.common.utils.AppUtils;
import com.common.utils.Constant;
import com.common.utils.DialogUtils;
import com.common.utils.T;
import com.common.utils.TimeUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gt.officeagent.R;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

/***
 * 关于
 * 
 * @author
 * 
 */
public class InLibPreviewActivity extends BaseActivity {

	private AgentCarrier agentCarrier;
	private List<AgentReceiveTask> inlibAgentReceiveTasks;
	private List<AgentReceiveTask> repeatReceiveTasks;
	ScanAdapter scanAdapter = null;

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void initParmers() {

	}

	@Override
	public int bindLayoutId() {
		return R.layout.activity_inlibpreview;
	}

	@Override
	public void initViewsAndValues(Bundle savedInstanceState) {
		try {
		agentCarrier=getDbUtils().findFirst(AgentCarrier.class);
		inlibAgentReceiveTasks=getDbUtils().findAll(Selector.from(AgentReceiveTask.class)
                   .where("type" ,"=",Constant.SCAN_KUAIJIANRUKU));
        repeatReceiveTasks= new ArrayList<AgentReceiveTask>();
		scanAdapter = new ScanAdapter(this, inlibAgentReceiveTasks,true,0);

		viewUtils.view(R.id.lv_inlibpreview, ListView.class).setAdapter(
				scanAdapter);

		viewUtils.setOnClickListener(R.id.btn_inlibpreview_ok);
		
		viewUtils.setViewText(R.id.tv_inlibpreview_time, TimeUtils.parseTimeToString(new Date().getTime(), "yyyy-MM-dd HH:mm:ss"));
		viewUtils.setViewText(R.id.tv_inlibpreview_count, "共扫描    "+inlibAgentReceiveTasks.size()+" 个");
		
		
			
			getTopbar().setTitleText(agentCarrier.getParentCarrierName());
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void releaseOnDestory() {

	}

	@Override
	public void onClickable(View view) {
		switch (view.getId()) {
		case R.id.btn_inlibpreview_ok:
			doRequest(Constant.intake);
			break;

		default:
			break;
		}

	}

	@Override
	public void doRequest(int requestCode, Object... needParams) {
		showLoadingDialog(requestCode);

		switch (requestCode) {
		case Constant.intake:
			Gson gson = new Gson();
			
			String expresscodes = gson.toJson(inlibAgentReceiveTasks,
					new TypeToken<List<AgentReceiveTask>>() {
					}.getType());
			String paramsValues = expresscodes;
			requestServer(Constant.RECEIVETASK_SERVER, Constant.intake,
					AppUtils.createParams(ApiCode.intake.code, paramsValues));
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
			try {
				getDbUtils().deleteAll(AgentCarrier.class);
				getDbUtils().deleteAll(inlibAgentReceiveTasks);
			} catch (DbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			repeatReceiveTasks = new Gson().fromJson(msm.getData().toString(),
					new TypeToken<List<AgentReceiveTask>>() {
			}.getType());
			String result=repeatReceiveTasks.size()==0?"":(repeatReceiveTasks.size()+"，个失败");
			DialogUtils.showTipDialog(this, 0, "入库成功，"+(inlibAgentReceiveTasks.size()-repeatReceiveTasks.size())+"个成功"+result,Constant.SCAN_KUAIJIANRUKU)
					.view(R.id.btn_dialog_ok)
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							//跳到失败快件预览页面
							if(repeatReceiveTasks!=null&&repeatReceiveTasks.size()>0){//入库的订单中有已经入库的订单
								List<String> list=new ArrayList<String>();
								for(AgentReceiveTask art:repeatReceiveTasks){
									list.add(art.getExpressCode());
								}
								intent(FailureReceTaskActivity.class, new IntentExtra(0, list));
							}
							DialogUtils.HideTipDialog();
							InLibPreviewActivity.this.finish();
							getEventBus().post(new CloseEvent("close"));
						}
					});
		}else{
			T.showShort(this, msm.getMessage());
		}
	}

	@Override
	public void doEmptyCheck(Object... needParams) {
		
	}

	@Override
	public void intentCallback(int requestCode, int resultCode, IntentExtra data) {

	}
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if(Constant.ISSCAN){
			if (event.getKeyCode()==KeyEvent.KEYCODE_DPAD_CENTER) {
				doRequest(Constant.intake);
			}
			
		}
		return super.onKeyUp(keyCode, event);
	}
}
