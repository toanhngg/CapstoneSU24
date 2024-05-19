package fpt.CapstoneSU24.model;

import jakarta.persistence.*;

@Entity
@Table(name = "product_log")
public class ProductLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_log_id")
    private int productLogId;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    @Column(name = "address", columnDefinition = "nvarchar(255)")
    private String address;
    @ManyToOne
    @JoinColumn(name = "actor_Id")
    private Actor actor;
    @Column(name = "timestamp")
    private long timeStamp;
    @Column(name = "description", columnDefinition = "nvarchar(255)")
    private String description;
    @Column(name = "event_Type", columnDefinition = "nvarchar(50)")
    private String eventType;
    @Column(name = "status")
    private int status;
    public ProductLog(){

    }

    public ProductLog(int productLogId, Product product, String address, Actor actor, long timeStamp, String description, String eventType, int status) {
        this.productLogId = productLogId;
        this.product = product;
        this.address = address;
        this.actor = actor;
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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Actor getActor() {
        return actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
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
