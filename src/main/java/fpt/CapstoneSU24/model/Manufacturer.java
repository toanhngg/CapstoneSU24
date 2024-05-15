package fpt.CapstoneSU24.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Manufacturer")
public class Manufacturer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Manufacturer")
    private int id_Manufacturer;

    @Column(name = "Name_Manufacturer")
    private String name_Manufacturer;

    @Column(name = "Description")
    private String description;

    @Column(name = "Address")
    private String address;

    @Column(name = "Country")
    private String country;

    @Column(name = "Phone")
    private String phone;

    @Column(name = "Email")
    private String email;

    @Column(name = "Supporting_Documents")
    private String supporting_Documents;

    public Manufacturer() {

    }
    public Manufacturer(int id_Manufacturer, String name_Manufacturer, String description, String address, String country, String phone, String email, String supporting_Documents) {
        this.id_Manufacturer = id_Manufacturer;
        this.name_Manufacturer = name_Manufacturer;
        this.description = description;
        this.address = address;
        this.country = country;
        this.phone = phone;
        this.email = email;
        this.supporting_Documents = supporting_Documents;
    }

    public int getId_Manufacturer() {
        return id_Manufacturer;
    }

    public void setId_Manufacturer(int id_Manufacturer) {
        this.id_Manufacturer = id_Manufacturer;
    }

    public String getName_Manufacturer() {
        return name_Manufacturer;
    }

    public void setName_Manufacturer(String name_Manufacturer) {
        this.name_Manufacturer = name_Manufacturer;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSupporting_Documents() {
        return supporting_Documents;
    }

    public void setSupporting_Documents(String supporting_Documents) {
        this.supporting_Documents = supporting_Documents;
    }
}
