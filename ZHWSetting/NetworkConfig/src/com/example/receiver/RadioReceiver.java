package com.example.receiver;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;

import com.example.pages.Netconfig_Radio;
import com.example.sender.Global;

import android.content.Context;
import android.util.Log;

import javax.microedition.khronos.opengles.GL;

public class RadioReceiver implements Runnable {
	String TAG = "RadioReceiver";
	boolean isRunning = false;
	
	private byte[] packetBuf = new byte[Global.DataLength];
	
	private DatagramSocket serverSocket = null;
	
	private Netconfig_Radio radioActivity;
	
	public RadioReceiver(Context mContext) {
		// TODO Auto-generated constructor stub
		this.radioActivity = (Netconfig_Radio)mContext;
	}
	
	public void startRecieving() {
		new Thread(this).start();
	}
	
	public void stopRecieving() {
		isRunning = false;
	}
	
	public void release() {
		if (serverSocket == null) {
			return;
		} else {
			serverSocket.close();
			serverSocket = null;
		}
	}
	public void PrintByteHex(String ssss, byte[] bbb ){
		if(Global.Debug == false)
		{
			return;
		}
		String s = "";
		for(int i = 0; i < bbb.length; i++){
			String ss = Integer.toHexString(bbb[i]&0xFF);
			s = s + "0x" + ss + ",";
		}
		Log.i(TAG, s);
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		DataParser dataParser = DataParser.getInstance(this.radioActivity);
		dataParser.startDataParser();
		
	//	Log.i(LOG, LOG + "run");
		
		isRunning = true;
		
		try {
//			@SuppressWarnings("resource")
			serverSocket = new DatagramSocket(Global.RadioPort);
			
			while (isRunning) {
				try {
					Thread.sleep(20);
				} catch (Exception e) {
					// TODO: handle exception
				}

				Arrays.fill(packetBuf, (byte)0);
				DatagramPacket receivePacket = new DatagramPacket(packetBuf, packetBuf.length);
				serverSocket.receive(receivePacket);
				
				byte[] data = receivePacket.getData();
								
				if (data.length > 0) {
					byte[] commandID = new byte[2];

					PrintByteHex("Recv",data);
					
					for (int i = 0; i < commandID.length; i++) {
						commandID[i] = data[i];
					}
					
					if (commandID[0] == (byte)0x55 && commandID[1] == (byte)0x45) {
						; // 话音不做处理						
					}
					else {
						dataParser.addData(data, data.length);	//查询返回值
					}
				}
				else {
					break;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			//System.err.println("RECIEVE ERROR!!");
			Log.e("RadioReceiver", "RECIEVE ERROR!!" + e.toString());
		}
		release();
		//Log.e(LOG, "stop recieving");
	}

}
