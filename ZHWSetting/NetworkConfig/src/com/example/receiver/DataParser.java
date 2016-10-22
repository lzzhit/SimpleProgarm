package com.example.receiver;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.example.pages.Netconfig_Radio;
import com.example.sender.Global;
import com.example.sender.RadioData;

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


                    if (cmdID[0] == (byte) 0x03 && cmdID[1] == (byte) 0x80) {
                        Global.ACK = Global.StateEnum.AckTrue;
                        Log.i(LOG, "Global.ACK = AckTrue");
                    } else if (cmdID[0] == (byte) 0x03 && cmdID[1] == (byte) 0x81) {
                        Global.ACK = Global.StateEnum.AckFalse;
                        Log.i(LOG, "Global.ACK = AckFalse");
                    } else if (cmdID[0] == (byte) 0x00 && cmdID[1] == (byte) 0x80) {
                        // 收到上报的软件版本
                        Global.ACK = Global.StateEnum.AckTrue;
                        byte[] radio = new byte[4];
                        System.arraycopy(tmpData, 4, radio, 0, 4);
                        try {
                            String strRadioInfo = BcdToStr(radio);
                            Message msg = new Message();
                            Bundle mBundle = new Bundle();
                            mBundle.putString("SoftVersion", strRadioInfo);
                            msg.what = 1;
                            msg.setData(mBundle);
                            radioActivity.mUiHandler.sendMessage(msg);
                            System.err.println(msg.what);
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    } else if (cmdID[0] == (byte) 0x00 && cmdID[1] == (byte) 0x81) {
                        // 收到上报的时间
                        Global.ACK = Global.StateEnum.AckTrue;
                        byte[] radioTime = new byte[7];
                        for (int i = 0; i < 7; i++) {
                            Log.i("data" + i, "" + (radioTime[i] & 0xFF));
                        }
                        System.arraycopy(tmpData, 4, radioTime, 0, 7);
                        try {
                            String strtime = BcdToStr(radioTime);
                            //System.out.println("str"+strtime);
                            Message msg = new Message();
                            Bundle mBundle = new Bundle();
                            mBundle.putString("TimeQuery", strtime);
                            msg.what = 2;
                            msg.setData(mBundle);
                            radioActivity.mUiHandler.sendMessage(msg);

                        } catch (Exception e) {
                            // TODO: handle exception
                            e.printStackTrace();
                        }
                    } else if (cmdID[0] == (byte) 0x00 && cmdID[1] == (byte) 0x83) {
                        // 收到上报的MAC地址
                        Global.ACK = Global.StateEnum.AckTrue;
                        byte[] iMac = new byte[1];
                        System.arraycopy(tmpData, 4, iMac, 0, 1);

//					System.err.println("iMac = "+ iMac[0]);

                        try {
                            String strMac = Integer.toString(iMac[0] & 0xFF);
                            Message msg = new Message();
                            Bundle mBundle = new Bundle();
                            mBundle.putString("MAC", strMac);
                            msg.what = 3;
                            msg.setData(mBundle);
                            radioActivity.mUiHandler.sendMessage(msg);
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    } else if (cmdID[0] == (byte) 0x00 && cmdID[1] == (byte) 0x84) {
                        // 收到上报的IP地址
                        byte[] radioIP = new byte[4];
                        Global.ACK = Global.StateEnum.AckTrue;
                        System.arraycopy(tmpData, 4, radioIP, 0, 4);
                        try {
                            Message msg = new Message();
                            Bundle mBundle = new Bundle();
                            mBundle.putByteArray("IPAddress", radioIP);
                            msg.what = 4;
                            msg.setData(mBundle);
                            radioActivity.mUiHandler.sendMessage(msg);

                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    } else if (cmdID[0] == (byte) 0x00 && cmdID[1] == (byte) 0x85) {
                        // 收到上报ICU IP地址掩码
                        Global.ACK = Global.StateEnum.AckTrue;
                        byte[] radioNetMask = new byte[1];
                        System.arraycopy(tmpData, 4, radioNetMask, 0, 1);
                        try {
                            String strNetMask = Integer.toString(radioNetMask[0] & 0xFF);
                            Message msg = new Message();
                            Bundle mBundle = new Bundle();
                            mBundle.putString("NetMask", strNetMask);
                            msg.what = 5;
                            msg.setData(mBundle);
                            radioActivity.mUiHandler.sendMessage(msg);
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    } else if (cmdID[0] == (byte) 0x00 && cmdID[1] == (byte) 0x86) {
                        // 收到上报功率值
                        Global.ACK = Global.StateEnum.AckTrue;
                        byte[] radioPower = new byte[1];
                        System.arraycopy(tmpData, 4, radioPower, 0, 1);
                        byte PowerValue = radioPower[0];
                        try {

                            Message msg = new Message();
                            Bundle mBundle = new Bundle();
//						mBundle.putByteArray("PowerQuery", radioPower);
                            mBundle.putByte("PowerQuery", PowerValue);
                            msg.what = 6;
                            msg.setData(mBundle);
                            radioActivity.mUiHandler.sendMessage(msg);
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    } else if (cmdID[0] == (byte) 0x00 && cmdID[1] == (byte) 0x8C) {
                        // 上报当前信道方式
                        byte[] radioAccess = new byte[1];
                        Global.ACK = Global.StateEnum.AckTrue;
                        System.arraycopy(tmpData, 4, radioAccess, 0, 1);
                        byte AccessValue = radioAccess[0];
                        try {
                            Message msg = new Message();
                            Bundle mBundle = new Bundle();
//						mBundle.putByteArray("AccessQuery", radioAccess);
                            mBundle.putByte("AccessQuery", AccessValue);
                            msg.what = 7;
                            msg.setData(mBundle);
                            radioActivity.mUiHandler.sendMessage(msg);
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    } else if (cmdID[0] == (byte) 0x01 && cmdID[1] == (byte) 0x80) {
                        // 上报当前使用的信道号
                        byte[] radioNum = new byte[1];
                        Global.ACK = Global.StateEnum.AckTrue;
                        System.arraycopy(tmpData, 4, radioNum, 0, 1);
//					String strNum = BcdToStr(radioNum);
                        int radionumber = Integer.parseInt(BcdToStr(radioNum));
                        String strNum = String.valueOf(radionumber);
                        try {
                            Message msg = new Message();
                            Bundle mBundle = new Bundle();
                            mBundle.putString("NumQuery", strNum);
                            msg.what = 8;
                            msg.setData(mBundle);
                            radioActivity.mUiHandler.sendMessage(msg);
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    } else if (cmdID[0] == (byte) 0x01 && cmdID[1] == (byte) 0x81) {
                        // ICU上报当前使用的信道参数
                        byte[] radioPara = new byte[14];
                        Global.ACK = Global.StateEnum.AckTrue;
                        System.arraycopy(tmpData, 4, radioPara, 0, 14);
                        String strAccessParam = BcdToStr(radioPara);
                        //System.out.println("Para"+strAccessParam);
                        try {
                            Message msg = new Message();
                            Bundle mBundle = new Bundle();
                            mBundle.putString("ParaQuery", strAccessParam);
                            msg.what = 9;
                            msg.setData(mBundle);
                            radioActivity.mUiHandler.sendMessage(msg);
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    } else if (cmdID[0] == (byte) 0x03 && cmdID[1] == (byte) 0x83) {
                        //ICU上报自检结果
                        byte[] radioCheck = new byte[16];
                        Global.ACK = Global.StateEnum.AckTrue;
                        System.arraycopy(tmpData, 4, radioCheck, 0, 16);
                        try {
                            Message msg = new Message();
                            Bundle mBundle = new Bundle();
                            mBundle.putByteArray("SelfCheckQuery", radioCheck);
                            msg.what = 10;
                            msg.setData(mBundle);
                            radioActivity.mUiHandler.sendMessage(msg);
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    } else if (cmdID[0] == (byte) 0x03 && cmdID[1] == (byte) 0x82) {

                        if (iCount == 5) {
                            //ICU上报工作状态信息
                            byte[] radioStatus = new byte[16];
                            Global.ACK = Global.StateEnum.AckTrue;
                            System.arraycopy(tmpData, 4, radioStatus, 0, 16);
                            try {
                                Message msg = new Message();
                                Bundle mBundle = new Bundle();
                                mBundle.putByteArray("StatusQuery", radioStatus);
                                msg.what = 11;
                                msg.setData(mBundle);
                                radioActivity.mUiHandler.sendMessage(msg);
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        } else {
                            iCount = iCount + 1;
                        }

                        if (iCount == 10) {
                            iCount = 0;
                        }

                    } else if (cmdID[0] == (byte) 0x55 && cmdID[1] == (byte) 0x00) {
                        //int dataLength =129;

                        int dataLength = tmpData[2] & 0xff;
                        dataLength = dataLength * 16 * 16;
                        dataLength += tmpData[3] & 0xff;
                        byte[] recvData = new byte[dataLength - 1];

                        //Global.ACK = Global.StateEnum.AckTrue;
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
                        //Global.ACK = Global.StateEnum.AckWaiting;
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
