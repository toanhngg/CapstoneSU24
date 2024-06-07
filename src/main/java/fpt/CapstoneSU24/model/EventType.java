package fpt.CapstoneSU24.model;

import jakarta.persistence.*;

@Entity
@Table(name = "event_type")
public class EventType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private int event_id;
    @Column(name = "event_type")
    private String event_type;

    public EventType(int event_id, String event_type) {
        this.event_id = event_id;
        this.event_type = event_type;
    }

    public EventType() {
    }

    public int getEvent_id() {
        return event_id;
    }

    public void setEvent_id(int event_id) {
        this.event_id = event_id;
    }

    public String getEvent_type() {
        return event_type;
    }

    public void setEvent_type(String event_type) {
        this.event_type = event_type;
    }
}
