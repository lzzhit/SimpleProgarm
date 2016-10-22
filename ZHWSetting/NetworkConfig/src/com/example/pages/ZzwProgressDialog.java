package com.example.pages;

import java.util.Timer;
import java.util.TimerTask;

import android.app.ProgressDialog;
import android.content.Context;

public class ZzwProgressDialog {
	private int mSeconds;
	private int count;
	private Context contex;
	private String title;
	private String Infor;
	
	public ZzwProgressDialog( int mSeconds, int count, Context contex, String title, String infor) {
		super();
		this.mSeconds = mSeconds;
		this.count = count;
		this.contex = contex;
		this.title = title;
		Infor = infor;
	}

	public void Show(){
		final ProgressDialog progressDialog = android.app.ProgressDialog.show(contex, title, Infor);
		
	 	final Timer timer = new Timer();
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				count++;
				if(count == mSeconds){
					progressDialog.dismiss();
					timer.cancel();// 停止定时器
				}
			}
		};
		timer.schedule(task, 100, 1000);// 1''秒一次
	}
}
