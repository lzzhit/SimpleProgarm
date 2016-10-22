package com.example.pages;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.android.internal.telephony.ITelephony;
import com.example.invoke.SystemPropInvoke;
import com.example.yemian.NetworkActivity;
import com.example.yemian.R;
import com.leadcore.ttyClusterController.TtyDevice;

import android.os.Bundle;
import android.telephony.Itelephony;
import android.util.Log;
import android.view.View;
import android.widget.TextView;



public class TTPage extends BasePage {

	 private TextView test_netstate;
	 private TextView test_getchatchannel;
	 private TextView test_getchannel;
	 private TextView test_getdefaultchannel;
	 private TextView test_getablechannel;
	 
	 private boolean isLTEinit;
	 private boolean isWIFIinit;
	 private boolean isZZWmianinit;
	 private boolean isZZWminorinit;
	 private boolean isTTinit;
	 private boolean isZHWinit;
	 private boolean isJQinit;
	 
	 private String isZZWString;
	 private String isZHWString;
	 private String strValueispad;
	 
	 private int[] testchatstate=new int[5];
	 private int[] teststate=new int[5];
	 private int[] testablestate=new int[5];
	 private int defalut;
	  String[] strtest=new String[5];
	 
	 ITelephony initstate;
	 NetworkActivity networkactivity;
	 private static final String WAKE_PATH = "/sys/class/i2c-adapter/i2c-3/3-0008/power_ctrl";
	 TtyDevice ttyDevice;
	 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.network_tt_manage_page);
        
        test_getchatchannel=(TextView)findViewById(R.id.test_getchatchannel);
        test_getchannel=(TextView)findViewById(R.id.test_getchannel);
        test_getablechannel=(TextView)findViewById(R.id.test_getablechannel);
        test_getdefaultchannel=(TextView)findViewById(R.id.test_getdefaultchannel);
        test_netstate=(TextView)findViewById(R.id.test_netstate);
            
        networkactivity = new NetworkActivity(this);
        //显示话音通道借口数据
        teststate = networkactivity.getChannelState();
        for(int i=0;i<5;i++){
        //System.out.println( teststate[i]);
        strtest[i]=String.valueOf(teststate[i]);
        }
        test_getchannel.setText("定位"+""+strtest[0]+""+strtest[1]+""+strtest[2]+""+strtest[3]+""+strtest[4]);
      
 /*      int[] teststate = networkactivity.getChannelState();
        for(int i=0;i<5;i++){
        System.out.println( teststate[i]);
        strtest[i]=String.valueOf(teststate[i]);
        }
        test_getchannel.setText("定位"+""+strtest[0]+""+strtest[1]+""+strtest[2]+""+strtest[3]+""+strtest[4]);*/
      
        //显示文电通道借口数据
/*        testchannlstate= networkactivity.getChannelState();
        for(int i=0;i<5;i++){
        System.out.println(testchannlstate[i]);
        }*/
//        test_getchannel.setText(""+testchannlstate[0]+""+testchannlstate[1]+""+testchannlstate[2]+""+testchannlstate[3]+""+testchannlstate[4]);
       
        //显示文电可用通道接口数据
  /*      testablestate= networkactivity.getChannelState();
        for(int i=0;i<5;i++){
        System.out.println(testablestate[i]);
        }*/
//        test_netstate.setText(""+testablestate[0]+""+testablestate[1]+""+testablestate[2]+""+testablestate[3]+""+testablestate[4]);
      
        //显示默认可用通道接口数据
        defalut=networkactivity.getDefaultChannel();
        test_getdefaultchannel.setText(String.valueOf(defalut));
        
        boolean isair= false;
        
        //显示当前真实网络状态
        strValueispad = SystemPropInvoke.SysPropGet("ro.product.name", null);
        initstate=Itelephony.getTelephonyManager(TTPage.this);
        try{
        	isLTEinit=initstate.isRadioOnMSMS(0);
        	//Log.i("isLTEinit",String.valueOf(isLTEinit));
   //     	isDataConnected=isLTEinit;
   //     	NetworkActivity.getInstance(getApplicationContext()).setLTEConnect(isDataConnected);
  //      	openOrCloseLTE(isDataConnected);
        	isZZWString=SystemPropInvoke.SysPropGet("persist.sys.master.chip.modem", null);
        	//Log.i("isZZWString",String.valueOf(isZZWString));
        	isZZWmianinit=initstate.isRadioOnMSMS(2);
           // Log.i("isZZWmianinit++++++",String.valueOf(isZZWmianinit));
            isZZWminorinit=initstate.isRadioOnMSMS(1);
           // Log.i("isZZWminorinit",String.valueOf(isZZWminorinit));
            isTTinit=initstate.isRadioOnMSMS(3);
            //Log.i("isTTinit",String.valueOf(isTTinit));     
        }catch (Exception e) {
			// TODO: handle exception
			System.err.println("网络状态查询失败！！");
		    }
        if (strValueispad.equals("full_lc1860beta")){  
        	TtyDevice ttyDevice=new TtyDevice();
        	isZHWString	= getString(WAKE_PATH);
        	isJQinit=ttyDevice.isOpened();
        	//Log.i("isZZWString",isZHWString);   
            test_netstate.setText("real"+"tongdao:"+isZZWString+"LTE:"+String.valueOf(isLTEinit)+"ZZWM:"+String.valueOf(isZZWmianinit)+"ZZWS:"+String.valueOf(isZZWminorinit)+"TT:"+String.valueOf(isTTinit)+"zhw"+isZHWString+"JQinit"+String.valueOf(isJQinit)+"isair"+String.valueOf(isair));
        }else{
//            test_netstate.setText(isZZWString+""+isLTEinit+""+isZZWmianinit+""+ isZZWminorinit+""+isTTinit);	
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
