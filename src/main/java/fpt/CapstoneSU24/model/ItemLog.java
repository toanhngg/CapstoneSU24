package fpt.CapstoneSU24.model;

import jakarta.persistence.*;
@Entity
@Table(name = "item_log")
public class ItemLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_log_id")
    private int productLogId;
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;
    @Column(name = "address", columnDefinition = "nvarchar(255)")
    private String address;
    @ManyToOne
    @JoinColumn(name = "party_id")
    private Party party;
    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;
    @Column(name = "timestamp")
    private long timeStamp;
    @Column(name = "description", columnDefinition = "nvarchar(255)")
    private String description;
    @Column(name = "event_type", columnDefinition = "nvarchar(50)")
    private String eventType;
    @Column(name = "status")
    private int status;

    public ItemLog(int productLogId, Item item, String address, Party party, Location location, long timeStamp, String description, String eventType, int status) {
        this.productLogId = productLogId;
        this.item = item;
        this.address = address;
        this.party = party;
        this.location = location;
        this.timeStamp = timeStamp;
        this.description = description;
        this.eventType = eventType;
        this.status = status;
    }

    public int getProductLogId() {
        return productLogId;
    }

    public void setProductLogId(int productLogId) {
        this.productLogId = productLogId;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Party getParty() {
        return party;
    }

    public void setParty(Party party) {
        this.party = party;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
