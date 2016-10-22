package com.example.pages;

import java.util.Timer;
import java.util.TimerTask;

import android.R.bool;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.os.RemoteException;
import android.telephony.Itelephony;
import android.util.Log;

import com.android.internal.telephony.ITelephony;
//
///
//zzwThread zzwthread = new zzwThread(getApplicationContext());
//zzwthread.setStatus(boolean bbbb){
//	mbbb = bbbb;
//}
public class zzwThread extends Thread{
	Context mContext;
	ITelephony newWorkResult = null ;//
	zzwThread(Context context){
		mContext = context;
		newWorkResult = Itelephony.getTelephonyManager(mContext);
	}
	
	boolean iszzwon = false;
	//Timer timer=new Timer();
	public void run()
	{
		
//		TimerTask task = new TimerTask() {
//			public void run(){
//				int count = 0;
//				count++;
//				try { 
//					iszzwon=newWorkResult.isRadioOnMSMS(2);
//					//test
//					iszzwon = false;
//				} catch (RemoteException e) {
//					e.printStackTrace();
//				}
//				if (iszzwon == true || count == 12) {
//					timer.cancel();// 停止定时器
//				}
//			}
//			
//		};
//		timer.schedule(task, 0, 5000);
		
		//boolean bRun = false;
		int iCount = 0;
	//	iszzwon = false;
		ZzwMainPage.mzuOrLi = 2;
		while( (iCount < 12) && (!iszzwon)){
			try { 
				iszzwon=newWorkResult.isRadioOnMSMS(2);
				//test
				//iszzwon = false;
				//Log.v("zzwThread","Try to Connect to zzw " + iCount +" times");
				try {
					sleep(1000*5);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				iCount++;
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			
		}
		//Log.v("zzwThread", "Thread returns");
		//mbbb = iszzwon;
		if(iszzwon){
			ZzwMainPage.mzuOrLi = 1;
			//send msg to gui
			Message message = Message.obtain();
			Bundle bundle = new Bundle();
			bundle.putSerializable("mzuOrLi", iszzwon);
			message.setData(bundle);
			ZzwMainPage.Zzw.sendMessage(message);
		}
		else{
			ZzwMainPage.mzuOrLi = 0;
		}
		
	
     }

}
