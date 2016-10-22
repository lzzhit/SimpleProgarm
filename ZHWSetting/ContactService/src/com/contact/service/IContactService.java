package com.contact.service;

import java.util.List;

/**
 * @author luojia * {@docRoot} 这是名录服务接口，接口中定义了本服务所提供的全部能力
 * @since 2016-4-5
 * @version V0.00.01 参考 PTZSML中提供的接口能力
 */
public interface IContactService {
	/*
	 * 名录属性名称
	 */
	public enum RY_ATTRIBUTEYNAME {
		NODEATTRI_BDXH, 	//部队序号
		NODEATTRI_BDFHRYMC, //部队番号人员名称
		NODEATTRI_BDNM, 	//部队内码
		NODEATTRI_SFZHM, 	//身份证号码
		NODEATTRI_RYZW, 	//人员职位
		NODEATTRI_SBBH, 	//设备编号
		NODEATTRI_RYNC, 	//人员昵称
		NODEATTRI_ZJLXSJ, 	//最近联系时间
		NODEATTRI_VMF,		//vmf
		NODEATTRI_AUTHORITY //权限赋予
	};
	public enum SB_ATTRIBUTEYNAME {
		NODEATTRI_JBH,		//级别号
		NODEATTRI_BPH,		//编排号
		NODEATTRI_ZH1, 		//组号1
		NODEATTRI_ZH2, 		//组号2
		NODEATTRI_TBFS1, 	//同步方式1
		NODEATTRI_TBFS2, 	//同步方式2
		NODEATTRI_PLMS, 	//频率模式
		NODEATTRI_TPPLJ1, 	//调频频率集1
		NODEATTRI_TPPLJ2, 	//调频频率集2
		NODEATTRI_PD1,		//频点1
		NODEATTRI_PD2,		//频点2
		NODEATTRI_SBLX, 	//设备类型
		NODEATTRI_TTKH,		//天通卡号
		NODEATTRI_WIFIIP,	//WIFI IP地址
		NODEATTRI_ZHWIP,	//战互网IP
		NODEATTRI_ZHWYM,	//战互网掩码
		NODEATTRI_IMEI,		//IMEI码
		NODEATTRI_LTEKH,	//LTE卡号
		NODEATTRI_LTEIP,	//LTEIP
		NODEATTRI_CTJD1, 	//ctjd1
		NODEATTRI_CTJD2, 	//ctjd2
	};
	/*
	 * 功能描述：利用节点编码取与此节点相关的各种属性值。 
	 * 参数说明： lNodeId [in]：节点编码值(目前在本应用里是vmf)
	 * 			 AttriName [in]：节点属性名称 返回值： AttriValue
	 */
	String getNodeAttributeById(int lNodeId,
			RY_ATTRIBUTEYNAME AttriName);
	
	String getNodeAttributeById(String sbbh,
			SB_ATTRIBUTEYNAME AttriName);

	/*
	 * 功能描述：输入一个属性值，取所有包含此属性值的节点编码
	 * 参数说明： AttriName [in]：节点属性名称
	 *  		 AttriValue[in]：节点属性值 
	 *  		  返回list 满足条件的全部id
	 */
	
	List<Integer> getNodeIdByAttribute(RY_ATTRIBUTEYNAME AttriName,
			String pAttriValue);
	
	List<String> getNodeIdByAttribute(SB_ATTRIBUTEYNAME AttriName,
			String pAttriValue);
	/*
	 * 功能描述：利用节点编码更新与此节点相关的各种属性值。 
	 * 参数说明： lNodeId [in]：节点编码值(目前在本应用里是vmf)
	 * 			 AttriName [in]：节点属性名称 
	 * 			 AttriValue [in]：节点属性名称
	 * 			   返回值： 0表示成功，非0表示不成功
	 */
	int updateAttributeById(int lNodeId,
			RY_ATTRIBUTEYNAME AttriName, String AttrValue);
	
	int updateAttributeById(String sbbh,
			SB_ATTRIBUTEYNAME AttriName, String AttrValue);
	
	/*
	 * 功能描述：获取所有节点
	 * 参数说明： 
	 */
	
	List<AccountBean> getAllNode();
	/*
	 * 功能描述：获取设备编号对应的VMF编码
	 * 参数说明： 
	 */
	
	int getVmfBySbbh(String sbbh);
	
	/*
	 * 功能描述：获取对应vmf节点
	 * 参数说明： 
	 */
	AccountBean getAccountBeanById(int lNodeId);
	
	DeviceInfoBean getDeviceInfoBeanById(String sbbh);
	/*
	 * 功能描述：获取自身节点VMF
	 * 参数说明： 
	 */
	int getSelfVmf();
	
	/*
	 * 功能描述：设置本身节点的Sbbh,这个接口只能由框架调用，各个应用不需要调用
	 * 参数说明： 
	 */
	int setSelfImei(String imei);
	
	/*
	 * 功能描述：设置本身节点的Sbbh,这个接口只能由框架调用，各个应用不需要调用
	 * 参数说明： 
	 */
	int SetCurrentVersion(String version);
}
