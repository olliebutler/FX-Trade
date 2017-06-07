package com.ollie.model;

import java.util.Vector;

import com.oanda.fxtrade.api.API;
import com.oanda.fxtrade.api.FXClient;
import com.oanda.fxtrade.api.FXHistoryPoint;
import com.oanda.fxtrade.api.FXPair;
import com.oanda.fxtrade.api.OAException;

public class CurrentPair {

	private static String currentPair = "GBP/USD";
	private static FXPair p;
	private static Vector<FXHistoryPoint> fiveSecVector = new Vector<FXHistoryPoint>();
	private static Vector<FXHistoryPoint> thirtySecVector = new Vector<FXHistoryPoint>();
	private static Vector<FXHistoryPoint> oneMinVector = new Vector<FXHistoryPoint>();
	private static Vector<FXHistoryPoint> fiveMinVector = new Vector<FXHistoryPoint>();
	private static Vector<FXHistoryPoint> oneHourVector = new Vector<FXHistoryPoint>();
	private static Vector<FXHistoryPoint> oneDayVector = new Vector<FXHistoryPoint>();
	
	public static void setCurrentPair(String pair){
		currentPair = pair;
		initCurrentPair();
	}
	
	public static String getCurrentPair(){
		return currentPair;
	}
	
	public static void initCurrentPair(){
		
		FXPair pr = API.createFXPair();
		pr.setPair(currentPair);
		p = pr;
		updateHistories();
	}
	
	@SuppressWarnings("unchecked")
	public static void updateHistories(){
			
		FXClient fxclient = Login.returnFXClient();
		
			try {
				
				fiveSecVector.clear();
				thirtySecVector.clear();
				oneMinVector.clear();
				fiveMinVector.clear();
				oneHourVector.clear();
				oneDayVector.clear();
				
				fiveSecVector.addAll(fxclient.getRateTable().getHistory(p, FXClient.INTERVAL_5_SEC, CurrentGraph.getCurrentGraphPointCount()));
				thirtySecVector.addAll(fxclient.getRateTable().getHistory(p, FXClient.INTERVAL_30_SEC, CurrentGraph.getCurrentGraphPointCount()));
				oneMinVector.addAll(fxclient.getRateTable().getHistory(p, FXClient.INTERVAL_1_MIN, CurrentGraph.getCurrentGraphPointCount()));
				fiveMinVector.addAll(fxclient.getRateTable().getHistory(p, FXClient.INTERVAL_5_MIN, CurrentGraph.getCurrentGraphPointCount()));
				oneHourVector.addAll(fxclient.getRateTable().getHistory(p, FXClient.INTERVAL_1_HOUR, CurrentGraph.getCurrentGraphPointCount()));
				oneDayVector.addAll(fxclient.getRateTable().getHistory(p, FXClient.INTERVAL_1_DAY, CurrentGraph.getCurrentGraphPointCount()));
				
			} catch (OAException e) {
				e.printStackTrace();
			}
				
	}
	public static Vector<FXHistoryPoint> getHistoryVector(Long interval){
		
		 if(interval == FXClient.INTERVAL_5_SEC) return fiveSecVector;
		 if(interval == FXClient.INTERVAL_30_SEC) return thirtySecVector;
		 if(interval == FXClient.INTERVAL_1_MIN) return oneMinVector;
		 if(interval == FXClient.INTERVAL_5_MIN) return fiveMinVector;
		 if(interval == FXClient.INTERVAL_1_HOUR) return oneHourVector;
		 if(interval == FXClient.INTERVAL_1_DAY) return oneDayVector;
		 return null;
	}
	
	@SuppressWarnings("unchecked")
	public static void updateHistory(Long interval){
		
		FXClient fxclient = Login.returnFXClient();
		
		try {
			
			if(interval == FXClient.INTERVAL_5_SEC){
				fiveSecVector.clear();
				fiveSecVector.addAll(fxclient.getRateTable().getHistory(p, FXClient.INTERVAL_5_SEC, CurrentGraph.getCurrentGraphPointCount()));
			}
			else if(interval == FXClient.INTERVAL_30_SEC){
				thirtySecVector.clear();
				thirtySecVector.addAll(fxclient.getRateTable().getHistory(p, FXClient.INTERVAL_30_SEC, CurrentGraph.getCurrentGraphPointCount()));
			}
			else if(interval == FXClient.INTERVAL_1_MIN){
				oneMinVector.clear();
				oneMinVector.addAll(fxclient.getRateTable().getHistory(p, FXClient.INTERVAL_1_MIN, CurrentGraph.getCurrentGraphPointCount()));
			}
			else if(interval == FXClient.INTERVAL_5_MIN){
				fiveMinVector.clear();
				fiveMinVector.addAll(fxclient.getRateTable().getHistory(p, FXClient.INTERVAL_5_MIN, CurrentGraph.getCurrentGraphPointCount()));
			}
			else if(interval == FXClient.INTERVAL_1_HOUR){
				oneHourVector.clear();
				oneHourVector.addAll(fxclient.getRateTable().getHistory(p, FXClient.INTERVAL_1_HOUR, CurrentGraph.getCurrentGraphPointCount()));
			}
			else if(interval == FXClient.INTERVAL_1_DAY){
				oneDayVector.clear();
				oneDayVector.addAll(fxclient.getRateTable().getHistory(p, FXClient.INTERVAL_1_DAY, CurrentGraph.getCurrentGraphPointCount()));
			}
			
		} catch (OAException e) {
			e.printStackTrace();
		}
		
		
	}
}
