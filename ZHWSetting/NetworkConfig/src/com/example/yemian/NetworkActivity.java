package com.example.yemian;

import com.example.invoke.SystemPropInvoke;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

public class NetworkActivity {

    private WifiManager wifiManager;
    private TelephonyManager telephonyManager;
    private ConnectivityManager connectivityManager;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String strValue;
    private boolean ispad;
    public static int[] boolvalue = new int[5];
    public static int[] boolvalue2 = new int[5];
    public static int[] boolvalue3 = new int[5];

    public NetworkActivity(Context context){
        wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        connectivityManager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        sharedPreferences = context.getSharedPreferences("networkManager",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    private static NetworkActivity netWorkManager;

    public static NetworkActivity getInstance(Context context){
        if(netWorkManager == null){
            netWorkManager = new NetworkActivity(context);
        }
        return netWorkManager;
    }

    public void setLTEConnect(boolean status){
    	editor.putBoolean("lte", status).commit();
    }
    public void setWIFIConnect(boolean status){
    	editor.putBoolean("wifi", status).commit();
    }
    public void setZZWConnect(boolean status){
        editor.putBoolean("zzw",status).commit();
    }

    public void setTTConnect(boolean status){
        editor.putBoolean("tt",status).commit();
    }

    public void setZHWConnect(boolean status){
        editor.putBoolean("zhw",status).commit();
    }
    
    public void setJQConnect(boolean status){
        editor.putBoolean("jq",status).commit();
    }
    
    public boolean getLTEConnect(){
	   return sharedPreferences.getBoolean("lte", true);
    }  
    public boolean getWIFIConnect(){
       return sharedPreferences.getBoolean("wifi", true);
    }
    public boolean getZZWConnect(){
        return sharedPreferences.getBoolean("zzw", false);
    }

    public boolean getTTConnect(){
        return sharedPreferences.getBoolean("tt",true);
    }

    public boolean getZHWConnect(){
        return sharedPreferences.getBoolean("zhw",false);
    }

    public boolean getJQConnect(){
        return sharedPreferences.getBoolean("jq",false);
    }

    public  int[] getChannelState(){
    	boolean lte  = getLTEConnect();
  //      boolean lte = (telephonyManager.getDataState() == TelephonyManager.DATA_CONNECTED);
 //       boolean wifi = wifiManager.isWifiEnabled();
        State wifiState=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        boolean wifi=(wifiState==State.CONNECTED); 
        strValue=SystemPropInvoke.SysPropGet("ro.product.name", null);
        if (strValue.equals("full_lc1860beta")){
        	boolvalue[0]=1;
        }else{
        	boolvalue[0]=0;
        }
        if (wifi==true){
        	boolvalue[1]=1;
        }else{
        	boolvalue[1]=0;
        }
        
        if (lte==true){
        	boolvalue[2]=1;
        }else{
        	boolvalue[2]=0;
        }       
        boolean zzw = getZZWConnect();
        if (zzw==true){
        	boolvalue[3]=1;
        }else{
        	boolvalue[3]=0;
        }     
  /*      boolean tt = getTTConnect();
        if (tt==true){
        	boolvalue[4]=1;
        }else{
        	boolvalue[4]=0;
        }   */  
        boolean zhw = getZHWConnect();
        if (zhw ==true){
        	boolvalue[4]=1;
        }else{
        	boolvalue[4]=0;
        }
        
        return boolvalue;
    }
   
    
    public  byte getDefaultChannel() {
		byte defaultChannel = 0x00;
//		boolean lte = (telephonyManager.getDataState() == TelephonyManager.DATA_CONNECTED);
		boolean lte  = getLTEConnect();
		State wifiState=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
	    boolean wifi=(wifiState==State.CONNECTED);
		boolean zzw = getZZWConnect();
		boolean tt = getTTConnect();
		boolean zhw = getZHWConnect();
		strValue=SystemPropInvoke.SysPropGet("ro.product.name", null);
		if (wifi==true) {			//WLAN通道
			defaultChannel = 0x15;
		}
		else if (lte==true) {		//LTE
			defaultChannel = 0x11;
		}
		else if (zzw==true) {		//自组�?
			defaultChannel = 0x12;
		}
		else if (strValue.equals("full_lc1860beta")&&zhw==true) {		//战互�?
			defaultChannel = 0x14;
		}
/*		else if (zhw==true) {		//战互�?
			defaultChannel = 0x14;
		}*/
		return defaultChannel;
    }
    
    public  int[] getChatChannelState(){
 //       boolean lte = (telephonyManager.getDataState() == TelephonyManager.DATA_CONNECTED);  
    	boolean lte  = getLTEConnect();
        if (lte==true){
        	boolvalue2[0]=1;
        }else{
        	boolvalue2[0]=0;
        }
        
        boolean zzw = getZZWConnect();
        if (zzw==true){
        	boolvalue2[1]=1;
        }else{
        	boolvalue2[1]=0;
        }
        boolean tt = getTTConnect();
        if (tt==true){
        	boolvalue2[2]=1;
        }else{
        	boolvalue2[2]=0;
        }
        
        boolean zhw = getZHWConnect();
        if (zhw ==true){
        	boolvalue2[3]=1;
        }else{
        	boolvalue2[3]=0;
        }
        boolean jq = getJQConnect();
        if (jq ==true){
        	boolvalue2[4]=1;
        }else{
        	boolvalue2[4]=0;
        }     
    
        return boolvalue2;
    }
    public int[] getchannelable(){
    	strValue=SystemPropInvoke.SysPropGet("ro.product.name", null);
        if (strValue.equals("full_lc1860beta")){
        	for(int i=0;i<5;i++){
        	  boolvalue3[i]=1;
        	}
        }else{
        	for(int i=0;i<4;i++){
          	  boolvalue3[i]=1;
          	}
        	 boolvalue3[4]=0;
        }
       // Log.i("ispad",String.valueOf(ispad));
        return  boolvalue3;
    }
    
}