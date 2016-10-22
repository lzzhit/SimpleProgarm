package com.example.invoke;

import java.lang.reflect.Method;

public class SystemPropInvoke {

	public static void SysPropSet(String key, String defaultValue) {
		String value = defaultValue;
		
		try {
			Class<?> S = Class.forName("android.os.SystemProperties");
			Method sysPropSet = S.getMethod("set", String.class, String.class);
			sysPropSet.invoke(S, key, value);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("finally")
	public static String SysPropGet(String key, String defaultValue) {
		String value = defaultValue;
		
		try {
			Class<?> S = Class.forName("android.os.SystemProperties");
			Method sysPropGet = S.getMethod("get", String.class, String.class);
			value = (String)(sysPropGet.invoke(S, key, "unknown"));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			return value;
		}
	}
}
