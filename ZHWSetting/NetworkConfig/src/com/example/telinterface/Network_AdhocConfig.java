package com.example.telinterface;

import java.net.ContentHandler;

import android.content.Context;
import android.telephony.Itelephony;

import com.android.internal.telephony.ITelephony;
import com.contact.service.ContactServiceProxy;
import com.contact.service.IContactService;
import com.contact.service.IContactService.RY_ATTRIBUTEYNAME;
import com.contact.service.IContactService.SB_ATTRIBUTEYNAME;


public class Network_AdhocConfig {
	
	
	static IContactService contactService = null;
	
	ITelephony setAhn;
	Context mcontext;
	private Network_AdhocConfig(Context context){
		super();
		mcontext=context;
	}
	
	
	private static String sbbh ;     //设备号，暂时写死，用于框架时用vmf获取
	private String JBH;			//级别号
	private String BPH;			//编排号
	private String ZH1;         //组号1
	private String ZH2;			//组号2
	private String TBFS1;		//同步方式1
	private String TBFS2;		//同步方式2
	private String SBLX;		//设备类型
	private String PLMS;		//频率模式
	private String TPPLJ1;      //调频频率集1
	private String TPPLJ2;		//调频频率集2
	private String PD1;         //频点1
	private String PD2;			//频点2
	private int vmf;            //身份识别，用于框架获取当前身份
	private String BPH2;        //用于编排号处理，无实际意义
	
	/**
	 * 读取名录服务获取参数
	 */
	public void getAdhocParam() {
		// 实例化名录服务
		contactService = ContactServiceProxy.getInstance();
		
		//在框架下，获取当前身份，由此获得设备号
		vmf = contactService.getSelfVmf();
	 	sbbh = contactService.getNodeAttributeById(vmf, RY_ATTRIBUTEYNAME.NODEATTRI_SBBH);
		
		//从名录获取String型的参数	
		//级别号
		JBH = contactService.getNodeAttributeById(sbbh, SB_ATTRIBUTEYNAME.NODEATTRI_JBH);
		//组号1
		ZH1 = contactService.getNodeAttributeById(sbbh, SB_ATTRIBUTEYNAME.NODEATTRI_ZH1);	
		//组号2
		ZH2 = contactService.getNodeAttributeById(sbbh, SB_ATTRIBUTEYNAME.NODEATTRI_ZH2);
		//设备类型
        SBLX = contactService.getNodeAttributeById(sbbh, SB_ATTRIBUTEYNAME.NODEATTRI_SBLX);
        //同步方式1
        TBFS1 = contactService.getNodeAttributeById(sbbh, SB_ATTRIBUTEYNAME.NODEATTRI_TBFS1);
        //同步方式2
        TBFS2 = contactService.getNodeAttributeById(sbbh, SB_ATTRIBUTEYNAME.NODEATTRI_TBFS2);
        //编排号
		BPH = contactService.getNodeAttributeById(sbbh, SB_ATTRIBUTEYNAME.NODEATTRI_BPH);
		//频率模式与频点
        PLMS = contactService.getNodeAttributeById(sbbh, SB_ATTRIBUTEYNAME.NODEATTRI_PLMS);
        
        if (PLMS.equals("0")) {
        	TPPLJ1 = contactService.getNodeAttributeById(sbbh, SB_ATTRIBUTEYNAME.NODEATTRI_TPPLJ1);//跳频频率集
        	TPPLJ2 = contactService.getNodeAttributeById(sbbh, SB_ATTRIBUTEYNAME.NODEATTRI_TPPLJ2);
		}
        else {
        	PD1 = contactService.getNodeAttributeById(sbbh, SB_ATTRIBUTEYNAME.NODEATTRI_PD1); //频点
	    	PD2 = contactService.getNodeAttributeById(sbbh, SB_ATTRIBUTEYNAME.NODEATTRI_PD2);
		}
	}
	
	/*
	 *参数配置
	 */
	public boolean setAdhocParam() {
		int levelId;        //级别号
		int groupId1;       //组号1
		int groupId2;       //组号2
		int nodeType;       //设备类型，普通节点，簇头节点
		int syncType1;      //同步方式1
		int syncType2;      //同步方式2
		int[] serialId = new int[5]; //编排号
		String[] strserialId = new String[5];
		boolean boolValueMain; 	// 设置返回值，主芯片设置成功或失败
	 	boolean boolValueSec;	// 设置返回值，主芯片设置成功或失败
	 	boolean boolValue = false;
		
		//将sring型的参数转化为setAhnConfigMSMs()所需的类型 		
        levelId = Integer.parseInt(JBH);
        groupId1 = Integer.parseInt(ZH1);
        syncType1 = Integer.parseInt(TBFS1);
	    nodeType = Integer.parseInt(SBLX);
	   	    
        strserialId[0] = BPH.substring(0, 2);
        BPH2 = BPH.substring(2);
        
        for(int i = 1; i < 5; i++) {
        	 strserialId[i] = BPH2.substring(0, 5);
        	 BPH2 = BPH.substring(5);
        }  
        for(int i = 0;i < 5; i++){
        	serialId[i] = Integer.parseInt(strserialId[i]);
        }
        

        
        		
        //进行setAhnConfigMSMS 参数配置,若为簇头节点则配置两次
        if (nodeType == 0) {
        	try {
        		groupId2 = Integer.parseInt(ZH2);
        		syncType2 = Integer.parseInt(TBFS2);
        		
        		boolValueMain = setAhn.setAhnConfigMSMS(2, levelId, groupId1, nodeType, syncType1, serialId);
                boolValueSec = setAhn.setAhnConfigMSMS(1, levelId, groupId2, nodeType, syncType2, serialId);
                
                if (boolValueMain && boolValueSec) {
                	boolValue = true;
				}
                else {
					boolValue = false;
				}
			} catch (Exception e) {
				// TODO: handle exception
				System.err.println("PAD参数配置失败");
			}
		}
        else if (nodeType == 1) {
        	try {
        		boolValue = setAhn.setAhnConfigMSMS(1, levelId, groupId1, nodeType, syncType1, serialId);
			} catch (Exception e) {
				// TODO: handle exception
				System.err.println("终端参数配置失败！！");
			}
		}
        
		return boolValue;
	}
	
	
	/*
	 * 频率参数设置
	 */
	public boolean setAdhocFreq() {
		
		int frequencyType;    				// 频率模式
		int nodeType;
		float[] frequencyMain = new float[24];  // 跳频频率集或频点
		float[] frequencySec = new float[24]; 	// 跳频频率集或频点
		boolean boolValueMain;				// 主CP设置返回值，设置成功设置失败
		boolean boolValueSec;				// 从CP设置返回值，设置成功设置失败
		boolean boolValue = false;
		
		//将sring型的参数转化为setAhnFrequencyMSMs()所需的类型，其中跳频或者定频的未定义
        frequencyType = Integer.parseInt(PLMS);
        
        nodeType = Integer.parseInt(SBLX);
        
        // 对字符串进行分割，并转为Float型
        String[] freqMainString = TPPLJ1.split("-"); 
        String[] freqSecString = TPPLJ2.split("-");
        for (int i = 0; i < 24; i++) {
        	frequencyMain[i] = Float.parseFloat(freqMainString[i]);
			frequencySec[i] = Float.parseFloat(freqSecString[i]);
		}
        
        setAhn = Itelephony.getTelephonyManager(mcontext);
        
        //进行setAhnFrequencyMSMS 参数配置，若为簇头节点则配置两次
        if (nodeType == 0) {
        	try {
        		boolValueMain = setAhn.setAhnFrequencyMSMS(2, frequencyType, frequencyMain);
            	boolValueSec = setAhn.setAhnFrequencyMSMS(1, frequencyType, frequencySec);
            	
            	if (boolValueSec && boolValueMain) {
    				boolValue = true;
    			}
            	else {
    				boolValue = false;
    			}
            	
			} catch (Exception e) {
				// TODO: handle exception
				System.err.println("PAD频点参数设置失败！！");
			}
		}
        else if (nodeType == 1) {
        	try {
        		boolValue = setAhn.setAhnFrequencyMSMS(1, frequencyType, frequencySec);
			} catch (Exception e) {
				// TODO: handle exception
				System.err.println("终端频点参数设置失败！！");
			}
		}
       
        return boolValue;
	}
	
//	public boolean setAdhocParam() {
//		
//		return boolValue;
//	}
//	
//	public boolean setAdhocFreq() {
//		
//		return boolValue;
//	}
}
