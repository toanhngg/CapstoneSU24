package fpt.CapstoneSU24.model;

import jakarta.persistence.*;

@Entity
@Table(name = "[Report_Comment]")
public class ReportComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private long comment_id;
    @Column(name = "createOn")
    private long createOn;
    @Column(name = "updateOn")
    private long updateOn;

    @ManyToOne
    @JoinColumn(name = "report_id")
    private Report report;
}
