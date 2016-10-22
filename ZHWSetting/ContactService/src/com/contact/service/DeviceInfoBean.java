package com.contact.service;

public class DeviceInfoBean {
	
	private String mSbbh; 	// 设备编号
	private String mJbh; 	// 级别号
	private String mBph; 	// 编排号
	private String mZh1;	// 组号1
	private String mZh2; 	// 组号2
	private String mTbfs1; 	// 同步方式1
	private String mTbfs2; 	// 同步方式2
	private String mPlms; 	// 频率模式
	private String mTpplj1; // 调频频率集1
	private String mTpplj2; // 调频频率集2
	private String mPd1; 	// 频点1
	private String mPd2; 	// 频点2
	private String mSblx; 	// 设备类型
	private String mTtkh; 	// 天通卡号
	private String mWifiip; // WIFI IP地址
	private String mZhwip;  // zhw IP地址
	private String mZhwym;  // zhw 掩码
	private String mImei;   // imei
	private String mLtekh; 	// LTE卡号
	private String mLteIP; 	// LTE ip
	private String mCtjd1; 	// ctjd1
	private String mCtjd2; 	// ctjd2

	public DeviceInfoBean(String mSbbh, String mJbh, String mBph, String mZh1,
			String mZh2, String mTbfs1, String mTbfs2, String mPlms,
			String mTpplj1, String mTpplj2, String mPd1, String mPd2,
			String mSblx, String mTtkh, String mWifiip,String zhwip,
			String zhwYm, String mImei, String mLtekh, String mLteip,
			String ctjd1, String ctjd2) {
		this.mSbbh = mSbbh;
		this.mJbh = mJbh;
		this.mBph = mBph;
		this.mZh1 = mZh1;
		this.mZh2 = mZh2;
		this.mTbfs1 = mTbfs1;
		this.mTbfs2 = mTbfs2;
		this.mPlms = mPlms;
		this.mTpplj1 = mTpplj1;
		this.mTpplj2 = mTpplj2;
		this.mPd1 = mPd1;
		this.mPd2 = mPd2;
		this.mSblx = mSblx;
		this.mTtkh = mTtkh;
		this.mWifiip = mWifiip;
		this.mZhwip= zhwip;
		this.mZhwym =zhwYm;
		this.mImei= mImei;
		this.mLtekh = mLtekh;
		this.mLteIP = mLteip;
		this.mCtjd1 = ctjd1;
		this.mCtjd2 = ctjd2;
	}

	/**
	 * @return the mSbbh
	 */
	public String getmSbbh() {
			return mSbbh;	
	}

	/**
	 * @param mSbbh
	 *            the mSbbh to set
	 */
	public void setmSbbh(String mSbbh) {
		this.mSbbh = mSbbh;
	}

	/**
	 * @return the mJbh
	 */
	public String getmJbh() {
		return mJbh;
	}

	/**
	 * @param mJbh
	 *            the mJbh to set
	 */
	public void setmJbh(String mJbh) {
		this.mJbh = mJbh;
	}

	/**
	 * @return the mBph
	 */
	public String getmBph() {
		return mBph;
	}

	/**
	 * @param mBph
	 *            the mBph to set
	 */
	public void setmBph(String mBph) {
		this.mBph = mBph;
	}

	/**
	 * @return the mZh1
	 */
	public String getmZh1() {
		return mZh1;
	}

	/**
	 * @param mZh1 the mZh1 to set
	 */
	public void setmZh1(String mZh1) {
		this.mZh1 = mZh1;
	}

	/**
	 * @return the mZh2
	 */
	public String getmZh2() {
		return mZh2;
	}

	/**
	 * @param mZh2 the mZh2 to set
	 */
	public void setmZh2(String mZh2) {
		this.mZh2 = mZh2;
	}

	/**
	 * @return the mTbfs1
	 */
	public String getmTbfs1() {
		return mTbfs1;
	}

	/**
	 * @param mTbfs1 the mTbfs1 to set
	 */
	public void setmTbfs1(String mTbfs1) {
		this.mTbfs1 = mTbfs1;
	}

	/**
	 * @return the mTbfs2
	 */
	public String getmTbfs2() {
		return mTbfs2;
	}

	/**
	 * @param mTbfs2 the mTbfs2 to set
	 */
	public void setmTbfs2(String mTbfs2) {
		this.mTbfs2 = mTbfs2;
	}

	/**
	 * @return the mPlms
	 */
	public String getmPlms() {
		return mPlms;
	}

	/**
	 * @param mPlms the mPlms to set
	 */
	public void setmPlms(String mPlms) {
		this.mPlms = mPlms;
	}

	/**
	 * @return the mTpplj1
	 */
	public String getmTpplj1() {
		return mTpplj1;
	}

	/**
	 * @param mTpplj1 the mTpplj1 to set
	 */
	public void setmTpplj1(String mTpplj1) {
		this.mTpplj1 = mTpplj1;
	}

	/**
	 * @return the mTpplj2
	 */
	public String getmTpplj2() {
		return mTpplj2;
	}

	/**
	 * @param mTpplj2 the mTpplj2 to set
	 */
	public void setmTpplj2(String mTpplj2) {
		this.mTpplj2 = mTpplj2;
	}

	/**
	 * @return the mPd1
	 */
	public String getmPd1() {
		return mPd1;
	}

	/**
	 * @param mPd1 the mPd1 to set
	 */
	public void setmPd1(String mPd1) {
		this.mPd1 = mPd1;
	}

	/**
	 * @return the mPd2
	 */
	public String getmPd2() {
		return mPd2;
	}

	/**
	 * @param mPd2 the mPd2 to set
	 */
	public void setmPd2(String mPd2) {
		this.mPd2 = mPd2;
	}

	/**
	 * @return the mSblx
	 */
	public String getmSblx() {
		return mSblx;
	}

	/**
	 * @param mSblx the mSblx to set
	 */
	public void setmSblx(String mSblx) {
		this.mSblx = mSblx;
	}

	/**
	 * @return the mTtkh
	 */
	public String getmTtkh() {
		return mTtkh;
	}

	/**
	 * @param mTtkh the mTtkh to set
	 */
	public void setmTtkh(String mTtkh) {
		this.mTtkh = mTtkh;
	}

	/**
	 * @return the mLtekh
	 */
	public String getmLtekh() {
		return mLtekh;
	}

	/**
	 * @param mLtekh the mLtekh to set
	 */
	public void setmLtekh(String mLtekh) {
		this.mLtekh = mLtekh;
	}

	/**
	 * @return the mWifiip
	 */
	public String getmWifiip() {
		return mWifiip;
	}

	/**
	 * @param mWifiip the mWifiip to set
	 */
	public void setmWifiip(String mWifiip) {
		this.mWifiip = mWifiip;
	}
	
	@Override
	public String toString() {
		return 	
				"级别号 = " + mJbh 
				+ ";编排号 = " + mBph 
				+ ";组网1 = " + mZh1 
				+ ";组网2 = " + mZh2 
				+ ";同步方式1 = " + mTbfs1
				+ ";同步方式2= " + mTbfs2 
				+ ";频率模式= " + mPlms
				+ ";调频频率集1= " + mTpplj1
				+ ";调频频率集2= " + mTpplj2
				+ ";频点1= " + mPd1
				+ ";频点2= " + mPd2
				+ ";设备类别= " + mSblx
				+ ";天通卡号= " + mTtkh
				+ ";WifiIp= " + mWifiip
				+ ";战互网IP= " + mZhwip
				+ ";战互网掩码= " + mZhwym
				+ ";IMEI码= " + mImei
				+ ";LTE卡号= " + mLtekh
				+ ";LTE IP= " + mLteIP
				+ ";簇头节点1= " + mCtjd1
				+ ";簇头节点2= " + mCtjd2
				;
	}

	public String getmZhwip() {
		return mZhwip;
	}

	public void setmZhwip(String mZhwip) {
		this.mZhwip = mZhwip;
	}

	public String getmZhwym() {
		return mZhwym;
	}

	public void setmZhwym(String mZhwym) {
		this.mZhwym = mZhwym;
	}

	public String getmImei() {
		return mImei;
	}

	public void setmImei(String mImei) {
		this.mImei = mImei;
	}

	public String getmLteIP() {
		return mLteIP;
	}

	public void setmLteIP(String mLteIP) {
		this.mLteIP = mLteIP;
	}

	public String getCtjd2() {
		return mCtjd2;
	}

	public void setCtjd2(String mCtjd2) {
		this.mCtjd2 = mCtjd2;
	}

	public String getCtjd1() {
		return mCtjd1;
	}

	public void setCtjd1(String mCtjd1) {
		this.mCtjd1 = mCtjd1;
	}
}
