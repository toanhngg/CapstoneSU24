package fpt.CapstoneSU24.model;

import jakarta.persistence.*;

@Entity
@Table(name = "[report]")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private int reportId;
    @Column(name = "email", columnDefinition = "nvarchar(50)")
    private String code;
    @Column(name = "title", columnDefinition = "nvarchar(250)")
    private String tile;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "type")
    private int firstName;
    @Column(name = "createOn")
    private Long createOn;
    @Column(name = "updateOn")
    private String description;
    @Column(name = "address", columnDefinition = "nvarchar(255)")
    private int Status;
    @Column(name = "country", columnDefinition = "nvarchar(50)")
    private String country;
    @Column(name = "phone")
    private String phone;
    @Column(name = "date_of_birth")
    private long dateOfBirth;
    @Column(name = "supporting_documents", columnDefinition = "nvarchar(255)")
    private String supportingDocuments;
    @Column(name = "status", columnDefinition = "int")
    private int status;
}
