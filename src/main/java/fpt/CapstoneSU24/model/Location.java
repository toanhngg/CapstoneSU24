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
    @Column(name = "coordinates", columnDefinition = "nvarchar(50)")
    private String coordinates;

    public Location() {
    }

    public Location(int locationId, String address, String city, String country, String coordinates) {
        this.locationId = locationId;
        this.address = address;
        this.city = city;
        this.country = country;
        this.coordinates = coordinates;
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

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }
}
