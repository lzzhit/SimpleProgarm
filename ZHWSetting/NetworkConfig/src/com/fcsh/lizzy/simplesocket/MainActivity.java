package com.fcsh.lizzy.simplesocket;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.yemian.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class MainActivity extends Activity implements Runnable {
	Socket socket = null;
	String buffer = "";
	TextView txt1;
	TextView recvTxt;
	Button send;
	EditText ed1;
	EditText ipEdit;
	EditText retryTimes;
	String geted1;

	Thread serverThread;
	ServerSocket serivce;

	public Handler myHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0x11) {
				Bundle bundle = msg.getData();
				//txt1.append("recv:"+bundle.getString("msg")+"\n");
				txt1.setText(Global.recvString+"\n");
				recvTxt.setText("times: " + Global.times );
			}
			if (msg.what == 0x12) {
				send.setEnabled(true);
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		txt1 = (TextView) findViewById(R.id.txt1);
		recvTxt = (TextView)findViewById(R.id.recv_times);
		send = (Button) findViewById(R.id.send);
		ed1 = (EditText) findViewById(R.id.ed1);
		ipEdit = (EditText) findViewById(R.id.ipaddress);
		retryTimes = (EditText)findViewById(R.id.times);
		serverThread=new Thread(this);
		serverThread.start();

		send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				geted1 = ed1.getText().toString();
				//txt1.append("client:"+geted1+"\n");
				Global.serverIp = ipEdit.getText().toString().trim();
				Global.retryTimes = Integer.parseInt(retryTimes.getText().toString().trim());
				send.setEnabled(false);
				//启动线程 向服务器发送和接收信息
				new MyThread(geted1).start();
			}
		});

	}

	class MyThread extends Thread {

		public String strToSend;

		public MyThread(String str) {
			strToSend = str;
		}

		@Override
		public void run() {
			//定义消息
			Message msg = new Message();
			msg.what = 0x12;
			Bundle bundle = new Bundle();
			bundle.clear();
			int i = Global.retryTimes;
			try {
				//连接服务器 并设置连接超时为5秒
				socket = new Socket();
				socket.connect(new InetSocketAddress(Global.serverIp, Global.serverPort), 5000);
				//获取输入输出流
				OutputStream ou = socket.getOutputStream();
				BufferedReader bff = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
				//读取发来服务器信息
				String line = null;
				buffer="";
				while ((line = bff.readLine()) != null) {
					buffer = line + buffer;
				}
				
				//向服务器发送信息
				while(i > 0) {
					ou.write((strToSend + "\r\n").getBytes("UTF-8"));//("gbk"));
					ou.flush();
					try {
						Thread.sleep(100);
					}
					catch (Exception e)
					{

					}
					i --;
				}

				bundle.putString("msg", buffer.toString());
				msg.setData(bundle);
				//发送消息 修改UI线程中的组件
				myHandler.sendMessage(msg);
				//关闭各种输入输出流
				bff.close();
				ou.close();
				socket.close();
			} catch (SocketTimeoutException aa) {
				//连接超时 在UI界面显示消息
				//bundle.putString("msg", "服务器连接失败！请检查网络是否打开");
				//msg.setData(bundle);
				//发送消息 修改UI线程中的组件
				//myHandler.sendMessage(msg);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	@Override
	public void run()    {
		try {
			serivce = new ServerSocket(Global.serverPort);
			while (true) {
				//等待客户端连接
				Socket socket = serivce.accept();
				new Thread(new MyServerThread(socket, myHandler)).start();
			}
		}
		catch (IOException e) {
				e.printStackTrace();
			}
    }
	@Override
	public void onStop(){
		super.onStop();
		try{
			serivce.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

}
