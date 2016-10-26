package com.example.sender;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.pages.Netconfig_Radio;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class RadioSender implements Runnable {
	String TAG = "RadioSender";

	private boolean isSendering = false;
	private List<RadioData> dataList;
	private DatagramSocket clientSocket = null;
	private Netconfig_Radio radioActivity;

	public RadioSender(Context context) {
		radioActivity = (Netconfig_Radio)context;
		// TODO Auto-generated constructor stub
		dataList = Collections.synchronizedList(new LinkedList<RadioData>());
	}

	/*
	 * 添加数据到队列
	 */
	public void addData(byte[] data, int size) {
		RadioData radioData = new RadioData();
		radioData.setSize(size);
		byte[] tempData = new byte[size];
		System.arraycopy(data, 0, tempData, 0, size);
		radioData.setRealData(tempData);
		dataList.add(radioData);
	}
	public void PrintByteHex(String ssss, byte[] bbb ){
		//if(Global.Debug == false)
		{
			return;
		}
//		String s = "";
//		for(int i = 0; i < bbb.length; i++){
//			String ss = Integer.toHexString(bbb[i]&0xFF);
//			s = s + "0x" + ss + ",";
//		}
//		Log.i(TAG ,""+s);
	}
	/*
	 * UDP发送数据
	 */
	public boolean sendData(byte[] data, int size) {
		byte[] buffer = new byte[size];
		System.arraycopy(data, 0, buffer, 0, size);

		PrintByteHex("Send",buffer);
		Global.ACK = Global.StateEnum.AckWaiting;

		try {
			DatagramSocket clientSocket = new DatagramSocket();
			InetAddress IP;

			 IP = InetAddress.getByName(Global.RadioAddress);


			DatagramPacket sendPacket = new DatagramPacket(buffer,
					size, IP, Global.RadioPort);

			clientSocket.send(sendPacket);
			clientSocket.close();
		}
		catch (SocketException e)
		{
			e.printStackTrace();
			//Log.e("RadioSender", e.toString());
			radioActivity.setActivityExit(true);
			return false;
		}
		catch (Exception e)	{
			e.printStackTrace();
			//Log.e("RadioSender", e.toString());
			return false;
		}
		return true;
	}

	/*
	 * start sending data
	 */
	public void startSending() {
		new Thread(this).start();
	}

	/*
	 * stop sending data
	 */
	public void stopSending() {
		this.isSendering = false;
	//	clientSocket.close();
	}

	@Override
	public void run() {
		Global.StateEnum st = Global.StateEnum.AckTrue;
		int timerCounts = 0;
		Looper.prepare();
		// TODO Auto-generated method stub
		this.isSendering = true;
		//System.out.println(LOG + "start....");

		byte[] tmpData = new byte[Global.DataLength];

		while (isSendering) {

            try {
                Thread.sleep(98);
                if (st != Global.ACK)       //ACK状态变化时打印log
                {
                    Log.i("RadioSender", "Global.ACK = " + Global.ACK);
                    st = Global.ACK;
                }
            } catch (Exception e) {
                // TODO: handle exception
                //System.out.println(LOG + "run thread stop error");
                Log.e("RadioSender", "run thread stop error" + e.toString());
            }
            if (Global.Debug== true) {
                    if (dataList.size() > 0) {      //队列里有数据时,
                        RadioData data = dataList.remove(0);
                        sendData(data.getRealData(), data.getSize());

                        tmpData = new byte[data.getSize()];
                        System.arraycopy(data.getRealData(), 0, tmpData, 0, data.getSize());
                        timerCounts = 1;
                        radioActivity.changeBtnEnable(false);
                    } //else if(Global.ACK == Global.StateEnum.AckFalse )
                    else if( timerCounts < Global.RetryTimes ) {//重发
                        sendData(tmpData, tmpData.length);        //重发
                        timerCounts ++;
                    }
                else{
                        timerCounts = Global.RetryTimes;
                        radioActivity.changeBtnEnable(true);
                    }
            } else {// if (Global.Debug == false) {
                if (dataList.size() > 0 || Global.ACK == Global.StateEnum.AckFalse) {
                    if (dataList.size() > 0) {      //收到肯定应答并且队列里有数据时,发送数据,ACK设为waiting状态
                        RadioData data = dataList.remove(0);
                        sendData(data.getRealData(), data.getSize());
                        timerCounts = 0;

                        tmpData = new byte[data.getSize()];
                        System.arraycopy(data.getRealData(), 0, tmpData, 0, data.getSize());
                        Global.ACK = Global.StateEnum.AckWaiting;
                    } //else if(Global.ACK == Global.StateEnum.AckFalse )
                    else {
                        sendData(tmpData, tmpData.length);        //收到否定应答,自动重发上次数据,ACK设为waiting状态

                        Global.ACK = Global.StateEnum.AckWaiting;

                    }

                }
                if (Global.ACK == Global.StateEnum.AckWaiting)   // 等待响应,不允许点击设置或者查询按钮
                {
                    radioActivity.changeBtnEnable(false);
                    timerCounts++;
                    if (timerCounts >= Global.RetryTimes)//20s之后,跳过这个命令
                    {
                        Global.ACK = Global.StateEnum.AckTrue;
                        timerCounts = 0;
                        Toast.makeText(radioActivity.getApplication(), "跳过本命令", Toast.LENGTH_SHORT).show();    //显示提示框
                    } else// if(timerCounts == 50)//10s 之后,认为失败,自动重发上次数据
                    {
                        Global.ACK = Global.StateEnum.AckFalse;
                    }

                } else     //允许点击设置或者查询按钮
                {
                    timerCounts =  Global.RetryTimes;
                    radioActivity.changeBtnEnable(true);
                }
            }// if (Global.Debug == false) {
        }
		//System.out.println(LOG + "stop!!!!");
	}
}
