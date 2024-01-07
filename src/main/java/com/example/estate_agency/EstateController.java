package com.example.estate_agency;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.VBox;

public class EstateController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private VBox registrContainer;

    @FXML
    private VBox loginContainer;

    @FXML
    private void showLoginForm() {
        registrContainer.setVisible(false);
        loginContainer.setVisible(true);
    }

    @FXML
    private void showRegisterForm() {
        loginContainer.setVisible(false);
        registrContainer.setVisible(true);
    }

    public void registerAction() {
        String name = nameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        // Implement registration logic here.
    }

}
