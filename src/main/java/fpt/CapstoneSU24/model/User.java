package fpt.CapstoneSU24.model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "[User]")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_User")
    private int userId;
    @Column(name = "Email")
    private String email;
    @Column(name = "Password")
    private String password;
    @ManyToOne
    @JoinColumn(name = "Id_Role")
    private Role role;
    @Column(name = "First_Name", columnDefinition = "nvarchar(50)")
    private String firstName;
    @Column(name = "Last_Name", columnDefinition = "nvarchar(50)")
    private String lastName;
    @Column(name = "Description", columnDefinition = "nvarchar(255)")
    private String description;
    @Column(name = "Address", columnDefinition = "nvarchar(255)")
    private String address;
    @Column(name = "Country", columnDefinition = "nvarchar(50)")
    private String country;
    @Column(name = "Phone")
    private String phone;
    @Column(name = "Age")
    private int age;
    @Column(name = "Supporting_Documents", columnDefinition = "nvarchar(255)")
    private String supportingDocuments;

    public User(){

    }

    public User(int userId, String email, String password, Role role, String firstName, String lastName, String description, String address, String country, String phone, int age, String supportingDocuments) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
        this.description = description;
        this.address = address;
        this.country = country;
        this.phone = phone;
        this.age = age;
        this.supportingDocuments = supportingDocuments;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSupportingDocuments() {
        return supportingDocuments;
    }

    public void setSupportingDocuments(String supportingDocuments) {
        this.supportingDocuments = supportingDocuments;
    }
}
