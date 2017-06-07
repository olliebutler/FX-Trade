package com.ollie.controller;

import java.io.IOException;

import com.ollie.model.Login;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;


public class MainApp extends Application {

	private Stage primaryStage;
    private BorderPane rootLayout;
    
	@Override
	public void start(Stage primaryStage) {
		
		  this.primaryStage = primaryStage;
	      this.primaryStage.setTitle("Login");

	       initRootLayout();
	       
	       showLogin();

	        
		
	}
	@Override
	public void stop(){
	    Login.returnFXClient().logout();
	}
	
	public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/com/ollie/view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	public void showLogin() {
        try {
            // Load test.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/com/ollie/view/Login.fxml"));
            AnchorPane login = (AnchorPane) loader.load();

            // Set test into the center of root layout.
            rootLayout.setCenter(login);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	/**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }
    public BorderPane getRootLayout(){
    	return rootLayout;
    }

	public static void main(String[] args) {
		launch(args);
	}
}
