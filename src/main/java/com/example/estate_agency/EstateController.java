package com.example.estate_agency;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.CheckBox;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;


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
    private TextField emailFieldLogin;

    @FXML
    private PasswordField passwordFieldLogin;

    @FXML
    private Label errorLabel;

    @FXML
    private CheckBox employeeCheckBox;


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

    public void loginAction() {
        String email = emailFieldLogin.getText();
        String password = passwordFieldLogin.getText();

        if (employeeCheckBox.isSelected()) {
            // Проверка наличия почты и пароля в таблице employee
            boolean isValidEmployee = checkEmployeeCredentials(email, password);
            if (isValidEmployee) {
                // Логика для входа сотрудника
                System.out.println("Вход выполнен как сотрудник");
            } else {
                // Обработка неправильных учетных данных сотрудника
                clearErrorLabel();
                showErrorLabel("Сотрудник с такой почтой и паролем не найден.");
            }
        } else {
            boolean loginSuccessful = loginUser(email, password);

            // Если вход успешен, выполните переход на новую страницу
            if (loginSuccessful) {
                // Ваш код для перехода на новую страницу
                System.out.println("Вход выполнен успешно!");
            } else {
                // В случае неудачного входа выполните соответствующие действия (например, выведите сообщение об ошибке)
                clearErrorLabel();
                showErrorLabel("Пользователь с такой почтой и паролем не найден.");
            }
        }
    }

    // Метод для проверки входа пользователя
    private boolean loginUser(String email, String password) {
        try {
            ConnectionClass connectionClass = new ConnectionClass();
            Connection connection = connectionClass.getConnection();

            // SQL-запрос для проверки наличия пользователя в базе данных
            String sql = "SELECT * FROM client WHERE mail = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, email);
            statement.setString(2, password);

            // Выполнение запроса
            ResultSet resultSet = statement.executeQuery();

            // Если результат запроса содержит записи, возвращаем true (успешный вход)
            if (resultSet.next()) {
                statement.close();
                connection.close();
                return true;
            }

            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Если не найдено соответствующих записей, возвращаем false (неудачный вход)
    }

    private boolean checkEmployeeCredentials(String email, String password) {
        String query = "SELECT * FROM employee WHERE mail = ? AND password = ?";
        try {
            ConnectionClass connectionClass = new ConnectionClass();
            Connection connection = connectionClass.getConnection();

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, email);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();

            boolean isValid = resultSet.next(); // Если есть хотя бы одна запись, считаем учетные данные правильными

            resultSet.close();
            statement.close();
            connection.close();

            return isValid;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showErrorLabel(String errorMessage) {
        errorLabel.setVisible(true);
        errorLabel.setText(errorMessage);
    }

    // Метод для очистки сообщения об ошибке
    private void clearErrorLabel() {
        errorLabel.setVisible(false);
        errorLabel.setText("");
    }
}
