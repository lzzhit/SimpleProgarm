package com.example.leadroidtest.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NetworkInfo01Receiver extends BroadcastReceiver {
	public static final String ACTION_AHN_NETWORK_INFO1 = "android.intent.action.AHN_NETWORK_INFO1";
	public static final String INTENT_KEY_LINKID = "linkID";
	public static final String INTENT_KEY_AHN_INFO1 = "ahnInfo1";

	private Callback callback;

	@Override
	public void onReceive(Context context, Intent intent) {
		int linkId = intent.getIntExtra(INTENT_KEY_LINKID, -1);
		int[] info = intent.getIntArrayExtra(INTENT_KEY_AHN_INFO1);

		if (callback != null) {
			callback.receive(linkId, info);
		}

	}

	public void setCallback(Callback callback) {
		this.callback = callback;
	}

	public interface Callback {
		void receive(int linkId, int[] info);
	}

}
