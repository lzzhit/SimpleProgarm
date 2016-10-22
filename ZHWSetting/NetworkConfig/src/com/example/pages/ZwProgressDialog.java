package com.example.pages;

import java.util.Timer;
import java.util.TimerTask;

import com.android.internal.telephony.ITelephony;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.RemoteException;
import android.telephony.Itelephony;
import android.view.KeyEvent;

public class ZwProgressDialog {

	private int mSeconds;
	private int count;
	private Context contex;
	private String title;
	private String Infor;
	private boolean newWorkResult;
	final ITelephony iTelephony = Itelephony.getTelephonyManager(contex);
	private int isRaioset;
	
	public ZwProgressDialog( int mSeconds, int count, Context contex, String title, String infor) {
		super();
		this.mSeconds = mSeconds;
		this.count = count;
		this.contex = contex;
		this.title = title;
		Infor = infor;
	}

	public int Show(){
		final ProgressDialog progressDialog = android.app.ProgressDialog.show(contex, title, Infor);
		
	 	final Timer timer = new Timer();
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				count++;
				try {
					newWorkResult = iTelephony.isRadioOnMSMS(2);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				if (newWorkResult == true || count == mSeconds) {
					if(newWorkResult == true){
						isRaioset=1;
					}
					progressDialog.dismiss();
					timer.cancel();// 停止定时器
				}
			}
		};
		timer.schedule(task, 100, 1000);// 1''秒一次
		while(isRaioset!=1){
			
		}
		return isRaioset;
	}

	/*
	private static ProgressDialog progressDialog;
	private static int mSeconds;
	public static boolean newWorkResult = false;
	private static int isRaioset=0;
	public static int Show(Context contex, String title, String Infor,int seconds){
		mSeconds = seconds;
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
			private int count;

			@Override
			public void run() {
				this.count++;
				try {
					newWorkResult = iTelephony.isRadioOnMSMS(2);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				if (newWorkResult == true || count == mSeconds) {
					if(newWorkResult == true){
						isRaioset=1;
					}
					progressDialog.dismiss();
					timer.cancel();// 停止定时器
				}

				
			}
		};
		timer.schedule(task, 0, 1000);// 1''秒一次
		return isRaioset;
	}
*/}

