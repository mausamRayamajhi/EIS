package com.example.mausam.eis;

import java.io.Serializable;

public class Student implements Serializable {
    private String address;
    private int current_semester;
    private String date_of_birth;
    private String email;
    private String first_name;
    private int gender;
    private String image;
    private String last_name;
    private String middle_name;
    private String password;
    private String phone;
    private int s_id;
    private int status;
    private String username;
    private String verificationCode;

    public int getS_id() {
        return this.s_id;
    }

    public void setS_id(int s_id) {
        this.s_id = s_id;
    }

    public String getFirst_name() {
        return this.first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getMiddle_name() {
        return this.middle_name;
    }

    public void setMiddle_name(String middle_name) {
        this.middle_name = middle_name;
    }

    public String getLast_name() {
        return this.last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getGender() {
        return this.gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getDate_of_birth() {
        return this.date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getCurrent_semester() {
        return this.current_semester;
    }

    public void setCurrent_semester(int current_semester) {
        this.current_semester = current_semester;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getVerificationCode() {
        return this.verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String toString() {
        return "Student{s_id=" + this.s_id + ", first_name='" + this.first_name + '\'' + ", middle_name='" + this.middle_name + '\'' + ", last_name='" + this.last_name + '\'' + ", username='" + this.username + '\'' + ", password='" + this.password + '\'' + ", email='" + this.email + '\'' + ", gender=" + this.gender + ", date_of_birth='" + this.date_of_birth + '\'' + ", phone='" + this.phone + '\'' + ", address='" + this.address + '\'' + ", image='" + this.image + '\'' + ", current_semester=" + this.current_semester + ", status=" + this.status + ", verificationCode='" + this.verificationCode + '\'' + '}';
    }
}
