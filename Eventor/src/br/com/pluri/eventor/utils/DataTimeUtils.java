package br.com.pluri.eventor.utils;

import java.util.Calendar;
import java.util.Date;

public class DataTimeUtils {
	
	public static Date merge(Date date, Date time){
		if (date == null || time == null) {
			return null;
		}
		Calendar aDate = Calendar.getInstance();
		aDate.setTime(date);
		
		Calendar aTime = Calendar.getInstance();
		aTime.setTime(time);
		aDate.set(Calendar.HOUR_OF_DAY, aTime.get(Calendar.HOUR));
		aDate.set(Calendar.MINUTE, aTime.get(Calendar.MINUTE));
		return aDate.getTime();
	}
	
}
