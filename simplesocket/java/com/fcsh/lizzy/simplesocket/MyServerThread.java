package com.fcsh.lizzy.simplesocket;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class MyServerThread implements Runnable {

	Socket socket = null;
	Handler myHandler;

	public MyServerThread(Socket socket, Handler handler) {
		this.socket = socket;
		myHandler = handler;
	}

	@Override
	public void run() {
		// 向android客户端输出hello worild
		String line = null;
		InputStream input;
		OutputStream output;
		String str = "hello world!";
		Message msg = new Message();
		msg.what = 0x11;
		Bundle bundle = new Bundle();
		bundle.clear();
		try {
			//向客户端发送信息
			output = socket.getOutputStream();
			input = socket.getInputStream();
			BufferedReader bff = new BufferedReader(
					new InputStreamReader(input));
			output.write(str.getBytes("UTF-8"));//("gbk"));
			output.flush();
			//半关闭socket  
			socket.shutdownOutput();
			//获取客户端的信息
			while ((line = bff.readLine()) != null) {
				//System.out.print(line);
				Global.recvString = line;
				Global.times ++;
			}
			//关闭输入输出流
			output.close();
			bff.close();
			input.close();
			socket.close();
			bundle.putString("msg", Global.recvString);
			msg.setData(bundle);
			//发送消息 修改UI线程中的组件
			myHandler.sendMessage(msg);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}