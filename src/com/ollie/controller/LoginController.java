package com.ollie.controller;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

import com.ollie.model.Login;

public class LoginController {
	
	@FXML
	public TextField userF;
    public PasswordField passwordF;
    public Label errorLabel;
    
    public void loginButtonPressed() throws IOException{

    	String user = userF.getText();
    	String pass = passwordF.getText();

    	int res = Login.loginAttempt(user, pass);
    	
    	System.out.println("Result of login attempt " + res);
    	
    	if (res == 1) {

            System.out.println("LOGIN SUCCESSFUL");

            //Change scene 
            
            Parent mainView = FXMLLoader.load(getClass().getResource("../view/MainView.fxml"));

            Scene mainScene = new Scene(mainView, 1200, 800);

            Stage mainStage = new Stage();

            mainStage.setTitle("FX Trade");

            mainStage.setScene(mainScene);

            mainStage.show();

            Stage stage = (Stage) userF.getScene().getWindow();
            stage.close();


        } 
    	else {
        	   	
        errorLabel.setText("Error Please Try Again");
       
    }

}
}
