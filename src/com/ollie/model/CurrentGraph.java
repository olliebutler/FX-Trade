package com.ollie.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.oanda.fxtrade.api.FXClient;


public class CurrentGraph {
	
	private static Long currentGraphInterval = FXClient.INTERVAL_5_SEC;
	private static int currentGraphPointCount = 75;
	private static boolean updateInProgress = false;
	
	public static Long getCurrentGraphInterval(){
		return currentGraphInterval;
	}
	public static void setCurrentGraphInterval(Long l){
		currentGraphInterval = l;
	}
	public static int getCurrentGraphPointCount(){
		return currentGraphPointCount;
	}
	public static void setCurrentGraphPointCount(int i){
		currentGraphPointCount = i;
	}
	public static String getTimeStamp(Long l){
		
		
		if( CurrentGraph.getCurrentGraphInterval().equals(FXClient.INTERVAL_1_DAY)){
			Date date = new Date(l*1000L); // *1000 is to convert seconds to milliseconds
			SimpleDateFormat sdf = new SimpleDateFormat("MM-dd"); // the format of your date
			String formattedDate = sdf.format(date);
			return formattedDate;
		}
		Date date = new Date(l*1000L); // *1000 is to convert seconds to milliseconds
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss"); // the format of your date
		String formattedDate = sdf.format(date);
		return formattedDate;
		
		
	}
	public static void setUpdateInProgress (boolean b){
		updateInProgress = b;
	}
	public static boolean getUpdateInProgress(){
		return updateInProgress;
	}

}
