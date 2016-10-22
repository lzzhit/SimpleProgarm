package com.example.leadroidtest.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 自检
 * @author shragy
 *
 */
public class ModemReceiver extends BroadcastReceiver {
	public static final String ACTION_AHN_SELF_CHECK_RESULT ="android.intent.action.AHN_SELF_CHECK_RESULT";
	public static final String INTENT_KEY_LINKID = "linkID";
	public static final String INTENT_KEY_AHN_RESULT = "ahnResult";
	
	private Callback callback;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		int linkid = intent.getIntExtra(INTENT_KEY_LINKID, -1);
		int result = intent.getIntExtra(INTENT_KEY_AHN_RESULT,-1);
		
		if(callback!=null){
			callback.receive(linkid, result);
		}
	}
	
	public void setCallback(Callback callback) {
		this.callback = callback;
	}
	
	public interface Callback {
		void receive(int linkid,int result);
	}

}
