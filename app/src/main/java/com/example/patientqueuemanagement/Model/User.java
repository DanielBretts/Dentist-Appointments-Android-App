package com.example.patientqueuemanagement.Model;

import java.util.ArrayList;
/**
 * The User class holds all the attributes from firebase database and firebase authentication
 */
public class User {
    private String name = "";
    private String email = "";
    private String phone = "";
    private ArrayList<Appointment> appointments = new ArrayList<>();

    public User(){
    }

    public User(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.appointments = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "User{" +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", appointments=" + appointments +
                '}';
    }

    public ArrayList<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(ArrayList<Appointment> appointments) {
        this.appointments = appointments;
    }

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
