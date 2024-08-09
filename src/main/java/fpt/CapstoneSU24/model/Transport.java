package fpt.CapstoneSU24.model;

import jakarta.persistence.*;

import java.util.Date;
@Entity
@Table(name = "transport")
public class Transport {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "transport_id")
        private int transportId;
        @Column(name = "transport_name", columnDefinition = "nvarchar(100)")
        private String transportName;
    @Column(name = "short_name", columnDefinition = "nvarchar(100)")
    private String shortName;
        @Column(name = "transport_email", columnDefinition = "nvarchar(100)")
        private String transportEmail;
        @Column(name = "transport_contact", columnDefinition = "nvarchar(20)")
        private String transportContact;

    public Transport(int transportId, String transportName, String shortName, String transportEmail, String transportContact) {
        this.transportId = transportId;
        this.transportName = transportName;
        this.shortName = shortName;
        this.transportEmail = transportEmail;
        this.transportContact = transportContact;
    }

    public Transport() {
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public int getTransportId() {
        return transportId;
    }

    public void setTransportId(int transportId) {
        this.transportId = transportId;
    }

    public String getTransportName() {
        return transportName;
    }

    public void setTransportName(String transportName) {
        this.transportName = transportName;
    }

    public String getTransportEmail() {
        return transportEmail;
    }

    public void setTransportEmail(String transportEmail) {
        this.transportEmail = transportEmail;
    }

    public String getTransportContact() {
        return transportContact;
    }

    public void setTransportContact(String transportContact) {
        this.transportContact = transportContact;
    }
}
