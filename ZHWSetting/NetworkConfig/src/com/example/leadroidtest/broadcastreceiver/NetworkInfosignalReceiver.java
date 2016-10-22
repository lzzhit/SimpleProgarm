package com.example.leadroidtest.broadcastreceiver;

import java.util.Arrays;


import com.example.yemian.R;
import com.zkxt.msgPopUp.IMsgPopService;
import com.zkxt.msgPopUp.LabelEntryConfig;
import com.zkxt.msgPopUp.MsgPopServiceProxy;



import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NetworkInfosignalReceiver extends BroadcastReceiver {
	public static final String ACTION_AHN_NETWORK_SIGNAL = "android.intent.action.AHN_NETWORK_SIGNAL";
	public static final String INTENT_KEY_LINKID = "linkID";
	public static final String INTENT_KEY_AHN_SIGNAL = "ahnSignal";
	public static int signal=0;
	public static int signalinit=0;
	private Callback callback;
	private LabelEntryConfig config = null;
	 private IMsgPopService mMsgPopService = null; 

	@Override
	public void onReceive(Context context, Intent intent) {
		int linkId = intent.getIntExtra(INTENT_KEY_LINKID, -1);
		int[] info = intent.getIntArrayExtra(INTENT_KEY_AHN_SIGNAL);
		int  infomax;
		if (callback != null) {
			callback.receive(linkId, info);
		}
//		int[] singal= intent.getIntArrayExtra("ahnSignal");
//		int linkid=intent.getIntExtra("linkID", 0);
		if (intent.getAction().equals(ACTION_AHN_NETWORK_SIGNAL)){
         infomax=array(info);
        // Log.i("INFOMAX",""+infomax);
         mMsgPopService = MsgPopServiceProxy.getInstance();
         if (infomax<20){
        	 signal=0;
        	 config = new LabelEntryConfig.Builder().setLabelname("state")
   					.setLabelIcon(R.drawable.wifilevel0)
   					.setmHoldDuration(Integer.MAX_VALUE)			
   					.setClickable(false)
   					.setmNotifyWhenScreenLocked(false)
   					.setIfBlink(false).build();
   		    mMsgPopService.showUpLabel(config);	
         }else if(infomax<40){
        	 signal=1;
        	 config = new LabelEntryConfig.Builder().setLabelname("state")
   					.setLabelIcon(R.drawable.wifilevel1)
   					.setmHoldDuration(Integer.MAX_VALUE)
   					.setClickable(false)
   					.setmNotifyWhenScreenLocked(false)
   					.setIfBlink(false).build();
   		    mMsgPopService.showUpLabel(config);	
         }else if(infomax<60){
        	 signal=2;
        	 config = new LabelEntryConfig.Builder().setLabelname("state")
   					.setLabelIcon(R.drawable.wifilevel2)
   					.setmNotifyWhenScreenLocked(false)
   					.setmHoldDuration(Integer.MAX_VALUE)
   					.setClickable(false)
   					.setIfBlink(false).build();
   		    mMsgPopService.showUpLabel(config);	
         }else if(infomax<80){
        	 signal=3;
        	 config = new LabelEntryConfig.Builder().setLabelname("state")
   					.setLabelIcon(R.drawable.wifilevel3)
   					.setmHoldDuration(Integer.MAX_VALUE)
   					.setClickable(false)
   					.setmNotifyWhenScreenLocked(false)
   					.setIfBlink(false).build();
   		    mMsgPopService.showUpLabel(config);	
         }else{
        	 signal=4;
        	 config = new LabelEntryConfig.Builder().setLabelname("state")
  					.setLabelIcon(R.drawable.wifilevel4)
  					.setmNotifyWhenScreenLocked(false)
  					.setmHoldDuration(Integer.MAX_VALUE)
  					.setClickable(false)
  					.setIfBlink(false).build();
  		    mMsgPopService.showUpLabel(config);	 
         } 
        // Log.i("signal",""+signal);
  /*       if(signalinit!=signal){
        	 if(signal==0){
        		 config = new LabelEntryConfig.Builder().setLabelname("state")
     					.setLabelIcon(R.drawable.wifilevel0)
     					.setmHoldDuration(Integer.MAX_VALUE)
     					.setClickable(false)
     					.setIfBlink(false).build();		
     		mMsgPopService.showUpLabel(config);	  
        	 }else if(signal==1){
        		 config = new LabelEntryConfig.Builder().setLabelname("state")
     					.setLabelIcon(R.drawable.wifilevel1)
     					.setmHoldDuration(Integer.MAX_VALUE)
     					.setClickable(false)
     					.setIfBlink(false).build();		
     		mMsgPopService.showUpLabel(config);	  
        	 }else if(signal==2){
        		 config = new LabelEntryConfig.Builder().setLabelname("state")
     					.setLabelIcon(R.drawable.wifilevel2)
     					.setmHoldDuration(Integer.MAX_VALUE)
     					.setClickable(false)
     					.setIfBlink(false).build();		
     		mMsgPopService.showUpLabel(config);	  
        	 }else if(signal==3){
        		 config = new LabelEntryConfig.Builder().setLabelname("state")
     					.setLabelIcon(R.drawable.wifilevel3)
     					.setmHoldDuration(Integer.MAX_VALUE)
     					.setClickable(false)
     					.setIfBlink(false).build();		
     		mMsgPopService.showUpLabel(config);	  
        	 }else if(signal==4){
        		 config = new LabelEntryConfig.Builder().setLabelname("state")
     					.setLabelIcon(R.drawable.wifilevel4)
     					.setmHoldDuration(Integer.MAX_VALUE)
     					.setClickable(false)
     					.setIfBlink(false).build();		
     		mMsgPopService.showUpLabel(config);	  
        	 }	 
         }*/
         signalinit=signal;
        
		}
	}

	public void setCallback(Callback callback) {
		this.callback = callback;
	}

	public interface Callback {
		void receive(int linkId, int[] info);
	}
   
	public int array(int[] array){
		int max=array[0];
		for(int i=0;i<array.length;i++){
			if(array[i]>max){
				max=array[i];
			}
		}
		return max;
	}
}