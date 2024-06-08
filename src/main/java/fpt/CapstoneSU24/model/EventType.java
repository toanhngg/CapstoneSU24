package fpt.CapstoneSU24.model;

import jakarta.persistence.*;

@Entity
@Table(name = "event_type")
public class EventType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private int eventId;
    @Column(name = "event_type")
    private String event_type;

    public EventType(int eventId, String event_type) {
        this.eventId = eventId;
        this.event_type = event_type;
    }

    public EventType() {
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getEvent_type() {
        return event_type;
    }

    public void setEvent_type(String event_type) {
        this.event_type = event_type;
    }
}
