package com.example.sprintproject.model;

public class User {
    private String username;
    private String userId;
    private String password;

    /**
     * Default constructor required for deserialization or general usage.
     */
    public User() {
    }

    /**
     * Constructs a {@code User} with the specified username, password, and user ID.
     *
     * @param username the username of the user
     * @param password the password of the user
     * @param userId   the unique identifier for the user
     */
    public User(String username, String password, String userId) {
        this.username = username;
        this.userId = userId;
        this.password = password;
    }

    /**
     * Constructs a {@code User} with the specified username and password.
     * The user ID is not initialized with this constructor.
     *
     * @param username the username of the user
     * @param password the password of the user
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Returns the username of the user.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the user.
     *
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the password of the user.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the user.
     *
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns the unique identifier of the user.
     *
     * @return the user ID
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Sets the unique identifier of the user.
     *
     * @param userId the user ID to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

}

