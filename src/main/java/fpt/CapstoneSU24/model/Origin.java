package fpt.CapstoneSU24.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Origin") // Đặt tên bảng trong cơ sở dữ liệu
public class Origin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Origin")
    private int idOrigin;
    @ManyToOne
    @JoinColumn(name = "Id_Product")
    private Product Product;
    @ManyToOne
    @JoinColumn(name = "Id_User")
    private User User;
    @Column(name = "Updated_At")
    private String updatedAt;
    //    link URL đến cách giấy tờ liên quan
    @Column(name = "Supporting_Documents", columnDefinition = "nvarchar(255)")
    private String supportingDocuments;
    //    kế thừa, bán lại, tặng ....
    @Column(name = "Acquisition_Method", columnDefinition = "nvarchar(255)")
    private String acquisitionMethod;
    @Column(name = "Note", columnDefinition = "nvarchar(255)")
    private String note;
    //   2 là cao nhất, số càng lớn thì sẽ đưa ra cảnh bảo
    @Column(name = "Legit_Level")
    private int legitLevel;

    public Origin(){

    }

    public Origin(int idOrigin, fpt.CapstoneSU24.model.Product product, fpt.CapstoneSU24.model.User user, String updatedAt, String supportingDocuments, String acquisitionMethod, String note, int legitLevel) {
        this.idOrigin = idOrigin;
        Product = product;
        User = user;
        this.updatedAt = updatedAt;
        this.supportingDocuments = supportingDocuments;
        this.acquisitionMethod = acquisitionMethod;
        this.note = note;
        this.legitLevel = legitLevel;
    }

    public int getIdOrigin() {
        return idOrigin;
    }

    public void setIdOrigin(int idOrigin) {
        this.idOrigin = idOrigin;
    }

    public fpt.CapstoneSU24.model.Product getProduct() {
        return Product;
    }

    public void setProduct(fpt.CapstoneSU24.model.Product product) {
        Product = product;
    }

    public fpt.CapstoneSU24.model.User getUser() {
        return User;
    }

    public void setUser(fpt.CapstoneSU24.model.User user) {
        User = user;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getSupportingDocuments() {
        return supportingDocuments;
    }

    public void setSupportingDocuments(String supportingDocuments) {
        this.supportingDocuments = supportingDocuments;
    }

    public String getAcquisitionMethod() {
        return acquisitionMethod;
    }

    public void setAcquisitionMethod(String acquisitionMethod) {
        this.acquisitionMethod = acquisitionMethod;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getLegitLevel() {
        return legitLevel;
    }

    public void setLegitLevel(int legitLevel) {
        this.legitLevel = legitLevel;
    }
}