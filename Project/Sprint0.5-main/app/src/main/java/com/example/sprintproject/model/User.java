package com.example.sprintproject.model;

public class User {
    private String username;
    private String userId;
    private String password;

    public User() {
    }

    public User(String username, String password, String userId) {
        this.username = username;
        this.userId = userId;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}

