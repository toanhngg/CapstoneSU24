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
        @Column(name = "transport_email", columnDefinition = "nvarchar(100)")
        private String transportEmail;
        @Column(name = "transport_contact", columnDefinition = "nvarchar(20)")
        private String transportContact;

    public Transport(int transportId, String transportName, String transportEmail, String transportContact) {
        this.transportId = transportId;
        this.transportName = transportName;
        this.transportEmail = transportEmail;
        this.transportContact = transportContact;
    }

    public Transport() {
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
