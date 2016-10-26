package com.example.receiver;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;

import com.example.pages.Netconfig_Radio;
import com.example.sender.RadioData;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class DataParser implements Runnable {
    private static String LOG = "DataParser";

    boolean isRunning = false;

    // 统计上报次数
    private int iCount = 0;

    private static DataParser dataParser = null;
    private static List<RadioData> dataList = null;

    private static Netconfig_Radio radioActivity;

    private DataParser() {
        // TODO Auto-generated constructor stub
        this.dataList = Collections
                .synchronizedList(new LinkedList<RadioData>());
    }

    public static DataParser getInstance(Context mContext) {
        if (dataParser == null) {
            dataParser = new DataParser();
        }

        radioActivity = (Netconfig_Radio) mContext;
        return dataParser;
    }

    /*
     * add Data to be parser
     *
     * @ data:the data recieved from server
     *
     * @ size:data size
     */
    public void addData(byte[] data, int size) {
        RadioData adata = new RadioData();
        adata.setSize(size);
        byte[] tempData = new byte[size];
        System.arraycopy(data, 0, tempData, 0, size);
        adata.setRealData(tempData);
        dataList.add(adata);
        //Log.e(LOG, "添加一次数据 " + dataList.size());
    }

    public void startDataParser() {
        new Thread(this).start();
    }


    @Override
    public void run() {
        // TODO Auto-generated method stub
        Looper.prepare();

        //Netconfig_Radio radioConfig = new Netconfig_Radio();

        this.isRunning = true;

        while (isRunning) {
            int dataListSize = 0;

            try {
                Thread.sleep(20);
            } catch (Exception e) {
                // TODO: handle exception
            }

            if ((dataList.size()) > 0) {
                RadioData data = null;
                //data = dataList.remove(0);

                //Log.i("dataList",""+dataList.size());
                try {
                    if (dataList.size() > 0) {
                        data = dataList.remove(0);
                        // Log.i(LOG, "remove ok");
                    } else {
                        continue;
                    }


                    //System.err.println(data);
                    byte[] tmpData = new byte[data.getSize()];
                    int size = data.getSize();

                    // 解析数据Buffer
                    //	byte[] radioInfo = new byte[BUFFLEN];

                    System.arraycopy(data.getRealData(), 0, tmpData, 0, size);

                    /**
                     * 解析上报的电台信息
                     * @param data 接收到的数据
                     * @param size 数据长度
                     */
//				if (tmpData.length > 0) {
                    // 用于存取上报的信令ID
                    byte[] cmdID = new byte[2];
                    System.arraycopy(tmpData, 0, cmdID, 0, 2);


                    if (cmdID[0] == (byte) 0x55 && cmdID[1] == (byte) 0x00) {
                        //int dataLength =129;

                        int dataLength = tmpData[2] & 0xff;
                        dataLength = dataLength * 16 * 16;
                        dataLength += tmpData[3] & 0xff;
                        byte[] recvData = new byte[dataLength - 1];

                        System.arraycopy(tmpData, 5, recvData, 0, dataLength - 1);
                        try {
                            Message msg = new Message();
                            Bundle mBundle = new Bundle();
                            mBundle.putByteArray("DataRecv", recvData);
                            msg.what = 0xFD;
                            msg.setData(mBundle);
                            radioActivity.mUiHandler.sendMessage(msg);
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    } else {
//						Log.i(LOG, "Global.ACK = " + Global.ACK );
                        ;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * BCD码转字符串
     *
     * @param bytes 输入的BCD码
     * @return 字符串
     */
    public static String BcdToStr(byte[] bytes) {

        try {
            String str = new String(bytes, "UTF-8");
            String outStr = "";
            char lowMark = 0x0f;
            char highMark = 0xf0;
            byte[] temp = str.getBytes();
            for (int i = 0; i < bytes.length; i++) {
                String strLow = String.valueOf(bytes[i] & lowMark);
                String strHigh = String.valueOf((bytes[i] & highMark) >> 4);
                outStr = outStr + strHigh + strLow;
            }
            return outStr;
        } catch (Exception e) {
            // TODO: handle exception
            return null;
        }

    }

    public void stopDataParser() {
        this.isRunning = false;
        Thread.currentThread().interrupt();
        radioActivity.mUiHandler.removeCallbacks(dataParser);
    }
}
