package com.elzobaba.fcisizitask;

/**
 * Created by ahmed on 3/19/2017.
 */

public class UserModel {
    public static String KEY;
    private String name;
    private String email;
    private String year;

    public UserModel(String name, String email, String year) {
        this.name = name;
        this.email = email;
        this.year = year;
    }

    public static String getKEY() {
        return KEY;
    }

    public static void setKEY(String KEY) {
        UserModel.KEY = KEY;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
