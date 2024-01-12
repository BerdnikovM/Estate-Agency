package com.example.estate_agency;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;


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
    private Label saveStatusLabel;

    @FXML
    private VBox estatePanel; // Получаем доступ к VBox панели недвижимости

    @FXML
    private Button addButton; // Кнопка "Добавить"

    @FXML
    private VBox addEstatePanel;

    @FXML
    private MenuItem reportsBtn;

    @FXML
    private MenuItem contractsBtn;

    @FXML
    private TextField estateNameField;

    @FXML
    private TextField floorSpaceField;

    @FXML
    private TextField balconiesSpaceField;

    @FXML
    private TextField numberBalconiesField;

    @FXML
    private TextField numberBedroomsField;

    @FXML
    private TextField numberGaragesField;

    @FXML
    private TextField numberParkingSpacesField;

    @FXML
    private TextField streetField;

    @FXML
    private TextField priceField;

    @FXML
    private TextField descriptField;

    @FXML
    private TextField imgPathField;

    @FXML
    private ScrollPane scrollEstate;

    @FXML
    private VBox InspectionPanel;

    @FXML
    private DatePicker datePicker;

    @FXML
    private ComboBox<String> estateComboBox;

    @FXML
    private void showLoginForm() {
        registrContainer.setVisible(false);
        loginContainer.setVisible(true);
        emailFieldLogin.setText("krutikov17@yandex.ru");
        passwordFieldLogin.setText("krutikov");
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
        estatePanel.setManaged(false);
        estatePanel.setVisible(false);
        addEstatePanel.setManaged(false);
        addEstatePanel.setVisible(false);
        scrollEstate.setManaged(false);
        scrollEstate.setVisible(false);
        InspectionPanel.setVisible(false);
        InspectionPanel.setManaged(false);
        profilePanel.setManaged(true);
        profilePanel.setVisible(true);
    }

    public void saveProfile() {
        String phone = profile_phoneField.getText();
        String address = profile_addressField.getText();

        UserProfile userProfile = UserProfile.getCurrentUserProfile();

        if (userProfile != null) {
            if (!userProfile.isEmployee()) {
                saveClientProfile(userProfile.getEmail(), phone, address);
            } else {
                saveEmployeeProfile(userProfile.getEmail(), phone, address);
            }
        } else {
            // Обработка случая, если userProfile равен null
        }
        saveStatusLabel.setText("Данные сохранены успешно");
    }

    private void saveClientProfile(String email, String phone, String address) {
        try {
            ConnectionClass connectionClass = new ConnectionClass();
            Connection connection = connectionClass.getConnection();

            String sql = "UPDATE client SET client_address = ?, phone = ? WHERE mail = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, address);
            statement.setString(2, phone);
            statement.setString(3, email);

            statement.executeUpdate();

            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void saveEmployeeProfile(String email, String phone, String address) {
        try {
            ConnectionClass connectionClass = new ConnectionClass();
            Connection connection = connectionClass.getConnection();

            String sql = "UPDATE employee SET address = ?, phone = ? WHERE mail = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, address);
            statement.setString(2, phone);
            statement.setString(3, email);

            statement.executeUpdate();

            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void initialize() {
        // Получаем значение isEmployee из UserProfile
        UserProfile userProfile = UserProfile.getCurrentUserProfile();
        if (userProfile != null) {
            if (userProfile.isEmployee()) {
                addButton.setVisible(true); // Показываем кнопку "Добавить"
                contractsBtn.setVisible(true);
                reportsBtn.setVisible(true);
                loadEstateData();
                loadEstateNames();
            } else {
                addButton.setVisible(false); // Скрываем кнопку "Добавить"
                contractsBtn.setVisible(false);
                reportsBtn.setVisible(false);
                loadEstateData();
                loadEstateNames();
            }
        }
    }



    public void showEstate() {
        profilePanel.setManaged(false);
        profilePanel.setVisible(false);
        addEstatePanel.setManaged(false);
        addEstatePanel.setVisible(false);
        InspectionPanel.setVisible(false);
        InspectionPanel.setManaged(false);
        scrollEstate.setManaged(true);
        scrollEstate.setVisible(true);
        estatePanel.setManaged(true);
        estatePanel.setVisible(true);
    }

    // Метод обработки нажатия на кнопку "Добавить"
    @FXML
    private void addEstate() {
        scrollEstate.setManaged(false);
        scrollEstate.setVisible(false);
        estatePanel.setManaged(false);
        addEstatePanel.setManaged(true);
        estatePanel.setVisible(false);
        addEstatePanel.setVisible(true);
    }
    @FXML
    private void createEstate() {
        String estateName = estateNameField.getText();
        double floorSpace = Double.parseDouble(floorSpaceField.getText());
        double balconiesSpace = Double.parseDouble(balconiesSpaceField.getText());
        int numberOfBalconies = Integer.parseInt(numberBalconiesField.getText());
        int numberOfBedrooms = Integer.parseInt(numberBedroomsField.getText());
        int numberOfGarages = Integer.parseInt(numberGaragesField.getText());
        int numberOfParkingSpaces = Integer.parseInt(numberParkingSpacesField.getText());
        String street = streetField.getText();
        double price = Double.parseDouble(priceField.getText());
        String estateDescription = descriptField.getText();
        String imagePath = imgPathField.getText();

        try {
            Connection connection = ConnectionClass.getConnection();
            if (connection != null) {
                String insertQuery = "INSERT INTO estate (estate_name, floor_space, balconies_space, number_of_balconies, number_of_bedrooms, number_of_garages, number_of_parking_spaces, estate_description, street, price, img_path) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                // Создаем PreparedStatement для выполнения запроса с параметрами
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
                preparedStatement.setString(1, estateName);
                preparedStatement.setDouble(2, floorSpace);
                preparedStatement.setDouble(3, balconiesSpace);
                preparedStatement.setInt(4, numberOfBalconies);
                preparedStatement.setInt(5, numberOfBedrooms);
                preparedStatement.setInt(6, numberOfGarages);
                preparedStatement.setInt(7, numberOfParkingSpaces);
                preparedStatement.setString(8, estateDescription);
                preparedStatement.setString(9, street);
                preparedStatement.setDouble(10, price);
                preparedStatement.setString(11, imagePath);

                int rowsInserted = preparedStatement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Новая недвижимость добавлена успешно!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // После добавления объекта недвижимости
        estatePanel.setManaged(true);
        addEstatePanel.setManaged(false);
        addEstatePanel.setVisible(false);
        estatePanel.setVisible(true);
        scrollEstate.setManaged(true);
        scrollEstate.setVisible(true);
    }

    public void loadEstateData() {
//        estatePanel.getChildren().clear(); // Очистим контейнер перед загрузкой новых данных

        try (Connection connection = ConnectionClass.getConnection();
             Statement statement = connection.createStatement()) {

            String selectQuery = "SELECT * FROM estate";
            ResultSet resultSet = statement.executeQuery(selectQuery);


            while (resultSet.next()) {
                Estate estate = createEstateFromResultSet(resultSet);
                addEstateBlock(estate);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Estate createEstateFromResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String estateName = resultSet.getString("estate_name");
        double floorSpace = resultSet.getDouble("floor_space");
        double balconiesSpace = resultSet.getDouble("balconies_space");
        int numberOfBalconies = resultSet.getInt("number_of_balconies");
        int numberOfBedrooms = resultSet.getInt("number_of_bedrooms");
        int numberOfGarages = resultSet.getInt("number_of_garages");
        int numberOfParkingSpaces = resultSet.getInt("number_of_parking_spaces");
        String street = resultSet.getString("street");
        double price = resultSet.getDouble("price");
        String estateDescription = resultSet.getString("estate_description");
        String imgPath = resultSet.getString("img_path");

        return new Estate(id, estateName, floorSpace, balconiesSpace, numberOfBalconies,
                numberOfBedrooms, numberOfGarages, numberOfParkingSpaces,
                street, price, estateDescription, imgPath);
    }

    private void addEstateBlock(Estate estate) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("EstateBlock.fxml"));
            HBox estateBlockNode = loader.load();

            EstateBlockController estateBlockController = loader.getController();
            estateBlockController.setEstateData(estate);

            estatePanel.getChildren().add(estateBlockNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openInspectionPanel() {
        scrollEstate.setManaged(false);
        scrollEstate.setVisible(false);
        estatePanel.setVisible(false);
        estatePanel.setManaged(false);
        addEstatePanel.setManaged(false);
        addEstatePanel.setVisible(false);
        profilePanel.setVisible(false);
        profilePanel.setManaged(false);
        InspectionPanel.setVisible(true);
        InspectionPanel.setManaged(true);
    }

    @FXML
    private void sendRequest() {
        String inspectionDate = datePicker.getValue().toString();
        String estateName = estateComboBox.getValue();
        String clientName = UserProfile.getCurrentUserProfile().getName();

        // Выполните подключение к базе данных и вставку данных в таблицу estate_inspection
        try (Connection connection = ConnectionClass.getConnection()) {
            String insertQuery = "INSERT INTO estate_inspection (inspection_date, estate_name, client_name) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                preparedStatement.setString(1, inspectionDate);
                preparedStatement.setString(2, estateName);
                preparedStatement.setString(3, clientName);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Запрос успешно отправлен.");
                } else {
                    System.out.println("Не удалось отправить запрос.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadEstateNames() {
        try (Connection connection = ConnectionClass.getConnection();
             Statement statement = connection.createStatement()) {

            String selectQuery = "SELECT estate_name FROM estate";
            ResultSet resultSet = statement.executeQuery(selectQuery);

            while (resultSet.next()) {
                String estateName = resultSet.getString("estate_name");
                estateComboBox.getItems().add(estateName);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
