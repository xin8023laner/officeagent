package com.gt.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.common.adapter.MsgAdapter;
import com.common.base.BaseActivity;
import com.common.db.PushModel;
import com.common.model.IntentExtra;
import com.common.model.MsMessage;
import com.common.ui.Topbar.OnControlTwoListener;
import com.gt.officeagent.R;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

/***
 * 消息
 * 
 * @author
 * 
 */
public class MsgActivity extends BaseActivity {

	private List<PushModel> pushModels=new ArrayList<PushModel>();
	private MsgAdapter msgAdapter = null;

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void initParmers() {

	}

	@Override
	public int bindLayoutId() {
		return R.layout.activity_msg;
	}

	@Override
	public void initViewsAndValues(Bundle savedInstanceState) {
		msgAdapter = new MsgAdapter(this, pushModels);
		viewUtils.view(R.id.lv_msg, ListView.class).setAdapter(
				msgAdapter);
		getData();

		getTopbar().setOnControlTwoListener(new OnControlTwoListener() {

			@Override
			public void onControlTwoListener() {
				try {
					// 删除消息
					if(pushModels!=null){
						getDbUtils().deleteAll(PushModel.class);
						pushModels.clear();
						msgAdapter.updateAll(pushModels);
					}
					doEmptyCheck();
				} catch (DbException e) {
					
				}
			}
		});

		viewUtils.view(R.id.lv_msg, ListView.class).setAdapter(msgAdapter);
		
		
		viewUtils.setOnClickListener(R.id.btn_empty_data);
	}

	private void getData() {
		try {
			// 根据推送时间倒序
			pushModels = getDbUtils().findAll(
					Selector.from(PushModel.class).orderBy("date", true));
			doEmptyCheck();
			if (pushModels != null && pushModels.size() > 0) {
				
//				viewUtils.view(R.id.lv_msg, ListView.class).setAdapter(
//						msgAdapter);
				msgAdapter.updateAll(pushModels);
				// 只要进入本页面就视为全部已读
				for (PushModel p : pushModels) {
					if (!p.isRead()) {
						p.setRead(true);
						getDbUtils().update(p);
					}
				}
			}

		} catch (DbException e) {
			
		}
	}

	@Override
	public void releaseOnDestory() {

	}

	@Override
	public void onClickable(View view) {
		switch (view.getId()) {
		case R.id.btn_empty_data:
			getData();
			break;

		default:
			break;
		}
	}

	@Override
	public void doRequest(int requestCode, Object... needParams) {

	}

	@Override
	public void requestCallBack(int requestCode, MsMessage msm,
			String failedReason) {

	}

	@Override
	public void doEmptyCheck(Object... needParams) {
		if (pushModels != null && pushModels.size() > 0) {
			viewUtils.setGone(R.id.nodata);
		} else {
			viewUtils.setVisiable(R.id.nodata);
			viewUtils.setViewText(R.id.tv_nodata_msg, "暂无消息");
		}
	}

	@Override
	public void intentCallback(int requestCode, int resultCode, IntentExtra data) {
		// TODO Auto-generated method stub

	}

	public void doSomeThing(int type, int position) {
		try {
			// 删除消息
			getDbUtils().delete(pushModels.get(position));
			pushModels.remove(position);
			msgAdapter.updateAll(pushModels);

			doEmptyCheck();
		} catch (DbException e) {
			e.printStackTrace();
		}

	}
	

}
