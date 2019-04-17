package com.example.devday19;

public class NewModel {

    private String Details;
    private String Location;
    private String Time;
    private String type;
    private String email;
    public NewModel() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public NewModel(String details, String location, String time, String type,String email) {
        Details = details;
        Location = location;
        Time = time;
        this.type = type;
        this.email = email;
    }

    public String getDetails() {
        return Details;
    }

    public void setDetails(String details) {
        Details = details;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
