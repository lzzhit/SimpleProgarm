package com.example.trans;

import android.util.Log;

public class TransData {

	static {
		System.loadLibrary("trans_jni");
		//Log.i("trans_jni", "loaded");
	}
	
	/**
	 * 打开设备
	 * @param index 对应设备号	1/2
	 * @return 返回对应的值，0表示成功
	 */
	public native int InitDev(int index);
	
	/**
	 * 关闭设备
	 */
	public native int CloseDev(int index);
	
	/**
	 * 初始化队列
	 */
	public native int Open(int index);
	
	/**
	 * 关闭队列
	 * @return 0：表示成功
	 */
	public native int Close();
	
	/**
	 * 自组网数字对讲模式话音传输
	 * @param iCount 拼包数量
	 * @param iOperID 业务号（0-128范围）
	 * @param iTransType 传播方式（单播：1，对下组播：2，对上组播：3，全网组播：4，对下广播：5，对上广播：6，全网广播：7）
	 * @param destip 目的地址（5byte）
	 * @param data 语音数据
	 * @param length 发送数据长度（12byte）
	 * @return 发送的数据长度
	 */
	public native int SendVoice(int iCount, int iOperID, int iTransType, byte[] destip, byte[] data, int length);

	/**
	 * 自组网数字对讲模式话音数据接收
	 * @param isRunning 状态判断
	 */
	public native void RecvFromVoiceData();
	
	/**
	 * 发送业务开始和结束信息
	 * @param iOperType 业务类型（协议定义  语音：0；报文：1；文件：2）
	 * @param TransStatus 业务状态（协议定义 开始：0；结束：1）
	 * @param iTransType 传播方式（单播：1，对下组播：2，对上组播：3，全网组播：4，对下广播：5，对上广播：6，全网广播：7）
	 * @param destip 目的地址
	 */
	public native void SendVoiceCtrl(int iOperType, int TransStatus, int iTransType, byte[] destip);
		
	public native void SendDataCtrl(int iFrameType, int iOperID, int iOperType, int TransStatus, int iTransType, byte[] destip);
	
	public native void RecvFromData(boolean isRecv);
	
	public native int SendData(byte[] destip);
}
