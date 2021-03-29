package com.svceindore.userservice.model;

public class Student extends User {
    @Override
    public String toString() {
        return "Student{" +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
