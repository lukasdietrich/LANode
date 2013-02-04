package me.lukasdietrich;

import java.io.IOException;
import java.util.Properties;

import me.lukasdietrich.commons.Logger;

public class Strings {

	private static Properties strings;
	
	static {
		strings = new Properties();
		try {
			strings.load(Strings.class.getResourceAsStream("/me/lukasdietrich/assets/strings.properties"));
		} catch (IOException e) {
			Logger.get().err(e, null);
		}
	}
	
	public static String getString(Class<?> clazz, String id) {
		return strings.getProperty(clazz.getName() +"."+ id);
	}
	
}
