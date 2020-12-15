package com.company;

public class UserData {
    int id;
    String name;
    String email = "";
    String street = "";
    String mobile_number;

    public UserData(int id, String name, String mobile_number) {
        this.id = id;
        this.name = name;
        this.mobile_number = mobile_number;
    }
}
