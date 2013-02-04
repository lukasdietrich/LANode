package me.lukasdietrich.commons;

import java.text.DecimalFormat;
import java.util.Calendar;

public class DateTime {

	private static DecimalFormat twoDiggit 		= new DecimalFormat("00");
	private static DecimalFormat threeDiggit 	= new DecimalFormat("000");
	
	/**
	 * %s = sec
	 * %m = min
	 * %h = hour
	 * %d = day
	 * %M = month
	 * %y = year
	 * %n = millisec
	 * 
	 * @param format
	 * @return the formatted {@link String}
	 */
	public static String format(String format) {
		Calendar cal = Calendar.getInstance();
		
		format = format
			.replace("%s", twoDiggit.format(cal.get(Calendar.SECOND)))
			.replace("%m", twoDiggit.format(cal.get(Calendar.MINUTE)))
			.replace("%h", twoDiggit.format(cal.get(Calendar.HOUR_OF_DAY)))
			.replace("%d", twoDiggit.format(cal.get(Calendar.DAY_OF_MONTH)))
			.replace("%M", twoDiggit.format(cal.get(Calendar.MONTH)+1))
			.replace("%y", String.valueOf(cal.get(Calendar.YEAR)))
			.replace("%n", threeDiggit.format(cal.get(Calendar.MILLISECOND)));
		
		return format;
	}
	
}
