package com.ollie.model;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.Vector;

import com.oanda.fxtrade.api.Account;
import com.oanda.fxtrade.api.FXClient;
import com.oanda.fxtrade.api.MarketOrder;
import com.oanda.fxtrade.api.OAException;
import com.oanda.fxtrade.api.RateTableException;
import com.oanda.fxtrade.api.SessionException;
import com.oanda.fxtrade.api.User;

import javafx.scene.control.Button;
import javafx.scene.paint.Color;

public class OpenOrders {

	private static Vector<MarketOrder> trades = new Vector<MarketOrder>();
	private static ArrayList<Button> buttons = new ArrayList<Button>();
	private static ArrayList<String> transactionID = new ArrayList<String>();
	private static ArrayList<String> time = new ArrayList<String>();
	private static ArrayList<String> units = new ArrayList<String>();
	private static ArrayList<String> pair = new ArrayList<String>();
	private static ArrayList<String> price = new ArrayList<String>();
	private static ArrayList<String> stopLoss = new ArrayList<String>();
	private static ArrayList<String> takeProfit = new ArrayList<String>();
	private static ArrayList<String> profitLoss = new ArrayList<String>();
	private static ArrayList<String> buysell = new ArrayList<String>();

	
	@SuppressWarnings("unchecked")
	public static void updateOpenOrders() throws SessionException{
		trades.clear();
		buttons.clear();
		transactionID.clear();
		time.clear();
		units.clear();
		pair.clear();
		price.clear();
		stopLoss.clear();
		takeProfit.clear();
		profitLoss.clear();
		
		
		FXClient fxclient = Login.returnFXClient();
		User me = fxclient.getUser();
		Vector<Account> accounts = me.getAccounts();
		Account myaccount = (Account)accounts.firstElement();
		try {
			trades = myaccount.getTrades();
		}
		catch (OAException oe) {
			System.out.println("Example: caught: " + oe);
		}
		
		for (int i = 0; i < trades.size(); i++) {
			
			MarketOrder mo = trades.get(i);
			
			transactionID.add(Objects.toString(mo.getTransactionNumber()));
			
			Date date = new Date(mo.getTimestamp()*1000L);
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			String formattedDate = sdf.format(date);
			time.add(formattedDate);
			
			
			if(mo.getUnits() < 0){
				buysell.add("SELL");
			}
			else{
				buysell.add("BUY");

			}
			
			long unitslong = mo.getUnits();
			if(unitslong < 0){
				unitslong = -unitslong;
				units.add(Objects.toString(unitslong));
			}
			else{
				units.add(Objects.toString(mo.getUnits()));
			}
			
			
			
			pair.add(mo.getPair().toString());
			
			price.add(Objects.toString(mo.getPrice()));
			
			stopLoss.add(Objects.toString(mo.getStopLoss()));
			
			takeProfit.add(Objects.toString(mo.getTakeProfit()));
			
			try {
				DecimalFormat df = new DecimalFormat("#.#####");
				String pl = df.format(mo.getUnrealizedPL(fxclient.getRateTable().getRate(mo.getPair())));
				profitLoss.add(pl);
				
			} catch (RateTableException e) {
				e.printStackTrace();
			}
			
			final int j = i;
			Button b = new Button();
			b.setStyle("-fx-background-color:  #ff5b5b");
			b.setOnAction(event -> {
	            try {
					closeOrder(j);
				} catch (Exception e) {
					e.printStackTrace();
				}
	        });
			b.setText("Close");
			buttons.add(b);
			
			
		}
	
		
	}
	
	@SuppressWarnings("unchecked")
	public static void closeOrder(int i) throws SessionException{
		
		MarketOrder mo = trades.get(i);
		
		FXClient fxclient = Login.returnFXClient();
		User me = fxclient.getUser();
		Vector<Account> accounts = me.getAccounts();
		Account myaccount = (Account)accounts.firstElement();
		
		try {
			myaccount.close(mo);
		} catch (OAException e) {
			e.printStackTrace();
		}
		
	}
	
	public static Button getButton(int i){
	
		return buttons.get(i);
	}
	
	public static String getTransactionID(int i){
		return transactionID.get(i);
	}

	public static String getTime(int i){
		return time.get(i);
	}

	public static String getUnits(int i){
		return units.get(i);
	}
	
	public static String getPair(int i){
		return pair.get(i);
	}
	
	public static String getPrice(int i){
		return price.get(i);
	}
	
	public static String getSL(int i){
		return stopLoss.get(i);
	}
	
	public static String getTP(int i){
		return takeProfit.get(i);
	}
	
	public static String getPL(int i){
		return profitLoss.get(i);
	}

	public static int getSize(){
		return trades.size();
	}
	public static String getBuySell(int i){
		return buysell.get(i);
	}
}
