package com.example.leadroidtest.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 网络融合指示
 * 
 * @author shragy
 * 
 */
public class NetworkMergeReceiver extends BroadcastReceiver {
	public static final String ACTION_AHN_NETWORK_MERGE = "android.intent.action.AHN_NETWORK_MERGE";
	public static final String INTENT_KEY_LINKID = "linkID";
	public static final String INTENT_KEY_AHN_MERGE = "ahnMerge";

	private Callback callback;

	@Override
	public void onReceive(Context context, Intent intent) {
 		int linkid = intent.getIntExtra(INTENT_KEY_LINKID, -1);
		int result = intent.getIntExtra(INTENT_KEY_AHN_MERGE, -2);

		if (callback != null) {
			callback.receive(linkid, result);
		}
	}

	public void setCallback(Callback callback) {
		this.callback = callback;
	}

	public interface Callback {
		void receive(int linkid, int result);
	}

}
