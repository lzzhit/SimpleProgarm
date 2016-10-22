package com.contact.service;

/**
 * @author lm xia
 * @date 2016年4月5日
 * @说明  ContactServiceProxy 单例获取名录服务代理
 */
public class ContactServiceProxy{
	private static IContactService contactService = null;
	//私有内部构造
	private ContactServiceProxy() {
		ContactServiceImp as = new ContactServiceImp();
		ProxyFactoryBean pfb  = new ProxyFactoryBean();
		pfb.setTarget(as);
		contactService = (IContactService)pfb.getProxy();
	}
	public static IContactService getInstance() {
		if (contactService == null) {
			new ContactServiceProxy();
		}
		return contactService;
	}
}