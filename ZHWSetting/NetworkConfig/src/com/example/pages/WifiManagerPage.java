package com.example.pages;

import android.app.AlertDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;






import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.example.wifi.LinkWifi;
import com.example.wifi.WifiListAdapter;
import com.example.yemian.R;


public class WifiManagerPage extends BasePage {

    private WifiManager wifiManager = null;
    public SetWifiHandler setWifiHandler;
    private ListView wifiListView;
    private LinkWifi linkWifi;
    private List<ScanResult> newWifList = new ArrayList<ScanResult>();
    private WifiListAdapter wifiListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.network_wifi_page);
        linkWifi = new LinkWifi(this);
        wifiManager = (WifiManager)getSystemService(Service.WIFI_SERVICE);
        setWifiHandler = new SetWifiHandler(Looper.getMainLooper());
        wifiListView = (ListView) findViewById(R.id.wifi_list);

        regWifiReceiver();
        wifiManager.startScan();
    }



    private void regWifiReceiver() {
        IntentFilter labelIntentFilter = new IntentFilter();
        labelIntentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        labelIntentFilter.addAction("android.net.wifi.STATE_CHANGE");
        labelIntentFilter.setPriority(1000);
        this.registerReceiver(wifiResultChange, labelIntentFilter);

    }


    private final BroadcastReceiver wifiResultChange = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (action.equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
                    || action.equals("android.net.wifi.STATE_CHANGE")) {
                showWifiList();
            }
        }
    };

    private void showWifiList() {
        List<ScanResult> wifiList = wifiManager.getScanResults();

        boolean isAdd;

        if (wifiList != null) {
            for (int i = 0; i < wifiList.size(); i++) {
                isAdd = true;
                for (int j = 0; j < newWifList.size(); j++) {
                    if (newWifList.get(j).SSID.equals(wifiList.get(i).SSID)) {
                        isAdd = false;
                        if (newWifList.get(j).level < wifiList.get(i).level) {
                            newWifList.remove(j);
                            newWifList.add(wifiList.get(i));
                            break;
                        }
                    }
                }
                if (isAdd)
                    newWifList.add(wifiList.get(i));
            }
        }
        wifiListAdapter = new WifiListAdapter(this, newWifList,
                setWifiHandler);
        wifiListView.setAdapter(wifiListAdapter);

        int connectedId = wifiManager.getConnectionInfo().getNetworkId();
        ScanResult scanResult = null;
        for(ScanResult result : newWifList){
            if(linkWifi.IsExsits(result.SSID) != null) {
                if (connectedId == linkWifi.IsExsits(result.SSID).networkId)
                    scanResult = result;
            }
        }
        if(scanResult !=null )
            moveConnectedToFirst(scanResult);


    }

    private void moveConnectedToFirst(ScanResult result) {

        newWifList.remove(result);
        newWifList.add(0, result);
        wifiListAdapter.notifyDataSetChanged();


    }

    public class SetWifiHandler extends Handler {
        public SetWifiHandler(Looper mainLooper) {
            super(mainLooper);
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    ScanResult wifiInfo = (ScanResult) msg.obj;
                    configWifiRelay(wifiInfo);
                    break;

            }
        }
    }

    private void configWifiRelay(final ScanResult wifiInfo) {

        if (linkWifi.IsExsits(wifiInfo.SSID) != null) {
            final int netID = linkWifi.IsExsits(wifiInfo.SSID).networkId;

            String actionStr;
            if (wifiManager.getConnectionInfo().getNetworkId() == netID) {
                actionStr = "断开";
            } else {
                actionStr = "连接";
            }

            new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("请选择你要进行的操作")
                    .setPositiveButton(actionStr,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {

                                    if (wifiManager.getConnectionInfo().getNetworkId() == netID) {
                                        wifiManager.disconnect();
                                    } else {
                                        WifiConfiguration config = linkWifi.IsExsits(wifiInfo.SSID);
                                        linkWifi.setMaxPriority(config);
                                        boolean status = linkWifi.ConnectToNetID(config.networkId);
                                        if(status)
                                            moveConnectedToFirst(wifiInfo);
                                    }

                                }
                            })
                    .setNeutralButton("忘记",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    wifiManager.removeNetwork(netID);
                                }
                            })
                    .setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.dismiss();
                                }
                            }).show();

            return;
        }

        String capabilities;

        if (wifiInfo.capabilities.contains("WPA2-PSK")) {
            capabilities = "psk2";
        } else if (wifiInfo.capabilities.contains("WPA-PSK")) {
            capabilities = "psk";
        } else if (wifiInfo.capabilities.contains("WPA-EAP")) {
            capabilities = "eap";
        } else if (wifiInfo.capabilities.contains("WEP")) {
            capabilities = "wep";
        } else {
            capabilities = "";
        }

        if (!capabilities.equals("")) {

            LayoutInflater factory = LayoutInflater.from(this);
            final View inputPwdView = factory.inflate(R.layout.network_wifi_input_dialog,null);
            new AlertDialog.Builder(this)
                    .setTitle("请输入密码")
                    .setMessage("无线SSID： " + wifiInfo.SSID)
                    .setView(inputPwdView)
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    EditText pwd = (EditText) inputPwdView
                                            .findViewById(R.id.dialog_password);
                                    String wifipwd = pwd.getText().toString();
                                    int netID = linkWifi.CreateWifiInfo2(wifiInfo, wifipwd);
                                    boolean status = linkWifi.ConnectToNetID(netID);
                                    if(status)
                                        moveConnectedToFirst(wifiInfo);
                                }
                            })
                    .setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).setCancelable(false).show();

        } else {
            new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("连接无密码wifi有风险，是否继续连接")
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    int netID = linkWifi.CreateWifiInfo2(wifiInfo, "");
                                    boolean status = linkWifi.ConnectToNetID(netID);
                                    if(status)
                                        moveConnectedToFirst(wifiInfo);
                                }
                            })
                    .setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int whichButton) {
                                }
                            }).show();

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(wifiResultChange);
    }

}
