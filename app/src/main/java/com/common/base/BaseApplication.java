package com.common.base;

import java.io.File;

import android.app.Application;
import cn.jpush.android.api.JPushInterface;

import com.common.model.AgentReceiveTask;
import com.common.utils.Constant;
import com.common.utils.CrashHandlerUtils;
import com.gt.officeagent.R;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.DbException;

/**
 * Created by zhangruntao on 15/5/12.
 */
public class BaseApplication extends Application implements DbUtils.DbUpgradeListener{

    public  static  BaseApplication baseApplication;

    private BitmapUtils bitmapUtils;

    private DbUtils dbUtils;
    private HttpUtils httpUtils;
    
    
    

    @Override
    public void onCreate() {
        super.onCreate();

        baseApplication = this;

        //创建缓存目录
        createCacheDirs();

        httpUtils = new HttpUtils();
        
        bitmapUtils = new BitmapUtils(this, Constant.IMG_CACHE_PATH);
        //设置加载中的默认图片
        bitmapUtils.configDefaultLoadingImage(R.drawable.ic_launcher);
        //设置加载失败后要显示的图片
        bitmapUtils.configDefaultLoadFailedImage(R.drawable.icon_icon);

        //dbUtils = DbUtils.create(this, Constant.DB_NAME,Constant.DB_VERS,this);
        dbUtils = DbUtils.create(this, Constant.DB_NAME,2, this);
        
        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush
        //初始化全局异常捕捉
		CrashHandlerUtils crashHandler = CrashHandlerUtils.getInstance();
		crashHandler.init(getApplicationContext());
		crashHandler.sendPreviousReportsToServer();//如果断网的话，本地还有日志，则手动调用这个方法上传
    }

    private void createCacheDirs() {
        File cacheFile = new File(Constant.IMG_CACHE_PATH);
        if(!cacheFile.exists())
            cacheFile.mkdirs();
    }

    public BitmapUtils getBitmapUtils(){
        return bitmapUtils;
    }
    public HttpUtils getHttpUtils(){
    	return httpUtils;
    }

    public DbUtils getDbUtils(){
        return dbUtils;
    }

    /**
     * 数据库升级函数
     * @param dbUtils
     * @param i
     * @param i1
     */
    @Override
    public void onUpgrade(DbUtils dbUtils, int i, int i1) {
    	try {
			
			dbUtils.dropTable(AgentReceiveTask.class);
			dbUtils.createTableIfNotExist(AgentReceiveTask.class);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    	
    }
}
