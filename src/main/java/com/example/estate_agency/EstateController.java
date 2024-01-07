package com.example.estate_agency;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.VBox;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


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

        // Вызываем метод для добавления пользователя в базу данных
        registerUser(name, email, password);
    }

    // Метод для добавления нового пользователя в базу данных
    private void registerUser(String name, String email, String password) {
        try {
            ConnectionClass connectionClass = new ConnectionClass();
            Connection connection = connectionClass.getConnection();

            // SQL-запрос для добавления пользователя в базу данных
            String sql = "INSERT INTO client (client_name, mail, password) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, password);

            // Выполнение запроса
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Пользователь зарегистрирован успешно!");
            }

            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
