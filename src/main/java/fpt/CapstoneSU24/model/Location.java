package fpt.CapstoneSU24.model;

import jakarta.persistence.*;

@Entity
@Table(name = "location")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    private int locationId;
    @Column(name = "address", columnDefinition = "nvarchar(255)")
    private String address;
    @Column(name = "city", columnDefinition = "nvarchar(50)")
    private String city;
    @Column(name = "country", columnDefinition = "nvarchar(50)")
    private String country;
    @Column(name = "coordinateX", columnDefinition = "nvarchar(50)")
    private String coordinateX;
    @Column(name = "coordinateY", columnDefinition = "nvarchar(50)")
    private String coordinateY;
    //manhdt
    //them quan huyen phuong xa
    @Column(name = "district", columnDefinition = "nvarchar(100)")
    private String district;
    @Column(name = "street", columnDefinition = "nvarchar(100)")
    private String street;


//    @Column(name = "coordinates", columnDefinition = "nvarchar(50)")
//    private String coordinates;

    public Location() {

    }
    public Location(int locationId, String address, String city, String country, String coordinateX,String coordinateY, String district, String street) {
        this.locationId = locationId;
        this.address = address;
        this.city = city;
        this.country = country;
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
        this.district = district;
        this.street = street;
    }






//    public Location(int locationId, String address, String city, String country, String coordinates) {
//        this.locationId = locationId;
//        this.address = address;
//        this.city = city;
//        this.country = country;
//        this.coordinates = coordinates;
//    }

    public String getCoordinateX() {
        return coordinateX;
    }

    public void setCoordinateX(String coordinateX) {
        this.coordinateX = coordinateX;
    }

    public String getCoordinateY() {
        return coordinateY;
    }

    public void setCoordinateY(String coordinateY) {
        this.coordinateY = coordinateY;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    //    public String getCoordinates() {
//        return coordinates;
//    }
//
//    public void setCoordinates(String coordinates) {
//        this.coordinates = coordinates;
//    }
}
