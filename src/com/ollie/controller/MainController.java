package com.ollie.controller;


import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Vector;

import com.oanda.fxtrade.api.Account;
import com.oanda.fxtrade.api.FXClient;
import com.oanda.fxtrade.api.FXHistoryPoint;
import com.oanda.fxtrade.api.MarketOrder;
import com.oanda.fxtrade.api.OAException;
import com.oanda.fxtrade.api.SessionException;
import com.oanda.fxtrade.api.User;
import com.ollie.model.CurrentGraph;
import com.ollie.model.CurrentOrder;
import com.ollie.model.CurrentPair;
import com.ollie.model.GraphUpdater;
import com.ollie.model.Login;
import com.ollie.model.OpenOrders;
import com.ollie.model.Ticker;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableRow;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;



public class MainController implements Initializable{
	
	public static XYChart.Series<String, Number> series = new  XYChart.Series<String, Number>();
	final ToggleGroup group = new ToggleGroup();
	public static double currentRate = 1;
	
	@FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;
	@FXML
	private LineChart<String, Number> myChart;
	@FXML
	private ComboBox<String> currencyPairCombo;
	@FXML
	private TextField unitsTF;
	@FXML
	private TextField takeProfitTF;
	@FXML
	private TextField stopLossTF;
	@FXML 
	private ToggleButton buyBtn;
	@FXML 
	private ToggleButton sellBtn;
	@FXML
    public TextField rateTF;
	@FXML
	public GridPane accountOrderGP;
	@FXML
	public GridPane openOrdersGP;
	
	public void updateAccountOrderGP(){
		
		//clear past values
		
		ArrayList<Node> nodes = new ArrayList<Node>();
		
		for(int i = 0; i < 8; i++){
			nodes.add(openOrdersGP.getChildren().get(i));
		}
		
		openOrdersGP.getChildren().clear();
		openOrdersGP.getChildren().addAll(nodes);
		
		accountOrderGP.getChildren().clear();
		
		
		//add account details
		Label username = new Label("Username: " + Login.getAccountName());
		username.setStyle("-fx-font-size: 14px");
		GridPane.setHalignment(username, HPos.CENTER);
		accountOrderGP.add(username, 0, 0);
		
		DecimalFormat df = new DecimalFormat("#.#####");
		String b = df.format(Login.getBalance());
		Label balance = new Label("Balance: " + b);
		balance.setStyle("-fx-font-size: 14px");
		GridPane.setHalignment(balance, HPos.CENTER);
		accountOrderGP.add(balance, 1, 0);
		
		
		// update orders
		try {
			OpenOrders.updateOpenOrders();
		} catch (SessionException e) {
			e.printStackTrace();
		}
	
		
		//loop through and print to grid
		for(int i = 0; i < OpenOrders.getSize(); i++){
			
			
			Label profitLoss = new Label(OpenOrders.getPL(i));
			
			if(OpenOrders.getPL(i).startsWith("-")){
				profitLoss.setStyle("-fx-background-color:  #ff5b5b");
			}
			else{
				profitLoss.setStyle("-fx-background-color: #22cda5");
			}
			
			openOrdersGP.add(new Label(OpenOrders.getTransactionID(i)), 0, i+1);
			openOrdersGP.add(new Label(OpenOrders.getUnits(i)), 1, i+1);
			openOrdersGP.add(new Label(OpenOrders.getPair(i)), 2, i+1);
			openOrdersGP.add(new Label(OpenOrders.getBuySell(i)), 3, i+1);
			openOrdersGP.add(new Label(OpenOrders.getPrice(i)), 4, i+1);
			openOrdersGP.add(profitLoss, 5, i+1);
			openOrdersGP.add(new Label(OpenOrders.getSL(i)), 6, i+1);
			openOrdersGP.add(new Label(OpenOrders.getTP(i)), 7, i+1);
			openOrdersGP.add(OpenOrders.getButton(i), 8, i+1);
		
			
		}
		
	}
	
	public void confirmOrderBtnPressed() throws SessionException{
		
		//need to get rates and update text field 
		//need to move this method to model
		
		CurrentOrder  co = new CurrentOrder();
		co.setPair(CurrentPair.getCurrentPair());
		co.setUnits(unitsTF.getText(), (String) group.getSelectedToggle().getUserData());	
		if(!stopLossTF.getText().equals("")){co.setSL(stopLossTF.getText());}
		if(!takeProfitTF.getText().equals("")){co.setTP(takeProfitTF.getText());}
		co.executeOrder();
		
		
		FXClient fxclient = Login.returnFXClient();
		User me = fxclient.getUser();
		Vector<Account> accounts = me.getAccounts();
		Account myaccount = (Account)accounts.firstElement();
		Vector<MarketOrder> trades = new Vector<MarketOrder>();
		try {
			trades = myaccount.getTrades();
		}
		catch (OAException oe) {
			System.out.println("Example: caught: " + oe);
		}
		System.out.println("CURRENT TRADES:");
		for (int i = 0; i < trades.size(); i++) {
			System.out.println(trades.elementAt(i));
		}
			
	}
	
	public void updateRateTF(double d){
		DecimalFormat df = new DecimalFormat("#.#####");
		rateTF.setText(df.format(d));
	}
	
	public static double getRateTF(){
		return currentRate; 
	}

	public void updatePairBtnPressed(){
		String s = (String) currencyPairCombo.getValue();
		CurrentPair.setCurrentPair(s);
		initGraph();

	}
	public void plusBtnPressed(){
		
		CurrentGraph.setCurrentGraphPointCount(CurrentGraph.getCurrentGraphPointCount() + 1);
		initGraph();
	}
	public void minusBtnPressed(){
		
		CurrentGraph.setCurrentGraphPointCount(CurrentGraph.getCurrentGraphPointCount() - 1);
		initGraph();
	}
	public void fiveSecBtnPressed(){
		
		CurrentGraph.setCurrentGraphInterval(FXClient.INTERVAL_5_SEC);
		initGraph();
	}
	public void thirtySecBtnPressed(){
		
		CurrentGraph.setCurrentGraphInterval(FXClient.INTERVAL_30_SEC);
		initGraph();
	}
	public void oneMinBtnPressed(){
		
		CurrentGraph.setCurrentGraphInterval(FXClient.INTERVAL_1_MIN);
		initGraph();
	}
	public void fiveMinBtnPressed(){
		
		CurrentGraph.setCurrentGraphInterval(FXClient.INTERVAL_5_MIN);
		initGraph();
	}
	public void oneHrBtnPressed(){
		
		CurrentGraph.setCurrentGraphInterval(FXClient.INTERVAL_1_HOUR);
		initGraph();
	}
	public void oneDayBtnPressed(){
		
		CurrentGraph.setCurrentGraphInterval(FXClient.INTERVAL_1_DAY);
		initGraph();
	}
	public void buySellBtnPressed(){
		if(buyBtn.isSelected()){
			
			buyBtn.setBorder(new Border(new BorderStroke(Color.web("#1f82b8"), 
		            BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
			sellBtn.setBorder(null);
		}
		else{
			
			sellBtn.setBorder(new Border(new BorderStroke(Color.web("#1f82b8"), 
		            BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
			buyBtn.setBorder(null);
		}
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void updateGraph(){
		
		// checks to see if update in progress 
		
		if(CurrentGraph.getUpdateInProgress() == false){
			
			CurrentGraph.setUpdateInProgress(true);
			
			//gets history vector of the current pair and current interval 
			//removes oldest data point from series and adds newest
				
			CurrentPair.updateHistory(CurrentGraph.getCurrentGraphInterval());
			
			Vector<FXHistoryPoint> currentVector = CurrentPair.getHistoryVector(CurrentGraph.getCurrentGraphInterval());
			
			series.getData().remove(0);
			String timeStamp = CurrentGraph.getTimeStamp(currentVector.elementAt(CurrentGraph.getCurrentGraphPointCount()-1).getTimestamp());
			Double ask = currentVector.elementAt(CurrentGraph.getCurrentGraphPointCount()-1).getMax().getAsk();
			series.getData().add(new XYChart.Data(timeStamp, ask));
			
			CurrentGraph.setUpdateInProgress(false);
			
		}
		
		
		
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void initGraph(){
		
		// checks to see if update in progress 
		
		if(CurrentGraph.getUpdateInProgress() == false){
			
			CurrentGraph.setUpdateInProgress(true);

			//gets history vector of the current pair and current interval 
			//loops through and adds to series
			//sets chart to display series

			CurrentPair.updateHistory(CurrentGraph.getCurrentGraphInterval());

			Vector<FXHistoryPoint> currentVector = CurrentPair.getHistoryVector(CurrentGraph.getCurrentGraphInterval());


			series.getData().clear();

			for(int i = 0; i < currentVector.size(); i++){

				String timeStamp = CurrentGraph.getTimeStamp(currentVector.elementAt(i).getTimestamp());
				Double ask = currentVector.elementAt(i).getMax().getAsk();
				series.getData().add(new XYChart.Data(timeStamp, ask));
			}

			
			CurrentGraph.setUpdateInProgress(false);
		}
		
		
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		//update open orders
		updateAccountOrderGP();
		
		
		//init rate ticker and rateTF
		
		FXClient fxclient = Login.returnFXClient();
		Login.returnFXClient().createRateThread(true);
		Ticker t = new Ticker();
		try { fxclient.getRateTable().getEventManager().add(t); }
		catch (SessionException e) { System.out.println(e);}
		
		Task rateUpdater = new Task<Void>() {
		    @Override public void run() {
		        do{
		        	updateRateTF(currentRate);
		        	
		        	
		        	//run in fx thread
		        	Platform.runLater(new Runnable() {
		                @Override public void run() {
		                	updateAccountOrderGP();
		                }
		            });
		        	
		        	try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
		        }
		        while(true);
		        
		    }

			@Override
			protected Void call() throws Exception {
				return null;
			}
		};
		new Thread(rateUpdater).start();
		
		
		//init combo boxes
		currencyPairCombo.getItems().removeAll(currencyPairCombo.getItems());
		currencyPairCombo.getItems().addAll("GBP/USD","EUR/GBP","EUR/USD","EUR/AUD","GBP/AUD","AUD/USD");
		currencyPairCombo.getSelectionModel().select("GBP/USD");
		
		
		//init toggle buttons
		
		buyBtn.setToggleGroup(group);
		buyBtn.setUserData("BUY");
		sellBtn.setToggleGroup(group);
		sellBtn.setUserData("SELL");
		buyBtn.setSelected(true);
		buyBtn.setBorder(new Border(new BorderStroke(Color.BLACK, 
	            BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		
		//init current pair
		CurrentPair.initCurrentPair();	
		
		//init series
		series.getData().add(new XYChart.Data("",0));
		
		//add series to chart
		myChart.getData().addAll(series);
		myChart.animatedProperty().set(false);
		myChart.setCreateSymbols(false);
		yAxis.autoRangingProperty().set(true);
		yAxis.setForceZeroInRange(false);
		xAxis.setLabel("");
		myChart.setLegendVisible(false);
		myChart.setTitle("");
		
		
		//start graph updater thread
		GraphUpdater updater = new GraphUpdater();
        Thread thread = new Thread(updater);
        thread.start();
		
		
	}
	

}


