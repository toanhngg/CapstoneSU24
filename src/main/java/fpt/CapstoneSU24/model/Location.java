package fpt.CapstoneSU24.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "location")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
    @Column(name = "coordinateX")
    private Double coordinateX;
    @Column(name = "coordinateY")
    private Double coordinateY;
    //manhdt
    //them quan huyen phuong xa
    @Column(name = "district", columnDefinition = "nvarchar(100)")
    private String district;
    @Column(name = "ward", columnDefinition = "nvarchar(100)")
    private String ward;


}
