package com.contact.service;

/**
 * @author lm xia
 * @date 2016年4月5日
 * @说明 名录节点对象，即
 */
public class AccountBean implements Comparable<AccountBean>{
	private String bdxh;	//部队序号
	private String bdfhrymc;//部队番号人员名称
	private String bdnm;	//部队内码
	private String sfzId;	//身份证ID
	private String ryzw;	//人员称谓
	private String sbbh;	//设备编号
	private String zjlxsj;	//最近联系时间
	private String rync;	//人员昵称
	private int vmf;		//vmf码
 
	public AccountBean() {
	}

	public AccountBean(String bdxh, String rymc, String bdnm, String sfzId, String ryzw, String sbbh, 
			String rync,String zjlxsj,int vmf) {
		this.bdxh = bdxh;
		this.sfzId = sfzId;
		this.bdfhrymc = rymc;
		this.ryzw = ryzw;
		this.sbbh = sbbh;
		this.zjlxsj = zjlxsj;
		this.rync = rync;
		this.bdnm = bdnm;
		this.vmf = vmf;
	}

	public String getSfzId() {
		return sfzId;
	}

	public void setSfzId(String sfzId) {
		this.sfzId = sfzId;
	}

	public String getRyzw() {
		return ryzw;
	}

	public String getBdfhrymc() {
		return bdfhrymc;
	}

	public void setBdfhrymc(String bdfhrymc) {
		this.bdfhrymc = bdfhrymc;
	}

	public void setRyzw(String rycw) {
		this.ryzw = rycw;
	}


	public String getSbbh() {
		return sbbh;
	}


	public void setSbbh(String sbbh) {
		this.sbbh = sbbh;
	}


	public String getZjlxsj() {
		return zjlxsj;
	}


	public void setZjlxsj(String sbIP) {
		this.zjlxsj = sbIP;
	}


	public String getRync() {
		return rync;
	}


	public void setRync(String rync) {
		this.rync = rync;
	}


	public String getBdnm() {
		return bdnm;
	}


	public void setBdnm(String bdnm) {
		this.bdnm = bdnm;
	}


	@Override
	public String toString() {
		return 	
				"作战单元编号 = " + vmf 
				+ ";部队序号 = " + bdxh 
				+ ";人员名称 = " + bdfhrymc 
				+ ";部队内码 = " + bdnm 
				+ ";身份证号码 = " + sfzId
				+ ";人员职位= " + ryzw 
				+ ";设备编号= " + sbbh
				+ ";人员昵称= " + rync;
	}

	public String getBdxh() {
		return bdxh;
	}

	public void setBdxh(String bdxh) {
		this.bdxh = bdxh;
	}

	@Override
	public int compareTo(AccountBean arg0) {
		String thisLastString = bdxh.substring(bdxh.length() - 4, bdxh.length() - 2);
		String arg0LastString = arg0.getBdxh().substring(arg0.getBdxh().length() - 4, arg0.getBdxh().length() - 2 );
		
		if (this.bdxh.length() == arg0.getBdxh().length() && thisLastString.equals(arg0LastString)) {
			if ((lengthIfNotNull(sfzId) == 0 && lengthIfNotNull(arg0.getSfzId()) == 0) || (lengthIfNotNull(sfzId) != 0 && lengthIfNotNull(arg0.getSfzId()) != 0)) {//若部队序号长度相同，且都没有身份证
				return this.bdxh.compareTo(arg0.getBdxh());//按照bdxh排序
			}else if (this.sfzId.length() == 0) {
				return 1;//本节点无身份证，我靠后显示
			}else{
				return -1;//被比较节点没有身份证，我靠前显示
			}
		}
		//如果大家都有身份证的话，判断共有长度，若共有长度为较短者减去两个字母，那么较短者靠前显示，否则则退化到判断bdxh的字符串
		if ((lengthIfNotNull(sfzId) != 0 && lengthIfNotNull(arg0.getSfzId()) != 0) && this.bdxh.length() != arg0.getBdxh().length()) {
			if (arg0.getBdxh().length() > this.bdxh.length()) {
				if (arg0.getBdxh().contains(bdxh.substring(0, bdxh.length() - 2)))
					return -1;
			}else {
				if (bdxh.contains(arg0.getBdxh().substring(0, arg0.getBdxh().length() - 2))) 
					return 1;
			}
		}
		return this.bdxh.compareTo(arg0.getBdxh());
	}
	
	private int lengthIfNotNull(String target) {
		if (target == null) {
			return 0;
		}else {
			return target.length();
		}
	}

	public int getVmf() {
		return vmf;
	}

	public void setVmf(int vmf) {
		this.vmf = vmf;
	}
}
