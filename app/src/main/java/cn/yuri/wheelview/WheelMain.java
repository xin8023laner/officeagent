package cn.yuri.wheelview;

import java.util.Calendar;

import android.content.Context;
import android.view.View;

import com.gt.officeagent.R;

public class WheelMain {

	private View view;
	private Context context;
	private WheelView wv_year;
	private WheelView wv_month;
	//private WheelView wv_day;
	@SuppressWarnings("unused")
	private WheelView wv_hours;
	@SuppressWarnings("unused")
	private WheelView wv_mins;
	private static int START_YEAR = 1990, END_YEAR = 2100;

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

	public static int getSTART_YEAR() {
		return START_YEAR;
	}

	public static void setSTART_YEAR(int sTART_YEAR) {
		START_YEAR = sTART_YEAR;
	}

	public static int getEND_YEAR() {
		return END_YEAR;
	}

	public static void setEND_YEAR(int eND_YEAR) {
		END_YEAR = eND_YEAR;
	}

	public WheelMain(View view) {
		super();

		this.view = view;
		setView(view);
	}

	public WheelMain(Context context, View view) {
		this.context = context;
		this.view = view;

		setView(view);
	}

	public void initDateTimePicker() {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
//		int day = calendar.get(Calendar.DATE);

//		String[] months_big = { "1", "3", "5", "7", "8", "10", "12" };
//		String[] months_little = { "4", "6", "9", "11" };

//		final List<String> list_big = Arrays.asList(months_big);
//		final List<String> list_little = Arrays.asList(months_little);

		wv_year = (WheelView) view.findViewById(R.id.year);
		wv_year.setAdapter(new NumericWheelAdapter(START_YEAR, END_YEAR));
		wv_year.setCyclic(true);
		wv_year.setCurrentItem(year - START_YEAR);
		wv_year.setVisibleItems(3);
		wv_year.TEXT_SIZE = 27;

		wv_month = (WheelView) view.findViewById(R.id.month);
		wv_month.setAdapter(new NumericWheelAdapter(1, 12));
		wv_month.setCyclic(true);
		wv_month.setCurrentItem(month);
		wv_month.setVisibleItems(3);
		wv_month.TEXT_SIZE = 27;

//		wv_day = (WheelView) view.findViewById(R.id.day);
//		wv_day.setVisibleItems(3);
//		wv_day.TEXT_SIZE = 27;
//		wv_day.setCyclic(true);
//		if (list_big.contains(String.valueOf(month + 1))) {
//			wv_day.setAdapter(new NumericWheelAdapter(1, 31));
//		} else if (list_little.contains(String.valueOf(month + 1))) {
//			wv_day.setAdapter(new NumericWheelAdapter(1, 30));
//		} else {
//			if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
//				wv_day.setAdapter(new NumericWheelAdapter(1, 29));
//			else
//				wv_day.setAdapter(new NumericWheelAdapter(1, 28));
//		}
//		wv_day.setCurrentItem(day - 1);

//		OnWheelChangedListener wheelListener_year = new OnWheelChangedListener() {
//			public void onChanged(WheelView wheel, int oldValue, int newValue) {
//				int year_num = newValue + START_YEAR;
//				if (list_big
//						.contains(String.valueOf(wv_month.getCurrentItem() + 1))) {
//					wv_day.setAdapter(new NumericWheelAdapter(1, 31));
//				} else if (list_little.contains(String.valueOf(wv_month
//						.getCurrentItem() + 1))) {
//					wv_day.setAdapter(new NumericWheelAdapter(1, 30));
//				} else {
//					if ((year_num % 4 == 0 && year_num % 100 != 0)
//							|| year_num % 400 == 0)
//						wv_day.setAdapter(new NumericWheelAdapter(1, 29));
//					else
//						wv_day.setAdapter(new NumericWheelAdapter(1, 28));
//				}
//			}
//		};
//		OnWheelChangedListener wheelListener_month = new OnWheelChangedListener() {
//			public void onChanged(WheelView wheel, int oldValue, int newValue) {
//				int month_num = newValue + 1;
//				if (list_big.contains(String.valueOf(month_num))) {
//					wv_day.setAdapter(new NumericWheelAdapter(1, 31));
//				} else if (list_little.contains(String.valueOf(month_num))) {
//					wv_day.setAdapter(new NumericWheelAdapter(1, 30));
//				} else {
//					if (((wv_year.getCurrentItem() + START_YEAR) % 4 == 0 && (wv_year
//							.getCurrentItem() + START_YEAR) % 100 != 0)
//							|| (wv_year.getCurrentItem() + START_YEAR) % 400 == 0);
//						wv_day.setAdapter(new NumericWheelAdapter(1, 29));
//					else
//						wv_day.setAdapter(new NumericWheelAdapter(1, 28));
//				}
//			}
//		};
//		wv_year.addChangingListener(wheelListener_year);
//		wv_month.addChangingListener(wheelListener_month);

		int textSize = 0;

		textSize = (int) context.getResources().getDimension(R.dimen.size_32);

//		wv_day.TEXT_SIZE = textSize;
		wv_month.TEXT_SIZE = textSize;
		wv_year.TEXT_SIZE = textSize;

	}

	public String getTime() {
		StringBuffer sb = new StringBuffer();
		sb.append((wv_year.getCurrentItem() + START_YEAR)).append("-")
				.append((wv_month.getCurrentItem() + 1));
//				.append("-")
//				.append((wv_day.getCurrentItem() + 1));
		return sb.toString();
	}

//	public String getDay() {
//		StringBuffer sb = new StringBuffer();
//		sb.append((wv_month.getCurrentItem() + 1));
//		.append("-")
//				.append((wv_day.getCurrentItem() + 1));
//		return sb.toString();
//	}
}
