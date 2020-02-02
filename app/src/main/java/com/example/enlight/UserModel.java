package com.example.enlight;

public class UserModel {

    private String name, email, userkey;

    public UserModel(){}

    public UserModel(String name, String email, String userkey) {
        this.name = name;
        this.email = email;
        this.userkey = userkey;
    }

    //Getter, Setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserkey() {
        return userkey;
    }

    public void setUserkey(String userkey) {
        this.userkey = userkey;
    }
}
