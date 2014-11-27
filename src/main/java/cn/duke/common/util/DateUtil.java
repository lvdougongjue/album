package cn.duke.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	private static final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static String getToday() {
		Calendar now = Calendar.getInstance();
		return sdf.format(now.getTime());
	}

	public static String getYestorday() {
		Calendar now = Calendar.getInstance();
		now.add(Calendar.DAY_OF_YEAR, -1);
		return sdf.format(now.getTime());
	}

	public static String getDate(Date date) {
		return sdf.format(date);
	}

	public static String getNextDay(Date date, int nextDays) {
		Calendar now = Calendar.getInstance();
		now.setTime(date);
		now.add(Calendar.DAY_OF_YEAR, nextDays);
		return sdf.format(now.getTime());
	}

	public static String getNextDay(String date, int nextDays) {
		Calendar now = Calendar.getInstance();
		try {
			now.setTime(sdf.parse(date));
		} catch (ParseException e) {
			return null;
		}
		now.add(Calendar.DAY_OF_YEAR, nextDays);
		return sdf.format(now.getTime());
	}

	public static Date parseDate(String dateStr) throws ParseException {
		return sdf2.parse(dateStr);
	}

}
