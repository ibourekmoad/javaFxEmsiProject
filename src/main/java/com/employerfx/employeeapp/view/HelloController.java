package com.employerfx.employeeapp.view;

import com.employerfx.employeeapp.dao.DaoAccountUser.DaoAccountUser;
import com.employerfx.employeeapp.dao.DaoAccountUser.impl.DaoAccountUserImpl;
import com.employerfx.employeeapp.entities.AccountUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class HelloController {
    @FXML
    private Label loginErrorLabel;

    @FXML
    private TextField  usernameTestfield;

    @FXML
    private PasswordField passwordField;



    private Stage stage;
    private Scene scene;
    private Parent root;


    public void loginButtonOnAction(ActionEvent e) throws IOException {
        loginErrorLabel.setText("you try to login");

        if (usernameTestfield.getText().isBlank() == false &&
             passwordField.getText().isBlank() == false){
//            loginErrorLabel.setText("you try to login");

            DaoAccountUser daoAccountUser = new DaoAccountUserImpl();
            AccountUser queryAccountUser = daoAccountUser.getAccountUser(usernameTestfield.getText(),
                    passwordField.getText());
            if (queryAccountUser != null){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("manage-View.fxml"));
                root = loader.load();

                stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();

            }else {
                loginErrorLabel.setText("invalid login ");
            }
        }else {
            loginErrorLabel.setText("Please enter username and password");
        }
    }



}