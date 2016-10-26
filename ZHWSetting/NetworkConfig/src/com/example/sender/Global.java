package com.example.sender;

public class Global {
	// 战互网模式IP地址和端口号
	public static int RadioPort = 8086;
	//public static String RadioAddress = "168.32.100.61";	// 电台侧IP地址
	//public static String RadioAddress = "192.168.100.3";	// 电台侧IP地址
	public static String RadioAddress= "127.0.0.1";	// 电台侧IP地址
	public enum StateEnum {
        //注：枚举写在最前面，否则编译出错
        AckWaiting, //发送命令之后，等待返回
        AckTrue, //收到返回:正确,
        AckFalse;//收到返回:错误     

    }
	public static int RetryTimes = 100;
	public static int DataLength = 600;
	public static boolean Debug = false;

	public static StateEnum ACK = Global.StateEnum.AckTrue;
    public static boolean SimpleState = false;

	public static void setIPAdress(String ip) {
			RadioAddress = ip;
	}
}
