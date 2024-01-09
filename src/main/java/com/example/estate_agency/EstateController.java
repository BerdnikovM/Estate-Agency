package com.example.estate_agency;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
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
    private VBox profilePanel;

    @FXML
    private TextField profile_nameField;

    @FXML
    private TextField profile_emailField;

    @FXML
    private TextField profile_phoneField;

    @FXML
    private TextField profile_addressField;


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
                clearErrorLabel();
                showErrorLabel("Пользователь зарегистрирован успешно!");
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
                try {
                    // Загрузка новой сцены для сотрудника
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("real-estate.fxml"));
                    Parent root = loader.load();

                    // Создание новой сцены и установка в Stage
                    Scene newScene = new Scene(root);
                    Stage currentStage = (Stage) emailFieldLogin.getScene().getWindow();
                    currentStage.setScene(newScene);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                // Обработка неправильных учетных данных сотрудника
                clearErrorLabel();
                showErrorLabel("Сотрудник с такой почтой и паролем не найден.");
            }
        } else {
            boolean loginSuccessful = loginUser(email, password);

            // Если вход успешен, выполните переход на новую страницу
            if (loginSuccessful) {
                try {
                    // Загрузка новой сцены
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("real-estate.fxml"));
                    Parent root = loader.load();

                    // Создание новой сцены и установка в Stage
                    Scene newScene = new Scene(root);
                    Stage currentStage = (Stage) emailFieldLogin.getScene().getWindow();
                    currentStage.setScene(newScene);
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
                fillUserProfile(email);
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
            if (isValid) {
                fillEmployeeProfile(email);
            }
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

    private void fillUserProfile(String email) {
        try {
            ConnectionClass connectionClass = new ConnectionClass();
            Connection connection = connectionClass.getConnection();

            // SQL-запрос для получения данных пользователя по email
            String sql = "SELECT * FROM client WHERE mail = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, email);

            // Выполнение запроса
            ResultSet resultSet = statement.executeQuery();

            // Если результат запроса содержит записи, заполняем объект UserProfile
            if (resultSet.next()) {
                UserProfile userProfile = new UserProfile();
                userProfile.setName(resultSet.getString("client_name"));
                userProfile.setEmail(resultSet.getString("mail"));
                userProfile.setPhoneNumber(resultSet.getString("phone"));
                userProfile.setAddress(resultSet.getString("client_address"));
                userProfile.setEmployee(false);
                UserProfile.setCurrentUserProfile(userProfile);
            }

            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void fillEmployeeProfile(String email) {
        try {
            ConnectionClass connectionClass = new ConnectionClass();
            Connection connection = connectionClass.getConnection();

            // SQL-запрос для получения данных сотрудника по email из таблицы employee
            String sql = "SELECT * FROM employee WHERE mail = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, email);

            // Выполнение запроса
            ResultSet resultSet = statement.executeQuery();

            // Если результат запроса содержит записи, заполняем объект UserProfile
            if (resultSet.next()) {
                UserProfile userProfile = new UserProfile();
                userProfile.setName(resultSet.getString("full_name"));
                userProfile.setEmail(resultSet.getString("mail"));
                userProfile.setPhoneNumber(resultSet.getString("phone"));
                userProfile.setAddress(resultSet.getString("address"));
                userProfile.setEmployee(true);
                UserProfile.setCurrentUserProfile(userProfile);
            }

            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void showProfile() {
        UserProfile userProfile = UserProfile.getCurrentUserProfile();

        if (userProfile != null) {
            profile_nameField.setText(userProfile.getName());
            profile_emailField.setText(userProfile.getEmail());
            profile_phoneField.setText(userProfile.getPhoneNumber());
            profile_addressField.setText(userProfile.getAddress());
        }

        // Показать панель профиля
        profilePanel.setVisible(true);
    }


    public void saveProfile() {
        // Здесь добавьте логику сохранения данных профиля в базу данных
        // Получите значения из полей profile_phoneField и profile_addressField и сохраните их в базу данных
        String phone = profile_phoneField.getText();
        String address = profile_addressField.getText();

        // Ваша логика для сохранения этих данных в базу данных
    }
}
