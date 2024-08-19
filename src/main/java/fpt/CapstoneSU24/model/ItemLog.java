package fpt.CapstoneSU24.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "item_log")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_log_id")
    private int itemLogId;
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
    @ManyToOne
    @JoinColumn(name = "authorized_id")
    private Authorized authorized;
    @ManyToOne
    @JoinColumn(name = "event_id", columnDefinition = "int default 0")
    private EventType event_id;
    @Column(name = "status")
    private int status;
    @OneToOne(mappedBy = "itemLog", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private ImageItemLog imageItemLog;
    @Column(name = "point", columnDefinition = "nvarchar(255)")
    private String point;
    @Column(name = "idEdit", columnDefinition = "int")
    private Integer  idEdit;
    public ItemLog(Item item, String address, Party party, Location location, long timeStamp, String description, Authorized authorized, EventType event_id, int status, ImageItemLog imageItemLog, String point,Integer  idEdit) {
        this.item = item;
        this.address = address;
        this.party = party;
        this.location = location;
        this.timeStamp = timeStamp;
        this.description = description;
        this.authorized = authorized;
        this.event_id = event_id;
        this.status = status;
        this.imageItemLog = imageItemLog;
        this.point = point;
        this.idEdit = idEdit;
    }
    public ItemLog(Item item, String address, Party party, Location location, long timeStamp, String description, Authorized authorized, EventType event_id, int status, ImageItemLog imageItemLog, String point) {
        this.item = item;
        this.address = address;
        this.party = party;
        this.location = location;
        this.timeStamp = timeStamp;
        this.description = description;
        this.authorized = authorized;
        this.event_id = event_id;
        this.status = status;
        this.imageItemLog = imageItemLog;
        this.point = point;
    }
}
