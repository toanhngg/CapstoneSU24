package fpt.CapstoneSU24.model;

import java.util.Date;

public class B03_GetDataGridDTO {

    private int userId;
    private Date createOn;
    private String username;
    private String name;
    private String email;
    private int roleId;
    private String roleName;
    private String description;
    private String address;
    private String country;
    private String phone;
    private Date dateOfBirth;
    private Integer status;


    public B03_GetDataGridDTO() {
    }

    public B03_GetDataGridDTO(Integer status, Date dateOfBirth, String phone, String country, String address, String description, String roleName, int roleId, String email, String name, String username, Date createOn, int userId) {
        this.status = status;
        this.dateOfBirth = dateOfBirth;
        this.phone = phone;
        this.country = country;
        this.address = address;
        this.description = description;
        this.roleName = roleName;
        this.roleId = roleId;
        this.email = email;
        this.name = name;
        this.username = username;
        this.createOn = createOn;
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getCreateOn() {
        return createOn;
    }

    public void setCreateOn(Date createOn) {
        this.createOn = createOn;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
