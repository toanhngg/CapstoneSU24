package fpt.CapstoneSU24.model;

import jakarta.persistence.*;

@Entity
@Table(name = "image_product") // Đặt tên bảng trong cơ sở dữ liệu
public class ImageProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_product_id")
    private int imageProductId;

    @Column(name = "image_name")
    private String imageName;

    @Column(name = "image", columnDefinition = "varbinary(MAX)")
    private byte[] image;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    public ImageProduct(){

    }

    public ImageProduct(int imageProductId, String imageName, byte[] image, Product product) {
        this.imageProductId = imageProductId;
        this.imageName = imageName;
        this.image = image;
        this.product = product;
    }

    public int getImageProductId() {
        return imageProductId;
    }

    public void setImageProductId(int imageProductId) {
        this.imageProductId = imageProductId;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}

