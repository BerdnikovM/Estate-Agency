package com.example.estate_agency;

public class UserProfile {
    private static UserProfile currentUserProfile;
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private boolean isEmployee;
    // геттеры и сеттеры для каждого поля данных пользователя
    public boolean isEmployee() {
        return isEmployee;
    }

    public void setEmployee(boolean isEmployee) {
        this.isEmployee = isEmployee;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public static void setCurrentUserProfile(UserProfile userProfile) {
        currentUserProfile = userProfile;
    }

    public static UserProfile getCurrentUserProfile() {
        return currentUserProfile;
    }
}
