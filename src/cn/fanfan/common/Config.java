package cn.fanfan.common;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

/*
 * ���ܣ���һЩ���õļ�ֵ���磺�ӿڵ�url��д�������ļ��У������Ժ��޸ġ�
 * 
 * edit at 2014/7/14 by huangchen
 * 
 * �������ļ�(config.properties)��д����Ϣ����Ϣ��ʽ�Ѿ��Ѿ��������ļ��и���
 * ���ú���getValue(String key)
 * �磺String loginUrl = Config.getValue("loginUrl")
 */
public class Config {
	private static Properties props = new Properties();
	static String profilepath = "config.properties";
	static {
		try {
			props.load(Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("config.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getValue(String key) {
		return props.getProperty(key);
	}

	public static void updateProperties(String keyname, String keyvalue) {
		try {
			OutputStream fos = new FileOutputStream(Thread.currentThread()
					.getContextClassLoader().getResource("config.properties")
					.getFile());
			props.setProperty(keyname, keyvalue);
			props.store(fos, "Update '" + keyname + "' value");
			fos.close();
		} catch (IOException e) {
		}
	}

}
