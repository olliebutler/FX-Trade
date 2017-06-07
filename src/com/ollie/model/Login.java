package com.ollie.model;

import java.util.Vector;

import com.oanda.fxtrade.api.API;
import com.oanda.fxtrade.api.Account;
import com.oanda.fxtrade.api.AccountException;
import com.oanda.fxtrade.api.FXClient;
import com.oanda.fxtrade.api.InvalidPasswordException;
import com.oanda.fxtrade.api.InvalidUserException;
import com.oanda.fxtrade.api.MultiFactorAuthenticationException;
import com.oanda.fxtrade.api.SessionException;
import com.oanda.fxtrade.api.User;

public class Login {

	private static FXClient fxclient;
	
	public static int loginAttempt(String user, String pass){
		
		fxclient = API.createFXGame();
		fxclient.setWithRateThread(true); 
		
		System.out.println("login attempt with user " + user);
		
		try { fxclient.login(user, pass); }
		catch (SessionException e) { return -1; }
		catch (InvalidUserException e) { return -2; }
		catch (InvalidPasswordException e) { return -3; }
		catch (MultiFactorAuthenticationException e){return -4;}
			
		return 1;
		
	}
	
	public static FXClient returnFXClient(){
		return fxclient;
	}
	public static void createKeepAlive(){
		fxclient.createKeepAliveThread(true);
	}
	public static String getAccountName(){
		try {
			return fxclient.getUser().getUserName();
		} catch (SessionException e) {
			e.printStackTrace();
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	public static double getBalance(){
		User me;
		try {
			me = fxclient.getUser();
			Vector<Account> accounts = me.getAccounts();
			Account myaccount = (Account)accounts.firstElement();
			return myaccount.getBalance();
			
		} catch (SessionException | AccountException e) {
			e.printStackTrace();
		}
		
		return 0;
		
	}
}
