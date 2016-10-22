package com.example.pages;

import java.util.Timer;
import java.util.TimerTask;

import com.android.internal.telephony.ITelephony;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Message;
import android.os.RemoteException;
import android.telephony.Itelephony;
import android.view.KeyEvent;

public class MergeProgressDialog {
	
	private static ProgressDialog progressDialog;
	private static int mSeconds;
	public static int newWorkResult = 0;
	private static int isRaioset=0;
	private static int mlinkId;
	private static int count=0;
	public static int Show(Context contex, String title, String Infor,int seconds,int linkId){
		mSeconds = seconds;
		mlinkId=linkId;
		progressDialog = android.app.ProgressDialog.show(contex, title, Infor);
		//progressDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);
		progressDialog.setOnKeyListener(new android.content.DialogInterface.OnKeyListener() {
			
			@Override
			public boolean onKey(DialogInterface arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
				switch (arg1) {
				case KeyEvent.KEYCODE_HOME:
					//Log.v("11111","KEYCODE_HOME");
					return true;
				//	break;

				default:
					break;
				}
				return false;
			}
		});
		final ITelephony iTelephony = Itelephony
				.getTelephonyManager(contex);
		final Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			

			@Override
			public void run() {
					count++;
				try {
					newWorkResult = iTelephony.getAhnMergedStateMSMS(mlinkId);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				if (newWorkResult == 1 || count == mSeconds) {
					if(newWorkResult == 1){
						isRaioset=1;
					}else{
						isRaioset=2;
					}
					progressDialog.dismiss();
					timer.cancel();// 停止定时器
				}

				
			}
		};
		timer.schedule(task, 0, 1000);// 1''秒一次
		while(isRaioset!=2){
			
		}
		System.out.println("111111"+isRaioset);
		return isRaioset;
	}
}

