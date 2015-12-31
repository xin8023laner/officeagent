package com.common.utils;

import android.os.Environment;

import java.io.File;

/**
 * 常量集
 * Created by zhangruntao on 15/5/12.
 */
public class Constant {

    private Constant() {
    }

    ;

    /**
     * SharedPreferences Keys
     */
    public enum SPKeys {
        //SharedPreferences文件名称
        MAIN_SHAREDPREF,

    }
    
    /**
     * View Tag Keys ，根据int类型的key保存或获取View的tag属性
     */

    /***/

    /**
     * Sqlite数据库常量
     */
    public static final int DB_VERS = 1;
    public static final String DB_NAME = "YourDatabaseName";
    /***/

    /**
     * 文件系统常量集
     */
    public static final String CACHE_ROOT = Environment.getExternalStorageDirectory().getAbsolutePath()
            + File.separator + "Android"
            + File.separator + "data"
            + File.separator + "YourPackageName"//这个地方需要替换自己的包名
            + File.separator + "cache" + File.separator;
    //图片缓存路径
    public static final String IMG_CACHE_PATH = CACHE_ROOT + "imageCache" + File.separator;
    /***/

	/* Android、Ios请求appKey */
	public static final String APP_KEY_ANDROID = "QUdFTlRfQU5EUk9JRF9PUkdJRDc3MTM5RjFBQ0M2MjQyMzFCNzU1RTRENkZCMDY1QUY2";// Android AppKey
	
	/* Android、Ios请求密钥appSecret */
	
	public static final String APP_SECRET_ANDROID = "CE568423181CF753C4D7B922DA3EA2E9";
	
	/* 各项业务请求码 */
	
	/* 基础功能（ 检索代办点， 意见反馈，软件更新） 请求 */
	public static final int queryAgentsByCoord = 1001;// 根据经纬度坐标获取服务站信息接口
	public static final int queryAgentsByName = 1002;// 根据服务站名称获取服务站信息接口
	public static final int suggestion = 1003;// 意见反馈接口
	public static final int softwareUpdate = 1004;// 版本更新接口
	public static final int queryTask = 1005;// 快件查询接口
	public static final int queryIsPrint = 1006;// 揽件详情，查询快递公司是否可打印电子面单
	
	/*  用户功能（登录， 个人中心）请求 */
	public static final int login = 2001;// 登录 App 接口
	public static final int forgetPassword = 2002;// 忘记密码接口
	public static final int editPassword = 2003;// 修改密码接口
	public static final int updatePersonInfo = 2004;// 修改个人信息
	public static final int uploadPhoto =2005;// 上传头像
	public static final int queryAgentUsers =2006;// 查询代办点下所有代办员
	
	/*  揽件功能请求 */
	public static final int queryReadySendTask = 3001;// 待接取揽收任务列表接口
	public static final int takeSendTask = 3002;// 接取任务接口
	public static final int queryTakeRecord = 3003;// 接取记录接口
	public static final int querySendTaskList = 3004;// 待揽收、 今日揽收列表接口
//	public static final int takeMySendTask = 3005;// 揽件接口
	public static final int takeMySendTaskReturn = 3006;// 揽件退回接口，揽收详情
	public static final int queryMySendTask = 3007;// 检索快件接口 
	public static final int returnSendTask = 3008;// 退回任务接口,新揽收进入页面
	public static final int takeMySendTaskFailure = 3009;// 揽件接口,失败
	public static final int takeMySendTaskSuccess = 3010;// 揽件接口,成功
	
	
	/*  派件功能请求 */
	public static final int queryCommonCarriers = 4001;// 常用快递公司检索接口
	public static final int intake = 4002;// 快件入库接口
	public static final int queryReceiveTask =4003;// 扫描检索接口
	public static final int editReceiveTask = 4004;// 补录信息接口
	public static final int queryReturnReason = 4005;// 获取未妥投原因接口
	public static final int editReceiveTaskFinish = 4006;// 妥投操作接口
	public static final int editReceiveTaskUnFinish = 4007;// 未妥投操作接口 
	public static final int queryReceiveTaskList =4008;// 已妥投、未妥投列表接口
	public static final int queryMyReceiveTask = 4009;// 检索快件接口
	public static final int queryReceiveTaskByProperty = 4010;// 自提检索快件接口
	public static final int editReceiveTaskFinishSelf = 4011;// 确认自提接口
	public static final int queryReceiveTaskQianshouList = 4012;// 未签收list接口
	public static final int editReceiveTaskQianshouList = 4013;// 批量入库接口
	public static final int queryContactsList = 4014;// 获得联系人列表
	public static final int querySendingList = 4015;// 查询代派员待已经派送任务列表
	public static final int editReceiveTaskPaiSongList = 4016;// 待发任务接口
	public static final int queryReceiveTaskPaiSongList = 4017;// 待派送list接口
	public static final int queryGLOrders = 4018;//够啦订单
	public static final int zljcx = 4019;// 滞留件查询
	public static final int zljcl = 4020;// 滞留件处理
	public static final int queryCustomer = 4021;// 联系客户接口
	public static final int addCustomer = 4022;// 新增联系客户接口
	public static final int editCustomer = 4023;// 编辑联系客户接口
	/*  统计功能请求 */
	public static final int queryStatisticsByMonth = 5001;// 按月查询派件量、 揽件量接口
	public static final int queryStatistics = 5002;//获取当天揽收派送数
	
	
	
	
	
	/**网络请求的各个接口的URL*/
//	public static final String ROOT_URL = "http://192.168.30.12:8080/gtagent/";

//	public static final String ROOT_URL = "http://192.168.100.89:8080/gtagent/";
	
//	public static final String ROOT_URL = "http://192.168.30.14:8080/gtagent/";
	
//	public static final String ROOT_URL = "http://192.168.30.39:8080/gtagent/";

//	public static final String ROOT_URL = "http://182.92.159.160:8088/gtagent/";
//	public static final String ROOT_URL = "http://123.56.117.94:8088/gtagent/";//正式服务器，IP
	public static final String ROOT_URL = "http://gtagent.gtexpress.cn/gtagent/";//正式服务器，域名
	
	
	// 基础功能（检索代办点，意见反馈，软件更新）请求 Url 
	public static final String BASIC_SERVER=ROOT_URL +"app/basic/business.action";
	// 用户功能（登录，个人中心）请求 Url 
	public static final String USER_SERVER=ROOT_URL +"app/user/business.action";
	//揽件功能 Url 
	public static final String SENDTASK_SERVER=ROOT_URL +"agentAPPSendTask/business.action";
	//派件功能 Url 
	public static final String RECEIVETASK_SERVER=ROOT_URL +"receiveTask/business.action";
	//统计功能 Url 
	public static final String STATISTICS_SERVER=ROOT_URL +"statistics/business.action";
	//够啦查询Url 
	public static final String GLORDER_SERVER=ROOT_URL +"gouLaOrder/business.action";
	
	
	//访问头像
	public static final String HEAD_SERVER = ROOT_URL+"uploadFile/";
	//上传头像
	public static final String UPLOAD_SERVER = ROOT_URL + "mobile/upload.action";
	//上传报错日志
			public static final String UPLOADERRORLOG_SERVER = ROOT_URL + "file_new/upLoadException.action";
	//快递公司logo路径
	public static final String CARRIERLOGO_SERVER = "http://ms.gtexpress.cn/images/expresslogo/";

	//更新apk的地址
	public static final String MobileAPK_URL = "http://gtagent.gtexpress.cn/soft/lygtagent_mobile.apk";
	public static final String PDAAPK_URL = "http://gtagent.gtexpress.cn/soft/lygtagent_pda.apk";
	public static final String LYAPK_URL = "http://gtagent.gtexpress.cn/soft/lygtagent.apk";

    /**
     * Zxing扫描类型
     * SCANNER_SINGLE：单个扫描即返回结果
     * SCANNER_MANY：扫描多个后点击按钮返回结果
     */
    public static final int SCAN_PAISONG = 2;
    public static final int SCAN_CONTACT = 3;
    public static final int SCAN_NORMAL = 4;
    public static final int SCAN_KEHUQIANSHOU = 0;
    public static final int SCAN_KUAIJIANRUKU = 1;
    public static final int SCAN_KEHUZITI = 5;
    public static final int SCAN_KEHUQIANSHOU_MORE = 6;
    public static final int SCAN_INPUTINFO = 7;
    public static final int SCAN_ZHILIUJIANCHULI = 8;
    
    /**
     * 运单状态
     */
    public static final int WAYBILL_FAIL = 0;
    public static final int WAYBILL_OK = 1;
    /**
     * 派送方式
     */
    public static final int SENDTYPE_KEHUZITI = 2;
    public static final int SENDTYPE_SHANGMENPAISONG = 1;
    /**
     * 列表类型
     */
    public static final int LIST_SEND_SUCCESS = 2;
    public static final int LIST_SEND_FAILED = 7;
    public static final int LIST_SEND_PAISONG = 4;
    public static final int LIST_CONSTANTS = 4;
    public static final int LIST_PICKUP_UNPICAUP = 0;//待揽收
    public static final int LIST_PICKUP_PICKUPED = 1;//已揽收
    
    /**
     * 任务状态
     */
    public static final int TASKSTATUS_EDIT = 0;//快件可再次操作
    public static final int TASKSTATUS_INLIB = 1;//已入库,设置异常件
    public static final int TASKSTATUS_OK = 2;//已妥投
    public static final int TASKSTATUS_EXCEPTION = 3;//异常
    public static final int TASKSTATUS_UNFINISH = 7;//未妥投
    public static final int TASKSTATUS_PAISONG = 4;//派送中
    /**
     * 主页tab文字
     */
    public static final String TAB_HOME = "首页";
    public static final String TAB_SEND = "我的派件";
    public static final String TAB_PICKUP = "我的揽收";
    public static final String TAB_MINE= "个人中心";
    public static final String TAB_SEND_FINISHED= "已妥投(";
    public static final String TAB_SEND_UNFINISHED= "未妥投(";
    public static final String TAB_PICKUP_FINISHED= "已揽收（";
    public static final String TAB_PICKUP_UNFINISHED= "未揽收（";
    public static final String TAB_SEND_SHEDING= "待派送(";
    /**
     * 客户签收时未入库
     */
    public static final int ERROR_ONTINLIB = 3;
    
    /**
     * 是否启用扫描头
     */
    public static boolean ISSCAN = true;
    /**
     * 快递公司显示
     */
    public static final int TYPE_COMPANY_TEXT = 1;//有edittext显示的快递公司
    public static final int TYPE_COMPANY = 0;//无edittext显示的快递公司
    /*
	 * 每页条数
	 */
	/** 条数 */
	public static final String PAGE_SIZE = "10";
}
