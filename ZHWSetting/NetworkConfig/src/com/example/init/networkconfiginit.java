package com.example.init;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.android.internal.telephony.ITelephony;
import com.example.invoke.SystemPropInvoke;
import com.example.yemian.NetworkActivity;
import com.example.yemian.R;
import com.leadcore.ttyClusterController.TtyDevice;
import com.zkxt.msgPopUp.IMsgPopService;
import com.zkxt.msgPopUp.LabelEntryConfig;
import com.zkxt.msgPopUp.MsgPopServiceProxy;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.telephony.Itelephony;
import android.util.Log;
import android.widget.Toast;

public class networkconfiginit extends Service {
 
	 private boolean isWifiConnected;
	 private boolean isDataConnected;
	 private boolean isZZWConnected;
	 private boolean isTTConnected=true;
	 private boolean isZHWConnected;
	 private boolean isJQConnected;
	 
	 private boolean Wifi;
	 private boolean Data;
	 private boolean ZZW;
	 private boolean TT;
	 private boolean ZHW;
	 private boolean JQ;
	 ITelephony init;
	 private WifiManager wifiManager;
	 public static final String WAKE_PATH = "/sys/class/i2c-adapter/i2c-0/0-0008/power_ctrl";
	 private String strValueispad;
	 private LabelEntryConfig config = null;
	 private IMsgPopService mMsgPopService = null; 
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public void onCreate() {
/*    	isDataConnected = NetworkActivity.getInstance(getApplicationContext()).getLTEConnect();
        isZZWConnected = NetworkActivity.getInstance(getApplicationContext()).getZZWConnect();
        isTTConnected = NetworkActivity.getInstance(getApplicationContext()).getTTConnect();
        isZHWConnected = NetworkActivity.getInstance(getApplicationContext()).getZHWConnect();
        isJQConnected = NetworkActivity.getInstance(getApplicationContext()).getJQConnect();
    	Log.i("LTEinit",String.valueOf(isDataConnected));
    	Log.i("ZZWinit",String.valueOf( isZZWConnected ));
    	Log.i("TTinit",String.valueOf(isTTConnected));
    	Log.i("ZHWinit",String.valueOf(isZHWConnected));*/
		
		mMsgPopService = MsgPopServiceProxy.getInstance();
		
		
		strValueispad=SystemPropInvoke.SysPropGet("ro.product.name", null);
    	/*Log.i("isDataConnected",""+isDataConnected);
    	Log.i("isTTConnected",""+isTTConnected);
    	Log.i("isZZWConnected",""+isZZWConnected);
    	Log.i("isZHWConnected",""+isZHWConnected);
    	Log.i("isJQConnected ",""+isJQConnected );*/
		init=Itelephony.getTelephonyManager(this);
		String zzworlte=SystemPropInvoke.SysPropGet("persist.sys.master.chip.modem", null);
		init=Itelephony.getTelephonyManager(this);
    	try{
    		isTTConnected= init.isRadioOnMSMS(3);
    	   if (zzworlte.equals("2")){
			isZZWConnected=true;
			isDataConnected=false;
		   }else{
			isZZWConnected=false;
			isDataConnected=init.isRadioOnMSMS(0);
		  }
    	   Log.i("isDataConnected",""+isDataConnected);
           Log.i("isTTConnected",""+isTTConnected);
       	   Log.i("isZZWConnected",""+isZZWConnected);
           Log.i("isZHWConnected",""+isZHWConnected);
       	   Log.i("isJQConnected ",""+isJQConnected );
    	   NetworkActivity.getInstance(getApplicationContext()).setLTEConnect(isDataConnected);
    	   NetworkActivity.getInstance(getApplicationContext()).setTTConnect(isTTConnected);
    	   NetworkActivity.getInstance(getApplicationContext()).setZZWConnect(isZZWConnected);	
    	}catch(Exception e){
    	}
    	wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
    	wifiManager.setWifiEnabled(isWifiConnected);
    	
    	
    	
/*    	init=Itelephony.getTelephonyManager(this);
    	try{
    	boolean ttstate= init.isRadioOnMSMS(3);
    	TT=init.setRadioMSMS(isTTConnected, 3);
    	Log.i("openorcloseTT",""+TT);
    	Log.i("openorcloseTT",""+TT);
    	Log.i("openorcloseTT",""+TT);
    	Log.i("openorcloseTT",""+TT);
    	Log.i("openorcloseTT",""+TT);
    	Log.i("openorcloseTT",""+TT);   	
    	}catch(Exception e){
    	}
    	wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
    	wifiManager.setWifiEnabled(isWifiConnected);
    	
    	if(isZZWConnected){

	         SystemPropInvoke.SysPropSet("persist.sys.master.chip.modem", "2");    
	         try{
	        	 boolean zzwmainstate=init.isRadioOnMSMS(2);
	        	 boolean zzwminorstate=init.isRadioOnMSMS(1); 
	        	 if(zzwmainstate){
	        		 config = new LabelEntryConfig.Builder().setLabelname("zzw")
	        					.setLabelIcon(R.drawable.qbdwdh_drawable_slidbtnon)
	        					.setmHoldDuration(Integer.MAX_VALUE)
	        					.setClickable(false)
	        					.setmNotifyWhenScreenLocked(false)
	        					.setIfBlink(false).build();		
	        		mMsgPopService.showUpLabel(config);	        			
	        	 }else{
	        		 config = new LabelEntryConfig.Builder().setLabelname("zzw")
	        					.setLabelIcon(R.drawable.qbdwdh_drawable_slidbtnoff)
	        					.setmHoldDuration(Integer.MAX_VALUE)
	        					.setClickable(false)
	        					.setmNotifyWhenScreenLocked(false)
	        					.setIfBlink(false).build();		
	        		mMsgPopService.showUpLabel(config);	  
	        	 }
	         }catch(Exception e){
	 	     	}
	         Log.i("changexingdao","2");
	         Log.i("changexingdao","2");
	         Log.i("changexingdao","2");
	         Log.i("changexingdao","2");
    	}else{
    		 SystemPropInvoke.SysPropSet("persist.sys.master.chip.modem", "0");  
    		 config = new LabelEntryConfig.Builder().setLabelname("zzw")
       		      .setLabelIcon(R.drawable.qbdwdh_drawable_slidbtnoff)
       			  .setmHoldDuration(Integer.MAX_VALUE)
       			  .setmNotifyWhenScreenLocked(false)
       			  .setClickable(false)
       			  .setIfBlink(false).build();
    		 mMsgPopService.showUpLabel(config);	     
    		 try{
 	        	Data=init.setRadioMSMS(isDataConnected, 0);
 	        	Log.i("openorcloseLTE",""+Data);
 	        	Log.i("openorcloseLTE",""+Data);
 	        	Log.i("openorcloseLTE",""+Data);
 	        	Log.i("openorcloseLTE",""+Data);
 	         }catch(Exception e){
 	     	}
 	         Log.i("changexingdao","0");
 	         Log.i("changexingdao","0");
 	         Log.i("changexingdao","0");
 	         Log.i("changexingdao","0");
    	}*/
    	
    	if (strValueispad.equals("full_lc1860beta")){  
    		TtyDevice ttyDevice=new TtyDevice();
    		if(isJQConnected){
    			ttyDevice.ttyOpen(TtyDevice.O_RDWR);
        		JQ=ttyDevice.isOpened();
        		//Log.i("集群开关状态",String.valueOf(JQ));
    		}else {
    			ttyDevice.ttyClose();
    			JQ=ttyDevice.isOpened();
        		//Log.i("集群开关状态",String.valueOf(JQ));
			}
    	   
    	 String strZHW=getString(WAKE_PATH);
    	 if(strZHW.equals("1")){
    		 isZHWConnected=true;
    		 NetworkActivity.getInstance(getApplicationContext()).setLTEConnect(isDataConnected);
    	 }
  /*         if(isZHWConnected){
    	        setZHWon( WAKE_PATH);
    	        ZHWIPSet.getInstance().StartSettingIP();
           }else {
    		    setZHWoff( WAKE_PATH);
		   }   	*/
           
    	}
	}
	private void setZHWon( String path){
	        try {
	            BufferedWriter bufWriter = null;
	            bufWriter = new BufferedWriter(new FileWriter(path));
	            bufWriter.write("1");  // 写操�?
	            bufWriter.close();	        
	        } catch (IOException e) {
	            e.printStackTrace();
	            Log.e("设备节点","can't write the " +path);
	        }
	    }
	private void setZHWoff( String path){
        try {
            BufferedWriter bufWriter = null;
            bufWriter = new BufferedWriter(new FileWriter(path));
            bufWriter.write("0");  // 写操�?
            bufWriter.close();            
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("设备节点","can't write the " +path);
        }
    }
	private static String getString(String path) {
		String prop = "waiting";// 默认值
		try {
			BufferedReader reader = new BufferedReader(new FileReader(path));
			prop = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop;
	}
}
