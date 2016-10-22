package com.contact.service;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author lm xia
 * @version V0.00.01
 * @说明:获取target动态代理
 */

class ProxyFactoryBean implements InvocationHandler {
	private Object target;

	public void setTarget(Object target) {
		this.target = target;
	}
	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		Object resulst = null;
		try {
			resulst = method.invoke(target, args);
			//记录每一条修改记录
			if (method.getName().contains("UpdateAttributeById")) {
				System.out.println(method.getName() + " 执行记录是" + args.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resulst;
	}
	public Object getProxy(){
		return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), this);
	}
}
