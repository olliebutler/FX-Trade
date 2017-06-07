package com.ollie.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.oanda.fxtrade.api.FXEventInfo;
import com.oanda.fxtrade.api.FXEventManager;
import com.oanda.fxtrade.api.FXRateEvent;
import com.oanda.fxtrade.api.FXRateEventInfo;
import com.ollie.controller.MainController;

import javafx.scene.chart.XYChart;

public class Ticker extends FXRateEvent
{
	public void handle(FXEventInfo EI, FXEventManager EM)
	{

		FXRateEventInfo REI = (FXRateEventInfo) EI;
			
		if(REI.getPair().getPair().equals(CurrentPair.getCurrentPair())){
			double ask = REI.getTick().getAsk();
			
			MainController.currentRate = ask;
		}
		
	}
}