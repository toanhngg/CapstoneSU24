package fpt.CapstoneSU24.dto;

import fpt.CapstoneSU24.model.Location;

public class UserViewDTO {
    private int userId;
    private String description;
    private String profileImage;
    private String org_name;

    public UserViewDTO(int userId, String description, String profileImage, String org_name) {
        this.userId = userId;
        this.description = description;
        this.profileImage = profileImage;
        this.org_name = org_name;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
