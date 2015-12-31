package com.common.ui;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gt.officeagent.R;

public class Topbar extends FrameLayout implements View.OnClickListener {

	/**
	 * <attr name="topbarBackMode"> <enum name="other" value="0"></enum> <enum
	 * name="normal" value="1"></enum> <enum name="finish" value="2"></enum>
	 * </attr> <attr name="topbarTitleMode"> <enum name="click"
	 * value="0"></enum> <enum name="normal" value="1"></enum> </attr>
	 */
	public static final int BACKMODE_OTHER = 0;
	public static final int BACKMODE_NORMAL = 1;
	public static final int BACKMODE_FINISH = 2;
	public static final int TITLEMODE_CLICK = 0;
	public static final int TITLEMODE_NORMAL = 1;
	public static final int TYPE_CONTROL_ONE = 0;
	public static final int TYPE_CONTROL_TWO = 1;

	private FrameLayout fl_back_layout;
	private ImageView iv_back_image;
	private TextView tv_back_text;

	private LinearLayout ll_title_layout;
	private TextView tv_title;
	private ImageView iv_title_lable;

	private RelativeLayout rl_control_layout;
	private ImageView iv_control_one_image;
	private ImageView iv_control_one_dot;
	private ImageView iv_control_two_image;
	private ImageView iv_control_two_dot;
	private TextView tv_control_one_text;
	private TextView tv_control_two_text;
	private FrameLayout fl_control_one_dot_layout;
	private FrameLayout fl_control_two_dot_layout;

	private int backMode;
	private int titleMode;
	private int backBg;
	private String backText;
	private int backTextColor;
	private String titleText;
	private String controlOneText;
	private String controlTwoText;
	private int titleBelowBg;
	private int titleTextColor;
	private float titleTextSize;
	private int controlOneBg;
	private int controlTwoBg;
	private int controlDotBg;
	private int controlOneTextColor;
	private int controlTwoTextColor;

	private int backLayoutWidth = 60;
	private Context context;

	private OnOtherListener onOtherListener;
	private OnTitleListener onTitleListener;
	private OnControlOneListener onControlOneListener;
	private OnControlTwoListener onControlTwoListener;

	public Topbar(Context context, AttributeSet attrs) {
		super(context, attrs);

		this.context = context;

		TypedArray array = context.obtainStyledAttributes(attrs,
				R.styleable.Topbar);
		backMode = array.getInt(R.styleable.Topbar_topbarBackMode,
				BACKMODE_NORMAL);
		titleMode = array.getInt(R.styleable.Topbar_topbarTitleMode,
				TITLEMODE_NORMAL);
		backBg = array.getResourceId(R.styleable.Topbar_topbarBackBg, 0);
		backText = array.getString(R.styleable.Topbar_topbarBackText);
		backTextColor = array.getColor(R.styleable.Topbar_topbarBackTextColor,
				Color.BLACK);
		titleText = array.getString(R.styleable.Topbar_topbarTitleText);
		titleBelowBg = array.getResourceId(
				R.styleable.Topbar_topbarTitleBelowBg, 0);
		titleTextColor = array.getColor(
				R.styleable.Topbar_topbarTitleTextColor, Color.BLACK);
		titleTextSize = array.getDimension(
				R.styleable.Topbar_topbarTitleTextSize, 16);
		controlOneBg = array.getResourceId(
				R.styleable.Topbar_topbarControlOneBg, 0);
		controlTwoBg = array.getResourceId(
				R.styleable.Topbar_topbarControlTwoBg, 0);
		controlDotBg = array.getResourceId(
				R.styleable.Topbar_topbarControlDotBg,
				R.drawable.topbar_dot_shape);
		controlOneText = array
				.getString(R.styleable.Topbar_topbarControlOneText);
		controlTwoText = array
				.getString(R.styleable.Topbar_topbarControlTwoText);
		controlOneTextColor = array.getColor(
				R.styleable.Topbar_topbarControlOneTextColor, Color.BLACK);
		controlTwoTextColor = array.getColor(
				R.styleable.Topbar_topbarControlTwoTextColor, Color.BLACK);

		init();

		array.recycle();
	}

	private void init() {

		setId(R.id.topbar);

		fl_back_layout = new FrameLayout(context);
		LayoutParams fl_backParams = null;
		if (backMode == BACKMODE_NORMAL) {
			fl_backParams = new LayoutParams(parseDip2px(context, 0), 0,
					Gravity.LEFT);
		} else if (backMode == BACKMODE_FINISH || backMode == BACKMODE_OTHER) {
			fl_backParams = new LayoutParams(parseDip2px(context,
					backLayoutWidth), LayoutParams.MATCH_PARENT, Gravity.LEFT);
			fl_back_layout.setPadding(parseDip2px(context, 15), 0,
					parseDip2px(context, 15), 0);
		}
		fl_back_layout.setLayoutParams(fl_backParams);
		fl_back_layout.setId(R.id.topbar_back);
		if (backMode == BACKMODE_FINISH || backMode == BACKMODE_OTHER) {
			if (TextUtils.isEmpty(backText)) {
				iv_back_image = new ImageView(context);
				LayoutParams iv_backParams = new LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
				iv_back_image.setLayoutParams(iv_backParams);
				iv_back_image.setImageResource(backBg);
				iv_back_image.setScaleType(ImageView.ScaleType.CENTER);
				fl_back_layout.setOnClickListener(this);
				fl_back_layout.addView(iv_back_image);
				// iv_back_image.setBackgroundColor(Color.YELLOW);
			} else {
				tv_back_text = new TextView(context);
				LayoutParams tv_backParams = new LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
						Gravity.CENTER);
				tv_back_text.setLayoutParams(tv_backParams);
				tv_back_text.setText(backText);
				tv_back_text.setTextColor(backTextColor);
				fl_back_layout.setOnClickListener(this);
				fl_back_layout.addView(tv_back_text);
			}
		}
		// test code color
		// fl_back_layout.setBackgroundColor(Color.BLUE);

		ll_title_layout = new LinearLayout(context);
		ll_title_layout.setId(R.id.topbar_title);
		ll_title_layout.setOrientation(LinearLayout.VERTICAL);
		ll_title_layout.setGravity(Gravity.CENTER);
		LayoutParams ll_titleParams = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT,
				Gravity.CENTER);
		ll_title_layout.setLayoutParams(ll_titleParams);
		tv_title = new TextView(context);
		tv_title.setText(TextUtils.isEmpty(titleText) ? context
				.getString(R.string.app_name) : titleText);
		tv_title.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize);
		tv_title.setTextColor(titleTextColor);
		ll_title_layout.addView(tv_title);
		//test
		fl_control_one_dot_layout = new FrameLayout(context);
		fl_control_one_dot_layout.setId(R.id.topbar_control_one);
		LayoutParams oneTextParamsR = new LayoutParams(
				LayoutParams.WRAP_CONTENT, getResources()
						.getDimensionPixelSize(
								R.dimen.topbar_height));
		tv_control_one_text = new TextView(context);
		tv_control_one_text.setLayoutParams(oneTextParamsR);
		tv_control_one_text.setGravity(Gravity.CENTER);
//		tv_control_one_text.setId(R.id.topbar_control_one);
		tv_control_one_text.setTextColor(Color.WHITE);
		fl_control_one_dot_layout.addView(tv_control_one_text);
//		setOnControlOneListener(onControlOneListener);
		fl_control_one_dot_layout.setOnClickListener(this);
		rl_control_layout=new RelativeLayout(context);
		LayoutParams rl_controlParamsR = new LayoutParams(android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT
				,android.widget.RelativeLayout.LayoutParams.MATCH_PARENT,Gravity.RIGHT);
		rl_controlParamsR.rightMargin = parseDip2px(context, 8);
//		rl_controlParamsR.addRule(RelativeLayout.RIGHT_OF,
//				R.id.topbar_control_one);
		rl_control_layout.setLayoutParams(rl_controlParamsR);
		rl_control_layout.setGravity(Gravity.CENTER);
		rl_control_layout.addView(fl_control_one_dot_layout);
		if (titleMode == TITLEMODE_CLICK) {
			iv_title_lable = new ImageView(context);
			iv_title_lable.setImageResource(titleBelowBg);
			ll_title_layout.addView(iv_title_lable);
			ll_title_layout.setOnClickListener(this);
		}
		// test code color
		// ll_title_layout.setBackgroundColor(Color.RED);

		if (TextUtils.isEmpty(controlOneText)
				&& TextUtils.isEmpty(controlTwoText)) {
			if (controlOneBg != 0 || controlTwoBg != 0) {
				rl_control_layout = new RelativeLayout(context);
				LayoutParams rl_controlParams = new LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT,
						Gravity.RIGHT);
				rl_control_layout.setLayoutParams(rl_controlParams);
				rl_control_layout.setGravity(Gravity.CENTER);
				if (controlOneBg != 0) {
					fl_control_one_dot_layout = new FrameLayout(context);
					fl_control_one_dot_layout.setId(R.id.topbar_control_one);

					LayoutParams controlOneImgParams = new LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.MATCH_PARENT);
					iv_control_one_image = new ImageView(context);
					iv_control_one_image.setImageResource(controlOneBg);
					iv_control_one_image
							.setScaleType(ImageView.ScaleType.CENTER_CROP);
					iv_control_one_image.setId(R.id.topbar_control_one);
					iv_control_one_image.setOnClickListener(this);
					iv_control_one_image.setLayoutParams(controlOneImgParams);
					fl_control_one_dot_layout
							.setLayoutParams(controlOneImgParams);

					iv_control_one_dot = new ImageView(context);
					LayoutParams iv_controlDotParams = new LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT, Gravity.RIGHT
									| Gravity.TOP);
					iv_control_one_dot.setImageResource(controlDotBg);
					iv_control_one_dot.setLayoutParams(iv_controlDotParams);
					iv_control_one_dot.setPadding(0, parseDip2px(context, 8),
							parseDip2px(context, 8), 0);

					fl_control_one_dot_layout.addView(iv_control_one_image);
					fl_control_one_dot_layout.addView(iv_control_one_dot);
					rl_control_layout.addView(fl_control_one_dot_layout);

					iv_control_one_dot.setVisibility(View.GONE);
				}
				if (controlTwoBg != 0) {
					fl_control_two_dot_layout = new FrameLayout(context);
					fl_control_two_dot_layout.setId(R.id.topbar_control_two);

					LayoutParams controlTwoImgParams = new LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.MATCH_PARENT);
					iv_control_two_image = new ImageView(context);
					iv_control_two_image.setImageResource(controlTwoBg);
					iv_control_two_image
							.setScaleType(ImageView.ScaleType.CENTER_CROP);
					iv_control_two_image.setId(R.id.topbar_control_two);
					iv_control_two_image.setOnClickListener(this);
					iv_control_two_image.setLayoutParams(controlTwoImgParams);
					fl_control_two_dot_layout
							.setLayoutParams(controlTwoImgParams);

					iv_control_two_dot = new ImageView(context);
					LayoutParams iv_controlDotParams = new LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT, Gravity.RIGHT
									| Gravity.TOP);
					iv_control_two_dot.setImageResource(controlDotBg);
					iv_control_two_dot.setLayoutParams(iv_controlDotParams);
					iv_control_two_dot.setPadding(0, parseDip2px(context, 8),
							parseDip2px(context, 8), 0);

					fl_control_two_dot_layout.addView(iv_control_two_image);
					fl_control_two_dot_layout.addView(iv_control_two_dot);
					rl_control_layout.addView(fl_control_two_dot_layout);

					iv_control_two_dot.setVisibility(View.GONE);
				}

				if (controlOneBg != 0 && controlTwoBg != 0) {
					RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(
							RelativeLayout.LayoutParams.WRAP_CONTENT,
							RelativeLayout.LayoutParams.WRAP_CONTENT);
					params1.addRule(RelativeLayout.RIGHT_OF,
							R.id.topbar_control_one);
					fl_control_two_dot_layout.setLayoutParams(params1);
				}

				// test code color
				// rl_control_layout.setBackgroundColor(Color.YELLOW);

			}
		} else {
			if (!TextUtils.isEmpty(controlOneText)
					|| !TextUtils.isEmpty(controlTwoText)) {
				rl_control_layout = new RelativeLayout(context);
				LayoutParams rl_controlParams = new LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT,
						Gravity.RIGHT);
				rl_controlParams.rightMargin = parseDip2px(context, 10);
				rl_control_layout.setLayoutParams(rl_controlParams);
				rl_control_layout.setGravity(Gravity.CENTER);
				if (!TextUtils.isEmpty(controlOneText)) {
					fl_control_one_dot_layout = new FrameLayout(context);
					fl_control_one_dot_layout.setId(R.id.topbar_control_one);

					LayoutParams oneTextParams = new LayoutParams(
							LayoutParams.WRAP_CONTENT, getResources()
									.getDimensionPixelSize(
											R.dimen.topbar_height));
					tv_control_one_text = new TextView(context);
					tv_control_one_text.setText(controlOneText);
					tv_control_one_text.setLayoutParams(oneTextParams);
					tv_control_one_text.setGravity(Gravity.CENTER);
					tv_control_one_text.setTextColor(controlOneTextColor);
					tv_control_one_text.setId(R.id.topbar_control_one);
					tv_control_one_text.setOnClickListener(this);

					iv_control_one_dot = new ImageView(context);
					LayoutParams iv_controlDotParams = new LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT, Gravity.RIGHT
									| Gravity.TOP);
					iv_control_one_dot.setImageResource(controlDotBg);
					iv_control_one_dot.setLayoutParams(iv_controlDotParams);

					fl_control_one_dot_layout.addView(tv_control_one_text);
					fl_control_one_dot_layout.addView(iv_control_one_dot);
					rl_control_layout.addView(fl_control_one_dot_layout);

					iv_control_one_dot.setVisibility(View.GONE);
				}
				if (!TextUtils.isEmpty(controlTwoText)) {
					fl_control_two_dot_layout = new FrameLayout(context);
					fl_control_two_dot_layout.setId(R.id.topbar_control_two);

					LayoutParams twoTextParams = new LayoutParams(
							LayoutParams.WRAP_CONTENT, getResources()
									.getDimensionPixelSize(
											R.dimen.topbar_height));
					tv_control_two_text = new TextView(context);
					tv_control_two_text.setText(controlTwoText);
					tv_control_two_text.setLayoutParams(twoTextParams);
					tv_control_two_text.setGravity(Gravity.CENTER);
					tv_control_two_text.setTextColor(controlTwoTextColor);
					tv_control_two_text.setId(R.id.topbar_control_two);
					tv_control_two_text.setOnClickListener(this);

					iv_control_two_dot = new ImageView(context);
					LayoutParams iv_controlDotParams = new LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT, Gravity.RIGHT
									| Gravity.TOP);
					iv_control_two_dot.setImageResource(controlDotBg);
					iv_control_two_dot.setLayoutParams(iv_controlDotParams);

					fl_control_two_dot_layout.addView(tv_control_two_text);
					fl_control_two_dot_layout.addView(iv_control_two_dot);
					rl_control_layout.addView(fl_control_two_dot_layout);

					iv_control_two_dot.setVisibility(View.GONE);
				}

				if (!TextUtils.isEmpty(controlOneText)
						&& !TextUtils.isEmpty(controlTwoText)) {
					RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(
							RelativeLayout.LayoutParams.WRAP_CONTENT,
							RelativeLayout.LayoutParams.WRAP_CONTENT);
					params1.leftMargin = parseDip2px(context, 8);
					params1.addRule(RelativeLayout.RIGHT_OF,
							R.id.topbar_control_one);
					fl_control_two_dot_layout.setLayoutParams(params1);
				}

				// test code color
				// rl_control_layout.setBackgroundColor(Color.YELLOW);

			}
		}

		addView(fl_back_layout);
		addView(ll_title_layout);
		if (rl_control_layout != null)
			addView(rl_control_layout);
	}

	public void resetBackControl(int resid, String text,
			OnClickListener onClickListener) {
		if (resid == 0 && text.equals("")) {
			fl_back_layout.setVisibility(GONE);
		} else {
			fl_back_layout.setVisibility(VISIBLE);
			if (resid == 0) {
				tv_back_text.setText(text);
				tv_back_text.setOnClickListener(onClickListener);
			} else {
				iv_back_image.setImageResource(resid);
				iv_back_image.setOnClickListener(onClickListener);
			}

		}
	}

	public void resetFristControl(int resid, String text,
			OnClickListener onClickListener) {
		if (resid == 0 && text.equals("")) {
			fl_control_one_dot_layout.setVisibility(GONE);
		} else {
			fl_control_one_dot_layout.setVisibility(VISIBLE);
			if (resid == 0) {
				tv_control_one_text.setText(text);
				tv_control_one_text.setOnClickListener(onClickListener);
			} else {
				iv_control_one_image.setImageResource(resid);
				iv_control_one_image.setOnClickListener(onClickListener);
			}

		}
	}

	public void resetSecondControl(int resid, String text,
			OnClickListener onClickListener) {
		if (resid == 0 && text.equals("")) {
			fl_control_two_dot_layout.setVisibility(GONE);
		} else {
			fl_control_two_dot_layout.setVisibility(VISIBLE);
			if (resid == 0) {
				tv_control_two_text.setText(text);
				tv_control_two_text.setOnClickListener(onClickListener);
			} else {
				iv_control_two_image.setImageResource(resid);
				iv_control_two_image.setOnClickListener(onClickListener);
			}

		}
	}

	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus) {
		super.onWindowFocusChanged(hasWindowFocus);

	}

	private int parseDip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	@SuppressWarnings("unused")
	private int parsePx2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public void setOnOtherListener(OnOtherListener onOtherListener) {
		this.onOtherListener = onOtherListener;
	}

	public void setOnTitleListener(OnTitleListener onTitleListener) {
		this.onTitleListener = onTitleListener;
	}

	public void setOnControlOneListener(
			OnControlOneListener onControlOneListener) {
		this.onControlOneListener = onControlOneListener;
	}

	public void setOnControlTwoListener(
			OnControlTwoListener onControlTwoListener) {
		this.onControlTwoListener = onControlTwoListener;
	}

	public void setTitleText(String text) {
		tv_title.setText(text);
		LayoutParams ll_titleParams = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT,
				Gravity.CENTER);
		ll_title_layout.setLayoutParams(ll_titleParams);
		LinearLayout.LayoutParams tv_titleParams = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		tv_title.setLayoutParams(tv_titleParams);
	}
	public void setControlOneText(String text) {
		tv_control_one_text.setText(text);
	}

	public void setControlOneResource(int resId) {
		iv_control_one_image.setImageResource(resId);
	}

	public void setControlTwoResource(int resId) {
		iv_control_two_image.setImageResource(resId);
	}

	private void controlDot(int type, int visiable) {
		switch (type) {
		case TYPE_CONTROL_ONE:
			if (iv_control_one_dot == null)
				return;
			iv_control_one_dot.setVisibility(visiable);
			break;
		case TYPE_CONTROL_TWO:
			if (iv_control_two_dot == null)
				return;
			iv_control_two_dot.setVisibility(visiable);
			break;
		}
	}

	public void showDot(int type) {
		controlDot(type, View.VISIBLE);
	}

	public void hideDot(int type) {
		controlDot(type, View.GONE);
	}

	public interface OnOtherListener {
		void onOtherListener();
	}

	public interface OnTitleListener {
		void onTitleListener();
	}

	public interface OnControlOneListener {
		void onControlOneListener();
	}

	public interface OnControlTwoListener {
		void onControlTwoListener();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.topbar_back:
			if (backMode == BACKMODE_FINISH) {
				((Activity) context).finish();
				// ((Activity)
				// context).overridePendingTransition(R.anim.activity_zoom_enter,
				// R.anim.activity_zoom_exit);
			} else if (backMode == BACKMODE_OTHER) {
				if (onOtherListener == null)
					throw new NullPointerException("请先调用setOnOtherListener()");
				onOtherListener.onOtherListener();
			}
			break;
		case R.id.topbar_title:
			if (onTitleListener == null)
				throw new NullPointerException("请先调用setOnTitleListener()");
			onTitleListener.onTitleListener();
			break;
		case R.id.topbar_control_one:
			if (onControlOneListener == null)
				throw new NullPointerException("请先调用setOnControlOneListener()");
			onControlOneListener.onControlOneListener();
			break;
		case R.id.topbar_control_two:
			if (onControlTwoListener == null)
				throw new NullPointerException("请先调用setOnControlTwoListener()");
			onControlTwoListener.onControlTwoListener();
			break;
		}
	}
}
