package fpt.CapstoneSU24.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "support_system")
public class SupportSystem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "support_system_id")
    private int supportSystemId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "title", columnDefinition = "nvarchar(255)")
    private String title;
    @Column(name = "content", columnDefinition = "nvarchar(255)")
    private String content;
    @Column(name = "status", columnDefinition = "int")
    private int status;
    @Column(name = "timestamp", columnDefinition = "bigint")
    private long timestamp;
    @Column(name = "support_timestamp", columnDefinition = "bigint")
    private long supportTimestamp;
    @Column(name = "supporter_name", columnDefinition = "nvarchar(255)")
    private String supporterName;
    @Column(name = "support_content", columnDefinition = "nvarchar(255)")
    private String supportContent;
    @Column(name = "reply_id", columnDefinition = "int")
    private int replyId;


}
