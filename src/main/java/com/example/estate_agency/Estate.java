package com.example.estate_agency;

public class Estate {
    private int id;
    private String estateName;
    private double floorSpace;
    private double balconiesSpace;
    private int numberOfBalconies;
    private int numberOfBedrooms;
    private int numberOfGarages;
    private int numberOfParkingSpaces;
    private String street;
    private double price;
    private String estateDescription;
    private String imgPath; // Путь к изображению

    // Конструктор
    public Estate(int id, String estateName, double floorSpace, double balconiesSpace, int numberOfBalconies,
                  int numberOfBedrooms, int numberOfGarages, int numberOfParkingSpaces, String street,
                  double price, String estateDescription, String imgPath) {
        this.id = id;
        this.estateName = estateName;
        this.floorSpace = floorSpace;
        this.balconiesSpace = balconiesSpace;
        this.numberOfBalconies = numberOfBalconies;
        this.numberOfBedrooms = numberOfBedrooms;
        this.numberOfGarages = numberOfGarages;
        this.numberOfParkingSpaces = numberOfParkingSpaces;
        this.street = street;
        this.price = price;
        this.estateDescription = estateDescription;
        this.imgPath = imgPath;
    }

    // Геттеры и сеттеры (можно сгенерировать их автоматически в вашей IDE)

    public int getId() {
        return id;
    }

    public String getEstateName() {
        return estateName;
    }

    public double getFloorSpace() {
        return floorSpace;
    }

    public double getBalconiesSpace() {
        return balconiesSpace;
    }

    public int getNumberOfBalconies() {
        return numberOfBalconies;
    }

    public int getNumberOfBedrooms() {
        return numberOfBedrooms;
    }

    public int getNumberOfGarages() {
        return numberOfGarages;
    }

    public int getNumberOfParkingSpaces() {
        return numberOfParkingSpaces;
    }

    public String getStreet() {
        return street;
    }

    public double getPrice() {
        return price;
    }

    public String getEstateDescription() {
        return estateDescription;
    }

    public String getImgPath() {
        return imgPath;
    }
}

