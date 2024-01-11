package com.example.estate_agency;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class EstateBlockController {

    @FXML
    private ImageView estateImageView;

    @FXML
    private Label nameLabel;

    @FXML
    private Label priceLabel;

    @FXML
    private Button contactAgentButton;

    private Estate estate; // Объект недвижимости, связанный с этим блоком

    public void initialize() {
        // Настройка начальных значений или обработка событий при инициализации
    }

    // Метод для установки данных объекта недвижимости
    public void setEstateData(Estate estate) {
        this.estate = estate;

        // Установка изображения (предположим, что в Estate есть метод getImagePath())
        Image image = new Image(getClass().getResource("/com/example/estate_agency/images/" + estate.getImgPath()).toExternalForm());
        estateImageView.setImage(image);

        // Установка названия объекта и цены
        nameLabel.setText(estate.getEstateName());
        priceLabel.setText("Цена: $" + estate.getPrice());
    }

    // Обработчик события при нажатии на кнопку "Связаться с агентом"
    @FXML
    private void contactAgent(ActionEvent event) {
        // Добавьте свой код для действий при нажатии на кнопку
        // Например, можно отобразить окно с контактной информацией агента
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Контакт с агентом");
        alert.setHeaderText("Свяжитесь с нашим агентом для получения дополнительной информации.");
        alert.showAndWait();
    }
}
