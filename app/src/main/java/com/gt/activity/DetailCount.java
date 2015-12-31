package com.gt.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import cn.yuri.wheelview.WheelMain;

import com.common.adapter.DetailCountAdapter;
import com.common.base.BaseActivity;
import com.common.model.ApiCode;
import com.common.model.IntentExtra;
import com.common.model.MsMessage;
import com.common.model.MyWorkStatistics;
import com.common.utils.AppUtils;
import com.common.utils.Constant;
import com.common.utils.SPUtils;
import com.common.utils.ScreenUtils;
import com.common.utils.T;
import com.common.utils.TimeUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gt.officeagent.R;

public class DetailCount extends BaseActivity {

	public static View timePicker;
	public static WheelMain wheelMain;
	private List<MyWorkStatistics> myStatisticsLists = new ArrayList<MyWorkStatistics>();
	private DetailCountAdapter detailCountAdapter;
	private Calendar calendar;
	private Date currentDate;
	private String date;
	private String time;
	private SimpleDateFormat sdf = new SimpleDateFormat("M-d");

	@Override
	public int bindLayoutId() {
		return R.layout.activity_detailcount;
	}

	@Override
	public void initParmers() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initViewsAndValues(Bundle savedInstanceState) {
		// 将日历控件中的月份截取请求服务器数据
		calendar = Calendar.getInstance();
		String yearAndMonth = calendar.get(Calendar.YEAR) + "-"
				+ (calendar.get(Calendar.MONTH) + 1);
		doRequest(Constant.queryStatisticsByMonth, yearAndMonth);
		// 获得当前时间展示顶部
		currentDate = new Date(System.currentTimeMillis());
		date = sdf.format(currentDate).toString();
		viewUtils.setViewText(R.id.tv_top_date, date);
		viewUtils.setViewText(R.id.tv_top_week,
				getWeekOfDate(currentDate));
		viewUtils.setOnClickListener(R.id.iv_choicedate,
				R.id.iv_detailcount_zuo, R.id.iv_detailcount_you);
		viewUtils.view(R.id.btn_dialogconfirm_changebirthday);
		viewUtils.view(R.id.btn_dialogcancel_changebirthday);

		detailCountAdapter = new DetailCountAdapter(this, myStatisticsLists);
		viewUtils.view(R.id.lv_detailcount_dayofmonth, ListView.class)
				.setAdapter(detailCountAdapter);
	}

	// 获取当前日期为星期几方法
	public String getWeekOfDate(Date date) {
		String[] weekOfDay = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		calendar = Calendar.getInstance();
		if (date != null) {
			calendar.setTime(currentDate);
		}
		int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0) {
			w = 0;
		}
		return weekOfDay[w];
	}

	@Override
	public void releaseOnDestory() {

	}

	@Override
	public void onClickable(View view) {
		switch (view.getId()) {
		case R.id.iv_detailcount_zuo:
			// calendar.add(Calendar.MONTH, 0);
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			String first = sdf.format(calendar.getTime());
			if (viewUtils.getViewText(R.id.tv_top_date).equals(first)) {
				T.showShort(this, "请选择您要查询的月份");

			} else {
				calendar.setTime(currentDate);
				calendar.add(calendar.DATE, -1);
				currentDate = calendar.getTime();
				date = sdf.format(currentDate).toString();
				viewUtils.setViewText(R.id.tv_top_date, date);
				viewUtils.setViewText(R.id.tv_top_week,
						getWeekOfDate(currentDate));
				int day = currentDate.getDate() - 1;
				if (myStatisticsLists.size() != 0) {

					// T.showShort(this, "下标是："+day+"，数据个数为："
					// +myStatisticsLists.size());
					try {
						viewUtils.setViewText(R.id.tv_home_paisong,
								myStatisticsLists.get(day).getOut());
						viewUtils.setViewText(R.id.tv_home_lanshou,
								myStatisticsLists.get(day).getTotal());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			break;

		case R.id.iv_detailcount_you:
			calendar.set(Calendar.DAY_OF_MONTH,
					calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
			String last = sdf.format(calendar.getTime());
			if (viewUtils.getViewText(R.id.tv_top_date).equals(last)) {
				T.showShort(this, "请选择您要查询的月份");

			} else {
				calendar.setTime(currentDate);
				calendar.add(calendar.DATE, 1);
				currentDate = calendar.getTime();
				date = sdf.format(currentDate).toString();
				viewUtils.setViewText(R.id.tv_top_date, date);
				viewUtils.setViewText(R.id.tv_top_week,
						getWeekOfDate(currentDate));
				int day = currentDate.getDate() - 1;
				if (myStatisticsLists.size() != 0) {

					// T.showShort(this, "下标是："+day+"，数据个数为："
					// +myStatisticsLists.size());
					try {
						viewUtils.setViewText(R.id.tv_home_paisong,
								myStatisticsLists.get(day).getOut());
						viewUtils.setViewText(R.id.tv_home_lanshou,
								myStatisticsLists.get(day).getTotal());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			break;

		case R.id.iv_choicedate:
			final Dialog dateDialog = new Dialog(this, R.style.Dialog);
			LayoutInflater inflater = (LayoutInflater) this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			LinearLayout layout = (LinearLayout) inflater.inflate(
					R.layout.dialog_changedate, null);
			timePicker = layout.findViewById(R.id.timePicker1);
			wheelMain = new WheelMain(this, timePicker);
			wheelMain.initDateTimePicker();
			dateDialog.setContentView(layout);

			Window window2 = dateDialog.getWindow();
			WindowManager.LayoutParams lp2 = window2.getAttributes();
			int screenW2 = ScreenUtils.getScreenWidth(this);
			lp2.width = screenW2 - 60;
			Button btn_dialogconfirm_changebirthday = (Button) layout
					.findViewById(R.id.btn_dialogconfirm_changebirthday);
			Button btn_dialogcancel_changebirthday = (Button) layout
					.findViewById(R.id.btn_dialogcancel_changebirthday);
			btn_dialogconfirm_changebirthday
					.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {

							time = wheelMain.getTime().toString();
							viewUtils.setViewText(R.id.edt_choicedate, time);
							doRequest(Constant.queryStatisticsByMonth, time);
							String[] top = time.split("-");
							viewUtils.setViewText(R.id.tv_top_date, top[1]
									+ "-" + calendar.get(Calendar.DAY_OF_MONTH));

							calendar.set(Integer.parseInt(top[0]),
									Integer.parseInt(top[1]) - 1,
									calendar.get(Calendar.DATE));
							currentDate = calendar.getTime();
							dateDialog.dismiss();

						}
					});
			btn_dialogcancel_changebirthday
					.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							dateDialog.dismiss();
						}
					});
			dateDialog.show();
			break;

		default:
			break;
		}
	}

	@Override
	public void doEmptyCheck(Object... needParams) {

	}

	@Override
	public void doRequest(int requestCode, Object... needParams) {
		switch (requestCode) {
		case Constant.queryStatisticsByMonth:
			Gson gson = new Gson();
			Map<String, String> map = new HashMap<String, String>();
			map.put("agentId", SPUtils.getAgent(this).getAgentId() + "");
			map.put("agentUserId", SPUtils.getUser(this).getAgentUserId() + "");
			map.put("month", needParams[0] + "");
			String paramsValues = gson.toJson(map);
			requestServer(Constant.STATISTICS_SERVER,
					Constant.queryStatisticsByMonth, AppUtils.createParams(
							ApiCode.queryStatisticsByMonth.code, paramsValues));
			break;

		default:
			break;
		}
	}

	@Override
	public void requestCallBack(int requestCode, MsMessage msm,
			String failedReason) {
		if (msm.getResult() == 0) {
			T.showShort(this, msm.getMessage());
			myStatisticsLists = new Gson().fromJson(msm.getData().toString(),
					new TypeToken<List<MyWorkStatistics>>() {
					}.getType());
			Log.w("AAA", myStatisticsLists.size() + "");
			detailCountAdapter.updateAll(myStatisticsLists);

			viewUtils.setViewText(R.id.tv_home_paisong,
					myStatisticsLists.get(currentDate.getDate() - 1).getOut()
							+ "");
			viewUtils.setViewText(R.id.tv_home_lanshou,
					myStatisticsLists.get(currentDate.getDate() - 1).getTotal()
							+ "");

		} else {
			T.showShort(this, msm.getMessage());
		}
	}

	@Override
	public void intentCallback(int requestCode, int resultCode, IntentExtra data) {
		// TODO Auto-generated method stub

	}

}
