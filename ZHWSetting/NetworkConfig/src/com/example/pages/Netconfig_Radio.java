package com.example.pages;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.contact.service.ContactServiceProxy;
import com.contact.service.IContactService;
import com.contact.service.IContactService.RY_ATTRIBUTEYNAME;
import com.contact.service.IContactService.SB_ATTRIBUTEYNAME;
import com.example.receiver.RadioReceiver;
import com.example.sender.Global;
import com.example.sender.RadioSender;
import com.example.yemian.R;

public class Netconfig_Radio extends Activity {
	private static String TAG = "Netconfig_Radio";
	private IContactService contactService = null;
	private int mSelfVmf = -1;
	private String mSelfSbbh = null;
	private TextView edit_send;
	private TextView text_recv;
	private Button btn_sendData;
	private TextView text_recv_times;
	private RadioSender radioSender = null;
	private RadioReceiver radioReceiver = null;
	private boolean btnEnabled = false;

	private EditText edit_ip;
	private EditText  edit_times;
	private Button BTFresh;
	private Button BTDebug;

	private int recv_times = 0;
	private String recvDataString;

	public Netconfig_Radio() {
		// TODO Auto-generated constructor stub

	}

	public Handler mUiHandler = new Handler() {

		// 接受数据
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			// 更新UI
			Bundle mBundle = msg.getData();

			switch (msg.what) {

			case 0xFD:// 收到普通数据
				btn_sendData.setEnabled(btnEnabled);// btnEnabled);
				byte[] recvData = mBundle.getByteArray("DataRecv");
				recv_times ++;
				recvDataString =  new String(recvData);
				text_recv_times.setText("Times: " + recv_times);
				break;
			case 0xFE:
				//finish();
//				Toast.makeText(getApplicationContext(), "网卡初始化失败,请检查后再试!",
//						Toast.LENGTH_LONG).show(); // 显示提示消息框"请输入信道号,范围0-99"

				break;

			case 0xFF:
				btn_sendData.setEnabled(btnEnabled);// btnEnabled);
				break;
			default:
				break;
			}
		}
	};

	// static int iFlag = 0;
	public void changeBtnEnable(boolean enabled) {
		if (btnEnabled != enabled) {
			btnEnabled = enabled;
			try {
				Message msg = new Message();
				msg.what = 0xFF;
				this.mUiHandler.sendMessage(msg);
				System.err.println(msg.what);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void setActivityExit(boolean enabled) {
		if (btnEnabled != enabled) {
			btnEnabled = enabled;
			try {
				Message msg = new Message();
				msg.what = 0xFE;
				this.mUiHandler.sendMessage(msg);
				System.err.println(msg.what);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.network_netconfig_radio);
		contactService = ContactServiceProxy.getInstance();
		mSelfVmf = contactService.getSelfVmf();
		mSelfSbbh = contactService.getNodeAttributeById(mSelfVmf,
				RY_ATTRIBUTEYNAME.NODEATTRI_SBBH);
		String zhwString = contactService.getNodeAttributeById(mSelfSbbh,
				SB_ATTRIBUTEYNAME.NODEATTRI_ZHWIP);


		setTitle("战互网设置");



		edit_send = (EditText) findViewById(R.id.edit_send);
		text_recv = (TextView) findViewById(R.id.text_recv);
		btn_sendData = (Button) findViewById(R.id.BTSendNormalData);

		text_recv_times  = (TextView) findViewById(R.id.text_recv_times);
		edit_ip = (EditText) findViewById(R.id.edit_ip);

		edit_times  = (EditText) findViewById(R.id.edit_times_context);
		BTFresh = (Button) findViewById(R.id.BTFresh);
		BTDebug =  (Button) findViewById(R.id.BTDebug);

		radioSender = new RadioSender(this);
		radioReceiver = new RadioReceiver(this);
		// 开启发送线程和接收线程
		radioReceiver.startRecieving();
		radioSender.startSending();
		// 设置电台进入遥控状态
		Global.ACK = Global.StateEnum.AckTrue;

		BTFresh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				text_recv.setText(recvDataString);
				text_recv_times.setText("Times: " + recv_times);
				}
		});
		BTDebug.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(Global.Debug)
				{
					Global.Debug = false;
					BTDebug.setText("调试: 关");
				}
				else
				{
					Global.Debug = true;
					BTDebug.setText("调试: 开");
				}
			}
		});

		btn_sendData.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String ip = edit_ip.getText().toString();

				String timesStr = edit_times.getText().toString();

				int times = 0;
				if(timesStr.isEmpty())
				{
					times = 1;
				}
				else
					times = Integer.parseInt(timesStr );

				edit_times.setText(times + "");
				Global.ACK = Global.StateEnum.AckTrue;
				if(ip.isEmpty() )
				{
					ip = "168.32.100.61" ;//"168.32.100.61";
					edit_ip.setText("168.32.100.61");
				}
				Global.setIPAdress(ip);
				if(times > 1001 )
				{
					Global.RetryTimes = 100;
				}
				else if(times == 0)
				{
					Global.RetryTimes = 0;
				}
				else
				{
					Global.RetryTimes = times;
				}
				edit_times.setText(Global.RetryTimes + "");

				SendNormalData();
			}
		});


	}


	private boolean CharInRange(char c) {
		boolean result = false;
		if (c >= '0' && c <= '9')
			result = true;
		if (c >= 'a' && c <= 'f')
			result = true;
		if (c >= 'A' && c <= 'F')
			result = true;
		return result;
	}

	private byte StrToByte(String s) {
		return Integer.valueOf(String.valueOf(Integer.parseInt(s, 16)))
				.byteValue();
	}

	private byte[] ChangeHexData(String SData, String UIdata) {
		int datalen = SData.getBytes().length;
		int bytelen = 0;
		byte temp = 0;
		if ((datalen % 2) == 0)
			bytelen = (int) (datalen / 2);
		else
			bytelen = (int) (datalen / 2) + 1;

		byte[] sBuffer = new byte[bytelen];
		int i = 0, j = 0;
		while (i < datalen) {
			while (i >= 0 && (!CharInRange(SData.charAt(i))))
				i++;

			if (((i + 1) >= datalen) || (!CharInRange(SData.charAt(i + 1)))) {
				sBuffer[j] = StrToByte(String.valueOf(SData.charAt(i)));
				j++;
			} else {
				sBuffer[j] = StrToByte(SData.substring(i, i + 2));
				j++;
			}

			i += 2;
		}

		byte[] uiBuffer;
		byte[] sBufferAll;
		try {
			uiBuffer = UIdata.getBytes("UTF-8");
			sBufferAll = new byte[sBuffer.length + uiBuffer.length + 2];
			System.arraycopy(sBuffer, 0, sBufferAll, 0, sBuffer.length);
			System.arraycopy(uiBuffer, 0, sBufferAll, sBuffer.length,
					uiBuffer.length);
			short crc = getCRC(sBufferAll);
			sBufferAll[sBufferAll.length - 2] = StrToByte(Integer
					.toHexString((crc & 0xFF00) >> 8));
			sBufferAll[sBufferAll.length - 1] = StrToByte(Integer
					.toHexString(crc & 0x00FF));// 正确

			// sBuffer[j ++] = StrToByte(Integer.toHexString(crc&0x00FF));
			// sBuffer[j ++] = StrToByte(Integer.toHexString((crc&0xFF00)>>8));
			return sBufferAll;
		} catch (Exception e) {
			Log.e("Send", e.toString());
		}

		return sBuffer;

	}

	short getCRC(byte[] sBuffer) {
		short i, j;
		short stand = (short) 0x8000;
		short crc, flag;
		short length = (short) (sBuffer.length - 2);
		crc = 0x0000;
		for (i = 0; i < length; i++) {
			crc ^= (sBuffer[i]) << 8;
			for (j = 0; j < 8; j++) {
				flag = (short) (crc & stand);
				crc <<= 1;
				if (flag != 0) {
					crc &= 0xfffe;
					crc ^= 0x8005;
				}
			}
		}
		return crc;
	}

	private void SendNormalData() {
		String uiBuffer;
		String prefix = "5500";
		String dataString;
		int length = 0;
		String lengthStr;
		byte[] sBuffer = null;
		
		
		uiBuffer = edit_send.getText().toString();
		length = uiBuffer.length() + 1;
		lengthStr = Integer.toHexString(length);
		//prefix = prefix + "00" +"81" ;
		if(lengthStr.length() == 1)
		{
			prefix = prefix + "000" +lengthStr ;
		}
		else if(lengthStr.length() == 2)
		{
			prefix = prefix + "00" +lengthStr ;
		}
		else if(lengthStr.length() == 3)
		{
			prefix = prefix + "0" +lengthStr ;
		}
		else
		{
			prefix = prefix  +lengthStr ;
		}

		prefix = prefix + "00"; //无包头
		sBuffer = ChangeHexData(prefix, uiBuffer);
		//Global.IgnoreAck = true; //depend on send content
		
		radioSender.addData(sBuffer, sBuffer.length);
	}

	public static byte[] toByteArray(int iSource, int iArrayLen) {
		byte[] bLocalArr = new byte[iArrayLen];

		for (int i = 0; (i < bLocalArr.length) && (i < 4); i++) {
			bLocalArr[i] = (byte) (iSource >> 8 * i & 0xFF);
		}

		return bLocalArr;
	}

	public static int toInt(byte[] bRefArr) {
		int iOutcome = 0;
		byte bLoop;

		for (int i = 0; i < bRefArr.length; i++) {
			bLoop = bRefArr[i];
			iOutcome += (bLoop & 0xFF) << (8 * i);
		}

		return iOutcome;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
//		Thread.currentThread().interrupt();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		radioSender.stopSending();
		radioReceiver.stopRecieving();
		finish();
		super.onDestroy();
	}
}
