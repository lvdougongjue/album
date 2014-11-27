package cn.duke.common;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class ApplicationProperties {
	private static Properties prop;

	public ApplicationProperties() {
	}

	public Properties getProp() {
		return prop;
	}

	public void setProp(Properties prop) {
		ApplicationProperties.prop = prop;
	}

	public static String getProperty(String key) {
		String value = "";
		try {
			value = new String(prop.getProperty(key).getBytes("iso-8859-1"));
		} catch (UnsupportedEncodingException e) {
		}

		return value;
	}
}
