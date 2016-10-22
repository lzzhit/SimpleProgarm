package com.example.telinterface;

import com.example.sender.RadioSender;

public class QueryRadioInformation {
	
	// 电台参数查询类指令
	private static final short TBR173_QUERY_SOFTVERSION = 0x0040;		// 软件版本
	private static final short TBR173_QUERY_TIME = 0x0041;				// 时间
	private static final short TBR173_QUERY_NETMAC = 0x0043;			// MAC地址
	private static final short TBR173_QUERY_IPADDRESS = 0x0044;			// IP地址
	private static final short TBR173_QUERY_NETMASK = 0x0045;			// IP掩码
	private static final short TBR173_QUERY_POWER = 0x0046;				// 功率
	private static final short TBR173_QUERY_CHANNELMODE = 0x004C;		// 信道接入方式
	
	// 信道参数查询类指令
	private static final short TBR173_QUERY_CHANNELACCESS = 0x0140;		// 当前信道号
	private static final short TBR173_QUERY_ACCESSPARAM = 0x0141;		// 当前信道参数
	
	// 服务和维护类指令
	private static final short TBR173_SERVICE_CHECKSELF = 0x0300;	// 启动自检
	private static final short TBR173_SERVICE_CHECKLINK = 0x0304;	// 链路测试
	private static final short TBR173_SERVICE_DESTORY = 0x0306;		// 自毁
	private static final short TBR173_SERVICE_RESET = 0x0307;		// 恢复出厂设置
	private static final short TBR173_SERVICE_POWEROFF = 0x0308;	// 电源关
	
	private static QueryRadioInformation instanceQueryRadioInformation;
	private RadioSender radioSender;
	
	private static final int BYTELEN = 10;
	
	public QueryRadioInformation(RadioSender rs) {
		// TODO Auto-generated constructor stub
		radioSender = rs;
	}
	
//	public static QueryRadioInformation getInstance() {
//		if (instanceQueryRadioInformation == null) {
//			instanceQueryRadioInformation = new QueryRadioInformation();
//		}
//		
//		return instanceQueryRadioInformation;
//	}
	
	public void ShutDownICU() {
		byte[] softVersionCtr = new byte[BYTELEN];
		int index = 0;
		
		softVersionCtr[index++] = TBR173_SERVICE_POWEROFF >> 8;
		softVersionCtr[index++] = TBR173_SERVICE_POWEROFF & 0xFF;
		softVersionCtr[index++] = 0x00;
		softVersionCtr[index++] = 0x00;
		
		radioSender.addData(softVersionCtr, index);
	}
	
	/**
	 * 查询软件版本
	 */
	public void QuerySoftVersion() {
		byte[] softVersionCtr = new byte[BYTELEN];
		int index = 0;
		
		softVersionCtr[index++] = TBR173_QUERY_SOFTVERSION >> 8;
		softVersionCtr[index++] = TBR173_QUERY_SOFTVERSION & 0xFF;
		softVersionCtr[index++] = 0x00;
		softVersionCtr[index++] = 0x00;
		
		radioSender.addData(softVersionCtr, index);
	}
	/**
	 * 查询时间
	 */
	public void QueryTime() {
		byte[] timeCtr = new byte[BYTELEN];
		int index = 0;
		
		timeCtr[index++] = TBR173_QUERY_TIME >> 8;
		timeCtr[index++] = TBR173_QUERY_TIME & 0xFF;
		timeCtr[index++] = 0x00;
		timeCtr[index++] = 0x00;
		
		radioSender.addData(timeCtr, index);
	}
	
	/**
	 * 查询MAC地址
	 */
	public void QueryNetMac() {
		byte[] macAddress = new byte[BYTELEN];
		int index = 0;
		
		macAddress[index++] = TBR173_QUERY_NETMAC >> 8;
		macAddress[index++] = TBR173_QUERY_NETMAC & 0xFF;
		macAddress[index++] = 0x00;
		macAddress[index++] = 0x00;
		
		radioSender.addData(macAddress, index);
	}
	
	/**
	 * 查询IP地址
	 */
	public void QueryIPAddress() {
		byte[] ipAddress = new byte[BYTELEN];
		int index = 0;
		
		ipAddress[index++] = TBR173_QUERY_IPADDRESS >> 8;
		ipAddress[index++] = TBR173_QUERY_IPADDRESS & 0xFF;
		ipAddress[index++] = 0x00;
		ipAddress[index++] = 0x00;
		
		radioSender.addData(ipAddress, index);
	}
	
	/**
	 * 查询地址掩码
	 */
	public void QueryNetMask() {
		byte[] netMaskCtr = new byte[BYTELEN];
		int index = 0;
		
		netMaskCtr[index++] = TBR173_QUERY_NETMASK >> 8;
		netMaskCtr[index++] = TBR173_QUERY_NETMASK & 0xFF;
		netMaskCtr[index++] = 0x00;
		netMaskCtr[index++] = 0x00;
		
		radioSender.addData(netMaskCtr, index);
	}
	
	/**
	 * 查询功率大小
	 */
	public void QueryPower() {
		byte[] powerCtr = new byte[BYTELEN];
		int index = 0;
		
		powerCtr[index++] = TBR173_QUERY_POWER >> 8;
		powerCtr[index++] = TBR173_QUERY_POWER & 0xFF;
		powerCtr[index++] = 0x00;
		powerCtr[index++] = 0x00;
		
		radioSender.addData(powerCtr, index);
	}
	
	/**
	 * 查询信道接入方式
	 */
	public void QueryChannelMode() {
		byte[] channelModeCtr = new byte[BYTELEN];
		int index = 0;
		
		channelModeCtr[index++] = TBR173_QUERY_CHANNELMODE >> 8;
		channelModeCtr[index++] = TBR173_QUERY_CHANNELMODE & 0xFF;
		channelModeCtr[index++] = 0x00;
		channelModeCtr[index++] = 0x00;
		
		radioSender.addData(channelModeCtr, index);
	}
	
	/**
	 * 查询当前信道号
	 */
	public void QueryChannelAccess() {
		byte[] channelAccessCtr = new byte[BYTELEN];
		int index = 0;
		
		channelAccessCtr[index++] = TBR173_QUERY_CHANNELACCESS >> 8;
		channelAccessCtr[index++] = TBR173_QUERY_CHANNELACCESS & 0xFF;
		channelAccessCtr[index++] = 0x00;
		channelAccessCtr[index++] = 0x00;
		
		radioSender.addData(channelAccessCtr, index);
	}
	
	/**
	 * 查询信道参数
	 */
	public void QueryChannelParam() {
		byte[] channelParamCtr = new byte[BYTELEN];
		int index = 0;
		
		channelParamCtr[index++] = TBR173_QUERY_ACCESSPARAM >> 8;
		channelParamCtr[index++] = TBR173_QUERY_ACCESSPARAM & 0xFF;
		channelParamCtr[index++] = 0x00;
		channelParamCtr[index++] = 0x00;
		
		radioSender.addData(channelParamCtr, index);
	}
	
}
