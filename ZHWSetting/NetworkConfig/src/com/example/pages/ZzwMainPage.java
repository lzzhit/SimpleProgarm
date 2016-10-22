package com.example.pages;

import java.security.SignedObject;

import android.R.integer;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.telephony.Itelephony;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.internal.telephony.ITelephony;
import com.contact.service.ContactServiceProxy;
import com.contact.service.IContactService;
import com.contact.service.IContactService.RY_ATTRIBUTEYNAME;
import com.contact.service.IContactService.SB_ATTRIBUTEYNAME;
import com.example.invoke.SystemPropInvoke;
import com.example.leadroidtest.broadcastreceiver.ModemReceiver;
import com.example.leadroidtest.broadcastreceiver.NetworkMergeReceiver;
import com.example.topology.Topology_Ahoc_main;
import com.example.yemian.NetworkActivity;
import com.example.yemian.R;
import com.zkxt.msgPopUp.IMsgPopService;
import com.zkxt.msgPopUp.LabelEntryConfig;
import com.zkxt.msgPopUp.MsgPopServiceProxy;

public class ZzwMainPage extends BasePage {

	private Button btn_set;
	private Button btn_merge;
	private Button btn_net;
	private Button valueset;
	private Button freqset;
	private Button powerset;
	private static Button btn_ahnpower;
	
	private TextView textView_levelnum;
	private TextView textView_subnet;
	private TextView textView_id;
	private TextView textView_sync;
	private TextView textView_frequency;
	private TextView textView_hop;
	private TextView textView_freqpoint;
	private TextView textView_test;
	private TextView textView_check;
	private EditText editText_power;
	public TextView mergeView = null;

	private String sbbh; // 设备号
	private String JBH; // 级别号
	private String BPH; // 编排号
	private String ZH1; // 组号1
	private String TBFS1; // 同步方式1
	private String SBLX; // 设备类型
	private String PLMS; // 频率模式
	private String TPPLJ1; // 调频频率集2
	private String PD1; // 频点2
	private String CTJD1;

	private int levelId; // 级别号
	private int groupId; // 组号
	private int nodeType; // 普通1/簇头0节点
	private int syncType; // 同步方式外同步0/自同步1
	private int[] serialId = new int[5]; // 编排号

	private int frequencyType; // 跳频0/定频1
	private int power;
	private float[] frequency = new float[33];
	private float[] frequency2 = new float[1];
	private int bph = 1082400;
	private String bph2;
	private int[] bph1;

	private String aaa = "111.111-222.222-333.333-444.444-555.555";
	private String[] tpplj1;
	private int vmf;
	public ModemReceiver broadcastReceiver4;
	public NetworkMergeReceiver broadcastReceiver5;
	public boolean isRunning;
	private static int networkmergemain;
	private static int check;
	private static int linkid1;
	private static int linkid2;
	private boolean boolValue;
	private boolean boolValue_setcongfig;
	private boolean boolValue_setfre;
	private boolean boolpowerstate;
	private boolean iscongfigset = true;
	private boolean isfreset = true;
	private boolean zuorli = false;
	private int isRadioSet;
	private int isMergeNetWorkSet;
	ITelephony setAhn;
	ITelephony setAhn1;
	ITelephony Ahnstate;
	IContactService contactservice = null;
	public static zzwThread zzwthread = null;
	
	public static int mzuOrLi = 0; // 0 shibai  1 chenggong 2 zhengzai zu  
	ITelephony setAhnfre;
	private static LabelEntryConfig config = null;
	private static IMsgPopService mMsgPopService = null; 
	private ProgressDialog progressDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.network_zzw_main_page);
		btn_ahnpower = (Button) findViewById(R.id.btn_main_ahnpower);
		setAhn = Itelephony.getTelephonyManager(ZzwMainPage.this);
		Ahnstate = Itelephony.getTelephonyManager(ZzwMainPage.this);
		setAhn1 = Itelephony.getTelephonyManager(ZzwMainPage.this);
		this.mergeView = (TextView) this.findViewById(R.id.netMerge);
/*		if(zzwthread == null){
			zzwthread  = new zzwThread(getApplicationContext());
		}*/
		btn_merge = (Button) findViewById(R.id.btn_main_merge);
		if (Ahnstate != null) {
				boolean boolpowerstate = false;
				int mergeState=0;
				try {
					System.out.println("111111111");
					mergeState= Ahnstate.getAhnMergedStateMSMS(2);
					System.out.println("111111111"+mergeState);
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//0：为初始值；1：为网络融合完成；2：为网络分离完成
				if(mergeState==1){
					btn_merge.setText("网络断开");
				}else{
					btn_merge.setText("网络融合");
				}
				try {
					boolpowerstate = Ahnstate.isRadioOnMSMS(2);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				if (boolpowerstate == false) {
					//Log.i("自组网网络状态", String.valueOf(boolpowerstate));
					btn_ahnpower.setText("组     网");
				} else {
					btn_ahnpower.setText("离     网");

				}
		}

		// 初始化控件
		textView_levelnum = (TextView) findViewById(R.id.textView_main_levelnum);
		textView_subnet = (TextView) findViewById(R.id.textView_main_subnet);
		textView_id = (TextView) findViewById(R.id.textView_main_id);
		textView_sync = (TextView) findViewById(R.id.textView_main_sync);
		textView_frequency = (TextView) findViewById(R.id.textView_main_frequency);
		textView_hop = (TextView) findViewById(R.id.textView_main_hop);
		textView_freqpoint = (TextView) findViewById(R.id.editText_main_freqpoint);
		textView_check = (TextView) findViewById(R.id.textView_main_check);
		// 实例化名录服务
		contactservice = ContactServiceProxy.getInstance();

		vmf = contactservice.getSelfVmf();

		sbbh = contactservice.getNodeAttributeById(vmf,
				RY_ATTRIBUTEYNAME.NODEATTRI_SBBH);
		// 级别号
		JBH = contactservice.getNodeAttributeById(sbbh,
				SB_ATTRIBUTEYNAME.NODEATTRI_JBH);
		textView_levelnum.setText(JBH);
		// 编排号
		BPH = contactservice.getNodeAttributeById(sbbh,
				SB_ATTRIBUTEYNAME.NODEATTRI_BPH);
		if (!BPH.equals("")) {
			serialId = GetBPHIntFromString(BPH);
		}
		textView_subnet.setText("" + serialId[0] + "营" + serialId[1] + "连" + ""
				+ serialId[2] + "排" + "" + serialId[3] + "班" + "" + serialId[4]
				+ "号");
		// 组号1
		ZH1 = contactservice.getNodeAttributeById(sbbh,
				SB_ATTRIBUTEYNAME.NODEATTRI_ZH1);
		textView_id.setText(ZH1);
		// 同步方式1
		TBFS1 = contactservice.getNodeAttributeById(sbbh,
				SB_ATTRIBUTEYNAME.NODEATTRI_TBFS1);
		if (TBFS1.equals("1")) {
			textView_sync.setText("自同步");
		} else if(TBFS1.equals("0")) {
			textView_sync.setText("外同步");
		}
		// 获取终端软件版本号
		// textView_test.setText(phoneManager.getDeviceSoftwareVersionMSMS(1));
		// 设备类型
		SBLX = contactservice.getNodeAttributeById(sbbh,
				SB_ATTRIBUTEYNAME.NODEATTRI_SBLX);
		CTJD1 = contactservice.getNodeAttributeById(sbbh,
				SB_ATTRIBUTEYNAME.NODEATTRI_CTJD1);
		if (CTJD1.equals("1")) {
			textView_frequency.setText("簇头节点");
		} else if(CTJD1.equals("0")) {
			textView_frequency.setText("普通节点");
		}
		// 频率模式
		PLMS = contactservice.getNodeAttributeById(sbbh,
				SB_ATTRIBUTEYNAME.NODEATTRI_PLMS);
		if (PLMS.equals("0")) {
			textView_hop.setText("跳频");
			TPPLJ1 = contactservice.getNodeAttributeById(sbbh,
					SB_ATTRIBUTEYNAME.NODEATTRI_TPPLJ1);
			textView_freqpoint.setText(TPPLJ1);
		} else if(PLMS.equals("1")) {
			textView_hop.setText("定频");
			PD1 = contactservice.getNodeAttributeById(sbbh,
					SB_ATTRIBUTEYNAME.NODEATTRI_PD1);
			
			textView_freqpoint.setText(PD1);
		}
	/*	 try{
	        	int powervalue=setAhn.getAhnRFPowerMSMS(2);            
	        	editText_power.setText(powervalue);
	        }catch (Exception e) {
				e.printStackTrace();
				System.err.println("获取功率失败！");
			}*/
		/*
		 * if(ZH1!=null){ groupId=Integer.parseInt(ZH1); } if(SBLX!=null){
		 * nodeType=Integer.parseInt(SBLX); } if(TBFS1!=null){
		 * syncType=Integer.parseInt(TBFS1); }
		 */

		/*
		 * frequencyType=Integer.parseInt(PLMS); if(frequencyType==0){ String[]
		 * freqMainString = TPPLJ1.split("-");
		 * frequency[0]=freqMainString.length; for(int
		 * i=1;i<freqMainString.length+1;i++){
		 * frequency[i]=Float.parseFloat(freqMainString[i-1]);
		 * Log.i("frequency",String.valueOf(frequency[i])); } }else {
		 * frequency[0]=Float.parseFloat(PD1); }
		 */

		
		btn_merge.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

				//Log.i("networkmerge", String.valueOf(networkmergemain));
				if (btn_merge.getText().equals("网络融合")) {
		//			if (networkmergemain == 1) {
						/*Toast.makeText(getApplicationContext(), "有可以融合的网络",
								Toast.LENGTH_SHORT).show();*/					
						if (setAhn != null) {
								//progressDialog = android.app.ProgressDialog.show(ZzwMainPage.this, "Loading","网络融合..");
							/*progressDialog=new ProgressDialog(ZzwMainPage.this);
							progressDialog.setCancelable(false);
							progressDialog.setMessage("网络融合中");
							progressDialog.show();*/
							
						/*	final Handler handler=new Handler(){

								@Override
								public void handleMessage(Message msg) {
									// TODO Auto-generated method stub
									progressDialog.dismiss();
									super.handleMessage(msg);
								}
								
							};*/
								/*Dialog dialog = new AlertDialog.Builder(ZzwMainPage.this)
								.setTitle("网络融合")
								.setCancelable(false)
								.setMessage("确定改变网络状态？")
								.setNegativeButton("取消",null)
								.setPositiveButton("确定",
										new DialogInterface.OnClickListener() {
											public void onClick(DialogInterface dialog,
													int whichButton) {*/
							
												//发送网络融合指令
												try {
													boolValue = setAhn.mergeAhnNetworkMSMS(2, 1);
												} catch (RemoteException e1) {
													// TODO Auto-generated catch block
													e1.printStackTrace();
												}
												
												/*Thread thread=new Thread(){

													@Override
													public void run() {
														// TODO Auto-generated method stub
														super.run();
														int count=0;
														int mergeState=0;
														try {
															mergeState=setAhn.getAhnMergedStateMSMS(2);
														} catch (RemoteException e1) {
															// TODO Auto-generated catch block
															e1.printStackTrace();
														}
														while(count!=10&&mergeState!=1){
															count++;
															System.out
																	.println("count="+count);
															try {
																mergeState=setAhn.getAhnMergedStateMSMS(2);
															} catch (RemoteException e1) {
																// TODO Auto-generated catch block
																e1.printStackTrace();
															}
															try {
																Thread.sleep(1000);
															} catch (InterruptedException e) {
																// TODO Auto-generated catch block
																e.printStackTrace();
															}
														}
														if(mergeState==1){
															btn_merge.setText("网络断开");
														}else{
															Toast.makeText(getApplicationContext(),
																	"网络融合失败", Toast.LENGTH_SHORT)
																	.show();
														}
														Bundle bundle=new Bundle();
														bundle.putInt("dismiss", 1);
														Message m=new Message();
														m.setData(bundle);
														handler.sendMessage(m);
													}
													
												};
												
												thread.start();*/
												//progressDialog.dismiss();
												//ProgressDialog progressDialog = android.app.ProgressDialog.show(ZzwMainPage.this, "Loading...", "网络融合中..");
												displayModalView("网络融合中..");
												int count=0;
												int mergeState=0;
												try {
													mergeState=setAhn.getAhnMergedStateMSMS(2);
												} catch (RemoteException e1) {
													// TODO Auto-generated catch block
													e1.printStackTrace();
												}
												while(count!=5&&mergeState!=1){
													count++;
													System.out
															.println("count="+count);
													try {
														mergeState=setAhn.getAhnMergedStateMSMS(2);
													} catch (RemoteException e1) {
														// TODO Auto-generated catch block
														e1.printStackTrace();
													}
													try {
														Thread.sleep(1000);
													} catch (InterruptedException e) {
														// TODO Auto-generated catch block
														e.printStackTrace();
													}
												}
												if(mergeState==1){
													dismissModalView();
													btn_merge.setText("网络断开");
												}else{
													dismissModalView();
												}
												/*SelfProgressDialog selfProgressDialog=new SelfProgressDialog(5,0,0,2, ZzwMainPage.this, "Loading...", "网络融合中..");
												int isMergeOk=selfProgressDialog.Show();
												if(isMergeOk==1){
													//progressDialog.dismiss();
													dismissModalView();
													btn_merge.setText("网络断开");
												}*/
											}
										/*}).create();
						dialog.show();*/
								/*if (boolValue == true) {
									Toast.makeText(getApplicationContext(),
											"网络融合成功", Toast.LENGTH_SHORT)
											.show();
									btn_merge.setText("网络断开");
								}*/
						//}
/*					} else {
						Toast.makeText(getApplicationContext(), "没有有可以融合的网络",
								Toast.LENGTH_SHORT).show();
					}*/
					// 调用接口实现网络融合
				} else {				
					if (setAhn != null) {
						try {
							boolValue = setAhn.mergeAhnNetworkMSMS(2, 2);
							//Log.i("boolValuemerge", String.valueOf(boolValue));
							if (boolValue == true) {
								Toast.makeText(getApplicationContext(),
										"网络断开成功", Toast.LENGTH_SHORT).show();
								btn_merge.setText("网络融合");
							}
						} catch (Exception e) {
							// TODO: handle exception
							System.err.println("网络断开失败！！");
						}
					}
				}

			}
		});
		mMsgPopService = MsgPopServiceProxy.getInstance();
		btn_ahnpower.setOnClickListener(new OnClickListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View view) {
				setAhn1 = Itelephony.getTelephonyManager(ZzwMainPage.this);
				if (setAhn1 != null) {
					try {
						if (btn_ahnpower.getText().equals("组     网")) {
							boolean boolzu = false;
							if(mzuOrLi == 0){
								//dangqian weizuwang 
								//xuyaokaishi zuwang 
								boolzu = setAhn1.setAhnPowerMSMS(2, 1);
								if(zzwthread != null){
									//zzwthread.stop();
								}
								zzwthread = new zzwThread(getApplicationContext());
								zzwthread.start();
								
								//Log.i("组网命令", String.valueOf(boolzu));
								Toast.makeText(getApplicationContext(),
										"组网命令已发送！", Toast.LENGTH_SHORT).show();
							}
//							}else if(mzuOrLi == 1){
//								
//							}
							else if(mzuOrLi == 2){
								//zhengzai zuwang 
								Toast.makeText(getApplicationContext(),
										"正在组网中", Toast.LENGTH_SHORT).show();
							}
							
							//btn_ahnpower.setText("离     网");
							
							
//							if (boolzu == true) {
//								zuorli = true;
//								Toast.makeText(getApplicationContext(),
//										"组网已成功", Toast.LENGTH_SHORT).show();
//								 config = new LabelEntryConfig.Builder().setLabelname("zzw")
//				        					.setLabelIcon(R.drawable.qbdwdh_drawable_slidbtnon)
//				        					.setmHoldDuration(Integer.MAX_VALUE)
//				        					.setClickable(false)
//				        					.setIfBlink(false).build();		
//				        		mMsgPopService.showUpLabel(config);	
//							} else {
//								Toast.makeText(getApplicationContext(), "组网失败",
//										Toast.LENGTH_SHORT).show();
//							}
						} else {
							boolean boolli = setAhn1.setAhnPowerMSMS(2, 0);
							//Log.i("离网命令", String.valueOf(boolli));
							if (boolli == true) {
								zuorli = false;
								btn_ahnpower.setText("组     网");
								Toast.makeText(getApplicationContext(), "离网成功",
										Toast.LENGTH_SHORT).show();
								config = new LabelEntryConfig.Builder().setLabelname("zzw")
			        					.setLabelIcon(R.drawable.qbdwdh_drawable_slidbtnoff)
			        					.setmHoldDuration(Integer.MAX_VALUE)
			        					.setClickable(false)
			        					.setIfBlink(false).build();		
			        		mMsgPopService.showUpLabel(config);	
			        			mzuOrLi = 0;
							} else {
								Toast.makeText(getApplicationContext(), "离网失败",
										Toast.LENGTH_SHORT).show();
								//mzuOrLi = 0;
							}
						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
						System.err.println("组网离网失败！！");
					}
				}
			}
		});
		
        valueset=(Button)findViewById(R.id.valueSet);
        valueset.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (JBH != null && !JBH.equals("")) {
					levelId = Integer.parseInt(JBH);
				} else {
					iscongfigset = false;
					Toast.makeText(getApplicationContext(), "编排号缺省",
							Toast.LENGTH_SHORT).show();
				}
				if (ZH1 != null && !ZH1.equals("")) {
					groupId = Integer.parseInt(ZH1);
				} else {
					iscongfigset = false;
					Toast.makeText(getApplicationContext(), "组号缺省",
							Toast.LENGTH_SHORT).show();
				}
				if (CTJD1 != null && !CTJD1.equals("")) {
					nodeType = Integer.parseInt(CTJD1);
				} else {
					iscongfigset = false;
					Toast.makeText(getApplicationContext(), "节点类型缺省",
							Toast.LENGTH_SHORT).show();
				}
				if (TBFS1 != null && !TBFS1.equals("")) {
					syncType = Integer.parseInt(TBFS1);
				} else {
					iscongfigset = false;
					Toast.makeText(getApplicationContext(), "同步方式缺省",
							Toast.LENGTH_SHORT).show();
				}
//				setAhn = Itelephony.getTelephonyManager(ZzwMainPage.this);
				//Log.i("setAhn",""+setAhn);
				if (setAhn != null) {
					try {
						if (iscongfigset) {
							boolValue_setcongfig = setAhn.setAhnConfigMSMS(2,
									levelId, groupId, nodeType, syncType,
									serialId);
							Log.i("打印频率设置参数2222", "" + levelId + "" + groupId + ""
									+ nodeType + "" + syncType);
							Log.i("打印频率设置参数22222", "" + serialId[0] + ""
									+ serialId[1] + "" + "" + serialId[2] + ""
									+ "" + serialId[3] + "" + "" + serialId[4]);
						}
					}catch (Exception e) {
						e.printStackTrace();
						System.err.println("设置失败！");
					}
					if (boolValue_setcongfig == true) {
						Toast.makeText(getApplicationContext(), "参数设置成功",
								Toast.LENGTH_SHORT).show();
					}
			}	
			}
		});	
        freqset=(Button) findViewById(R.id.freqSet);
        freqset.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (PLMS != null && !PLMS.equals("")) {
					frequencyType = Integer.parseInt(PLMS);
					if (frequencyType == 0) {
						if (TPPLJ1!=null&&!TPPLJ1.equals("")) {
							String[] freqMainString = TPPLJ1.split("-");
							if (freqMainString.length > 23
									&& freqMainString.length < 33) {
								frequency[0] = freqMainString.length;
								for (int i = 1; i < freqMainString.length + 1; i++) {
									frequency[i] = Float
											.parseFloat(freqMainString[i - 1]);
									/*Log.i("frequency",
											String.valueOf(frequency[i]));*/
								}
							} else {
								Toast.makeText(getApplicationContext(),
										"跳频频率集不符合规则", Toast.LENGTH_SHORT)
										.show();
								isfreset = false;
							}
						} else {
							Toast.makeText(getApplicationContext(), "跳频缺省",
									Toast.LENGTH_SHORT).show();
							isfreset = false;
						}

					} else {
						if (!PD1.equals("")&&PD1!= null ) {
							frequency2[0] = Float.parseFloat(PD1);
						} else {
							Toast.makeText(getApplicationContext(), "频点缺省",
									Toast.LENGTH_SHORT).show();
							isfreset = false;
						}
					}
				} else {
					Toast.makeText(getApplicationContext(), "频率模式缺省",
							Toast.LENGTH_SHORT).show();
					isfreset = false;
				}
				
//				setAhn = Itelephony.getTelephonyManager(ZzwMainPage.this);
				//Log.i("setAhn",""+setAhn);
				if (setAhn != null) {
					try {						
							if(PLMS.equals("0")){
								for(int i=0;i<33;i++){
									Log.i("跳频频率集22222",""+frequency[i]);
								}
							}else{
							    Log.i("dingping222", ""+frequency2[0]);
							}
						if (isfreset) {
							if (frequencyType == 0) {
								boolValue_setfre = setAhn.setAhnFrequencyMSMS(
										2, frequencyType, frequency);
							} else {
								boolValue_setfre = setAhn.setAhnFrequencyMSMS(
										2, frequencyType, frequency2);
							}
						}
						if (boolValue_setfre == true) {
							Toast.makeText(getApplicationContext(), "频率设置成功",
									Toast.LENGTH_SHORT).show();
						}
					} catch (Exception e) {
						e.printStackTrace();
						System.err.println("设置失败！");
					}
				}
				
			}
		});
        editText_power=(EditText)findViewById(R.id.power);
        editText_power.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				String nums = editText_power.getText().toString(); // 获取输入的频率
				if(nums.isEmpty()){
					return;
				}else if (!nums.isEmpty()&&nums.charAt(0)!='-') // 不为空时
				{
					int nums_changed = Integer.parseInt(nums); // 转为double型
					if (nums_changed > 40) // 越界
					{
						nums_changed = 40; // 强制转换为40
						editText_power.setText(Integer.toString(nums_changed));
					}
				} else if(nums.charAt(0)=='-'&&nums.length()>1){
					int changed = Integer.parseInt(nums.substring(1));
					if(changed>40){
						changed = 40; // 强制转换为40
						editText_power.setText('-'+Integer.toString(changed));
					}
				}
			}
			});
        
        powerset=(Button)findViewById(R.id.powerSet);            
        powerset.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
	/*	    int powervalue=100;
			String strpower=editText_power.getText().toString();
			if (!strpower.isEmpty()&&strpower.charAt(0)!='-') {
		    powervalue=Integer.parseInt(strpower);	
			}else if(strpower.charAt(0)=='-'&&strpower.length()>1){
			powervalue = 0-Integer.parseInt(strpower.substring(1));
			}else if(strpower.isEmpty()){
				Toast.makeText(getApplicationContext(), "功率输入为空", Toast.LENGTH_SHORT).show();
			}
			Log.i("ZzwMainPage",""+powervalue);
			if(powervalue!=100){
				String strpowervalue=editText_power.getText().toString();
				Toast.makeText(getApplicationContext(), strpowervalue , Toast.LENGTH_SHORT).show();
				try{
					boolean powersetvalue=setAhn.setAhnRFPowerMSMS(2, powervalue);
					if(powersetvalue){
						Toast.makeText(getApplicationContext(), "功率设置成功", Toast.LENGTH_SHORT).show();
					}else{
						Toast.makeText(getApplicationContext(), "功率设置失败", Toast.LENGTH_SHORT).show();
					}
				}catch (Exception e) {
					e.printStackTrace();
					System.err.println("功率设置失败！");
				}
			}*/
			
			}
		});
		btn_set = (Button) findViewById(R.id.btn_main_set);
		btn_set.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				if (JBH != null && !JBH.equals("")) {
					levelId = Integer.parseInt(JBH);
				} else {
					iscongfigset = false;
					Toast.makeText(getApplicationContext(), "编排号缺省",
							Toast.LENGTH_SHORT).show();
				}
				if (ZH1 != null && !ZH1.equals("")) {
					groupId = Integer.parseInt(ZH1);
				} else {
					iscongfigset = false;
					Toast.makeText(getApplicationContext(), "组号缺省",
							Toast.LENGTH_SHORT).show();
				}
				if (SBLX != null && !SBLX.equals("")) {
					nodeType = Integer.parseInt(SBLX);
				} else {
					iscongfigset = false;
					Toast.makeText(getApplicationContext(), "设备类型缺省",
							Toast.LENGTH_SHORT).show();
				}
				if (TBFS1 != null && !TBFS1.equals("")) {
					syncType = Integer.parseInt(TBFS1);
				} else {
					iscongfigset = false;
					Toast.makeText(getApplicationContext(), "同步方式缺省",
							Toast.LENGTH_SHORT).show();
				}
				if (PLMS != null && !PLMS.equals("")) {
					frequencyType = Integer.parseInt(PLMS);
					if (frequencyType == 0) {
						if (TPPLJ1!=null&&!TPPLJ1.equals("")) {
							String[] freqMainString = TPPLJ1.split("-");
							if (freqMainString.length > 23
									&& freqMainString.length < 33) {
								frequency[0] = freqMainString.length;
								for (int i = 1; i < freqMainString.length + 1; i++) {
									frequency[i] = Float
											.parseFloat(freqMainString[i - 1]);
									/*Log.i("frequency",
											String.valueOf(frequency[i]));*/
								}
							} else {
								Toast.makeText(getApplicationContext(),
										"跳频频率集不符合规则", Toast.LENGTH_SHORT)
										.show();
								isfreset = false;
							}
						} else {
							Toast.makeText(getApplicationContext(), "跳频缺省",
									Toast.LENGTH_SHORT).show();
							isfreset = false;
						}

					} else {
						if (!PD1.equals("")&&PD1!= null ) {
							frequency2[0] = Float.parseFloat(PD1);
						} else {
							Toast.makeText(getApplicationContext(), "频点缺省",
									Toast.LENGTH_SHORT).show();
							isfreset = false;
						}
					}
				} else {
					Toast.makeText(getApplicationContext(), "频率模式缺省",
							Toast.LENGTH_SHORT).show();
					isfreset = false;
				}
	/*			Log.i("打印频率设置参数", "" + levelId + "" + groupId + ""
						+ nodeType + "" + syncType);
				Log.i("打印频率设置参数", "" + serialId[0] + ""
						+ serialId[1] + "" + "" + serialId[2] + ""
						+ "" + serialId[3] + "" + "" + serialId[4]);*/
				if(PLMS.equals("0")){
					for(int i=0;i<33;i++){
						Log.i("跳频频率集",""+frequency[i]);
					}
				}else{
				    Log.i("dingping", ""+frequency2[0]);
				}
				//Log.i("iscongfigset",""+iscongfigset);
				//Log.i("isfreset",""+isfreset);
				setAhn = Itelephony.getTelephonyManager(ZzwMainPage.this);
				//Log.i("setAhn",""+setAhn);
				if (setAhn != null) {
					try {
						if (iscongfigset) {
							boolValue_setcongfig = setAhn.setAhnConfigMSMS(2,
									levelId, groupId, nodeType, syncType,
									serialId);
							/*Log.i("打印频率设置参数2222", "" + levelId + "" + groupId + ""
									+ nodeType + "" + syncType);
							Log.i("打印频率设置参数22222", "" + serialId[0] + ""
									+ serialId[1] + "" + "" + serialId[2] + ""
									+ "" + serialId[3] + "" + "" + serialId[4]);*/
/*							if(PLMS.equals("0")){
								for(int i=0;i<33;i++){
									Log.i("跳频频率集22222",""+frequency[i]);
								}
							}else{
							    Log.i("dingping222", ""+frequency2[0]);
							}*/
							
						}
						if (isfreset) {
							if (frequencyType == 0) {
								boolValue_setfre = setAhn.setAhnFrequencyMSMS(
										2, frequencyType, frequency);
							} else {
								boolValue_setfre = setAhn.setAhnFrequencyMSMS(
										2, frequencyType, frequency2);
							}
						}
						/*Log.i("MainsetAhnConfigMSMS",
								String.valueOf(boolValue_setcongfig));
						Log.i("MainsetAhnFrequencyMSMS",
								String.valueOf(boolValue_setfre));*/
						if (boolValue_setcongfig == true) {
							Toast.makeText(getApplicationContext(), "参数设置成功",
									Toast.LENGTH_SHORT).show();
						}
						if (boolValue_setfre == true) {
							Toast.makeText(getApplicationContext(), "频率设置成功",
									Toast.LENGTH_SHORT).show();
						}
					} catch (Exception e) {
						e.printStackTrace();
						System.err.println("设置失败！");
					}
				}

			}
		});

		btn_net = (Button) findViewById(R.id.btn_main_net);
		btn_net.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				// 网络拓扑信息显示--wangwei的
				
					Intent intent = null;
					intent = new Intent(ZzwMainPage.this,
							Topology_Ahoc_main.class);
					startActivity(intent);				
	/*			Intent intent = null;
				intent = new Intent(ZzwMainPage.this,
						Topology_Ahoc_main.class);
				startActivity(intent);*/
			}
		});
	}
	private void displayModalView(String text) {
		this.mergeView.setVisibility(View.VISIBLE);
		this.mergeView.setText(text);
	}
	private void dismissModalView() {
		this.mergeView.setVisibility(View.INVISIBLE);
		this.mergeView.setText("");
	}

	public void onResume() {
		super.onResume();
		broadcastReceiver4 = new ModemReceiver();
		broadcastReceiver4.setCallback(new ModemReceiver.Callback() {
			@Override
			public void receive(int linkid, int result) {
				// TODO Auto-generated method stub
				linkid1 = linkid;
				if (linkid == 2 && result == 1) {
					check=1;
					textView_check.setText("成功");
				} else if (linkid == 2 && result == 0) {
					textView_check.setText("失败");
					check=2;
				}
				// Log.i("linkid1",String.valueOf(linkid1));
				// Log.i("自检状态",String.valueOf(result));
			}
		});
		broadcastReceiver5 = new NetworkMergeReceiver();
		broadcastReceiver5.setCallback(new NetworkMergeReceiver.Callback() {
			@Override
			public void receive(int linkid, int result) {
				// TODO Auto-generated method stub
				linkid2 = linkid;
				if (linkid == 2) {
					networkmergemain = result;
				}
				// Log.i("linkid2",String.valueOf(linkid2));
				// Log.i("网络融合",String.valueOf(result));
			}
		});
		IntentFilter filter4 = new IntentFilter(
				ModemReceiver.ACTION_AHN_SELF_CHECK_RESULT);
		registerReceiver(broadcastReceiver4, filter4);
		IntentFilter filter5 = new IntentFilter(
				NetworkMergeReceiver.ACTION_AHN_NETWORK_MERGE);
		registerReceiver(broadcastReceiver5, filter5);
	}

	public void onPause() {
		super.onPause();
		isRunning = false;
		unregisterReceiver(broadcastReceiver4);
		unregisterReceiver(broadcastReceiver5);
	}

	static int[] GetBPHIntFromString(String ss) {
		int bph = Integer.parseInt(ss);
		int[] retval = new int[5];
		retval[0] = (int) ((bph & 0x1800) >> 11);
		retval[1] = (int) ((bph & 0x600) >> 9);
		retval[2] = (int) ((bph & 0x180) >> 7);
		retval[3] = (int) ((bph & 0x60) >> 5);
		retval[4] = (int) ((bph & 0x1F));
		return retval;
	}
	
	public static Handler Zzw = new Handler() {
		public void handleMessage(Message msg) {
			Boolean iszzwon0 = (Boolean)msg.getData().getSerializable("mzuOrLi");
			//int isMergeReady=(int)msg.getData().getInt("isMergeReady");
			//Log.v("12345", String.valueOf(iszzwon0));
			if(iszzwon0){
				btn_ahnpower.setText("离网");
				config = new LabelEntryConfig.Builder().setLabelname("zzw")
		       		      .setLabelIcon(R.drawable.qbdwdh_drawable_slidbtnon)
		       			  .setmHoldDuration(Integer.MAX_VALUE)
		       			  .setmNotifyWhenScreenLocked(false)
		       			  .setClickable(false)
		       			  .setIfBlink(false).build();
		       mMsgPopService.showUpLabel(config);	
			}
			
			/*if(isMergeReady==1){
				btn_merge.setText("网络断开");
			}*/
//			milSymbol megLabel=(milSymbol)msg.getData().getSerializable("loadingLabel");
			
	
	}
	};
}
