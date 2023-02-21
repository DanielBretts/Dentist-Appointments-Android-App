package com.example.patientqueuemanagement.Model;
/**
 * The Appointment class holds the attributes for a single appointment about the patient
 */
public class Appointment {
    private String time;
    private String name;
    private String age;
    private String address;
    private String phoneNumber;
    private String otherDoctor;
    private String notes;
    private String dayName;
    private int day;
    private int month;
    private int year;
    private String uid;

    public Appointment(){}

    public Appointment(String uid, String time, String name, String age, String address, String phoneNumber, String otherDoctor, String notes, int day, int month, int year,String dayName) {
        this.uid = uid;
        this.time = time;
        this.name = name;
        this.age = age;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.otherDoctor = otherDoctor;
        this.notes = notes;
        this.day = day;
        this.month = month;
        this.year = year;
        this.dayName = dayName;
    }

    public String prettyStringDate(){
        return String.format("%d.%d.%d",day,month,year);
    }

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getOtherDoctor() {
        return otherDoctor;
    }

    public void setOtherDoctor(String otherDoctor) {
        this.otherDoctor = otherDoctor;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "time='" + time + '\'' +
                ", name='" + name + '\'' +
                ", age='" + age + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", otherDoctor='" + otherDoctor + '\'' +
                ", notes='" + notes + '\'' +
                ", day=" + day +
                ", month=" + month +
                ", year=" + year +
                ", uid='" + uid + '\'' +
                '}';
    }
}
