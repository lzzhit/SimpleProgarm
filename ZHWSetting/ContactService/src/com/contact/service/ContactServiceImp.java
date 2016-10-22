package com.contact.service;

import java.util.List;

/**
 * @author CP1107004
 * @date 2011年6月12日
 * @filename ContactServiceImp.java
 * @说明 TODO
 */
class ContactServiceImp implements IContactService {

	private static final String TAG = "ContactServiceImp";
	//renyuan...
	public native String nativeGetNodeAttributeById(int vmf,int AttriNameIndex);
	
	public native List<Integer> nativeGetNodeIdByAttribute(int AttriNameIndex,String AttriValue);
	
	public native int nativeUpdateAttributeById(int lNodeId, int AttriNameIndex,String AttriValue);
	//device...
	public native String nativeGetNodeAttributeBySbbh(String sbbh,int AttriNameIndex);
	
	public native List<String> nativeGetSbbhByAttribute(int AttriNameIndex,String AttriValue);
	
	public native int nativeUpdateAttributeBySbbh(String sbbh, int AttriNameIndex,String AttriValue);
	//end...
	public native List<AccountBean> nativeGetAllNode();
	
	public native int nativeGetVmfBySbbh(String sbbh);

	public native AccountBean nativeGetAccountBeanById(int lNodeId);
	
	public native DeviceInfoBean nativeGetDeviceInfoBeanById(String sbbh);
	
	public native int nativeGetSelfVmf();
	
	public native int nativeSetSelfImei(String sbbh);
	
	public native int nativeSetCurrentVersion(String version);
	
	static {
		System.loadLibrary("Mlfw");
	}

	public ContactServiceImp() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.contact.service.IContactService#GetNodeAttributeById(long,
	 * com.contact.service.IContactService.ATTRIBUTEYNAME)
	 */
	@Override
	public String getNodeAttributeById(int lNodeId, RY_ATTRIBUTEYNAME AttriName) {
		return nativeGetNodeAttributeById(lNodeId,AttriName.ordinal());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.contact.service.IContactService#GetNodeIdByAttribute(com.contact.
	 * service.IContactService.ATTRIBUTEYNAME, java.lang.String)
	 * 
	 */
	@Override
	public List<Integer> getNodeIdByAttribute(RY_ATTRIBUTEYNAME AttriName,
			String pAttriValue) {
		return nativeGetNodeIdByAttribute(AttriName.ordinal(),pAttriValue);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.contact.service.IContactService#UpdateAttributeById(long,
	 * com.contact.service.IContactService.ATTRIBUTEYNAME, java.lang.String)
	 */
	@Override
	public int updateAttributeById(int lNodeId, RY_ATTRIBUTEYNAME AttriName,
			String AttrValue) {
		return nativeUpdateAttributeById(lNodeId,AttriName.ordinal(),AttrValue);
	}

	@Override
	public List<AccountBean> getAllNode() {
		return nativeGetAllNode();
	}

	@Override
	public int getVmfBySbbh(String sbbh) {
		return nativeGetVmfBySbbh(sbbh);
	}

	@Override
	public String getNodeAttributeById(String sbbh, SB_ATTRIBUTEYNAME AttriName) {
		return nativeGetNodeAttributeBySbbh(sbbh,AttriName.ordinal());
	}

	@Override
	public List<String> getNodeIdByAttribute(SB_ATTRIBUTEYNAME AttriName,
			String pAttriValue) {
		return nativeGetSbbhByAttribute(AttriName.ordinal(),pAttriValue);
	}

	@Override
	public int updateAttributeById(String sbbh, SB_ATTRIBUTEYNAME AttriName,
			String AttrValue) {
		return nativeUpdateAttributeBySbbh(sbbh,AttriName.ordinal(),AttrValue);
	}

	@Override
	public AccountBean getAccountBeanById(int lNodeId) {
		return nativeGetAccountBeanById(lNodeId);
	}

	@Override
	public int getSelfVmf() {
		return nativeGetSelfVmf();
	}

	@Override
	public int setSelfImei(String Imei) {
		return nativeSetSelfImei(Imei);
	}

	@Override
	public int SetCurrentVersion(String version) {
		return nativeSetCurrentVersion(version);
	}

	@Override
	public DeviceInfoBean getDeviceInfoBeanById(String sbbh) {
		return nativeGetDeviceInfoBeanById(sbbh);
	}

}