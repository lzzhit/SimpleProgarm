package com.example.pages;

import java.util.Timer;
import java.util.TimerTask;

import com.android.internal.telephony.ITelephony;

import android.R.integer;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.RemoteException;
import android.telephony.Itelephony;
import android.util.Log;
import android.view.KeyEvent;

public class SelfProgressDialog {

	private int mSeconds;
	private int count;
	private Context contex;
	private String title;
	private String Infor;
	private int mergeFlag;
	private int mlinkId;
	private int newWorkResult;
	
	public SelfProgressDialog( int mSeconds, int count,int merge,int mlinkId, Context contex, String title, String infor) {
		super();
		this.mSeconds = mSeconds;
		this.count = count;
		this.contex = contex;
		this.title = title;
		Infor = infor;
		this.mergeFlag=merge;
		this.mlinkId=mlinkId;
	}

	public int Show(){
		//final ProgressDialog progressDialog = android.app.ProgressDialog.show(contex, title, Infor);
		final ITelephony iTelephony = Itelephony.getTelephonyManager(contex);
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
				if(newWorkResult==1||count == mSeconds){
					mergeFlag=1;
					//progressDialog.dismiss();
					timer.cancel();// 停止定时器
				}
			}
		};
		timer.schedule(task, 1000, 1000);// 1''秒一次
		while(mergeFlag==0){}
		System.out.println("111111===="+mergeFlag);
		return mergeFlag;
	}

}
