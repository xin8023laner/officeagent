package com.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.drawable.AnimationDrawable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import cn.greenrobot.eventbus.EventBus;
import cn.yuri.wheelview.AgentUserWheelAdapter;
import cn.yuri.wheelview.PickupBySelfWheelAdapter;
import cn.yuri.wheelview.WheelView;

import com.common.events.GetSearchTextEvent;
import com.common.listener.ChooseAgentUserOrPickupBySelfListener;
import com.common.model.AgentUser;
import com.common.model.CityListItem;
import com.common.model.PickUpBySelf;
import com.gt.activity.MainActivity;
import com.gt.officeagent.R;

/**
 * Created by zhangruntao on 15/6/25.
 */
public class DialogUtils {
	private static Dialog dialog;

	private static Map<Integer, Dialog> loadingDialogQueue = new HashMap<Integer, Dialog>();

	private static Dialog chooseAgentUserDialog;
	private static SoundUtils mSoundUtils;
	private static OnKeyListener onKeyListener = new OnKeyListener() {

		@Override
		public boolean onKey(DialogInterface arg0, int arg1, KeyEvent arg2) {
			// TODO Auto-generated method stub
			if (Constant.ISSCAN) {
				if (arg2.getAction() == KeyEvent.ACTION_DOWN) {
					Log.w("asdasd", String.valueOf(arg1));
						switch (arg2.getKeyCode()) {
						case KeyEvent.KEYCODE_0:
							mSoundUtils.playOther(0);
							break;
						case KeyEvent.KEYCODE_1:
							mSoundUtils.playOther(1);
							break;
						case KeyEvent.KEYCODE_2:
							mSoundUtils.playOther(2);
							break;
						case KeyEvent.KEYCODE_3:
							mSoundUtils.playOther(3);
							break;
						case KeyEvent.KEYCODE_4:
							mSoundUtils.playOther(4);
							break;
						case KeyEvent.KEYCODE_5:
							mSoundUtils.playOther(5);
							break;
						case KeyEvent.KEYCODE_6:
							mSoundUtils.playOther(6);
							break;
						case KeyEvent.KEYCODE_7:
							mSoundUtils.playOther(7);
							break;
						case KeyEvent.KEYCODE_8:
							mSoundUtils.playOther(8);
							break;
						case KeyEvent.KEYCODE_9:
							mSoundUtils.playOther(9);
							break;
						//case KeyEvent.KEYCODE_BACK:
						case KeyEvent.KEYCODE_BUTTON_A:
						case KeyEvent.KEYCODE_DEL:
						case KeyEvent.KEYCODE_POUND:
							return false;
						default:
							return true;
						}
					
				}
			}
			return false;
		}
	};
	/**
	 * 显示等待框，根据requestCode来区分等待框
	 * 
	 * @param context
	 *            上下文
	 * @param requestCode
	 *            网络请求Code
	 */
	
	public static void showLoadingDialog(Context context, int requestCode) {
		if(loadingDialogQueue.get(requestCode) != null){
			return;
		}
		
		Dialog dialog = new Dialog(context, R.style.Dialog);
		View view = LayoutInflater.from(context).inflate(
				R.layout.dialog_loading, null);
		TextView tv_loadingMsg = (TextView) view.findViewById(R.id.loading_msg);
		tv_loadingMsg.setText("加载中，请稍后...");

		ImageView iv_dialogIv = (ImageView) view
				.findViewById(R.id.iv_loadingDialog_bg);

		iv_dialogIv.setImageResource(R.anim.dialog_loadingnew_frame);
		AnimationDrawable drawable = (AnimationDrawable) iv_dialogIv
				.getDrawable();
		drawable.start();

		dialog.setContentView(view);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);

		loadingDialogQueue.put(requestCode, dialog);

		dialog.show();
	}

	/**
	 * 关闭等待框，根据传过来的请求Code来具体关闭哪个等待框
	 * 
	 * @param requestCode
	 *            请求Code
	 */
	public static void hideLoadingDialog(int requestCode) {
		if (loadingDialogQueue.get(requestCode) != null) {
			loadingDialogQueue.get(requestCode).dismiss();
			loadingDialogQueue.get(requestCode).cancel();
			loadingDialogQueue.remove(requestCode);
		}
	}

	/**
	 * 关闭所有等待框
	 */
	public static void hideLoadingDialogAll() {
		for (int i = 0; i < loadingDialogQueue.size(); i++) {
			if (loadingDialogQueue.get(i) != null) {
				loadingDialogQueue.get(i).dismiss();
				loadingDialogQueue.get(i).cancel();
			}
			loadingDialogQueue.remove(i);
		}
	}

	/**
	 * 忘记密码弹出提示对话框
	 * 
	 * @param context
	 * @param requestCode
	 */
	public static ViewUtils showForgetPwd(Context context, int requestCode) {

		dialog = new Dialog(context, R.style.Dialog);

		ViewUtils viewUtils = new ViewUtils(context, R.layout.dialog_forgetpwd);

		dialog.setContentView(viewUtils.getRootView());
		dialog.show();

		return viewUtils;
	}

	/**
	 * 登录失败提示对话框
	 * 
	 * @param context
	 * @param requestCode
	 */
	public static void showLoginFail(Context context, int requestCode) {
		Dialog dialog = new Dialog(context, R.style.Dialog);
		View view = LayoutInflater.from(context).inflate(
				R.layout.dialog_loginfail, null);
		dialog.setContentView(view);
		dialog.show();
	}

	public static void showSearchDialog(Context context) {
		final Dialog dialog = new Dialog(context, R.style.SearchDialog);

		final ViewUtils viewUtils = new ViewUtils(context,
				R.layout.dialog_search);
		viewUtils.view(R.id.tv_search_ok).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// EventBus.getDefault().post(
						// new GetSearchTextEvent(viewUtils
						// .getViewText(R.id.edt_search)));
						dialog.cancel();
					}
				});
		viewUtils.view(R.id.edt_search, EditText.class).addTextChangedListener(
				new TextWatcher() {
					@Override
					public void onTextChanged(CharSequence arg0, int arg1,
							int arg2, int arg3) {
						EventBus.getDefault().post(
								new GetSearchTextEvent(arg0.toString()));
					}

					@Override
					public void beforeTextChanged(CharSequence arg0, int arg1,
							int arg2, int arg3) {
						// TODO Auto-generated method stub

					}

					@Override
					public void afterTextChanged(Editable arg0) {
						// TODO Auto-generated method stub

					}
				});
		dialog.setContentView(viewUtils.getRootView());
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
	}

	/**
	 * 弹出普通话框
	 * 
	 * @param context
	 */
	public static ViewUtils showConfirmDialog(Context context, String title,
			String text) {

		dialog = new Dialog(context, R.style.Dialog);
		ViewUtils viewUtils = new ViewUtils(context, R.layout.dialog_confirm);

		dialog.setContentView(viewUtils.getRootView());
		viewUtils.setViewText(R.id.btn_dialog_save, "取消");
		viewUtils.setViewText(R.id.btn_dialog_ok, "拨打");
		viewUtils.view(R.id.btn_dialog_save).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						dialog.cancel();
					}
				});
		viewUtils.setViewText(R.id.tv_dialog_title, title);
		viewUtils.setViewText(R.id.tv_dialog_text, text);

		dialog.show();
		return viewUtils;
	}

	/**
	 * 弹出编辑电话框
	 * 
	 * @param context
	 */
	public static ViewUtils showEditCallDialog(Context context,String text) {

		dialog = new Dialog(context, R.style.Dialog);
		ViewUtils viewUtils = new ViewUtils(context, R.layout.dialog_editcall);

		dialog.setContentView(viewUtils.getRootView());

		viewUtils.setViewText(R.id.tv_dialog_title, text);

		dialog.show();
		return viewUtils;
	}

	/**
	 * 弹出编辑
	 * 
	 * @param context
	 * @param requesxxxtCode
	 */
	public static ViewUtils showTipDialog(Context context, int imgId,
			String text,int type) {

		
		dialog = new Dialog(context, R.style.Dialog);
		ViewUtils viewUtils = new ViewUtils(context, R.layout.dialog_tip);

		dialog.setContentView(viewUtils.getRootView());

		viewUtils.setViewText(R.id.tv_dialog_text, text);
		if (imgId != 0) {
			viewUtils.setImageResource(R.id.iv_dialog_icon, imgId);
		}
		if(type==Constant.SCAN_PAISONG){
			viewUtils.setVisiable(R.id.btn_dialog_paisong);
		}else{
			viewUtils.setGone(R.id.btn_dialog_paisong);
		}
		dialog.show();
		return viewUtils;
	}
	public static void HideTipDialog(){
		if(dialog!=null&&dialog.isShowing()){
			dialog.dismiss();
			dialog.cancel();
			dialog=null;
		}
	}
	/**
	 * 弹出编辑
	 * 
	 * @param context
	 */
	public static ViewUtils showInLibDialog(Context context) {

		final Dialog dialog = new Dialog(context, R.style.Dialog);
		ViewUtils viewUtils = new ViewUtils(context, R.layout.dialog_inlib);

		dialog.setContentView(viewUtils.getRootView());

		dialog.show();
		return viewUtils;
	}
	/**
	 * 重试框
	 * 
	 * @param context
	 */
	public static ViewUtils showRetryDialog(Context context,OnKeyListener onKeyListener) {
		
		dialog = new Dialog(context, R.style.Dialog);
		ViewUtils viewUtils = new ViewUtils(context, R.layout.dialog_retry);
		
		dialog.setContentView(viewUtils.getRootView());
		dialog.setCanceledOnTouchOutside(false);
		dialog.setOnKeyListener(onKeyListener);
		dialog.show();
		return viewUtils;
	}
	/**
	 * 重试框
	 * 
	 * @param context
	 */
	public static void cancelDialog() {
		if(dialog!=null){
			dialog.dismiss();
			dialog.cancel();
			dialog=null;
		}
	}
	/**
	 * 快递入库弹框
	 * 
	 * @param context
	 * @param requesxxxtCode
	 */
	public static  ViewUtils showRUKUDialog(Context context, String text) {

		dialog = new Dialog(context, R.style.Dialog);
		ViewUtils viewUtils = new ViewUtils(context, R.layout.dialog_ruku);

		dialog.setContentView(viewUtils.getRootView());

		viewUtils.setViewText(R.id.tv_dialog_text, text);

		dialog.show();
		return viewUtils;
	}
	/**
	 * 退出登录（本地有未入库单号）
	 * 
	 * @param context
	 */
	public static ViewUtils showLoginOutDialog(Context context,String text) {

		final Dialog dialog = new Dialog(context, R.style.Dialog);
		ViewUtils viewUtils = new ViewUtils(context, R.layout.dialog_ruku);

		dialog.setContentView(viewUtils.getRootView());
		dialog.setCanceledOnTouchOutside(false);
		
		viewUtils.setViewText(R.id.tv_dialog_text, text);
		viewUtils.view(R.id.btn_dialog_cancel).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});
		dialog.show();
		return viewUtils;
	}
	
	public static ViewUtils showChooseAgentUserDialog(Context context,final List<AgentUser> agentUserList,final List<PickUpBySelf> PickUpBySelfList,final ChooseAgentUserOrPickupBySelfListener listener){
		chooseAgentUserDialog=new Dialog(context, R.style.Dialog);
		final ViewUtils viewUtils = new ViewUtils(context, R.layout.dialog_choose_agentuser);
		final WheelView wv_agentUserOrPickupBySelf=viewUtils.view(R.id.wv_agentUser,WheelView.class);
		//代派员list
		wv_agentUserOrPickupBySelf.setVisibleItems(5);
		wv_agentUserOrPickupBySelf.TEXT_SIZE = DensityUtils.dp2px(context, 15);
		final AgentUserWheelAdapter agentUserAdapter=new AgentUserWheelAdapter(agentUserList);
		wv_agentUserOrPickupBySelf.setAdapter(agentUserAdapter);
		wv_agentUserOrPickupBySelf.setCurrentItem(0);
		viewUtils.view(R.id.btn_dialog_cancel,Button.class).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				chooseAgentUserDialog.cancel();
			}
		});
		viewUtils.view(R.id.btn_dialog_ok,Button.class).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(viewUtils.view(R.id.rb_dialog_user,RadioButton.class).isChecked()){
					listener.chooseAgentUserOrPickupBySelfResult(agentUserList.get(wv_agentUserOrPickupBySelf.getCurrentItem()));
				}else{
					listener.chooseAgentUserOrPickupBySelfResult(PickUpBySelfList.get(wv_agentUserOrPickupBySelf.getCurrentItem()));
				}
				DialogUtils.hideChooseAgentUserDialog();
			}
		});
		chooseAgentUserDialog.setContentView(viewUtils.getRootView());
		chooseAgentUserDialog.setCanceledOnTouchOutside(false);
		Window window=chooseAgentUserDialog.getWindow();
		WindowManager.LayoutParams lp=window.getAttributes();
		int screenWid=ScreenUtils.getScreenWidth(context);
		window.setWindowAnimations(R.style.ActionSheetAnimation);
		lp.width=screenWid;
		window.setGravity(Gravity.BOTTOM);
		chooseAgentUserDialog.show();
		
		return viewUtils;
	}
	
	public static void hideChooseAgentUserDialog(){
		if(chooseAgentUserDialog!=null){
			chooseAgentUserDialog.dismiss();
			chooseAgentUserDialog.cancel();
		}
	}
	/**
	 * 弹出电话
	 * 
	 * @param context
	 * @param requesxxxtCode
	 */
	public static ViewUtils showTelephoneDialog(Context context, String text) {
		if (Constant.ISSCAN) {
			mSoundUtils = SoundUtils.getInstance();
			mSoundUtils.init(context);

		}
		dialog = new Dialog(context, R.style.Dialog);
		ViewUtils viewUtils = new ViewUtils(context,
				R.layout.dialog_changenickname);
		// View view = LayoutInflater.from(context).inflate(
		// R.layout.dialog_changenickname, null);
		if (Constant.ISSCAN) {
//			viewUtils.view(R.id.tv_dialog_rukutelephone_text, EditText.class).setInputType(InputType.TYPE_NULL);
			dialog.setOnKeyListener(onKeyListener);
		}
		dialog.setContentView(viewUtils.getRootView());

		viewUtils.setViewText(R.id.tv_rukutelephone_danhao, "单号：" + text);

		Window window = dialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		int screenW = ScreenUtils.getScreenWidth(context);

		lp.width = screenW - 60;
		
		dialog.show();
		return viewUtils;
	}
}
