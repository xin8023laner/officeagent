package com.common.model;

/**
 * 代办点列表查询实体
 * 
 * @author zhb
 * 
 */
public enum ApiCode {

	/* 基础功能（ 检索代办点， 意见反馈，软件更新） 请求 */
	queryAgentsByCoord(1001, "RC_BASIC_1001"), // 根据经纬度坐标获取服务站信息接口
	queryAgentsByName(1002, "RC_BASIC_1002"), // 根据服务站名称获取服务站信息接口
	suggestion(1003, "RC_BASIC_1003"), // 意见反馈接口
	softwareUpdate(1004, "RC_BASIC_1004"), // 版本更新接口
	queryTask(1005, "RC_BASIC_1005"), // 快件查询接口
	queryIsPrint(1006, "RC_ISPRINT_1001"), // 揽件详情，查询快递公司是否可打印电子面单
	
	/* 用户功能（登录， 个人中心）请求 */
	login(2001, "RC_USER_1001"), // 登录 App 接口
	forgetPassword(2002, "RC_USER_1002"), // 忘记密码接口
	editPassword(2003, "RC_USER_1003"), // 修改密码接口
	updatePersonInfo(2004, "RC_USER_1004"), // 修改个人信息
	uploadPhoto(2005, "RC_USER_1005"), // 上传头像
	queryAgentUsers(2006, "RC_RECEIVETASK_1019"), // 搜索代办点所有代办员

	/* 揽件功能请求 */
	queryReadySendTask(3001, "RC_SENDTASK_1001"), // 待接取揽收任务列表接口
	takeSendTask(3002, "RC_SENDTASK_1002"), // 接取任务接口
	queryTakeRecord(3003, "RC_SENDTASK_1003"), // 接取记录接口
	querySendTaskList(3004, "RC_SENDTASK_1004"), // 待揽收、 今日揽收列表接口
//	takeMySendTask(3005, "RC_SENDTASK_1005"), // 揽件接口,成功
	takeMySendTaskReturn(3006, "RC_SENDTASK_1006"), // 揽件退回接口，揽收详情
	queryMySendTask(3007, "RC_SENDTASK_1007"), // 检索快件接口
	returnSendTask(3008, "RC_SENDTASK_1008"), // 退回快件接口,新揽收进入页面
	takeMySendTaskFailure(3009, "RC_SENDTASK_1009"), // 揽件接口,失败
	takeMySendTaskSuccess(3010, "RC_SENDTASK_1005"), // 揽件接口,成功

	/* 派件功能请求 */
	queryCommonCarriers(4001, "RC_RECEIVETASK_1001"), // 常用快递公司检索接口
	intake(4002, "RC_RECEIVETASK_1002"), // 快件入库接口
	queryReceiveTask(4003, "RC_RECEIVETASK_1003"), // 扫描检索接口
	editReceiveTask(4004, "RC_RECEIVETASK_1004"), // 补录信息接口
	queryReturnReason(4005, "RC_RECEIVETASK_1005"), // 获取未妥投原因接口
	editReceiveTaskFinish(4006, "RC_RECEIVETASK_1006"), // 妥投操作接口
	editReceiveTaskUnFinish(4007, "RC_RECEIVETASK_1007"), // 未妥投操作接口
	queryReceiveTaskList(4008, "RC_RECEIVETASK_1008"), // 已妥投、未妥投列表接口
	queryMyReceiveTask(4009, "RC_RECEIVETASK_1009"), // 检索快件接口
	queryReceiveTaskByProperty(4010, "RC_RECEIVETASK_1010"), // 自提检索快件接口
	editReceiveTaskFinishSelf(4011, "RC_RECEIVETASK_1011"), // 确认自提接口
	queryReceiveTaskQianshouList(4012,"RC_RECEIVETASK_1016"),//获得签收列表
	editReceiveTaskQianshouList(4013,"RC_RECEIVETASK_1017"),//提交签收列表
	queryContactsList(4014,"RC_RECEIVETASK_1018"),//获得联系人列表
	querySendingList(4015,"RC_RECEIVETASK_1022"),//获得分配任务列表（派送）
	editReceiveTaskPaiSongList(4016,"RC_RECEIVETASK_1021"),//编辑派送列表
	queryReceiveTaskPaiSongList(4017,"RC_RECEIVETASK_1020"),//查询派送列表（用于扫描比对）
	queryGLOrders(4018, "GL_GOULAORDER_1001"),//获得够啦订单列表
	queryZhiLiu(4019, "RC_RECEIVETASK_1023"),//查询滞留件
	chuliZhiLiu(4020, "RC_RECEIVETASK_1024"),//滞留件处理
	queryCustomer(4021, "RC_RECEIVETASK_1012"), // 联系客户接口
	addCustomer(4022, "RC_RECEIVETASK_1013"), // 新增联系客户接口
	editCustomer(4023,"RC_RECEIVETASK_1014"),// 编辑联系客户接口
	/* 统计功能请求 */
	queryStatisticsByMonth(5001, "RC_STATISTICS_1001"), // 按月查询派件量、 揽件量接口
	queryStatistics(5002, "RC_STATISTICS_1002");//获取当天揽收派送数
	public int id;
	public String code;

	ApiCode(int id, String code) {
		this.id = id;
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public static ApiCode valueOf(int ordinal) {
		if (ordinal < 0 || ordinal >= values().length) {
			throw new IndexOutOfBoundsException("Invalid ordinal");
		}
		return values()[ordinal];
	}

	public static ApiCode getApiCode(String apiCode) {
		return valueOf(apiCode.toLowerCase());
	}

}
