package com.ollie.model;
import com.oanda.fxtrade.api.*;

public class RateTicker{
	
	public static void startRateTicker()	{
		
		//import client
		
		FXClient fxclient = Login.returnFXClient();
		
		//create new ticker to monitor current pair
		Ticker t = new Ticker();
		try { fxclient.getRateTable().getEventManager().add(t); }
		catch (SessionException e) { System.out.println(e); }
	}
}

