package com.common.sdk;

/**
 * 天波API错误码定义
 * 
 * @author linhx
 * @since 1.0
 */
public class TelpoErrorCode {

	/** 操作成功 */
	public final static int OK = 0;

/**********************************系统共用错误**************************/	
	private final static int BASE_SYS_ERR = 0xf000;
	/**参数非法*/
	public final static int ERR_SYS_INVALID = BASE_SYS_ERR + 1;
	/** 设备未找到 */
	public final static int ERR_SYS_NO_DEV = BASE_SYS_ERR + 2;
	/**设备或资源未初始化*/
	public final static int ERR_SYS_NO_INIT = BASE_SYS_ERR + 3;
	/**设备或资源已经初始化*/
	public final static int ERR_SYS_ALREADY_INIT = BASE_SYS_ERR + 4;
	/**缓存不足*/
	public final static int ERR_SYS_OVER_FLOW = BASE_SYS_ERR + 5;
	/**操作超时*/
	public final static int ERR_SYS_TIMEOUT = BASE_SYS_ERR + 6;
	/**暂不支持*/
	public final static int ERR_SYS_NOT_SUPPORT = BASE_SYS_ERR + 7;
	/**未知错误 */
	public final static int ERR_SYS_UNEXPECT = BASE_SYS_ERR + 8;
	/**无权限访问*/
	public final static int ERR_SYS_NO_PERMISSION = BASE_SYS_ERR + 9;
	
/**********************************热敏打印错误**************************/	
	private final static int BASE_PRN_ERR = 0xf100;
	/** 打印机缺纸 */
	public final static int ERR_PRN_NO_PAPER = BASE_PRN_ERR + 1;
	/** 打印机过热 */
	public final static int ERR_PRN_OVER_TEMP = BASE_PRN_ERR + 2;
	/** 打印字库错误 */
	public final static int ERR_PRN_FONT = BASE_PRN_ERR + 3;
	/** 找不到黑标 */
	public final static int ERR_PRN_NO_BALCK_BLOCK = BASE_PRN_ERR + 4;
}
