package com.example.pages;

import com.android.internal.telephony.ITelephony;
import com.contact.service.ContactServiceProxy;
import com.contact.service.IContactService;
import com.contact.service.IContactService.RY_ATTRIBUTEYNAME;
import com.contact.service.IContactService.SB_ATTRIBUTEYNAME;
import com.example.leadroidtest.broadcastreceiver.ModemReceiver;
import com.example.leadroidtest.broadcastreceiver.NetworkMergeReceiver;
import com.example.topology.Topology_Ahoc_minor;
import com.example.yemian.R;

import android.R.integer;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.RemoteException;
import android.telephony.Itelephony;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;



/**
 * Created by yangmingyue on 16/8/5.
 */
public class ZzwMinorPage extends BasePage {
	private Button btn_set;
	private Button btn_merge;
	private Button btn_net;
	private Button btn_ahnpower;
	private TextView textView_levelnum;
	private TextView textView_subnet;
	private TextView textView_id;
	private TextView textView_sync;
	private TextView textView_frequency;
	private TextView textView_hop;
	private TextView textView_freqpoint;
	private TextView textView_test;
	private TextView textView_check;
	private int isMergeNetWorkSet;
	TelephonyManager Manager;
	
	private String sbbh;        //设备号
	private String JBH;			//级别号
	private String BPH;			//编排号
	private String ZH2;			//组号2
	private String TBFS2;		//同步方式2
	private String SBLX;		//设备类型
	private String PLMS;		//频率模式
	private String TPPLJ2;		//调频频率集2
	private String PD2;			//频点2
	private String CTJD2;       //节点类型2
	
	private int levelId;       //级别号
	private int groupId;       //组号
	private int nodeType;     //普通1/簇头0节点
	private int syncType;     //同步方式外同步0/自同步1
	private int[] serialId= new int[5]; //编排号
	
	private int frequencyType;    //跳频0/定频1 
	private float[] frequency=new float[33]; 
	private float[] frequency2=new float[1];
	
	private int bph=1082400;
	private String bph2;
	private int[] bph1;
	private String[] strserialId= new String[5];
	private String aaa="111.111-222.222-333.333-444.444-555.555";
	private String[] tpplj1; 
	private int vmf;
	private boolean boolValue;
	private boolean boolValue1;
	private boolean boolpowerstate;
	private boolean isconfigset=true;
	private boolean isfreset=true;
	private boolean boolValue_setconfig;

	private boolean boolValue_setfre;
	private boolean zuorli;
	public ModemReceiver broadcastReceiver4;
	public NetworkMergeReceiver broadcastReceiver5;
	public boolean isRunning;
	private static int networkmerge;
	private static int check;
	private static int linkid1;
	private static int linkid2;
	IContactService contactservice = null;
	ITelephony  setAhn;
	ITelephony  setAhn1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.network_zzw_minor_page);
        btn_ahnpower = (Button)findViewById(R.id.btn_minor_ahnpower);
        setAhn = Itelephony.getTelephonyManager(ZzwMinorPage.this);
        btn_merge = (Button)findViewById(R.id.btn_minor_merge);
        if (setAhn !=null) {
        	int mergeState=0;
			try {
				mergeState= setAhn.getAhnMergedStateMSMS(1);
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
				boolpowerstate=setAhn.isRadioOnMSMS(1);
        		if( boolpowerstate==false){   	
            	   Log.i("自组网网络状态", String.valueOf(boolValue));
            	   btn_ahnpower.setText("组网");
            	    }else{
            	   btn_ahnpower.setText("离网"); 
            	     } 
			} catch (Exception e) {
				// TODO: handle exception
				System.err.println("获取自组网组网离网状态失败！！");
			    }
          }		
        /*		String IMEI = ((TelephonyManager)(this.getSystemService(Context.TELEPHONY_SERVICE))).getDeviceId();
		System.out.println("my IMEI is " + IMEI);
		int result = 0;
		if ("000000000000000".equals(IMEI)) {
			result = contactservice.setSelfSbbh("1000000000000010");//保护至此，设置成手机
			IMEI = "1000000000000010";
		}else if("0000000000000000".equals(IMEI)){
			result = contactservice.setSelfSbbh("1000000000000010");//保护至此，设置成手机
			IMEI = "1000000000000010";
		}else {
			result = contactservice.setSelfSbbh(IMEI);//正常设置
		}*/
		
		
		
		//初始化控件
     	textView_levelnum = (TextView)findViewById(R.id.textView_minor_levelnum);
		textView_subnet = (TextView)findViewById(R.id.textView_minor_subnet);
		textView_id = (TextView)findViewById(R.id. textView_minor_id);
		textView_sync = (TextView)findViewById(R.id. textView_minor_sync); 
		textView_frequency = (TextView)findViewById(R.id. textView_minor_frequency); 
		textView_hop = (TextView)findViewById(R.id. textView_minor_hop);
		textView_freqpoint = (TextView)findViewById(R.id.editText_minor_freqpoint);
		textView_check=(TextView)findViewById(R.id.textView_minor_check);
		//实例化名录服务
		contactservice = ContactServiceProxy.getInstance();
		
		vmf=contactservice.getSelfVmf();
	
		sbbh=contactservice.getNodeAttributeById(vmf,RY_ATTRIBUTEYNAME.NODEATTRI_SBBH);				
		//级别号
		JBH = contactservice.getNodeAttributeById(sbbh, SB_ATTRIBUTEYNAME.NODEATTRI_JBH);
		textView_levelnum.setText(JBH);		
		//编排号
		BPH = contactservice.getNodeAttributeById(sbbh, SB_ATTRIBUTEYNAME.NODEATTRI_BPH);
		if(!BPH.equals("")){
		serialId=GetBPHIntFromString(BPH);
		}
        textView_subnet.setText(""+serialId[0]+"营"+serialId[1]+"连"+""+serialId[2]+"排"+""+serialId[3]+"班"+""+serialId[4]+"号");       
        //组号2
        ZH2 = contactservice.getNodeAttributeById(sbbh, SB_ATTRIBUTEYNAME.NODEATTRI_ZH2);
        textView_id.setText(ZH2);      
        //同步方式2
        TBFS2 = contactservice.getNodeAttributeById(sbbh, SB_ATTRIBUTEYNAME.NODEATTRI_TBFS2);
        if (TBFS2.equals("1")) {
	    	textView_sync.setText("自同步");
	    }
	    else if(TBFS2.equals("0")){
	    	textView_sync.setText("外同步");
		}
        // 获取终端软件版本号
//        textView_test.setText(phoneManager.getDeviceSoftwareVersionMSMS(1));     
        //设备类型
        SBLX = contactservice.getNodeAttributeById(sbbh, SB_ATTRIBUTEYNAME.NODEATTRI_SBLX);
        CTJD2 =contactservice.getNodeAttributeById(sbbh, SB_ATTRIBUTEYNAME.NODEATTRI_CTJD2);
        if ( CTJD2.equals("1")) {
	    	textView_frequency.setText("簇头节点");
	    }
	    else if(CTJD2.equals("0")) {
	    	textView_frequency.setText("普通节点");
		}        
        //频率模式
        PLMS= contactservice.getNodeAttributeById(sbbh, SB_ATTRIBUTEYNAME.NODEATTRI_PLMS);
        if (PLMS.equals("0")) {
        	textView_hop.setText("跳频");
        	TPPLJ2= contactservice.getNodeAttributeById(sbbh, SB_ATTRIBUTEYNAME.NODEATTRI_TPPLJ2);
		    textView_freqpoint.setText(TPPLJ2);
		}
        else if(PLMS.equals("1")) {
        	textView_hop.setText("定频");
	    	PD2= contactservice.getNodeAttributeById(sbbh, SB_ATTRIBUTEYNAME.NODEATTRI_PD2);
			textView_freqpoint.setText(PD2);
		}
       
        
		btn_merge.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view) {
				
				//Log.i("networkmerge",String.valueOf(networkmerge));
				if(btn_merge.getText().equals("网络融合")){
			       if(networkmerge==1){
				 //  Toast.makeText(getApplicationContext(), "有可以融合的网络", Toast.LENGTH_SHORT).show();
					 if (setAhn !=null) {
							/*Dialog dialog = new AlertDialog.Builder(ZzwMinorPage.this)
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
												boolValue = setAhn.mergeAhnNetworkMSMS(1, 1);
											} catch (RemoteException e1) {
												// TODO Auto-generated catch block
												e1.printStackTrace();
											}
											//MergeProgressDialog Show();
										}
									/*}).create();
					dialog.show();*/
							/*if (boolValue == true) {
								Toast.makeText(getApplicationContext(),
										"网络融合成功", Toast.LENGTH_SHORT)
										.show();
								btn_merge.setText("网络断开");
							}
					*///}		
			        }else{
				           Toast.makeText(getApplicationContext(), "没有有可以融合的网络", Toast.LENGTH_SHORT).show(); 
				          }
				//调用接口实现网络融合
			  }else{
					 if (setAhn !=null) {
				        	try {
				            	boolValue=setAhn.mergeAhnNetworkMSMS(1, 2);        	
				            	//Log.i("boolValuemerge", String.valueOf(boolValue));
				            	if(boolValue==true){
				            		 Toast.makeText(getApplicationContext(), "网络断开成功", Toast.LENGTH_SHORT).show();
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
		
		btn_set = (Button)findViewById(R.id.btn_minor_set);
		btn_set.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				//实现设置功能--将参数按格式写入framework接口
				if(JBH !=null && !JBH.equals("")){ 
		            levelId=Integer.parseInt(JBH);
		           }else{
		        	   isconfigset=false;
		        	   Toast.makeText(getApplicationContext(), "编排号缺省", Toast.LENGTH_SHORT).show();
		           }	   
		          if(ZH2!=null && !ZH2.equals("")){
		            groupId=Integer.parseInt(ZH2);
		           }else{
		        	   isconfigset=false;
		        	   Toast.makeText(getApplicationContext(), "组号缺省", Toast.LENGTH_SHORT).show();
		           }	 
		          if(CTJD2!=null && !CTJD2.equals("")){
		            nodeType=Integer.parseInt(CTJD2);
		           }else{
		        	   isconfigset=false;
		        	   Toast.makeText(getApplicationContext(), "节点类型缺省", Toast.LENGTH_SHORT).show();
		           }	 
		          if(TBFS2!=null && !TBFS2.equals("")){
		            syncType=Integer.parseInt(TBFS2);
		           }else{
		        	   isconfigset=false;
		        	   Toast.makeText(getApplicationContext(), "设备型号缺省", Toast.LENGTH_SHORT).show();
		           }	 
		         
		          if(PLMS!=null && !PLMS.equals("")){  
		            frequencyType=Integer.parseInt(PLMS);
		            if(frequencyType==0){
		            	if(!TPPLJ2.equals("")){
		            		String[] freqMainString = TPPLJ2.split("-"); 
				               frequency[0]=freqMainString.length;
				               for(int i=1;i<freqMainString.length+1;i++){
				        	      frequency[i]=Float.parseFloat(freqMainString[i-1]);
				        	       //Log.i("frequency",String.valueOf(frequency[i]));
				               }
		            	 }else{
				        	   Toast.makeText(getApplicationContext(), "跳频缺省", Toast.LENGTH_SHORT).show();
				        	   isfreset=false;
				           }
		     
		             }else {
		            	 if(!PD2.equals("")){
		        	   frequency[0]=Float.parseFloat(PD2); 
		            	 }else{
		            		 Toast.makeText(getApplicationContext(), "频点缺省", Toast.LENGTH_SHORT).show();
		            		 isfreset=false;
		            	 }
				     }
		          }else{
		        	   Toast.makeText(getApplicationContext(), "频率模式缺省", Toast.LENGTH_SHORT).show();
		        	   isfreset=false;
		           }	 
		          if (PLMS != null && !PLMS.equals("")) {
						frequencyType = Integer.parseInt(PLMS);
						if (frequencyType == 0) {
							if (!TPPLJ2.equals("")) {
								String[] freqMainString = TPPLJ2.split("-");
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
							if (!PD2.equals("")) {
								frequency2[0] = Float.parseFloat(PD2);
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
					if (setAhn != null) {
						try {
							if (isconfigset) {
								 boolValue_setconfig = setAhn.setAhnConfigMSMS(2,
										levelId, groupId, nodeType, syncType,
										serialId);
								/*Log.i("打印频率设置参数", "" + levelId + "" + groupId + ""
										+ nodeType + "" + syncType);
								Log.i("打印频率设置参数", "" + serialId[0] + ""
										+ serialId[1] + "" + "" + serialId[2] + ""
										+ "" + serialId[3] + "" + "" + serialId[4]);*/
								}else{
								    Log.i("dingping", ""+frequency2[0]);
								}
							if (isfreset) {
								if (frequencyType == 0) {
									boolValue_setfre = setAhn.setAhnFrequencyMSMS(
											2, frequencyType, frequency);
									for(int i=0;i<33;i++){
										Log.i("跳频频率集",""+frequency[i]);
									}
								} else {
									boolValue_setfre = setAhn.setAhnFrequencyMSMS(
											1, frequencyType, frequency2);
									 // Log.i("跳频频率集",""+frequency2[0]);
								}
							}
							/*Log.i("MainsetAhnConfigMSMS",
									String.valueOf(boolValue_setconfig));
							Log.i("MainsetAhnFrequencyMSMS",
									String.valueOf(boolValue_setfre));*/
							if (boolValue_setconfig == true) {
								Toast.makeText(getApplicationContext(), "参数设置成功",
										Toast.LENGTH_SHORT).show();
							}
							if (boolValue_setfre == true) {
								Toast.makeText(getApplicationContext(), "频率设置成功",
										Toast.LENGTH_SHORT).show();
							}
						} catch (Exception e) {
							System.err.println("设置失败！");
						}
					}

			}
			
		});
		
		btn_net = (Button)findViewById(R.id.btn_minor_net);
		btn_net.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				//网络拓扑信息显示--wangwei的
	        if(networkmerge==1){
				Intent intent = null;	
				intent = new Intent(ZzwMinorPage.this, Topology_Ahoc_minor.class);
				startActivity(intent);			
				}else{
					Toast.makeText(getApplicationContext(), "获取自检信息失败", Toast.LENGTH_SHORT).show();
				}
				
			}
		});
		btn_ahnpower.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				//组网离网
/*				setAhn1 = Itelephony.getTelephonyManager(ZzwMinorPage.this);
				 if (setAhn1!=null) {
			        	try {	        		 
			        		if( zuorli==false){
			        	    boolean  boolzu=setAhn1.setAhnPowerMSMS(1, 1);
			        	    btn_ahnpower.setText("离网");	
			            	   Log.i("组网命令", String.valueOf(boolzu));			        
			            	   if(boolzu==true){
			            		   zuorli=true;
			            		  
			            		   Toast.makeText(getApplicationContext(), "组网已成功", Toast.LENGTH_SHORT).show();		           
			            	   }else{
			            		   Toast.makeText(getApplicationContext(), "组网失败", Toast.LENGTH_SHORT).show();
			            	       }
			        		}else {
			        	    boolean boolli=setAhn1.setAhnPowerMSMS(1, 0); 
			        	    btn_ahnpower.setText("组网");
				                Log.i("离网命令", String.valueOf(boolValue));
				            	if(boolli==true){
				            		zuorli=false;
				            		Toast.makeText(getApplicationContext(), "离网成功", Toast.LENGTH_SHORT).show();							          		
				            	 }else{
				            		Toast.makeText(getApplicationContext(), "离网失败", Toast.LENGTH_SHORT).show();
				            	  }
			        		}
						} catch (Exception e) {
							// TODO: handle exception
							System.err.println("组网离网失败！！");
						    }
			     }		*/
			/*	  setAhn1 = Itelephony.getTelephonyManager(ZzwMinorPage.this);
					 if (setAhn1!=null) {
				        	try {
				        		if(setAhn1.isRadioOnMSMS(1)==false){
				            	   boolValue=setAhn1.setAhnPowerMSMS(1, 1); 			            	 
				            	   Log.i("组网命令", String.valueOf(boolValue));
				            	   if(boolValue==true){
				            		   Toast.makeText(getApplicationContext(), "组网命令已发送", Toast.LENGTH_SHORT).show();
				            		   if(setAhn1.isRadioOnMSMS(1)==true){
				            		     btn_ahnpower.setText("离网");
				            		     Toast.makeText(getApplicationContext(), "组网成功", Toast.LENGTH_SHORT).show();
				            		   }else{
				            			 Toast.makeText(getApplicationContext(), "组网没有成功", Toast.LENGTH_SHORT).show();
				            		   }
				            	    }else{
				            		   Toast.makeText(getApplicationContext(), "组网命令发送失败", Toast.LENGTH_SHORT).show();
				            	     }
				        		}else if(setAhn1.isRadioOnMSMS(1)==true){
				        			boolValue=setAhn1.setAhnPowerMSMS(1, 0); 	
					                Log.i("离网命令", String.valueOf(boolValue));
					            	if(boolValue==true){
					            		Toast.makeText(getApplicationContext(), "离网命令已成功", Toast.LENGTH_SHORT).show();
					            		 if(setAhn1.isRadioOnMSMS(1)==false){
					            		     btn_ahnpower.setText("离网");
					            		     Toast.makeText(getApplicationContext(), "离网成功", Toast.LENGTH_SHORT).show();
					            		   }else{
					            			 Toast.makeText(getApplicationContext(), "离网没有成功", Toast.LENGTH_SHORT).show();
					            		   }
					            	 }else{
					            		 Toast.makeText(getApplicationContext(), "离网命令发送失败", Toast.LENGTH_SHORT).show();
					            	  }
				        		}
							} catch (Exception e) {
								// TODO: handle exception
								System.err.println("组网离网失败！！");
							    }
				     }	*/	
				}
		});
		
    }
    public void onResume() {
		super.onResume();
		broadcastReceiver4=new ModemReceiver();
		broadcastReceiver4.setCallback(new ModemReceiver.Callback() {
			@Override
			public void receive(int linkid, int result) {
				// TODO Auto-generated method stub
				linkid1=linkid;
				check=result;
				if(linkid==1&&result==1){
					textView_check.setText("成功");
				}else if(linkid==1&&result==0) {
					textView_check.setText("失败");
				}
		//		Log.i("linkid1",String.valueOf(linkid1));
		//		Log.i("自检状态",String.valueOf(result));
			}
		});
		broadcastReceiver5=new NetworkMergeReceiver();
		broadcastReceiver5.setCallback(new NetworkMergeReceiver.Callback() {
			@Override
			public void receive(int linkid, int result) {
				// TODO Auto-generated method stub
				linkid2=linkid;
				if(linkid==1){
				networkmerge=result;
				}
			//	Log.i("linkid2",String.valueOf(linkid2));
			//	Log.i("网络融合",String.valueOf(result));
			}
		});
		IntentFilter filter4 = new IntentFilter(
				ModemReceiver. ACTION_AHN_SELF_CHECK_RESULT);
		registerReceiver(broadcastReceiver4, filter4);
		IntentFilter filter5 = new IntentFilter(
				NetworkMergeReceiver. ACTION_AHN_NETWORK_MERGE);
		registerReceiver(broadcastReceiver5, filter5);
    }
    public void onPause() {
		super.onPause();
		isRunning = false;
		unregisterReceiver(broadcastReceiver4);
		unregisterReceiver(broadcastReceiver5);
	}
    int[] GetBPHIntFromString(String ss){
		int bph = Integer.parseInt(ss);
		int[]  retval = new int[5];
		retval[0] = (int)((bph&0x1800) >> 11);
		retval[1] = (int)((bph&0x600) >> 9);
		retval[2] = (int)((bph&0x180) >> 7);
		retval[3] = (int)((bph&0x60) >> 5);
		retval[4] = (int)((bph&0x1F) );
		return retval;
	}
}
