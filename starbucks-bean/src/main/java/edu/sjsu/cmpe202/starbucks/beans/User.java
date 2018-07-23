package edu.sjsu.cmpe202.starbucks.beans;

public class User {
    private String firstName;
    private String lastName;
    private String profile;

    public User(String firstName, String lastName, String profile) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.profile = profile;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
