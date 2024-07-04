package fpt.CapstoneSU24.dto;

import fpt.CapstoneSU24.model.Location;

public class UserViewDetailDTO {
    private String email;
    private String firstName;
    private  String lastName;
    private  String description;
    private  String phone;
    private Location location;
    private String profileImage;
    private String org_name;

    public UserViewDetailDTO(String email, String firstName, String lastName, String description, String phone, Location location, String profileImage, String org_name) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.description = description;
        this.phone = phone;
        this.location = location;
        this.profileImage = profileImage;
        this.org_name = org_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getOrg_name() {
        return org_name;
    }

    public void setOrg_name(String org_name) {
        this.org_name = org_name;
    }
}
