package com.landlords.entity;

public class Task {
    private int id;
    private String username;
    private int current_progress;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getCurrent_progress() {
        return current_progress;
    }

    public String getUsername() {
        return username;
    }

    public void setCurrent_progress(int current_progress) {
        this.current_progress = current_progress;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
