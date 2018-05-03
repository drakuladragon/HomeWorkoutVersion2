package com.example.nhtha.homeworkoutversion2.dto;

/**
 * Created by nhtha on 03-Feb-18.
 */

public class UserDto {
    private String name;
    private String avatar;
    private int workout;
    private int kcal;

    public UserDto() {
    }

    public int getWorkout() {
        return workout;
    }

    public void setWorkout(int workout) {
        this.workout = workout;
    }

    public int getKcal() {
        return kcal;
    }

    public void setKcal(int kcal) {
        this.kcal = kcal;
    }

    public UserDto(String name, String avatar) {
        this.name = name;
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
