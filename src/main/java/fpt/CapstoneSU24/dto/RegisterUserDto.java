package fpt.CapstoneSU24.dto;

import fpt.CapstoneSU24.model.Role;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

public class RegisterUserDto {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private int role;
    private String address;
    private String city;
    private String country;
    private String phone;
    private long dateOfBirth;

    public RegisterUserDto(String email, String password, String firstName, String lastName, int role, String address, String city, String country, String phone, long dateOfBirth) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.address = address;
        this.city = city;
        this.country = country;
        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public long getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(long dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}