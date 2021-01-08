package com.svceindore.userservice.model;

public class Student extends User {
    private String course;
    private String branch;

    @Override
    public String toString() {
        return "Student{" +
                "course='" + course + '\'' +
                ", branch='" + branch + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
