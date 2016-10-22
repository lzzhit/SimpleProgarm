package com.example.topology;

import java.util.Random;

import android.app.Activity;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.leadroidtest.broadcastreceiver.ModemReceiver;
import com.example.leadroidtest.broadcastreceiver.NetworkInfo01Receiver;
import com.example.leadroidtest.broadcastreceiver.NetworkInfo02Receiver;
import com.example.leadroidtest.broadcastreceiver.NetworkInfosignalReceiver;
import com.example.leadroidtest.broadcastreceiver.NetworkMergeReceiver;
import com.example.yemian.R;

public class Topology_Ahoc_minor extends Activity {
	public int[] info1=null;
	public int[] info2=null;
	public int[] info3=null;
	public int[] info4=null;
	public int[] info5=null;
	public int[] info6=null;
	public boolean isRunning;
	public NetworkInfo01Receiver broadcastReceiver1;
	public NetworkInfo02Receiver broadcastReceiver2;
	public NetworkInfosignalReceiver broadcastReceiver3;
	public ModemReceiver broadcastReceiver4;
	public NetworkMergeReceiver broadcastReceiver5;
	public TextView textViewNetworksInfo1;
	public TextView textViewNetworksInfo2;
	public TextView textViewNetworkssignal;
	public TextView textViewNetworksAd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.network_topology__ahoc);
		textViewNetworksInfo1 = (TextView) findViewById(R.id.textView_NetworksInfo1);
		textViewNetworksInfo2 = (TextView) findViewById(R.id.textView_NetworksInfo2);
		textViewNetworkssignal = (TextView) findViewById(R.id.textView_Networkssignal);
		textViewNetworksAd=(TextView) findViewById(R.id.textView__NetworksAd);
		StringBuilder sb1 = new StringBuilder();
		sb1.append("\n");
		for (int i = 0; i < 20; i++) {
			sb1.append("节点号" + i + ":");
			sb1.append("\n");
		}
		textViewNetworksAd.setText(sb1);
	}

	@Override
	
	public void onResume() {
		super.onResume();
		broadcastReceiver1 = new NetworkInfo01Receiver();
		broadcastReceiver1.setCallback(new NetworkInfo01Receiver.Callback() {
  
			@Override
			public void receive(int linkId, int[] info) {
				if(info!=null){
					Log.i("广播", "已接收到在线状态");
				}
				if (linkId == 1) {
					info1 = info;
				} else {
					info2 = info;
				}

				StringBuilder sb = new StringBuilder();

/*				if (info2 != null) {
					sb.append("LINKID:" + 2+ "\n");
					for (int i = 0; i < info2.length; i++) {
						if (info2[i] == 1) {
							sb.append("在线");
						} else {
							sb.append("离线");
						}
						sb.append("\n");
					}
				}*/

				if (info1 != null) {
					sb.append("LINKID:" + 1 + "\n");
					for (int i = 0; i < info1.length; i++) {
						if (info1[i] == 1) {
							sb.append("在线");
						} else {
							sb.append("离线");
						}
						sb.append("\n");
					}
				}
				textViewNetworksInfo1.setText(sb);
			}}
			);
		
	    	broadcastReceiver2 = new NetworkInfo02Receiver();
			broadcastReceiver2.setCallback(new NetworkInfo02Receiver.Callback() {
			@Override
				public void receive(int linkId, int[] info) {
				if(info!=null){
					Log.i("广播", "已接收到干扰状态");
				}
					if (linkId == 1) {
						info3 = info;
					} else {
						info4 = info;
					}

					StringBuilder sb = new StringBuilder();

	/*				if (info4 != null) {
						sb.append("LINKID:" + 2 + "\n");
						for (int i = 0; i < info4.length; i++) {
							if (info4[i] == 1) {
								sb.append("有干扰");
							} else {
								sb.append("无干扰");
							}
							sb.append("\n");
						}
					}*/

					if (info3 != null) {
						sb.append("LINKID:" + 1 + "\n");
						for (int i = 0; i < info3.length; i++) {
							if (info3[i] == 1) {
								sb.append("有干扰");
							} else {
								sb.append("无干扰");
							}
							sb.append("\n");
						}
					}
					textViewNetworksInfo2.setText(sb);
				}
		
	    });
			broadcastReceiver3 = new NetworkInfosignalReceiver();
			broadcastReceiver3.setCallback(new NetworkInfosignalReceiver.Callback() {
			@Override
				public void receive(int linkId, int[] info) {
				if(info!=null){
					Log.i("广播", "已接收到信噪状态");
				}
					if (linkId == 1) {
						info5 = info;
					} else {
						info6 = info;
					}

					StringBuilder sb = new StringBuilder();

	/*				if (info6 != null) {
						sb.append("LINKID:" + 2 + "\n");
						for (int i = 0; i < info6.length; i++) {
							sb.append(String.valueOf(info6[i]));
							sb.append("\n");
						}
					}*/

					if (info5 != null) {
						sb.append("LINKID:" + 1 + "\n");
						for (int i = 0; i < info5.length; i++) {
							sb.append(String.valueOf(info5[i]));
							sb.append("\n");
						}
				
					}
					textViewNetworkssignal.setText(sb);
				}
		
	    });
		broadcastReceiver4=new ModemReceiver();
		broadcastReceiver4.setCallback(new ModemReceiver.Callback() {
			@Override
			public void receive(int linkid, int result) {
				// TODO Auto-generated method stub
				Log.i("自检状态",String.valueOf(result));
			}
		});
/*		broadcastReceiver5=new NetworkMergeReceiver();
		broadcastReceiver5.setCallback(new NetworkMergeReceiver.Callback() {
			@Override
			public void receive(int linkid, int result) {
				// TODO Auto-generated method stub
				Log.i("网络融合",String.valueOf(result));
			}
		});*/
		IntentFilter filter1 = new IntentFilter(
				NetworkInfo01Receiver.ACTION_AHN_NETWORK_INFO1);
		registerReceiver(broadcastReceiver1, filter1);
		IntentFilter filter2 = new IntentFilter(
				NetworkInfo02Receiver.ACTION_AHN_NETWORK_INFO2);
		registerReceiver(broadcastReceiver2, filter2);
		IntentFilter filter3 = new IntentFilter(
				NetworkInfosignalReceiver.ACTION_AHN_NETWORK_SIGNAL);
		registerReceiver(broadcastReceiver3, filter3);
		IntentFilter filter4 = new IntentFilter(
				ModemReceiver. ACTION_AHN_SELF_CHECK_RESULT);
		registerReceiver(broadcastReceiver4, filter4);
/*		IntentFilter filter5 = new IntentFilter(
				NetworkMergeReceiver. ACTION_AHN_NETWORK_MERGE);
		registerReceiver(broadcastReceiver5, filter5);*/

		// 妯℃嫙鍙戝箍鎾�
/*		new AsyncTask<Void, Integer, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				isRunning = true;
				while (isRunning) {
					publishProgress(1);
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				return null;
			}

			protected void onProgressUpdate(Integer[] values) {
				Intent intent1 = new Intent(
						NetworkInfo01Receiver.ACTION_AHN_NETWORK_INFO1);
				intent1.putExtra(NetworkInfo01Receiver.INTENT_KEY_LINKID, 1);
				int[] inf01=new int[20];
				inf01=zzwrandom1();
				intent1.putExtra(NetworkInfo01Receiver.INTENT_KEY_AHN_INFO1, inf01);
				sendBroadcast(intent1);
				//
				Intent intent2 = new Intent(
						NetworkInfo02Receiver.ACTION_AHN_NETWORK_INFO2);
				intent2.putExtra(NetworkInfo02Receiver.INTENT_KEY_LINKID, 1);
				int[] inf02=new int[20];
				inf02=zzwrandom1();
				intent2.putExtra(NetworkInfo02Receiver.INTENT_KEY_AHN_INFO2, inf02);
				sendBroadcast(intent2);
				//
				Intent intents = new Intent(
						NetworkInfosignalReceiver.ACTION_AHN_NETWORK_SIGNAL);
				intents.putExtra(NetworkInfosignalReceiver.INTENT_KEY_LINKID, 1);
				int[] infsignal=new int[20];
				infsignal=zzwrandom2();
				intents.putExtra(NetworkInfosignalReceiver.INTENT_KEY_AHN_SIGNAL, infsignal);
				sendBroadcast(intents);
			}
		}.execute();*/
	}

	@Override
	public void onPause() {
		super.onPause();
		isRunning = false;
		unregisterReceiver(broadcastReceiver1);
		unregisterReceiver(broadcastReceiver2);
		unregisterReceiver(broadcastReceiver3);
		unregisterReceiver(broadcastReceiver4);
//		unregisterReceiver(broadcastReceiver5);
	}
	public int[] zzwrandom1(){
		int []a=new int[20];   
		for (int i = 0; i < 20; i++) {
			a[i]= Math.random()>0.5?1:0;			
		}
		return a;
	}
	public int[] zzwrandom2(){
		int[] a = new int[20];  
		Random ranNum = new Random();
		for (int i = 0; i < 20; i++) {
			a[i]= ranNum.nextInt(199)-99;			
		}
		return a;
	}
}
