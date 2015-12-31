package cn.zjl.zxing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;

import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioGroup.OnCheckedChangeListener;

import cn.yuri.wheelview.AgentUserWheelAdapter;
import cn.yuri.wheelview.PickupBySelfWheelAdapter;
import cn.yuri.wheelview.WheelView;

import com.common.adapter.ScanAdapter;
import com.common.adapter.ScanContactAdapter;
import com.common.adapter.ScanZhiLiuAdapter;
import com.common.base.BaseActivity;
import com.common.events.AutoRefreshEvent;
import com.common.events.ChangeContactPhoneEvent;
import com.common.events.ChangeMainTabByTagEvent;
import com.common.events.ChangeTabEvent;
import com.common.events.CloseEvent;
import com.common.events.ScanResultEvent;
import com.common.events.UpdateZhiLiuList;
import com.common.listener.ChooseAgentUserOrPickupBySelfListener;
import com.common.model.AgentCarrier;
import com.common.model.AgentContactCustomer;
import com.common.model.AgentReceiveTask;
import com.common.model.AgentUser;
import com.common.model.ApiCode;
import com.common.model.IntentExtra;
import com.common.model.MsMessage;
import com.common.model.PickUpBySelf;
import com.common.ui.Topbar.OnControlOneListener;
import com.common.ui.Topbar.OnOtherListener;
import com.common.utils.AppUtils;
import com.common.utils.CommonUtils;
import com.common.utils.Constant;
import com.common.utils.DialogUtils;
import com.common.utils.KeyBoardUtils;
import com.common.utils.SPUtils;
import com.common.utils.ScreenUtils;
import com.common.utils.SoundUtils;
import com.common.utils.T;
import com.common.utils.ViewUtils;
import com.gt.activity.CapturePDAActivity;
import com.gt.activity.CarrierBookActivity2;
import com.gt.activity.InputInfoDetailActivity;
import com.gt.activity.SendExpressDetailActivity;
import com.gt.activity.InLibPreviewActivity;
import com.gt.activity.ZhiLiuActivity;
import com.gt.officeagent.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;

/**
 * 扫描条码界面
 * 
 * @author zhangruntao
 * 
 */
public class CaptureActivity extends BaseActivity implements Callback,OnKeyListener {

	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
//	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private boolean vibrate;
	private Dialog dialog,dialogRuKu;

	private int type,rukuCounts; // 扫描类型，分为单个扫描和多个扫描,待入库的件数
	private ArrayList<String> results, qianshouResult,contacts,phones,paisongResult; // 多个扫描结果集

	private ScanAdapter scanAdapter = null;
	private ScanContactAdapter scanAdapter1 = null;
	// dialog入库使用
	private AgentCarrier agentCarrier;
	private ViewUtils vDialogUtils = null;

	private TelephonyManager telephonyManager;
	private AgentCarrier dbAgentCarrier;
	private List<AgentReceiveTask> listAgentReceiveTasks, qianshouList,paisongList;
	private String editPhone;
	private AgentReceiveTask artContact;
	private PopupWindow popupWindow;
	private String excepReason;
	private int pageNo=1;
	private AgentReceiveTask agentReceiveTask;
	private boolean isLoadMore;
	private List<AgentUser> agentUserList;
	private List<PickUpBySelf> pickUpBySelfList;//自提点list
	private AgentUser selectUser;
	private PickUpBySelf pickUpBySelf;
	private ScanZhiLiuAdapter zhiLiuAdapter;
	//改回楼宇的联系客户
	private ArrayList<AgentContactCustomer> list_contacts;
	private ArrayList<AgentContactCustomer> search_contacts;//用来存放联系客户中搜索结果
	private AgentContactCustomer agentContactCustomer;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
		super.onResume();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy() {
//		inactivityTimer.shutdown();
		super.onDestroy();
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats,
					characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();

	}
	/**
	 * 摄像头扫描结果处理
	 * @param obj
	 * @param barcode
	 * void
	 */
	public void handleDecode(final Result obj, Bitmap barcode) {
//		inactivityTimer.onActivity();
		if(dialogRuKu!=null&&dialogRuKu.isShowing()){
			T.showShort(this, "您有未入库的快件，请先进行入库操作！");
			return;
		}
		viewfinderView.drawResultBitmap(barcode);
		playBeepSoundAndVibrate();
		if(obj.getText()!=null){
			if(obj.getText().length()<6||obj.getText().contains("http")){
				T.showShort(this, "您的单号格式不正确！");
				continueScan();
				return;
			}
		switch (type) {
		case Constant.SCAN_KUAIJIANRUKU:

			viewUtils.setViewText(R.id.edt_scan_expresscode, obj.getText());
			if("韵达快递".equals(dbAgentCarrier.getParentCarrierName())
					&&obj.getText().length()>13){
				viewUtils.setViewText(R.id.edt_scan_expresscode, TextUtils.substring(obj.getText(), 0, 13));
			}
			for (int i = 0; i < results.size(); i++) {
				if (viewUtils.getViewText(R.id.edt_scan_expresscode).equals(results.get(i))) {
					T.showShort(CaptureActivity.this, "快递单号重复啦，换一个吧");
					viewfinderView.drawResultBitmap(null);
					handler.postDelayed(new Runnable() {
						@Override
						public void run() {

							if (handler != null) {
								handler.obtainMessage(R.id.restart_preview)
										.sendToTarget();
							}
						}
					}, 1000);
					return;
				}
			}
			continueScan();

			saveAgentReceiveTasks(null,null);
//			results.add(obj.getText());
			break;
		case Constant.SCAN_KEHUQIANSHOU:
			viewUtils.setViewText(R.id.edt_scan_expresscode, obj.getText());
			doRequest(Constant.queryReceiveTask);
			break;
		case Constant.SCAN_KEHUZITI:
			result(1, new IntentExtra(1, obj.getText()));
			finish();
			break;
		case Constant.SCAN_CONTACT:
			viewUtils.setViewText(R.id.edt_scan_expresscode, obj.getText());
			if (contacts.contains(viewUtils.getViewText(R.id.edt_scan_expresscode))) {// 已存在列表中
				search_contacts.clear();
				for(AgentContactCustomer acc:list_contacts){
					if(viewUtils.getViewText(R.id.edt_scan_expresscode).contains(acc.getExpressCode())
							||viewUtils.getViewText(R.id.edt_scan_expresscode).contains(acc.getReceiverPhone())){
						search_contacts.add(acc);
					}
				}
				scanAdapter1.updateAll(search_contacts);
			} else {// 不存在列表中，
				scanAdapter1.updateAll(list_contacts);
				doRequest(Constant.queryReceiveTask);
			}
			continueScan();
			break;
		case Constant.SCAN_NORMAL:
			result(1, new IntentExtra(1, obj.getText()));
			finish();
			break;
		case Constant.SCAN_KEHUQIANSHOU_MORE:// 批量签收
			agentCarrier=null;
			viewUtils.setViewText(R.id.edt_scan_expresscode, obj.getText());
			for(AgentReceiveTask art:qianshouList){
				if(obj.getText().contains(art.getExpressCode())&&"韵达快递".equals(art.getCarrierName())
						&&obj.getText().length()>13){
					viewUtils.setViewText(R.id.edt_scan_expresscode, TextUtils.substring(obj.getText(), 0, 13));
					break;
				}
			}
			if (qianshouResult.contains(viewUtils.getViewText(R.id.edt_scan_expresscode))) {
				for (int i = 0; i < results.size(); i++) {
					if (viewUtils.getViewText(R.id.edt_scan_expresscode).equals(results.get(i))) {
						T.showShort(CaptureActivity.this, "快递单号重复啦，换一个吧");
						viewfinderView.drawResultBitmap(null);
						handler.postDelayed(new Runnable() {
							@Override
							public void run() {

								if (handler != null) {
									handler.obtainMessage(R.id.restart_preview)
											.sendToTarget();
								}
							}
						}, 1000);
						return;
					}
				}
				saveAgentReceiveTasks(qianshouList,null);
				results.add(viewUtils.getViewText(R.id.edt_scan_expresscode));
				if(dialog!=null&&dialog.isShowing()){
					dialog.cancel();
				}
			} else {
				for (int i = 0; i < results.size(); i++) {
					if (viewUtils.getViewText(R.id.edt_scan_expresscode).equals(results.get(i))) {
						T.showShort(CaptureActivity.this, "快递单号重复啦，换一个吧");
						viewfinderView.drawResultBitmap(null);
						handler.postDelayed(new Runnable() {
							@Override
							public void run() {

								if (handler != null) {
									handler.obtainMessage(R.id.restart_preview)
											.sendToTarget();
								}
							}
						}, 1000);
						return;
					}
				}
				if (dialogRuKu == null || !dialogRuKu.isShowing()) {
					showRUKUDialog();// 入库
				}
			}
			
			continueScan();
			break;
		case Constant.SCAN_INPUTINFO:
			viewUtils.setViewText(R.id.edt_scan_expresscode, obj.getText());
			if (TextUtils.isEmpty(viewUtils
					.getViewText(R.id.edt_scan_expresscode))) {
				T.showShort(this, "快递单号不能为空！");
				return;
			}
			doRequest(Constant.queryReceiveTask);
			break;
		case Constant.SCAN_PAISONG:// 派送
			agentCarrier=null;
			if(dialogRuKu!=null&&dialogRuKu.isShowing()){
				dialogRuKu.cancel();
			}
			viewUtils.setViewText(R.id.edt_scan_expresscode, obj.getText());
			saveAgentReceiveTaskPaisong();
			for(AgentReceiveTask art:paisongList){
				if(obj.getText().contains(art.getExpressCode())&&"韵达快递".equals(art.getCarrierName())
						&&obj.getText().length()>13){
					viewUtils.setViewText(R.id.edt_scan_expresscode, TextUtils.substring(obj.getText(), 0, 13));
					break;
				}
			}
			if (paisongResult.contains(viewUtils.getViewText(R.id.edt_scan_expresscode))) {
				for (int i = 0; i < results.size(); i++) {
					if (viewUtils.getViewText(R.id.edt_scan_expresscode).equals(results.get(i))) {
						T.showShort(CaptureActivity.this, "快递单号重复啦，换一个吧");
						viewfinderView.drawResultBitmap(null);
						handler.postDelayed(new Runnable() {
							@Override
							public void run() {
								if (handler != null) {
									handler.obtainMessage(R.id.restart_preview)
											.sendToTarget();
								}
							}
						}, 1000);
						return;
					}
				}
				saveAgentReceiveTasks(paisongList,null);
			} else {
				for (int i = 0; i < results.size(); i++) {
					if (viewUtils.getViewText(R.id.edt_scan_expresscode).equals(results.get(i))) {
						T.showShort(CaptureActivity.this, "快递单号重复啦，换一个吧");
						viewfinderView.drawResultBitmap(null);
						handler.postDelayed(new Runnable() {
							@Override
							public void run() {
								if (handler != null) {
									handler.obtainMessage(R.id.restart_preview)
											.sendToTarget();
								}
							}
						}, 1000);
						return;
					}
				}
//					if (dialog == null || !dialog.isShowing()) {
				showRUKUDialog();// 入库
//					}
			}
			continueScan();
			break;
		case Constant.SCAN_ZHILIUJIANCHULI:
			viewUtils.setViewText(R.id.edt_scan_expresscode, obj.getText());
			doRequest(Constant.zljcx);
			continueScan();
			break;
		}
		}
	}

	/*
	 * 可以进行再次扫描
	 */
	private void continueScan() {
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				viewfinderView.drawResultBitmap(null);
				handler.postDelayed(new Runnable() {

					@Override
					public void run() {
						if (handler != null) {
							handler.obtainMessage(R.id.restart_preview)
									.sendToTarget();
						}
					}
				}, 1000);
			}
		}, 200);
	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			
//			  setVolumeControlStream(AudioManager.STREAM_MUSIC); 
//			  mediaPlayer =new MediaPlayer();
//			  mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//			  mediaPlayer.setOnCompletionListener(beepListener);
			  
//			  AssetFileDescriptor file = getResources().openRawResourceFd(
//			  R.raw.beep); 
//			  try {
//			  mediaPlayer.setDataSource(file.getFileDescriptor(),
//			  file.getStartOffset(), file.getLength()); file.close();
//			  mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
//			  mediaPlayer.prepare(); 
//			  } catch (IOException e) 
//			  { mediaPlayer =
//			  null; }
			 
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

	@Override
	public int bindLayoutId() {
		return R.layout.zxing_activity_scanner;
	}

	@Override
	public void initParmers() {
		getEventBus().register(this);
	}

	@Override
	public void initViewsAndValues(Bundle savedInstanceState) {
		CameraManager.init(getApplication());
		try {
			dbAgentCarrier = getDbUtils().findFirst(AgentCarrier.class);
			telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
			viewfinderView = viewUtils.view(R.id.viewfinder_view,
					ViewfinderView.class);
			hasSurface = false;
//			inactivityTimer = new InactivityTimer(this);

			results = new ArrayList<String>();
			listAgentReceiveTasks = new ArrayList<AgentReceiveTask>();
			type = intentExtra.getType();
			if (type == Constant.SCAN_KEHUQIANSHOU) {
				viewUtils.setViewText(R.id.tv_scan_tip,
						"温馨提示：\n1.请扫描(可通过PDA扫描)或输入要派送给用户的订单号\n2.扫描后将直接进入订单详情");
				viewUtils.setGone(R.id.lv_scan, R.id.ll_scan_bottom);
				viewUtils.setViewText(R.id.btn_scan_expresscodeok, "确定");
				getTopbar().setTitleText("客户签收");
				getTopbar().setControlOneText("批量签收");
				viewUtils.setOnClickListener(R.id.btn_scan_expresscodeok);
				// 返回
				getTopbar().setOnOtherListener(new OnOtherListener() {

					@Override
					public void onOtherListener() {
						finish();
					}
				});
				getTopbar().setOnControlOneListener(new OnControlOneListener() {
					@Override
					public void onControlOneListener() {
						//看数据库里是否有未签收的
						long agentUserId=SPUtils.getUser(CaptureActivity.this).getAgentUserId();
						List<AgentReceiveTask> list;
						try {
							list = getDbUtils().findAll(Selector.from(AgentReceiveTask.class)
							        .where("type" ,"=", Constant.SCAN_KEHUQIANSHOU_MORE)
							        .and(WhereBuilder.b("agentUserId", "=", agentUserId)));
							if(list!=null&&list.size()>0){
								editPQDialog(list);
							}else {
								getTopbar().setControlOneText("");
								getTopbar().setTitleText("批量签收");
//								failedAgentReceiveTasks = new ArrayList<String>();
								qianshouList=new ArrayList<AgentReceiveTask>();
								qianshouResult = new ArrayList<String>();
								scanAdapter = new ScanAdapter(CaptureActivity.this,
										listAgentReceiveTasks, false,
										Constant.SCAN_KEHUQIANSHOU_MORE);
								viewUtils.view(R.id.lv_scan, ListView.class)
										.setAdapter(scanAdapter);
								type = Constant.SCAN_KEHUQIANSHOU_MORE;// 批量签收
								doRequest(Constant.queryReceiveTaskQianshouList);
							}
						} catch (DbException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				});
			} else if (type == Constant.SCAN_KEHUZITI) {
				viewUtils.setViewText(R.id.tv_scan_tip,
						"温馨提示：\n1.请扫描(可通过PDA扫描)或输入快递单号");
				// viewUtils.setGone(R.id.ll_scan_input);
				viewUtils.setGone(R.id.lv_scan, R.id.ll_scan_bottom,R.id.ll_scan_input);

				viewUtils.setGone(R.id.btn_scan_expresscodeok,
						R.id.btn_scan_result);
				// viewUtils
				// .setGone(R.id.btn_scan_expresscodeok, R.id.btn_scan_result);
				getTopbar().setTitleText("扫描");
				// 返回
				getTopbar().setOnOtherListener(new OnOtherListener() {

					@Override
					public void onOtherListener() {
						finish();
					}
				});
			} else if (type == Constant.SCAN_KUAIJIANRUKU) {
				viewUtils.setViewText(R.id.tv_scan_tip,
						"温馨提示：\n本功能为批量入库，请扫描(可通过PDA扫描)或输入添加快件，扫描完成后点击扫描完成进行预览");
				viewUtils.setVisiable(R.id.lv_scan, R.id.ll_scan_bottom);
				viewUtils.setViewText(R.id.btn_scan_expresscodeok, "添加");
				getTopbar().setTitleText(
						dbAgentCarrier != null ? dbAgentCarrier
								.getParentCarrierName() : "快件入库");
				List<AgentReceiveTask> list;
				list = getDbUtils().findAll(Selector.from(AgentReceiveTask.class)
	                    .where("type" ,"=", type));
				if (list != null && list.size() > 0) {
					listAgentReceiveTasks=list;
				}
				scanAdapter = new ScanAdapter(this, listAgentReceiveTasks,
						false, Constant.SCAN_KUAIJIANRUKU);
				getAgentReceiveTasks(listAgentReceiveTasks);
				viewUtils.view(R.id.lv_scan, ListView.class).setAdapter(
						scanAdapter);
				viewUtils.setOnClickListener(R.id.btn_scan_result,
						R.id.btn_scan_expresscodeok);

				// 返回
				getTopbar().setOnOtherListener(new OnOtherListener() {

					@Override
					public void onOtherListener() {
						editRUKUDialog();
					}
				});

			} else if (type == Constant.SCAN_CONTACT) {
				list_contacts = new ArrayList<AgentContactCustomer>();
				search_contacts = new ArrayList<AgentContactCustomer>();//用来用户搜索
				contacts=new ArrayList<String>();
				phones=new ArrayList<String>();
				viewUtils.setGone(R.id.tv_scan_bottom,R.id.btn_scan_result);
				viewUtils.setVisiable(R.id.ll_scan_bottom,R.id.ll_scan_constants,R.id.lv_scan);
				viewUtils.setViewText(R.id.tv_scan_tip, "客户列表");
				viewUtils.view(R.id.tv_scan_tip, TextView.class).setTextColor(
						Color.BLACK);
				viewUtils.setViewText(R.id.btn_scan_expresscodeok, "搜索");
				viewUtils.setOnClickListener(R.id.btn_scan_expresscodeok,R.id.iv_expressCodeclear);
				getTopbar().setTitleText("联系客户");
				doRequest(Constant.queryCustomer);// 获得联系客户列表
				getTopbar().setOnOtherListener(new OnOtherListener() {

					@Override
					public void onOtherListener() {
						finish();
					}
				});
				viewUtils.view(R.id.edt_scan_expresscode,EditText.class).setHint("单号或手机末尾4位");
				viewUtils.view(R.id.edt_scan_expresscode,EditText.class).addTextChangedListener(new TextWatcher() {
					
					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {
						if(s.length()==0){
							viewUtils.setGone(R.id.iv_expressCodeclear);
							scanAdapter1.updateAll(list_contacts);
						}else{
							viewUtils.setVisiable(R.id.iv_expressCodeclear);
//							viewUtils.setOnClickListener(R.id.iv_expressCodeclear);
						}
					}
					
					@Override
					public void beforeTextChanged(CharSequence s, int start, int count,
							int after) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void afterTextChanged(Editable s) {
						// TODO Auto-generated method stub
						
					}
				});
				scanAdapter1 = new ScanContactAdapter(this, list_contacts);
				viewUtils.view(R.id.lv_scan, ListView.class).setAdapter(
						scanAdapter1);
			} else if (type == Constant.SCAN_NORMAL) {// 从揽收详情进入/派送列表
				viewUtils.setViewText(R.id.tv_scan_tip,
						"温馨提示：\n1.请扫描(可通过PDA扫描)或输入快递单号");
				viewUtils.setGone(R.id.ll_scan_input);
				viewUtils.setGone(R.id.lv_scan, R.id.ll_scan_bottom);

				viewUtils.setGone(R.id.btn_scan_expresscodeok,
						R.id.btn_scan_result);
				getTopbar().setTitleText("扫描");
				// 返回
				getTopbar().setOnOtherListener(new OnOtherListener() {

					@Override
					public void onOtherListener() {
						finish();
					}
				});
			}else if (type == Constant.SCAN_INPUTINFO) {//录入信息
				viewUtils.setViewText(R.id.tv_scan_tip,
						"温馨提示：\n1.请扫描(可通过PDA扫描)或输入快递单号");
				viewUtils.setGone(R.id.lv_scan, R.id.tv_scan_bottom);
				viewUtils.setGone(R.id.btn_scan_expresscodeok);
				viewUtils.setViewText(R.id.btn_scan_result, "调出订单");
				viewUtils.setOnClickListener(R.id.btn_scan_result);
				getTopbar().setTitleText("录入信息");
				// 返回
				getTopbar().setOnOtherListener(new OnOtherListener() {

					@Override
					public void onOtherListener() {
						finish();
					}
				});
			}else if(type==Constant.SCAN_PAISONG){//派送任务
				getTopbar().setTitleText("派送任务");
				// 返回
				getTopbar().setOnOtherListener(new OnOtherListener() {

					@Override
					public void onOtherListener() {
						finish();
					}
				});
				viewUtils.setGone(R.id.btn_scan_result);
				viewUtils.setVisiable(R.id.ll_scan_paisong,R.id.tv_scan_ruku_count);
				viewUtils.setViewText(R.id.tv_scan_tip,"准备完毕，请开始扫描");
				viewUtils.setViewText(R.id.tv_scan_ruku_count,"待入库"+rukuCounts+"单");
				viewUtils.view(R.id.tv_scan_tip, TextView.class).setTextColor(Color.BLACK);
				viewUtils.setViewText(R.id.btn_scan_expresscodeok, "添加");
				viewUtils.setOnClickListener(R.id.btn_scan_expresscodeok,R.id.ll_scan_person,R.id.btn_scan_fenpei);
				scanAdapter = new ScanAdapter(CaptureActivity.this,listAgentReceiveTasks, false,Constant.SCAN_PAISONG);
				viewUtils.view(R.id.lv_scan, ListView.class).setAdapter(scanAdapter);
				List<AgentReceiveTask> list;
				try {
					list = getDbUtils().findAll(Selector.from(AgentReceiveTask.class)
											.where("type","=",type));
					if (list != null && list.size() > 0) {
						editPQDialog(list);
					} else {
//						failedAgentReceiveTasks = new ArrayList<String>();
						paisongResult = new ArrayList<String>();
						paisongList = new ArrayList<AgentReceiveTask>();
						scanAdapter = new ScanAdapter(
								CaptureActivity.this,
								listAgentReceiveTasks, false,
								Constant.SCAN_PAISONG);
						viewUtils.view(R.id.lv_scan, ListView.class)
								.setAdapter(scanAdapter);
						doRequest(Constant.queryReceiveTaskPaiSongList);
					}
				} catch (DbException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else if (type == Constant.SCAN_ZHILIUJIANCHULI) {
				viewUtils.view(R.id.edt_scan_expresscode, EditText.class)
				.setFocusable(true);
		viewUtils.view(R.id.edt_scan_expresscode, EditText.class)
				.setFocusableInTouchMode(true);
		viewUtils.view(R.id.edt_scan_expresscode, EditText.class)
				.requestFocus();
		viewUtils.setViewText(R.id.tv_scan_tip,
				"温馨提示：\n1.请扫描或输入要处理的滞留件单号或手机号后4位\n2.扫描后将选择或直接进入滞留件处理详情");
		viewUtils.view(R.id.edt_scan_expresscode, EditText.class).setHint(
				"输入快递单号或手机号后4位查找快件");
		viewUtils.setGone(R.id.btn_scan_result);
		viewUtils.setViewText(R.id.btn_scan_expresscodeok, "确定");
		getTopbar().setTitleText("联系客户");
		viewUtils.setOnClickListener(R.id.btn_scan_expresscodeok);
		// 返回
		getTopbar().setOnOtherListener(new OnOtherListener() {

			@Override
			public void onOtherListener() {
				finish();
			}
		});
	}
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void getAgentReceiveTasks(List<AgentReceiveTask> list) {
		for (AgentReceiveTask agentReceiveTask : list) {
			results.add(agentReceiveTask.getExpressCode());
		}
		viewUtils.view(R.id.btn_scan_result, Button.class).setText(
				"扫描完成(" + list.size() + ")");
	}

	@Override
	public void releaseOnDestory() {
		getEventBus().unregister(this);
		SoundUtils.getInstance().release();
	}

	@Override
	public void onClickable(View view) {

		switch (view.getId()) {
		case R.id.btn_scan_result:
			switch (type) {
			case Constant.SCAN_KEHUQIANSHOU_MORE:
				if (listAgentReceiveTasks.size() > 0) {
				doRequest(Constant.editReceiveTaskQianshouList);
				}else{
					T.showShort(this, "请扫描快件！");
				}
				break;
//			case Constant.SCAN_CONTACT:
//				doRequest(Constant.queryContactsList);
//				break;
			case Constant.SCAN_INPUTINFO:
				if (TextUtils.isEmpty(viewUtils
						.getViewText(R.id.edt_scan_expresscode))) {
					T.showShort(this, "快递单号不能为空！");
					return;
				}
				doRequest(Constant.queryReceiveTask);
				break;
			default:
				if (listAgentReceiveTasks.size() > 0) {
					intent(InLibPreviewActivity.class, null);
				}else{
					T.showShort(this, "请扫描快件！");
				}
				break;
			}
			break;
		case R.id.btn_scan_expresscodeok:
//			if (TextUtils.isEmpty(viewUtils
//					.getViewText(R.id.edt_scan_expresscode))) {
//				T.showShort(this, "快递单号不能为空！");
//				return;
//			}else if(viewUtils
//					.getViewText(R.id.edt_scan_expresscode).length()<6||viewUtils
//					.getViewText(R.id.edt_scan_expresscode).contains("http")){
//				T.showShort(this, "您的单号格式不正确！");
//				return;
//			}
			if (type == Constant.SCAN_KEHUQIANSHOU_MORE) {// 批量签收
//				agentCarrier=null;
				if (TextUtils.isEmpty(viewUtils
						.getViewText(R.id.edt_scan_expresscode))) {
					T.showShort(this, "快递单号不能为空！");
					return;
				}else if(viewUtils
						.getViewText(R.id.edt_scan_expresscode).length()<6||viewUtils
						.getViewText(R.id.edt_scan_expresscode).contains("http")){
					T.showShort(this, "您的单号格式不正确！");
					return;
				}
				for(AgentReceiveTask art:qianshouList){
					if(viewUtils.getViewText(R.id.edt_scan_expresscode).contains(art.getExpressCode())&&"韵达快递".equals(art.getCarrierName())
							&&viewUtils.getViewText(R.id.edt_scan_expresscode).length()>13){
						viewUtils.setViewText(R.id.edt_scan_expresscode, TextUtils.substring(viewUtils.getViewText(R.id.edt_scan_expresscode), 0, 13));
						break;
					}
				}
				if (qianshouResult.contains(viewUtils
						.getViewText(R.id.edt_scan_expresscode))) {
					for (int i = 0; i < results.size(); i++) {
						if (viewUtils.getViewText(R.id.edt_scan_expresscode)
								.equals(results.get(i))) {
							T.showShort(CaptureActivity.this, "快递单号重复啦，换一个吧");
							return;
						}
					}
					saveAgentReceiveTasks(qianshouList,null);
					results.add(viewUtils
							.getViewText(R.id.edt_scan_expresscode));
					if(dialog!=null&&dialog.isShowing()){
						dialog.cancel();
					}
				} else {
					for (int i = 0; i < results.size(); i++) {
						if (viewUtils.getViewText(R.id.edt_scan_expresscode)
								.equals(results.get(i))) {
							T.showShort(CaptureActivity.this, "快递单号重复啦，换一个吧");
							return;
						}
					}
					if (dialog == null || !dialog.isShowing()) {
					showRUKUDialog();// 入库
					}
				}
			} else if(type == Constant.SCAN_PAISONG){
				agentCarrier=null;
				if (TextUtils.isEmpty(viewUtils
						.getViewText(R.id.edt_scan_expresscode))) {
					T.showShort(this, "快递单号不能为空！");
					return;
				}else if(viewUtils
						.getViewText(R.id.edt_scan_expresscode).length()<6||viewUtils
						.getViewText(R.id.edt_scan_expresscode).contains("http")){
					T.showShort(this, "您的单号格式不正确！");
					return;
				}
				saveAgentReceiveTaskPaisong();
				for(AgentReceiveTask art:paisongList){
					if(viewUtils.getViewText(R.id.edt_scan_expresscode).contains(art.getExpressCode())&&"韵达快递".equals(art.getCarrierName())
							&&viewUtils.getViewText(R.id.edt_scan_expresscode).length()>13){
						viewUtils.setViewText(R.id.edt_scan_expresscode, TextUtils.substring(viewUtils.getViewText(R.id.edt_scan_expresscode), 0, 13));
						break;
					}
				}
				if (paisongResult.contains(viewUtils
						.getViewText(R.id.edt_scan_expresscode))) {
					for (int i = 0; i < results.size(); i++) {
						if (viewUtils.getViewText(R.id.edt_scan_expresscode)
								.equals(results.get(i))) {
							T.showShort(this, "快递单号重复啦，换一个吧");
							return;
						}
					}
					saveAgentReceiveTasks(paisongList,null);
				} else {
					for (int i = 0; i < results.size(); i++) {
						if (viewUtils.getViewText(R.id.edt_scan_expresscode).equals(results.get(i))) {
							T.showShort(CaptureActivity.this, "快递单号重复啦，换一个吧");
							return;
						}
					}
					showRUKUDialog();// 入库
				}
			}else {
				if (viewUtils.getViewText(R.id.btn_scan_expresscodeok).equals(
						"确定")) {
					if (TextUtils.isEmpty(viewUtils
							.getViewText(R.id.edt_scan_expresscode))) {
						T.showShort(this, "快递单号不能为空！");
						return;
					}else if(viewUtils
							.getViewText(R.id.edt_scan_expresscode).length()<6||viewUtils
							.getViewText(R.id.edt_scan_expresscode).contains("http")){
						T.showShort(this, "您的单号格式不正确！");
						return;
					}
					doRequest(Constant.queryReceiveTask);
				} else if (viewUtils.getViewText(R.id.btn_scan_expresscodeok)
						.equals("搜索")) {
					if (TextUtils.isEmpty(viewUtils
							.getViewText(R.id.edt_scan_expresscode))) {
						T.showShort(this, "快递单号不能为空！");
						return;
					}else if(viewUtils
							.getViewText(R.id.edt_scan_expresscode).contains("http")){
						T.showShort(this, "您的单号格式不正确！");
						return;
					}
					search_contacts.clear();
					if(viewUtils.getViewText(R.id.edt_scan_expresscode).length()==4){//按照手机号后四位搜索
						for(AgentContactCustomer acc:list_contacts){
							if(viewUtils.getViewText(R.id.edt_scan_expresscode).equals(acc.getReceiverPhone().length()>=4?TextUtils.substring(acc.getReceiverPhone(), acc.getReceiverPhone().length()-4, acc.getReceiverPhone().length()):acc.getReceiverPhone())){
								search_contacts.add(acc);
							}
						}
						scanAdapter1.updateAll(search_contacts);
					}else if(contacts.contains(viewUtils.getViewText(R.id.edt_scan_expresscode))
							||phones.contains(viewUtils.getViewText(R.id.edt_scan_expresscode))){//已存在列表中
						search_contacts.clear();
						for(AgentContactCustomer acc:list_contacts){
							if(viewUtils.getViewText(R.id.edt_scan_expresscode).contains(acc.getExpressCode())
									||viewUtils.getViewText(R.id.edt_scan_expresscode).contains(acc.getReceiverPhone())){
								search_contacts.add(acc);
							}
						}
						scanAdapter1.updateAll(search_contacts);
					}else{//不存在列表中，
						scanAdapter1.updateAll(list_contacts);
						if (viewUtils.getViewText(R.id.edt_scan_expresscode)
								.length() < 6
								|| viewUtils.getViewText(R.id.edt_scan_expresscode)
										.contains("http")) {
							T.showShort(this, "您的单号格式不正确！");
							return;
						}
						doRequest(Constant.queryReceiveTask);
					}
				}else {//批量入库
					if (TextUtils.isEmpty(viewUtils
							.getViewText(R.id.edt_scan_expresscode))) {
						T.showShort(this, "快递单号不能为空！");
						return;
					}else if(viewUtils
							.getViewText(R.id.edt_scan_expresscode).length()<6||viewUtils
							.getViewText(R.id.edt_scan_expresscode).contains("http")){
						T.showShort(this, "您的单号格式不正确！");
						return;
					}
					if("韵达快递".equals(dbAgentCarrier.getParentCarrierName())
							&&viewUtils.getViewText(R.id.edt_scan_expresscode).length()>13){
						viewUtils.setViewText(R.id.edt_scan_expresscode, TextUtils.substring(viewUtils.getViewText(R.id.edt_scan_expresscode), 0, 13));
					}
					for (int i = 0; i < results.size(); i++) {
						if (viewUtils.getViewText(R.id.edt_scan_expresscode)
								.equals(results.get(i))) {
							T.showShort(CaptureActivity.this, "快递单号重复啦，换一个吧");
							return;
						}
					}

					saveAgentReceiveTasks(null,null);
				}
			}
			break;
		case R.id.iv_expressCodeclear:
			viewUtils.setViewText(R.id.edt_scan_expresscode, "");
//			scanAdapter1.updateAll(list_contacts);
			break;
		case R.id.ll_dialog_ruku_queren:
			if (agentReceiveTask!=null&&vDialogUtils!=null) {
				agentReceiveTask.setReceiverPhone(vDialogUtils.getViewText(R.id.tv_dialog_rukutelephone_text));
				try {
					getDbUtils().save(agentReceiveTask);
					dialog.cancel();
				} catch (DbException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				listAgentReceiveTasks.add(agentReceiveTask);
				scanAdapter.updateAll(listAgentReceiveTasks);
				results.add(agentReceiveTask.getExpressCode());
				if(type==Constant.SCAN_KUAIJIANRUKU){
					viewUtils.view(R.id.lv_scan, ListView.class).setSelection(listAgentReceiveTasks.size() - 1);
					viewUtils.view(R.id.btn_scan_result, Button.class)
					.setText("批量入库("+ listAgentReceiveTasks.size() + ")");
				}else{
					viewUtils.view(R.id.lv_scan, ListView.class).setSelection(0);
					viewUtils.setViewText(R.id.tv_scan_tip, "已扫描"+listAgentReceiveTasks.size()+"单");
					viewUtils.setViewText(R.id.btn_scan_fenpei,"分配("+ listAgentReceiveTasks.size() + ")");
				}
				agentReceiveTask=null;
				vDialogUtils=null;
				}
			break;
		case R.id.ll_scan_person:
			if((agentUserList!=null&&agentUserList.size()>0)||(pickUpBySelfList!=null&&pickUpBySelfList.size()>0)){
//				initPopWindow();
				chooseAgentUser();
			}else{
				doRequest(Constant.queryAgentUsers);
			}
			break;
		case R.id.btn_scan_fenpei://分配
			if(selectUser==null&&pickUpBySelf==null){
				T.showShort(this, "您还未选择派送员/自提点！");
				return;
			}else if(listAgentReceiveTasks.size()==0){
				T.showShort(this, "没有待派发任务！");
				return;
			}else{
				//请求服务器
				doRequest(Constant.editReceiveTaskPaiSongList);
			}
			break;
		default:
			break;
		}

	}

	@Override
	public void doRequest(int requestCode, Object... needParams) {
		if(!isLoadMore){
			showLoadingDialog(requestCode);
		}

		switch (requestCode) {
		case Constant.queryReceiveTask:
			Gson gson = new Gson();
			Map<String, String> maps = new HashMap<String, String>();
			maps.put("agentId", SPUtils.getAgent(this).getAgentId() + "");
			maps.put("expressCode",
					viewUtils.getViewText(R.id.edt_scan_expresscode));
			String paramsValues = gson.toJson(maps);
			requestServer(Constant.RECEIVETASK_SERVER,
					Constant.queryReceiveTask, AppUtils.createParams(
							ApiCode.queryReceiveTask.code, paramsValues));
			break;
		case Constant.intake:
			
			Gson gson1 = new Gson();
			
			String expresscodes = gson1.toJson(needParams[0],
					new TypeToken<List<AgentReceiveTask>>() {
					}.getType());
			String paramsValues1 = expresscodes;
			requestServer(Constant.RECEIVETASK_SERVER, Constant.intake,
					AppUtils.createParams(ApiCode.intake.code, paramsValues1));
			break;

		case Constant.queryReceiveTaskQianshouList:
			Gson gson5 = new Gson();
			Map<String, String> maps5 = new HashMap<String, String>();
			maps5.put("agentId", SPUtils.getAgent(this).getAgentId() + "");
			String paramsValues5 = gson5.toJson(maps5);
			requestServer(Constant.RECEIVETASK_SERVER,
					Constant.queryReceiveTaskQianshouList,
					AppUtils.createParams(
							ApiCode.queryReceiveTaskQianshouList.code,
							paramsValues5));
			break;
		case Constant.editReceiveTaskQianshouList:
			Gson gson6 = new Gson();
			String paramsValues6 = gson6.toJson(listAgentReceiveTasks);
			requestServer(Constant.RECEIVETASK_SERVER,
					Constant.editReceiveTaskQianshouList,
					AppUtils.createParams(
							ApiCode.editReceiveTaskQianshouList.code,
							paramsValues6));
			break;
		case Constant.queryContactsList:
			Gson gson7 = new Gson();
			Map<String, String> maps7 = new HashMap<String, String>();
			maps7.put("agentId", SPUtils.getAgent(this).getAgentId() + "");
			maps7.put("pageNo", pageNo + "");
			maps7.put("pageSize", Constant.PAGE_SIZE+ "");
			String paramsValues7 = gson7.toJson(maps7);
			requestServer(Constant.RECEIVETASK_SERVER,
					Constant.queryContactsList, AppUtils.createParams(
							ApiCode.queryContactsList.code, paramsValues7));
			break;
		case Constant.editReceiveTask:
			Gson gson8 = new Gson();
			Map<String, String> maps8 = new HashMap<String, String>();
			maps8.put("agentId", agentContactCustomer.getAgentId() + "");
			maps8.put("taskId", agentContactCustomer.getTaskId() + "");
			maps8.put("receiverPhone", agentContactCustomer.getReceiverPhone());
			maps8.put("sendType", (agentContactCustomer.getSendType()==0?Constant.SENDTYPE_SHANGMENPAISONG:agentContactCustomer.getSendType()) + "");
			String paramsValues8 = gson8.toJson(maps8);
			requestServer(Constant.RECEIVETASK_SERVER,
					Constant.editReceiveTask, AppUtils.createParams(
							ApiCode.editReceiveTask.code, paramsValues8));
			break;
		case Constant.editReceiveTaskUnFinish:
			Gson gson9 = new Gson();
			Map<String, String> maps9 = new HashMap<String, String>();
			maps9.put("taskId", agentContactCustomer.getTaskId() + "");
			maps9.put("agentId", SPUtils.getAgent(this).getAgentId() + "");
			maps9.put("type", Constant.TASKSTATUS_INLIB + "");
			maps9.put("statusDesc", agentContactCustomer.getStatusDesc());
			maps9.put("carrierName", agentContactCustomer.getCarrierName());
			maps9.put("agentUserId", SPUtils.getUser(this).getAgentUserId()+ "");
			maps9.put("agentUserName", SPUtils.getUser(this).getAgentUserName());
			maps9.put("agentUserCode", SPUtils.getUser(this).getAgentUserCode());
			maps9.put("expressCode", agentContactCustomer.getExpressCode());
			maps9.put("carrierId", agentContactCustomer.getCarrierId() + "");
			maps9.put("receiverName", agentContactCustomer.getReceiverName());
			maps9.put("receiverPhone", agentContactCustomer.getReceiverPhone());
			maps9.put("receiverAddress", agentContactCustomer.getReceiverAddress());
			requestServer(Constant.RECEIVETASK_SERVER,
					Constant.editReceiveTaskUnFinish, AppUtils.createParams(
							ApiCode.editReceiveTaskUnFinish.code,
							gson9.toJson(maps9)));
			break;
		case Constant.queryReceiveTaskPaiSongList:
			Gson gson10 = new Gson();
			Map<String, String> maps10 = new HashMap<String, String>();
			maps10.put("agentId", SPUtils.getAgent(this).getAgentId() + "");
			String paramsValues10 = gson10.toJson(maps10);
			requestServer(Constant.RECEIVETASK_SERVER,
					Constant.queryReceiveTaskPaiSongList,
					AppUtils.createParams(
							ApiCode.queryReceiveTaskPaiSongList.code,
							paramsValues10));
			break;
		case Constant.queryAgentUsers://查询代办下所有代办员
			Gson gson11 = new Gson();
			Map<String, String> maps11 = new HashMap<String, String>();
			maps11.put("agentId", SPUtils.getAgent(this).getAgentId() + "");
			String paramsValues11 = gson11.toJson(maps11);
			requestServer(Constant.RECEIVETASK_SERVER,
					Constant.queryAgentUsers,
					AppUtils.createParams(
							ApiCode.queryAgentUsers.code,
							paramsValues11));
			break;
		case Constant.editReceiveTaskPaiSongList:
			Gson gson12 = new Gson();
			Map<String, String> maps12 = new HashMap<String, String>();
			if(selectUser!=null){
				maps12.put("agentUserId", selectUser.getAgentUserId() + "");
				maps12.put("agentUserCode", selectUser.getAgentUserCode() + "");
				maps12.put("agentUserName", selectUser.getAgentUserName() + "");
				maps12.put("PickUpBySelfId", "");
			}else if(pickUpBySelf!=null){
				maps12.put("agentUserId", SPUtils.getUser(this).getAgentUserId() + "");
				maps12.put("agentUserCode", SPUtils.getUser(this).getAgentUserCode() + "");
				maps12.put("agentUserName", SPUtils.getUser(this).getAgentUserName() + "");
				maps12.put("PickUpBySelfId",pickUpBySelf.getPickUpBySelfId()+"");
			}
			String paisongList = gson12.toJson(listAgentReceiveTasks);
			maps12.put("receiveTasks", paisongList);
			String paramsValues12=gson12.toJson(maps12);
			requestServer(Constant.RECEIVETASK_SERVER,
					Constant.editReceiveTaskPaiSongList,
					AppUtils.createParams(
							ApiCode.editReceiveTaskPaiSongList.code,
							paramsValues12));
			break;
		case Constant.zljcx:
			Gson gson13 = new Gson();
			Map<String, String> maps13 = new HashMap<String, String>();
			maps13.put("agentId", SPUtils.getAgent(this).getAgentId() + "");
			maps13.put("code", viewUtils.getViewText(R.id.edt_scan_expresscode));
			String paramsValues13 = gson13.toJson(maps13);
			requestServer(Constant.RECEIVETASK_SERVER, Constant.zljcx,
					AppUtils.createParams(ApiCode.queryZhiLiu.code,
							paramsValues13));
			break;
		case Constant.queryCustomer:// 此处展示服务器返回的列表
			Gson gson3 = new Gson();
			Map<String, String> maps3 = new HashMap<String, String>();
			maps3.put("agentId", SPUtils.getAgent(this).getAgentId() + "");
			maps3.put("agentUserId", SPUtils.getUser(this).getAgentUserId()
					+ "");

			String paramsValues3 = gson3.toJson(maps3);
			requestServer(Constant.RECEIVETASK_SERVER, Constant.queryCustomer,
					AppUtils.createParams(ApiCode.queryCustomer.code,
							paramsValues3));
			break;
		case Constant.addCustomer:
			Gson gson14 = new Gson();
			Map<String, String> maps14 = new HashMap<String, String>();
			maps14.put("taskId", agentContactCustomer.getTaskId()+"");
			maps14.put("agentUserId", SPUtils.getUser(this).getAgentUserId()+"");
			maps14.put("receiverName", agentContactCustomer.getReceiverName());
			maps14.put("receiverPhone", agentContactCustomer.getReceiverPhone());
			maps14.put("expressCode", agentContactCustomer.getExpressCode());
			maps14.put("carrierName", agentContactCustomer.getCarrierName());
			maps14.put("carrierId", agentContactCustomer.getCarrierId()+"");
			maps14.put("agentId", SPUtils.getAgent(this).getAgentId()+"");
			requestServer(Constant.RECEIVETASK_SERVER, Constant.addCustomer,
					AppUtils.createParams(ApiCode.addCustomer.code, gson14.toJson(maps14)));
			break;
		default:
			break;
		}

	}

	@Override
	public void requestCallBack(int requestCode, MsMessage msm,
			String failedReason) {
		hideLoadingDialog(requestCode);
		switch (requestCode) {
		case Constant.queryReceiveTask:
			try {
				if (msm.getResult() == Constant.ERROR_ONTINLIB) {// 未入库
					switch (type) {
					case Constant.SCAN_INPUTINFO:// 录入信息
						T.showShort(this, msm.getMessage());
						break;

					default:
						if (dialog == null || !dialog.isShowing()) {
							showRUKUDialog();
						}
						break;
					}
					handler.postDelayed(new Runnable() {

						@Override
						public void run() {
							viewfinderView.drawResultBitmap(null);
							handler.postDelayed(new Runnable() {

								@Override
								public void run() {
									if (handler != null) {
										handler.obtainMessage(R.id.restart_preview)
												.sendToTarget();
									}
								}
							}, 1000);
						}
					}, 200);
				} else if (msm.getResult() == 0) {// 成功
					if (dialog != null && dialog.isShowing()) {
						dialog.cancel();
					}
					final AgentReceiveTask agentReceiveTask = new Gson()
							.fromJson(msm.getData().toString(),
									AgentReceiveTask.class);
					switch (type) {
					case Constant.SCAN_CONTACT:
						if (TextUtils.isEmpty(agentReceiveTask
								.getReceiverPhone())) {
							// 弹出框，输入电话，拨打电话，请求服务器，将此单加入进去
							vDialogUtils = showEditCallDialog(this, "呼叫客户");
							vDialogUtils
									.setVisiable(R.id.tv_dialog_sendexpress);
							vDialogUtils
									.setViewText(
											R.id.tv_dialog_sendexpress,
											agentReceiveTask.getCarrierName()
													+ "    "
													+ agentReceiveTask
															.getExpressCode());
							vDialogUtils.view(R.id.btn_dialog_call)
									.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											if (TextUtils.isEmpty(vDialogUtils
													.getViewText(R.id.edt_dialog))) {
												T.showShort(
														CaptureActivity.this,
														"请输入联系方式");
											} else {
												if (telephonyManager
														.getSimState() == TelephonyManager.SIM_STATE_ABSENT) {
													T.showShort(
															CaptureActivity.this,
															"无SIM卡！");
												} else {
													CommonUtils
															.call(CaptureActivity.this,
																	vDialogUtils
																			.getViewText(R.id.edt_dialog));
													// 并请求服务器，将其加入到列表中
													agentContactCustomer = new AgentContactCustomer();
													agentContactCustomer.setTaskId(agentReceiveTask.getTaskId());
													agentContactCustomer.setReceiverAddress(agentReceiveTask.getReceiverAddress());
													agentContactCustomer
															.setReceiverName(agentReceiveTask
																	.getReceiverName());
													agentContactCustomer
															.setReceiverPhone(vDialogUtils
																	.getViewText(R.id.edt_dialog));
													agentContactCustomer
															.setExpressCode(agentReceiveTask
																	.getExpressCode());
													agentContactCustomer
															.setCarrierName(agentReceiveTask
																	.getCarrierName());
													agentContactCustomer
															.setCarrierId(agentReceiveTask
																	.getCarrierId());

													doRequest(Constant.addCustomer);
													dialog.cancel();
												}
											}
										}
									});
						} else {
							// 弹出框，拨打电话
							vDialogUtils = DialogUtils
									.showConfirmDialog(this, "呼叫用户",
											agentReceiveTask.getReceiverPhone());
							vDialogUtils
									.setVisiable(R.id.tv_dialog_sendexpress);
							vDialogUtils
									.setViewText(
											R.id.tv_dialog_sendexpress,
											agentReceiveTask.getCarrierName()
													+ "    "
													+ agentReceiveTask
															.getExpressCode());
							vDialogUtils.view(R.id.btn_dialog_ok)
									.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											if (telephonyManager
													.getSimState() == TelephonyManager.SIM_STATE_ABSENT) {
												T.showShort(
														CaptureActivity.this,
														"无SIM卡！");
											} else {
											CommonUtils
													.call(CaptureActivity.this,
															agentReceiveTask
																	.getReceiverPhone());
											// 并请求服务器，将其加入到列表中
											agentContactCustomer = new AgentContactCustomer();
											agentContactCustomer.setTaskId(agentReceiveTask.getTaskId());
											agentContactCustomer.setReceiverAddress(agentReceiveTask.getReceiverAddress());
											agentContactCustomer
													.setReceiverName(agentReceiveTask
															.getReceiverName());
											agentContactCustomer
													.setReceiverPhone(agentReceiveTask
															.getReceiverPhone());
											agentContactCustomer
													.setExpressCode(agentReceiveTask
															.getExpressCode());
											agentContactCustomer
													.setCarrierName(agentReceiveTask
															.getCarrierName());
											agentContactCustomer
													.setCarrierId(agentReceiveTask
															.getCarrierId());
											doRequest(Constant.addCustomer);
											DialogUtils.cancelDialog();
										}
										}
									});
						}
						break;
					case Constant.SCAN_KEHUQIANSHOU:
						intent(SendExpressDetailActivity.class,
								new IntentExtra(Constant.SCAN_KEHUQIANSHOU,
										agentReceiveTask));
						finish();
						break;
					case Constant.SCAN_INPUTINFO:
						intent(InputInfoDetailActivity.class, new IntentExtra(
								0, agentReceiveTask));
						finish();
						break;
					}
				} else {// 测试用 2--已经签收
					if (dialog != null && dialog.isShowing()) {
						dialog.cancel();
					}
					handler.postDelayed(new Runnable() {

						@Override
						public void run() {
							viewfinderView.drawResultBitmap(null);
							handler.postDelayed(new Runnable() {

								@Override
								public void run() {
									if (handler != null) {
										handler.obtainMessage(R.id.restart_preview)
												.sendToTarget();
									}
								}
							}, 1000);
						}
					}, 200);
					T.showLong(this, msm.getMessage());
				}
			} catch (Exception e) {
				T.showShort(this, "获得个人数据异常！");
			}
			break;

		case Constant.intake:
//			T.showShort(this, msm.getMessage());
			try {
				if (msm.getResult() == 0) {// 成功
					if (dialog != null && dialog.isShowing()) {
						dialog.cancel();
					}
					if(dialogRuKu!=null&&dialogRuKu.isShowing()){
						dialogRuKu.cancel();
					}
					agentCarrier=null;
					doRequest(Constant.queryReceiveTask);
				}else{
					T.showShort(this, "入库失败！");
				}
				handler.postDelayed(new Runnable() {

					@Override
					public void run() {
						viewfinderView.drawResultBitmap(null);
						handler.postDelayed(new Runnable() {

							@Override
							public void run() {
								if (handler != null) {
									handler.obtainMessage(R.id.restart_preview)
											.sendToTarget();
								}
							}
						}, 1000);
					}
				}, 200);
			} catch (Exception e) {
				T.showShort(this, "获得个人数据异常！");
			}
			break;
		case Constant.queryReceiveTaskQianshouList:
			try {
				if (msm.getResult() == 0) {
					handler.postDelayed(new Runnable() {

						@Override
						public void run() {
							viewfinderView.drawResultBitmap(null);
							handler.postDelayed(new Runnable() {

								@Override
								public void run() {
									if (handler != null) {
										handler.obtainMessage(R.id.restart_preview)
												.sendToTarget();
									}
								}
							}, 1000);
						}
					}, 200);
					qianshouList = new Gson().fromJson(
							msm.getData().toString(),
							new TypeToken<List<AgentReceiveTask>>() {
							}.getType());
					qianshouResult.removeAll(qianshouResult);
					if (qianshouList != null && qianshouList.size() > 0) {
						for (AgentReceiveTask art : qianshouList) {
							qianshouResult.add(art.getExpressCode());
						}
					}
					viewUtils.setViewText(R.id.btn_scan_result, "确认批量签收");
					viewUtils.setVisiable(R.id.tv_scan_ruku_count,
							R.id.lv_scan, R.id.ll_scan_bottom);
					viewUtils.view(R.id.tv_scan_tip, TextView.class)
							.setTextColor(Color.BLACK);
					viewUtils.setViewText(R.id.tv_scan_tip, "准备完毕，请开始扫描");
					viewUtils.setViewText(R.id.tv_scan_ruku_count, "待入库"+rukuCounts+"单");
					viewUtils.setOnClickListener(R.id.btn_scan_result);
					// 看数据库里是否有未签收的
					long agentUserId = SPUtils.getUser(this).getAgentUserId();
					List<AgentReceiveTask> list = getDbUtils().findAll(
							Selector.from(AgentReceiveTask.class)
									.where("type", "=", type)
									.and(WhereBuilder.b("agentUserId", "=",
											agentUserId)));
					if (list != null && list.size() > 0) {
						listAgentReceiveTasks = list;

						for (AgentReceiveTask art : listAgentReceiveTasks) {
							results.add(art.getExpressCode());
							if(!qianshouResult.contains(art.getExpressCode())){
								qianshouResult.add(art.getExpressCode());
							}
							if(!TextUtils.isEmpty(art.getCarrierName())){
								rukuCounts++;
							}
						}
						scanAdapter.updateAll(listAgentReceiveTasks);
						viewUtils.view(R.id.lv_scan, ListView.class)
								.setSelection(listAgentReceiveTasks.size() - 1);
						viewUtils.view(R.id.tv_scan_tip, TextView.class).setText(Html.fromHtml("已扫描    "+"<font color='red'>"+listAgentReceiveTasks.size()+"</font>"+"    单"));
						viewUtils.view(R.id.tv_scan_ruku_count, TextView.class).setText(Html.fromHtml("待入库    "+"<font color='red'>"+rukuCounts+"</font>"+"    单"));
					}
				} else {
					T.showShort(this, msm.getMessage());
					// 下载失败请重试
					vDialogUtils = DialogUtils.showRetryDialog(this,this);
					vDialogUtils.view(R.id.btn_dialogretry_data)
							.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									doRequest(Constant.queryReceiveTaskQianshouList);
									DialogUtils.cancelDialog();
								}
							});
				}
			} catch (Exception e) {
				T.showShort(this, "获得个人数据异常");
			}
			break;
		case Constant.editReceiveTaskQianshouList:
			if (msm.getResult() == 0) {// 成功
				DialogUtils.showTipDialog(this,0,"签收成功！",Constant.SCAN_KEHUQIANSHOU_MORE)
						.view(R.id.btn_dialog_ok)
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								// 跳到失败快件预览页面
								long agentUserId = SPUtils.getUser(CaptureActivity.this).getAgentUserId();
								List<AgentReceiveTask> list;
								try {
									list = getDbUtils().findAll(Selector.from(AgentReceiveTask.class)
													.where("type", "=", type)
													.and(WhereBuilder.b("agentUserId", "=",agentUserId)));
									getDbUtils().deleteAll(list);
								} catch (DbException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								getEventBus().post(new AutoRefreshEvent());
								CaptureActivity.this.finish();
							}
						});
			}
			T.showShort(this, msm.getMessage());
			break;
		case Constant.queryReceiveTaskPaiSongList:
			try {
				DialogUtils.cancelDialog();
				if (msm.getResult() == 0) {

					paisongList = new Gson().fromJson(
							msm.getData().toString(),
							new TypeToken<List<AgentReceiveTask>>() {
							}.getType());
					paisongResult.removeAll(paisongResult);
					if (paisongList != null && paisongList.size() > 0) {
						for (AgentReceiveTask art : paisongList) {
							paisongResult.add(art.getExpressCode());
						}
					}
					// 看数据库里是否有未签收的
					List<AgentReceiveTask> list = getDbUtils().findAll(
							Selector.from(AgentReceiveTask.class)
									.where("type", "=", type));
					if (list != null && list.size() > 0) {
						listAgentReceiveTasks = list;

						for (AgentReceiveTask art : listAgentReceiveTasks) {
							results.add(art.getExpressCode());
							if(!TextUtils.isEmpty(art.getCarrierName())){
								rukuCounts++;
							}
						}
						scanAdapter.updateAll(listAgentReceiveTasks);
						viewUtils.view(R.id.lv_scan, ListView.class)
								.setSelection(listAgentReceiveTasks.size() - 1);
						viewUtils.setViewText(R.id.btn_scan_fenpei, "分配（"
								+ listAgentReceiveTasks.size() + "）");
						viewUtils.view(R.id.tv_scan_tip, TextView.class).setText(Html.fromHtml("已扫描    "+"<font color='red'>"+listAgentReceiveTasks.size()+"</font>"+"    单"));
						viewUtils.view(R.id.tv_scan_ruku_count, TextView.class).setText(Html.fromHtml("待入库    "+"<font color='red'>"+rukuCounts+"</font>"+"    单"));
					}
				} else {
					T.showShort(this, msm.getMessage());
					// 下载失败请重试
					vDialogUtils = DialogUtils.showRetryDialog(this,this);
					vDialogUtils.view(R.id.btn_dialogretry_data)
							.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									doRequest(Constant.queryReceiveTaskPaiSongList);
									DialogUtils.cancelDialog();
								}
							});
				}
			} catch (Exception e) {
				T.showShort(this, "获得个人数据异常");
			}
			break;
		case Constant.queryAgentUsers://查询代办点下所有代办员
			if (msm.getResult() == 0) {
				try {
					JSONArray arr = new JSONArray(msm.getData().toString());
					JSONObject obj1=arr.getJSONObject(0);
					JSONObject obj2=arr.getJSONObject(1);
					JSONArray arrayZiTi=obj1.getJSONArray("PickUpBySelf");
					JSONArray arrayAgentUser=obj2.getJSONArray("AgentUser");
					pickUpBySelfList=new Gson().fromJson(arrayZiTi.toString(), new TypeToken<List<PickUpBySelf>>(){}.getType());
					agentUserList=new Gson().fromJson(arrayAgentUser.toString(), new TypeToken<List<AgentUser>>(){}.getType());
					if((agentUserList!=null&&agentUserList.size()>0)||(pickUpBySelfList!=null&&pickUpBySelfList.size()>0)){
						chooseAgentUser();
					}else{
						T.showShort(this, "该代办点下还没有代办员/自提点！");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				T.showShort(this, msm.getMessage());
			}
			break;
		case Constant.editReceiveTaskPaiSongList:
			if (msm.getResult() == 0) {// 成功
				List<AgentReceiveTask> list;
				try {
					list = getDbUtils().findAll(Selector.from(AgentReceiveTask.class)
									.where("type", "=", type));
					getDbUtils().deleteAll(list);
				} catch (DbException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				vDialogUtils=DialogUtils.showTipDialog(this,0,"分配成功！",Constant.SCAN_PAISONG);
				vDialogUtils.view(R.id.btn_dialog_ok).setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								
								getEventBus().post(new AutoRefreshEvent());
								DialogUtils.HideTipDialog();
								CaptureActivity.this.finish();
							}
						});
				vDialogUtils.view(R.id.btn_dialog_paisong).setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						getEventBus().post(new AutoRefreshEvent());
						DialogUtils.HideTipDialog();
						getEventBus().post(new ChangeMainTabByTagEvent(Constant.TAB_SEND));
						getEventBus().post(new ChangeTabEvent(Constant.TAB_SEND_SHEDING));
						CaptureActivity.this.finish();
					}
				});
			}
			T.showShort(this, msm.getMessage());
			break;
		case Constant.zljcx:
			try {
				if (msm.getResult() == Constant.ERROR_ONTINLIB) {// 未入库
					if (viewUtils.getViewText(R.id.edt_scan_expresscode)
							.length() != 4) {
						if(dialogRuKu==null||!dialogRuKu.isShowing()){
						showRUKUDialog();// 入库dialog
						}
					}
				} else if (msm.getResult() == 0) {// 成功
//					List list = new ArrayList<AgentReceiveTask>();
					listAgentReceiveTasks = new Gson().fromJson(msm.getData().toString(),
							new TypeToken<List<AgentReceiveTask>>() {
							}.getType());
					// AgentReceiveTask agentReceiveTask = new
					// Gson().fromJson(msm
					// .getData().toString(), AgentReceiveTask.class);
					if (listAgentReceiveTasks.size() == 1) {
						intent(ZhiLiuActivity.class, new IntentExtra(
								Constant.SCAN_ZHILIUJIANCHULI, listAgentReceiveTasks.get(0)));
						listAgentReceiveTasks.clear();
						zhiLiuAdapter.updateAll(listAgentReceiveTasks);
						// finish();
					} else {
						viewUtils.setVisiable(R.id.lv_scan);

						zhiLiuAdapter = new ScanZhiLiuAdapter(this, listAgentReceiveTasks);
						viewUtils.view(R.id.lv_scan, ListView.class)
								.setAdapter(zhiLiuAdapter);
					}

				} else {// 测试用 2--已经签收
					T.showShort(this, msm.getMessage());
					handler.postDelayed(new Runnable() {

						@Override
						public void run() {
							viewfinderView.drawResultBitmap(null);
							handler.postDelayed(new Runnable() {

								@Override
								public void run() {
									if (handler != null) {
										handler.obtainMessage(R.id.restart_preview)
												.sendToTarget();
									}
								}
							}, 1000);
						}
					}, 200);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

			break;
		case Constant.queryCustomer:// 此处展示服务器返回的列表
			try {
				if (msm.getResult() == 0) {
					list_contacts = new Gson().fromJson(msm.getData()
							.toString(),
							new TypeToken<List<AgentContactCustomer>>() {
							}.getType());
					scanAdapter1.updateAll(list_contacts);
					for(AgentContactCustomer acc:list_contacts){
						contacts.add(acc.getExpressCode());
						phones.add(acc.getReceiverPhone());
					}
				} else {
					T.showShort(this, msm.getMessage());
				}
			} catch (Exception e) {
				T.showShort(this, "获得个人数据异常");
			}
			break;
		case Constant.addCustomer:// 新增联系客户列表
			if(msm.getResult()==0){
				DialogUtils.cancelDialog();
				list_contacts.add(0,agentContactCustomer);
				scanAdapter1.updateAll(list_contacts);
				contacts.add(agentContactCustomer.getExpressCode());
				phones.add(agentContactCustomer.getReceiverPhone());
				agentContactCustomer=null;
			}
			T.showShort(this, msm.getMessage());
			break;
		case Constant.editReceiveTaskUnFinish:
			if (msm.getResult() == 0) {
				for (int i = 0; i < list_contacts.size(); i++) {
					if (list_contacts.get(i).getExpressCode()
							.equals(agentContactCustomer.getExpressCode())) {
						list_contacts.remove(i);
						contacts.remove(i);
						phones.remove(i);
						agentContactCustomer=null;
						break;
					}
				}
				scanAdapter1.updateAll(list_contacts);
				excepReason = null;
				agentContactCustomer = null;
//				if (dialog != null && dialog.isShowing()) {
//					dialog.cancel();
//				}
			}
			T.showShort(this, msm.getMessage());
			break;
		case Constant.editReceiveTask:
			if (msm.getResult() == 0) {
				// 如果是新入库的，需要加入list中
				for (int i = 0; i < list_contacts.size(); i++) {
					if (list_contacts.get(i).getExpressCode()
							.equals(agentContactCustomer.getExpressCode())) {
						list_contacts.get(i)
								.setReceiverPhone(editPhone);
						phones.remove(i);
						phones.add(i, editPhone);
						agentContactCustomer = null;
						break;
					}
				}
				if (dialog != null && dialog.isShowing()) {
					dialog.cancel();
				}
				scanAdapter1.updateAll(list_contacts);
			} else {
				T.showShort(this, msm.getMessage());
			}
			break;
		}

	}
	
	/**
	 * 单入、批签、派送中入库弹框
	 * 
	 * void
	 */
	private void showRUKUDialog() {
		//待入库订单的独特声音（系统默认）
		NotificationManager manger = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE); 
		Notification notification = new Notification(); 
		//使用系统默认声音用下面这条
		notification.defaults=Notification.DEFAULT_SOUND;
		manger.notify(1, notification);

		vDialogUtils = showInLibDialog(this);
		vDialogUtils.setViewText(R.id.tv_dialog_txt, "快件未入库");
		vDialogUtils.setClickable(R.id.tv_dialog_carrier, false);
		vDialogUtils.view(R.id.fl_dialog_inlib_chooseExpressCompany)
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						intentForResult(CarrierBookActivity2.class, null, 0);

					}
				});
		vDialogUtils.view(R.id.btn_dialog_inlib).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						if (agentCarrier != null) {
							if("韵达快递".equals(agentCarrier.getParentCarrierName())
									&&viewUtils.getViewText(R.id.edt_scan_expresscode).length()>13){
								viewUtils.setViewText(R.id.edt_scan_expresscode, TextUtils.substring(viewUtils.getViewText(R.id.edt_scan_expresscode), 0, 13));
								for (int i = 0; i < results.size(); i++) {
									if (viewUtils.getViewText(R.id.edt_scan_expresscode).equals(results.get(i))) {
										T.showShort(CaptureActivity.this, "快递单号重复啦，换一个吧");
										dialogRuKu.cancel();
										viewfinderView.drawResultBitmap(null);
										handler.postDelayed(new Runnable() {
											@Override
											public void run() {
												if (handler != null) {
													handler.obtainMessage(R.id.restart_preview)
															.sendToTarget();
												}
											}
										}, 1000);
										return;
									}
								}
							}
							if (type == Constant.SCAN_KEHUQIANSHOU_MORE) {
								saveAgentReceiveTasks(qianshouList,null);
								results.add(viewUtils
										.getViewText(R.id.edt_scan_expresscode));
//								qianshouResult.add(viewUtils
//										.getViewText(R.id.edt_scan_expresscode));
								dialogRuKu.cancel();
							} else if (type == Constant.SCAN_PAISONG) {
								saveAgentReceiveTasks(paisongList,vDialogUtils.getViewText(R.id.tv_dialog_rukutelephone_text));
//								results.add(viewUtils
//										.getViewText(R.id.edt_scan_expresscode));
//								paisongResult.add(viewUtils
//										.getViewText(R.id.edt_scan_expresscode));
								dialogRuKu.cancel();
							} else {
								artContact = new AgentReceiveTask();
								artContact.setCarrierName(agentCarrier
										.getParentCarrierName());
								artContact.setCarrierCode(agentCarrier
										.getParentCarrierCode());
								artContact.setCarrierId(agentCarrier
										.getParentCarrierId());
								artContact.setExpressCode(viewUtils
										.getViewText(R.id.edt_scan_expresscode));
								artContact.setAgentId(SPUtils.getAgent(
										CaptureActivity.this).getAgentId());
								artContact.setStype("1");
								artContact.setAgentUserCode(SPUtils.getUser(CaptureActivity.this).getAgentUserCode());
								artContact.setAgentUserId(SPUtils.getUser(CaptureActivity.this).getAgentUserId());
								artContact.setAgentUserName(SPUtils.getUser(CaptureActivity.this).getAgentUserName());
								artContact.setScanTime(System.currentTimeMillis());
								List<AgentReceiveTask> inlibAgentReceiveTasks = new ArrayList<AgentReceiveTask>();
								inlibAgentReceiveTasks.add(artContact);
								doRequest(Constant.intake,
										inlibAgentReceiveTasks);
							}
						} else {
							T.showShort(CaptureActivity.this, "请选择快递公司！");
						}

					}
				});
	}

	@Override
	public void doEmptyCheck(Object... needParams) {

	}

	@Override
	public void intentCallback(int requestCode, int resultCode, IntentExtra data) {
		agentCarrier = (AgentCarrier) data.getValue();
		vDialogUtils.setViewText(R.id.tv_dialog_carrier,
				agentCarrier.getParentCarrierName());

	}

	public void onEvent(CloseEvent event) {
		finish();
	}
	
	/**
	 * 批入、批签、派送中item移除或修改手机号
	 * @param event
	 * void
	 */
	public void onEvent(ScanResultEvent event) {
		final AgentReceiveTask agentReceiveTask = event.getAgentReceiveTask();
		int scanType=event.getType();
		switch(scanType){
		case ScanResultEvent.TYPE_SCANBULU://补录/更改手机号
			vDialogUtils=showTelephoneDialog(this, agentReceiveTask);
			vDialogUtils.view(R.id.ll_dialog_ruku_queren,Button.class).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(TextUtils.isEmpty(vDialogUtils.getViewText(R.id.tv_dialog_rukutelephone_text))){
						T.showShort(CaptureActivity.this, "请输入手机号！");
					}else{
						for(int i=0;i<listAgentReceiveTasks.size();i++){
							if(agentReceiveTask.getExpressCode().equals(listAgentReceiveTasks.get(i).getExpressCode())){
								listAgentReceiveTasks.get(i).setReceiverPhone(vDialogUtils.getViewText(R.id.tv_dialog_rukutelephone_text));
								scanAdapter.updateAll(listAgentReceiveTasks);
								try {
//									getDbUtils().delete(
//											AgentReceiveTask.class,
//											WhereBuilder.b("expressCode", "=",
//													agentReceiveTask.getExpressCode()));
//									getDbUtils().save(listAgentReceiveTasks.get(i));
//									getDbUtils().update(listAgentReceiveTasks.get(i));
									AgentReceiveTask task =getDbUtils().findFirst(Selector.from(AgentReceiveTask.class)
															.where("expressCode","=",agentReceiveTask.getExpressCode()));
									if (task != null) {
										task.setReceiverPhone(vDialogUtils.getViewText(R.id.tv_dialog_rukutelephone_text));
										getDbUtils().update(task);
									} else {
											getDbUtils().save(listAgentReceiveTasks.get(i));
									}
								} catch (DbException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								break;
							}
						}
						dialog.cancel();
					}
				}
			});
			break;
		case ScanResultEvent.TYPE_SCANREMOVE://移除
			for (int i = 0; i < results.size(); i++) {
				if (agentReceiveTask.getExpressCode().equals(results.get(i))) {
					try {
						results.remove(i);
						getDbUtils().delete(
								AgentReceiveTask.class,
								WhereBuilder.b("expressCode", "=",
										agentReceiveTask.getExpressCode()));
						if (type == Constant.SCAN_KEHUQIANSHOU_MORE) {
							qianshouResult.remove(i);
							if(!TextUtils.isEmpty(agentReceiveTask.getCarrierName())){
								rukuCounts--;
							}
							viewUtils.view(R.id.tv_scan_tip, TextView.class).setText(Html.fromHtml("已扫描    "+"<font color='red'>"+listAgentReceiveTasks.size()+"</font>"+"    单"));
							viewUtils.view(R.id.tv_scan_ruku_count, TextView.class).setText(Html.fromHtml("待入库    "+"<font color='red'>"+rukuCounts+"</font>"+"    单"));

						} else if(type == Constant.SCAN_PAISONG){
							if(!TextUtils.isEmpty(agentReceiveTask.getCarrierName())){
								rukuCounts--;
							}
							viewUtils.view(R.id.tv_scan_tip, TextView.class).setText(Html.fromHtml("已扫描    "+"<font color='red'>"+listAgentReceiveTasks.size()+"</font>"+"    单"));
							viewUtils.view(R.id.tv_scan_ruku_count, TextView.class).setText(Html.fromHtml("待入库    "+"<font color='red'>"+rukuCounts+"</font>"+"    单"));
							viewUtils.setViewText(R.id.btn_scan_fenpei, "分配（"
									+ listAgentReceiveTasks.size() + "）");
						}else {
							viewUtils.view(R.id.btn_scan_result, Button.class)
									.setText(
											"扫描完成(" + listAgentReceiveTasks.size()
													+ ")");
						}
						return;
					} catch (DbException e) {
						e.printStackTrace();
					}
				}
			}
			break;
		}
	}
	
	/**
	 * 滞留件处理出现一个单号在不同快递公司的情形
	 * @param event
	 * void
	 */
	public void onEvent(UpdateZhiLiuList event){
		if(!TextUtils.isEmpty(viewUtils.getViewText(R.id.edt_scan_expresscode))&&listAgentReceiveTasks.size()>1){
			doRequest(Constant.zljcx);
		}else{
//		AgentReceiveTask agentReceiveTask=event.getAgentReceiveTask();
//		for(int i=0;i<listAgentReceiveTasks.size();i++){
//			if(listAgentReceiveTasks.get(i).getExpressCode().equals(agentReceiveTask.getExpressCode())&&listAgentReceiveTasks.get(i).getCarrierName().equals(agentReceiveTask.getCarrierName())){
//				listAgentReceiveTasks.remove(i);
//				listAgentReceiveTasks.add(i, agentReceiveTask);
//				zhiLiuAdapter.updateAll(listAgentReceiveTasks);
//			}
//		}
		}
	}
	
	/**
	 * 联系客户中item操作
	 * @param event
	 * void
	 */
	public void onEvent(ChangeContactPhoneEvent event) {
		// 请求服务器，修改手机
		int type_contants = event.getType();
		agentContactCustomer = event
				.getAgentContactCustomer();
		switch (type_contants) {
		case ChangeContactPhoneEvent.TYPE_CONSTANTS_EDITPHONE:// 编辑电话
			editCallDialog(agentContactCustomer,
					ChangeContactPhoneEvent.TYPE_CONSTANTS_EDITPHONE,
					agentContactCustomer.getTaskId());
			break;
		case ChangeContactPhoneEvent.TYPE_CONSTANTS_EXCEP:// 异常件处理
			editCallDialog(agentContactCustomer,
					ChangeContactPhoneEvent.TYPE_CONSTANTS_EXCEP,
					agentContactCustomer.getTaskId());
			break;
		case ChangeContactPhoneEvent.TYPE_CONSTANTS_SUPPLEINFO:// 补录电话
			editCallDialog(agentContactCustomer,
					ChangeContactPhoneEvent.TYPE_CONSTANTS_SUPPLEINFO,
					agentContactCustomer.getTaskId());
			break;
		}
	}

	/**派送中有已经入库的和未入库的订单，所以在扫描下一个订单之前，将上一个已经入库的单加入列表中
	 * 
	 * void
	 */
	private void saveAgentReceiveTaskPaisong() {
		if (agentReceiveTask!=null) {
			if(!viewUtils.getViewText(R.id.edt_scan_expresscode).equals(agentReceiveTask.getExpressCode())){
			agentReceiveTask.setReceiverPhone(vDialogUtils.getViewText(R.id.tv_dialog_rukutelephone_text));
				try {
					getDbUtils().save(agentReceiveTask);
				} catch (DbException e) {
					e.printStackTrace();
				}
				dialog.cancel();
				listAgentReceiveTasks.add(0, agentReceiveTask);
				scanAdapter.updateAll(listAgentReceiveTasks);
				viewUtils.view(R.id.lv_scan, ListView.class).setSelection(0);
				results.add(agentReceiveTask.getExpressCode());
				viewUtils.view(R.id.tv_scan_tip, TextView.class).setText(Html.fromHtml("已扫描    "+"<font color='red'>"+listAgentReceiveTasks.size()+"</font>"+"    单"));
				viewUtils.view(R.id.tv_scan_ruku_count, TextView.class).setText(Html.fromHtml("待入库    "+"<font color='red'>"+rukuCounts+"</font>"+"    单"));
				viewUtils.setViewText(R.id.btn_scan_fenpei,"分配("+ listAgentReceiveTasks.size() + ")");
				agentReceiveTask=null;
				vDialogUtils=null;
			}else{
				dialog.cancel();
			}
		}
	}
	
	/**批入、批签、派送中保存扫描单
	 * 
	 * @param list
	 * @param telPhone
	 * void
	 */
	private void saveAgentReceiveTasks(List<AgentReceiveTask> list,String telPhone) {
		if (type == Constant.SCAN_KUAIJIANRUKU) {// 入库
			if (agentReceiveTask!=null&&vDialogUtils!=null&&dialog.isShowing()) {
				if(!viewUtils.getViewText(R.id.edt_scan_expresscode).equals(agentReceiveTask.getExpressCode())){
				agentReceiveTask.setReceiverPhone(vDialogUtils.getViewText(R.id.tv_dialog_rukutelephone_text));
				try {
					getDbUtils().save(agentReceiveTask);
				} catch (DbException e) {
					e.printStackTrace();
				}
				dialog.cancel();
				listAgentReceiveTasks.add(agentReceiveTask);
				scanAdapter.updateAll(listAgentReceiveTasks);
				results.add(agentReceiveTask.getExpressCode());
				viewUtils.view(R.id.lv_scan, ListView.class).setSelection(listAgentReceiveTasks.size() - 1);
				viewUtils.view(R.id.btn_scan_result, Button.class)
						.setText("批量入库("+ listAgentReceiveTasks.size() + ")");
				}else{
					dialog.cancel();
				}
			}
			agentReceiveTask = new AgentReceiveTask();
			agentReceiveTask.setCarrierName(dbAgentCarrier
					.getParentCarrierName());
			agentReceiveTask.setCarrierCode(dbAgentCarrier
					.getParentCarrierCode());
			agentReceiveTask.setCarrierId(dbAgentCarrier.getParentCarrierId());
			agentReceiveTask.setAgentId(SPUtils.getAgent(this).getAgentId());
			agentReceiveTask.setAgentUserId(SPUtils.getUser(this).getAgentUserId());
			agentReceiveTask.setAgentUserName(SPUtils.getUser(this).getAgentUserName());
			agentReceiveTask.setAgentUserCode(SPUtils.getUser(this).getAgentUserCode());
			agentReceiveTask.setStype("2");
			agentReceiveTask.setType(type);
			agentReceiveTask.setExpressCode(viewUtils.getViewText(R.id.edt_scan_expresscode));
			agentReceiveTask.setScanTime(System.currentTimeMillis());
			vDialogUtils=showTelephoneDialog(this, agentReceiveTask);
			vDialogUtils.view(R.id.ll_dialog_ruku_queren,Button.class).setOnClickListener(this);
		} else if(type == Constant.SCAN_PAISONG){
		agentReceiveTask = new AgentReceiveTask();
		agentReceiveTask.setExpressCode(viewUtils.getViewText(R.id.edt_scan_expresscode));
		agentReceiveTask.setType(type);
		agentReceiveTask.setAgentId(SPUtils.getAgent(this).getAgentId());
		agentReceiveTask.setAgentUserId(SPUtils.getUser(this).getAgentUserId());
		agentReceiveTask.setAgentUserName(SPUtils.getUser(this).getAgentUserName());
		agentReceiveTask.setAgentUserCode(SPUtils.getUser(this).getAgentUserCode());
		for (AgentReceiveTask art : list) {
			if (viewUtils.getViewText(R.id.edt_scan_expresscode).equals(art.getExpressCode())) {
				agentReceiveTask.setCarrierId(art.getCarrierId());
				agentReceiveTask.setTaskStatus(art.getTaskStatus());
				agentReceiveTask.setReceiverPhone(art.getReceiverPhone());
				agentReceiveTask.setTaskId(art.getTaskId());
				break;
			}
		}
		if (agentCarrier != null) {
			agentReceiveTask.setCarrierName(agentCarrier
					.getParentCarrierName());
			agentReceiveTask.setCarrierCode(agentCarrier
					.getParentCarrierCode());
			agentReceiveTask
					.setCarrierId(agentCarrier.getParentCarrierId());
			agentReceiveTask.setStype("1");
			agentReceiveTask.setTaskStatus((short) 0);
			agentReceiveTask.setScanTime(System.currentTimeMillis());
			agentReceiveTask.setReceiverPhone(telPhone);
			rukuCounts++;
					try {
						getDbUtils().save(agentReceiveTask);
					} catch (DbException e) {
						e.printStackTrace();
					}
					listAgentReceiveTasks.add(0, agentReceiveTask);
					scanAdapter.updateAll(listAgentReceiveTasks);
					viewUtils.view(R.id.lv_scan, ListView.class).setSelection(0);
					results.add(agentReceiveTask.getExpressCode());
					viewUtils.view(R.id.tv_scan_tip, TextView.class).setText(Html.fromHtml("已扫描    "+"<font color='red'>"+listAgentReceiveTasks.size()+"</font>"+"    单"));
					viewUtils.view(R.id.tv_scan_ruku_count, TextView.class).setText(Html.fromHtml("待入库    "+"<font color='red'>"+rukuCounts+"</font>"+"    单"));
					viewUtils.setViewText(R.id.btn_scan_fenpei,"分配("+ listAgentReceiveTasks.size() + ")");
					agentReceiveTask=null;
					vDialogUtils=null;
					agentCarrier=null;
		}else{
			if(TextUtils.isEmpty(agentReceiveTask.getReceiverPhone())){
				vDialogUtils=showTelephoneDialog(this, agentReceiveTask);
				vDialogUtils.view(R.id.ll_dialog_ruku_queren,Button.class).setOnClickListener(this);
			}else{
				try {
					getDbUtils().save(agentReceiveTask);
				} catch (DbException e) {
					e.printStackTrace();
				}
				listAgentReceiveTasks.add(0, agentReceiveTask);
				scanAdapter.updateAll(listAgentReceiveTasks);
				viewUtils.view(R.id.lv_scan, ListView.class).setSelection(0);
				results.add(agentReceiveTask.getExpressCode());
				viewUtils.view(R.id.tv_scan_tip, TextView.class).setText(Html.fromHtml("已扫描    "+"<font color='red'>"+listAgentReceiveTasks.size()+"</font>"+"    单"));
				viewUtils.view(R.id.tv_scan_ruku_count, TextView.class).setText(Html.fromHtml("待入库    "+"<font color='red'>"+rukuCounts+"</font>"+"    单"));
				viewUtils.setViewText(R.id.btn_scan_fenpei,"分配("+ listAgentReceiveTasks.size() + ")");
				agentReceiveTask=null;
				vDialogUtils=null;
				agentCarrier=null;
			}
		}
	}else {//批签
			AgentReceiveTask agentReceiveTask = new AgentReceiveTask();
			agentReceiveTask.setExpressCode(viewUtils.getViewText(R.id.edt_scan_expresscode));
			agentReceiveTask.setType(type);
			agentReceiveTask.setAgentId(SPUtils.getAgent(this).getAgentId());
			agentReceiveTask.setAgentUserId(SPUtils.getUser(this)
					.getAgentUserId());
			agentReceiveTask.setAgentUserName(SPUtils.getUser(this)
					.getAgentUserName());
			agentReceiveTask.setAgentUserCode(SPUtils.getUser(this).getAgentUserCode());
			for (AgentReceiveTask art : list) {
				if (viewUtils.getViewText(R.id.edt_scan_expresscode).equals(art.getExpressCode())) {
					agentReceiveTask.setCarrierId(art.getCarrierId());
					agentReceiveTask.setTaskStatus(art.getTaskStatus());
					agentReceiveTask.setTaskId(art.getTaskId());
					break;
				}
			}
			if (agentCarrier != null) {
				agentReceiveTask.setCarrierName(agentCarrier
						.getParentCarrierName());
				agentReceiveTask.setCarrierCode(agentCarrier
						.getParentCarrierCode());
				agentReceiveTask
						.setCarrierId(agentCarrier.getParentCarrierId());
				agentReceiveTask.setStype("1");
				agentReceiveTask.setTaskStatus((short) 0);
				agentReceiveTask.setScanTime(System.currentTimeMillis());
				rukuCounts++;
				agentCarrier = null;
			}
			try {
				listAgentReceiveTasks.add(0, agentReceiveTask);
				scanAdapter.updateAll(listAgentReceiveTasks);
				viewUtils.view(R.id.lv_scan, ListView.class).setSelection(0);
				getDbUtils().save(agentReceiveTask);
				if(type==Constant.SCAN_KEHUQIANSHOU_MORE){
					viewUtils.view(R.id.tv_scan_tip, TextView.class).setText(Html.fromHtml("已扫描    "+"<font color='red'>"+listAgentReceiveTasks.size()+"</font>"+"    单"));
					viewUtils.view(R.id.tv_scan_ruku_count, TextView.class).setText(Html.fromHtml("待入库    "+"<font color='red'>"+rukuCounts+"</font>"+"    单"));
				}
			} catch (DbException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 批签/分配  dialog
	 * @param list
	 * void
	 */
	private void editPQDialog(final List<AgentReceiveTask> list) {
		String str=type==Constant.SCAN_PAISONG?"件待分配扫描记录":"件待签收扫描记录";
		vDialogUtils = showRUKUDialog(this, "当前有未完成的" + String.valueOf(list.size())
				+str);
		//vDialogUtils = showRUKUDialog(this, "当前有未完成的"+String.valueOf(i)+"件待签收扫描记录");
		vDialogUtils.setViewText(R.id.btn_dialog_cancel, "继续扫描");
		vDialogUtils.setViewText(R.id.btn_dialog_ok, "重新扫描");
		vDialogUtils.view(R.id.btn_dialog_cancel).setOnClickListener(
				new OnClickListener() {
		
					@Override
					public void onClick(View v) {
						// 取消
						dialog.cancel();
			//			failedAgentReceiveTasks = new ArrayList<String>();
						if(type==Constant.SCAN_KEHUQIANSHOU){
							getTopbar().setControlOneText("");
							getTopbar().setTitleText("批量签收");
							qianshouResult = new ArrayList<String>();
							qianshouList=new ArrayList<AgentReceiveTask>();
							listAgentReceiveTasks=new ArrayList<AgentReceiveTask>();
							scanAdapter = new ScanAdapter(CaptureActivity.this,
									listAgentReceiveTasks, false,
									Constant.SCAN_KEHUQIANSHOU_MORE);
							viewUtils.view(R.id.lv_scan, ListView.class)
									.setAdapter(scanAdapter);
							type = Constant.SCAN_KEHUQIANSHOU_MORE;// 批量签收
								doRequest(Constant.queryReceiveTaskQianshouList);
						}else{
							paisongResult = new ArrayList<String>();
							paisongList=new ArrayList<AgentReceiveTask>();
							listAgentReceiveTasks=new ArrayList<AgentReceiveTask>();
							scanAdapter = new ScanAdapter(CaptureActivity.this,
									listAgentReceiveTasks, false,Constant.SCAN_PAISONG);
							viewUtils.view(R.id.lv_scan, ListView.class).setAdapter(scanAdapter);
							doRequest(Constant.queryReceiveTaskPaiSongList);
						}
					}
				});
				vDialogUtils.view(R.id.btn_dialog_ok).setOnClickListener(
						new OnClickListener() {
		
							@Override
							public void onClick(View arg0) {
								try {
		//							long agentUserId=SPUtils.getUser(CaptureActivity.this).getAgentUserId();
		//							List<AgentReceiveTask> list=getDbUtils().findAll(Selector.from(AgentReceiveTask.class)
		//				                          .where("type" ,"=", type)
		//				                          .and(WhereBuilder.b("agentUserId", "=", agentUserId)));
		//							if(list!=null&&list.size()>0){
		//								listAgentReceiveTasks=list;}
									getDbUtils().deleteAll(list);
									dialog.cancel();
		//							failedAgentReceiveTasks = new ArrayList<String>();
									if(type==Constant.SCAN_KEHUQIANSHOU){
										getTopbar().setControlOneText("");
										getTopbar().setTitleText("批量签收");
										qianshouResult = new ArrayList<String>();
										qianshouList=new ArrayList<AgentReceiveTask>();
										listAgentReceiveTasks=new ArrayList<AgentReceiveTask>();
										scanAdapter = new ScanAdapter(CaptureActivity.this,
												listAgentReceiveTasks, false,
												Constant.SCAN_KEHUQIANSHOU_MORE);
										viewUtils.view(R.id.lv_scan, ListView.class)
												.setAdapter(scanAdapter);
										type = Constant.SCAN_KEHUQIANSHOU_MORE;// 批量签收
										doRequest(Constant.queryReceiveTaskQianshouList);
									}else{//派送
										paisongResult = new ArrayList<String>();
										paisongList=new ArrayList<AgentReceiveTask>();
										listAgentReceiveTasks=new ArrayList<AgentReceiveTask>();
										scanAdapter = new ScanAdapter(
												CaptureActivity.this,
												listAgentReceiveTasks, false,
												Constant.SCAN_PAISONG);
										viewUtils.view(R.id.lv_scan, ListView.class).setAdapter(scanAdapter);
										doRequest(Constant.queryReceiveTaskPaiSongList);
									}
								} catch (DbException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						});
				}
	
	/**
	 * 左上角返回弹框
	 * 
	 * void
	 */
	private void editRUKUDialog() {
		vDialogUtils = showRUKUDialog(this, "已保存扫描记录，若下次更换快递公司，将丢失本次扫描记录");
		vDialogUtils.view(R.id.btn_dialog_cancel).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// 取消
						dialog.cancel();
					}
				});
		vDialogUtils.view(R.id.btn_dialog_ok).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						dialog.cancel();
						finish();
					}
				});
	}

	/**
	 * 快递入库弹框
	 * 
	 * @param context
	 * @param requesxxxtCode
	 */
	private ViewUtils showRUKUDialog(Context context, String text) {

		dialog = new Dialog(context, R.style.Dialog);
		ViewUtils viewUtils = new ViewUtils(context, R.layout.dialog_ruku);

		dialog.setContentView(viewUtils.getRootView());

		viewUtils.setViewText(R.id.tv_dialog_text, text);
		dialog.setOnKeyListener(this);
		dialog.show();
		return viewUtils;
	}

	/**
	 * 弹出编辑电话框
	 * 
	 * @param context
	 */
	public ViewUtils showEditCallDialog(Context context,int type) {

		dialog = new Dialog(context, R.style.Dialog);
		ViewUtils viewUtils = new ViewUtils(context, R.layout.dialog_editcall_contacts);

		dialog.setContentView(viewUtils.getRootView());
		if(type==ChangeContactPhoneEvent.TYPE_CONSTANTS_EXCEP){
			dialog.setCanceledOnTouchOutside(false);
		}
		dialog.show();
		return viewUtils;
	}
	/**
	 * 弹出编辑电话框
	 * 
	 * @param context
	 */
	public ViewUtils showEditCallDialog(Context context, String text) {

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
	public ViewUtils showInLibDialog(Context context) {

		dialogRuKu = new Dialog(context, R.style.Dialog);
		ViewUtils viewUtils = new ViewUtils(context, R.layout.dialog_inlib);
		if(type==Constant.SCAN_PAISONG||type==Constant.SCAN_CONTACT||type==Constant.SCAN_ZHILIUJIANCHULI){
			viewUtils.setVisiable(R.id.tv_dialog_rukutelephone_text);
		}
		dialogRuKu.setCanceledOnTouchOutside(false);
		dialogRuKu.setOnKeyListener(this);
		dialogRuKu.setContentView(viewUtils.getRootView());
		dialogRuKu.show();
		return viewUtils;
	}

	/**
	 * 滞留件处理中做异常处理的原因展示
	 * 
	 * void
	 */
	private void initPopWindow() {
		final ArrayList<String> list = new ArrayList<String>();
		list.add("客户拒收");
		list.add("订单分发错误");
		list.add("无法联系到客户");
//		list.add("节假日无人接收");
//		list.add("快件遗失");
//		list.add("客户要求更改派送时间");
//		list.add("超出服务范围");
//		list.add("无法联系到客户");
//		list.add("客户拒付到付款");
//		list.add("客户拒收");
//		list.add("快件破损");
//		list.add("违禁品（揽件）");
//		list.add("面单信息不清晰，无法派送");
//		list.add("查无此人");
//		list.add("快件无人认领（自提件）");
//		list.add("客户要求自提");
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.item_pop_expressdetail,
				R.id.tv_pop_expressdetail_item, list);
		ViewUtils vUtils = new ViewUtils(this, R.layout.popup_unfinish_reason);

		vUtils.view(R.id.lv_pop_expressdetail, ListView.class).setAdapter(
				adapter);
		vUtils.view(R.id.lv_pop_expressdetail, ListView.class)
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {

						vDialogUtils.setViewText(
								R.id.tv_dialog_unfinishedReasen,
								list.get(arg2));
						excepReason=vDialogUtils.getViewText(R.id.tv_dialog_unfinishedReasen);
						popupWindow.dismiss();
					}
				});

		popupWindow = new PopupWindow(vUtils.getRootView(),
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable(getResources()));
		popupWindow.setFocusable(true);

		popupWindow.showAsDropDown(
				vDialogUtils.view(R.id.tv_dialog_title), 0, 0);

	}
	/**
	 * 弹出电话
	 * 
	 * @param context
	 * @param requesxxxtCode
	 */
	public ViewUtils showTelephoneDialog(Context context, AgentReceiveTask agentReceiveTask) {
		dialog = new Dialog(context, R.style.Dialog);
		ViewUtils viewUtils = new ViewUtils(context,
				R.layout.dialog_changenickname);
		dialog.setContentView(viewUtils.getRootView());

		viewUtils.setViewText(R.id.tv_rukutelephone_danhao, "单号：" + agentReceiveTask.getExpressCode());
		viewUtils.setViewText(R.id.tv_dialog_rukutelephone_text, agentReceiveTask.getReceiverPhone());

		Window window = dialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		int screenW = ScreenUtils.getScreenWidth(context);

		lp.width = screenW - 60;
		
		dialog.show();
		return viewUtils;
	}
	
	/**
	 * 派送功能选择代派员/自提点
	 * void
	 */
	private void chooseAgentUser() {
		vDialogUtils=DialogUtils.showChooseAgentUserDialog(this,agentUserList, pickUpBySelfList,new ChooseAgentUserOrPickupBySelfListener() {
			@Override
			public void chooseAgentUserOrPickupBySelfResult(Object result) {
				if (result instanceof AgentUser) {
					selectUser=(AgentUser)result;
					viewUtils.setViewText(R.id.tv_scan_person, selectUser.getAgentUserName());
				}else{
					pickUpBySelf=(PickUpBySelf)result;
					viewUtils.setViewText(R.id.tv_scan_person, pickUpBySelf.getPickUpBySelfName());
				}
			}
		});
		final WheelView wv_agentUserOrPickupBySelf=vDialogUtils.view(R.id.wv_agentUser,WheelView.class);
		final AgentUserWheelAdapter agentUserAdapter=new AgentUserWheelAdapter(agentUserList);
		final PickupBySelfWheelAdapter pickupBySelfAdapter=new PickupBySelfWheelAdapter(pickUpBySelfList);
		if(pickUpBySelfList==null||pickUpBySelfList.size()==0){
			vDialogUtils.setGone(R.id.rb_dialog_pickupBySelf);
		}
		if(agentUserList==null||agentUserList.size()==0){
			vDialogUtils.setGone(R.id.rb_dialog_user);
		}
		vDialogUtils.view(R.id.rg_dialog_uorpick,RadioGroup.class).setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_dialog_user:
					pickUpBySelf=null;
					wv_agentUserOrPickupBySelf.setCurrentItem(0);
					wv_agentUserOrPickupBySelf.setAdapter(agentUserAdapter);
					break;
				case R.id.rb_dialog_pickupBySelf:
					selectUser=null;
					wv_agentUserOrPickupBySelf.setCurrentItem(0);
					wv_agentUserOrPickupBySelf.setAdapter(pickupBySelfAdapter);
					break;

				default:
					break;
				}
			}
		});
	}
	
	/**
	 *  联系客户中订单处理弹框（异常、编辑电话、拨打电话）
	 * @param customer
	 * @param type
	 * @param taskId
	 * void
	 */
	private void editCallDialog(final AgentContactCustomer  customer,
			final int type, final Long taskId) {
		agentContactCustomer=customer;
		vDialogUtils = showEditCallDialog(this, type);
		vDialogUtils.setViewText(
				R.id.tv_dialog_title,
				agentContactCustomer.getCarrierName() + "  "
						+ agentContactCustomer.getExpressCode());
		if (type == ChangeContactPhoneEvent.TYPE_CONSTANTS_EXCEP) {
			vDialogUtils.setGone(R.id.edt_dialog);
			vDialogUtils.setVisiable(R.id.ll_editCall_contacts);
			vDialogUtils.view(R.id.btn_dialog_ok, Button.class).setText("确定");
			vDialogUtils.view(R.id.btn_dialog_cancel, Button.class).setText(
					"取消");
		} else {
			KeyBoardUtils.closeKeybordShowCursor(this,
					vDialogUtils.view(R.id.edt_dialog, EditText.class));
			if (TextUtils.isEmpty(agentContactCustomer.getReceiverPhone())) {
				vDialogUtils.view(R.id.btn_dialog_ok, Button.class).setText(
						"完成并拨打");
			} else {
				vDialogUtils.setViewText(R.id.edt_dialog,
						agentContactCustomer.getReceiverPhone());
				vDialogUtils.view(R.id.btn_dialog_ok, Button.class).setText(
						"保存并拨打");
			}
		}
		// 完成并拨打
		vDialogUtils.view(R.id.btn_dialog_ok).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (type == ChangeContactPhoneEvent.TYPE_CONSTANTS_EXCEP) {
							if (TextUtils.isEmpty(excepReason)) {
								T.showShort(CaptureActivity.this, "请输入原因");
							} else {
								agentContactCustomer.setStatusDesc(excepReason);
								doRequest(Constant.editReceiveTaskUnFinish);
								dialog.cancel();
							}
						} else {
							if (TextUtils.isEmpty(vDialogUtils
									.getViewText(R.id.edt_dialog))) {
								T.showShort(CaptureActivity.this, "请输入联系方式");
							} else {
								if (telephonyManager
										.getSimState() == TelephonyManager.SIM_STATE_ABSENT) {
									T.showShort(
											CaptureActivity.this,
											"无SIM卡！");
								} else {
								// 并请求服务器，将其加入到列表中
								editPhone = vDialogUtils
										.getViewText(R.id.edt_dialog);
								agentContactCustomer.setReceiverPhone(editPhone);
								// if(taskId!=0){
								agentContactCustomer.setTaskId(taskId);
								// }
								doRequest(Constant.editReceiveTask);
								// 拨打电话
								CommonUtils.call(CaptureActivity.this,
										agentContactCustomer.getReceiverPhone());
								}
								dialog.cancel();
							}
						}
					}
				});
		vDialogUtils.view(R.id.btn_dialog_cancel).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (type != ChangeContactPhoneEvent.TYPE_CONSTANTS_EXCEP) {
							if (TextUtils.isEmpty(vDialogUtils
									.getViewText(R.id.edt_dialog))) {
								T.showShort(CaptureActivity.this, "请输入联系方式");
							} else {
								editPhone = vDialogUtils
										.getViewText(R.id.edt_dialog);
								agentContactCustomer.setReceiverPhone(editPhone);
								doRequest(Constant.editReceiveTask);
								dialog.cancel();
							}
						} else {
							excepReason = null;
							dialog.cancel();
						}

					}
				});
		vDialogUtils.view(R.id.ll_dialog_unfinishedReasen).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						initPopWindow();
					}
				});
	}
	@Override
	public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			Log.w("asdasd", String.valueOf(keyCode));
				switch (event.getKeyCode()) {
				case KeyEvent.KEYCODE_BACK:
					if(type!=Constant.SCAN_KUAIJIANRUKU&&dialogRuKu!=null&&dialogRuKu.isShowing()){
						return true;
					}else if(type==Constant.SCAN_PAISONG||type==Constant.SCAN_KEHUQIANSHOU_MORE){
						DialogUtils.cancelDialog();
						finish();
					}else{
						if(dialog!=null){
							dialog.cancel();
							finish();
						}
					}
				default:
					break;
				}
				
		}
	
	return false;
}
}