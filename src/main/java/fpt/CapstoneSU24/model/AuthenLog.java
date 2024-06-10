package fpt.CapstoneSU24.model;

import jakarta.persistence.*;

@Entity
@Table(name = "[Authen_log]")
public class AuthenLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "authen_log_id")
    private int authen_log_id;
    @ManyToOne
    @JoinColumn(name = "user_id", columnDefinition = "int default 0")
    private User userId;
    @Column(name = "authen_log", columnDefinition = "nvarchar(max)")
    private String authenLog;
    @Column(name = "time", columnDefinition = "nvarchar(50)")
    private String time;

}
