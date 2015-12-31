package com.common.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import cn.greenrobot.eventbus.EventBus;
import cn.socks.autoload.AutoLoadListView;
import cn.socks.autoload.AutoLoadListView.OnLoadNextListener;
import cn.srain.cube.views.ptr.PtrFrameLayout;
import cn.srain.cube.views.ptr.PtrHandler;

import com.common.model.IntentExtra;
import com.common.model.MsMessage;
import com.common.ui.Topbar;
import com.common.utils.Constant;
import com.common.utils.DialogUtils;
import com.common.utils.JsonParser;
import com.common.utils.ViewUtils;
import com.gt.officeagent.R;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * Created by zhangruntao on 15/5/12.
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener {

	public ViewUtils viewUtils ;
    public IntentExtra intentExtra = null;

    //Fragment布局View
    private View rootView;

    //关联Fragment时的Activity实例
    protected BaseFragmentActivity activity;
    protected LayoutInflater inflater;

    private String fragmentName;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (BaseFragmentActivity) activity;
        inflater = LayoutInflater.from(activity);
    }

    
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (rootView == null) {
			if(getArguments()!=null){
				intentExtra=(IntentExtra)getArguments().get(IntentExtra.intent);
			}
			
			initParmers();
			this.rootView=inflater.inflate(bindLayoutId(), null);
			 viewUtils=new  ViewUtils(rootView,this);
			 initViewsAndValues(savedInstanceState);
		
		}
		// 缓存的rootView需要判断是否已经被加过parent，
		// 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}

		return rootView;
	}
    

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * 设置Fragment布局
     *
     * @param layoutId
     * @return
     */
    public abstract int bindLayoutId() ;

    public void intent(Class desClass, IntentExtra value) {
		if (null != value) {
			startActivity(new Intent(activity, desClass).putExtra(IntentExtra.intent,
					value));
		} else {
			startActivity(new Intent(activity, desClass));
		}

	}

	public void intentForResult(Class desClass, IntentExtra value,
			int requestCode) {
		if (null != value) {
			startActivityForResult(
					new Intent(activity, desClass).putExtra(IntentExtra.intent, value),
					requestCode);
		} else {
			startActivityForResult(new Intent(activity, desClass), requestCode);
		}

	}
    
    /**
     * 初始化变量
     */
    public abstract void initParmers();

    /**
     * 初始化页面控件
     */
    public abstract void initViewsAndValues(Bundle savedInstanceState);

  

    /**
     * 在onDestory()手动释放资源
     */
    public abstract void releaseOnDestory();

    /**
     * 点击事件的回调
     */
    public abstract void onClickable(View view);

    /**
     * 返回自己的Application实例
     *
     * @return
     */
    public final BaseApplication getApp() {
        return (BaseApplication) activity.getApplication();
    }

    /**
     * 返回全局的BitmapUtils，只实例化一次
     *
     * @return
     */
    public final BitmapUtils getBitmapUtils() {
        return getApp().getBitmapUtils();
    }

    /**
     * 返回全局的DbUtils，只实例化一次
     *
     * @return
     */
    public final DbUtils getDbUtils() {
        return getApp().getDbUtils();
    }

    /**
     * 获取EventBus实例， EventBus.getDefault本身就是单例
     *
     * @return
     */
    public final EventBus getEventBus() {
        return EventBus.getDefault();
    }

   
 

    /**
     * 初始化AutoloadListView和SwipeRefreshLayout
     */
    @Deprecated
    public final void initAutoloadRefresh(int autoloadListViewId, int swipeRefreshLayoutId, SwipeRefreshLayout.OnRefreshListener onRefreshListener, AutoLoadListView.OnLoadNextListener onLoadNextListener, ListAdapter adapter) {
    	viewUtils.view(swipeRefreshLayoutId, SwipeRefreshLayout.class).setColorSchemeResources(android.R.color.holo_red_light,
                android.R.color.holo_green_light,
                android.R.color.holo_blue_bright,
                android.R.color.holo_orange_light);
    	viewUtils.view(swipeRefreshLayoutId, SwipeRefreshLayout.class).setOnRefreshListener(onRefreshListener);
    	viewUtils.view(autoloadListViewId, AutoLoadListView.class).setAdapter(adapter);
    	viewUtils.view(autoloadListViewId, AutoLoadListView.class).setOnLoadNextListener(onLoadNextListener);
    }

	public final PtrFrameLayout initRefreshListener(int uitraId, PtrHandler handler) {
		PtrFrameLayout ptrFrameLayout=viewUtils.view(uitraId, PtrFrameLayout.class);
		ptrFrameLayout.setPtrHandler(handler);
		return ptrFrameLayout;
	}

	public final AutoLoadListView initAutoloadMoreListener(int autoId,
			OnLoadNextListener listener) {
		AutoLoadListView autoLoadListView=viewUtils.view(autoId, AutoLoadListView.class);
		autoLoadListView.setOnLoadNextListener(listener);
		return autoLoadListView;
	}

	
	
    public final Topbar getTopbar() {
        return viewUtils.view(R.id.topbar, Topbar.class);
    }

    
  
    /**
     * 开始构造参数请求服务器，在这里手动来显示等待框，requestServer不会自己显示等待框了
     *
     * @param requestCode 网络请求Code，用来多个网络请求的区分
     * @param needParams  自己所需的额外参数
     */
    public abstract void doRequest(int requestCode, Object... needParams);

    /**
     * 开始真正的请求服务器
     *
     * @param url         请求路径
     * @param requestCode 请求网络Code
     * @param params      请求参数
     */
    public void requestServer(final String url, final int requestCode, final RequestParams params) {


        getApp().getHttpUtils().send(HttpRequest.HttpMethod.POST, url, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(final ResponseInfo<String> responseInfo) {
                        MsMessage msMessage = JsonParser.getJsonParser(activity).parseJsonToMsMessage(responseInfo.result);

                        if (msMessage.getResult() == 0)
                            requestCallBack(requestCode, msMessage, "");
                        else //这个地方有三种情况会走，一是服务器本身返回失败，二是本地解析失败，三是服务器返回成功了但是data为null
                            requestCallBack(requestCode, msMessage, msMessage.getMessage());
                    }

                    @Override
                    public void onFailure(HttpException httpException,
                                          final String failedReason) {
                        MsMessage msMessage = new MsMessage();
                        msMessage.setResult(1);
                        if ("http://gtagent.gtexpress.cn/gtagent/".equals(Constant.ROOT_URL)) {
							
							msMessage.setMessage("请检查您的网络连接");
						
					} else {
						msMessage.setMessage(failedReason);
					}
                        requestCallBack(requestCode, msMessage, failedReason);
                    }
                });
    }

    /**
     * 子类请求网络的回调函数
     *
     * @param requestCode  网络请求Code
     * @param msm          返回MsMessage的json格式实体类
     * @param failedReason 请求失败原因
     */
    public abstract void requestCallBack(int requestCode,
                                         MsMessage msm, String failedReason);

    //显示加载框
    public final void showLoadingDialog(int requestCode) {
        DialogUtils.showLoadingDialog(activity, requestCode);
    }

    //隐藏加载框
    public final void hideLoadingDialog(int requestCode) {
        DialogUtils.hideLoadingDialog(requestCode);
    }

    @Override
    public void onClick(View view) {
        onClickable(view);
    }
    
    /**
     * 检查空页面
     * @param needParams 拓展参数
     */
    public abstract void doEmptyCheck(Object... needParams);

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewUtils.clearViews();
        System.gc();

        releaseOnDestory();
        System.gc();

        DialogUtils.hideLoadingDialogAll();
    }

	public String getFragmentName() {
		return fragmentName;
	}

	public void setFragmentName(String fragmentName) {
		this.fragmentName = fragmentName;
	}
    
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(data!=null){
			intentCallback(requestCode, resultCode,
					(IntentExtra)data.getSerializableExtra(IntentExtra.result));
		}
	}
	public abstract void intentCallback(int requestCode, int resultCode,
			IntentExtra data);
}
