package fpt.CapstoneSU24.model;

import jakarta.persistence.*;

@Entity
@Table(name = "image_product") // Đặt tên bảng trong cơ sở dữ liệu
public class ImageProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_product_id")
    private int imageProductId;

    @Column(name = "file_path")
    private String filePath;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "type")
    private int type = 0;

    public ImageProduct(){

    }

    public ImageProduct(int imageProductId, String filePath, Product product) {
        this.imageProductId = imageProductId;
        this.filePath = filePath;
        this.product = product;
    }

    public ImageProduct(int imageProductId, String filePath, Product product, int type) {
        this.imageProductId = imageProductId;
        this.filePath = filePath;
        this.product = product;
        this.type = type;
    }

    public int getImageProductId() {
        return imageProductId;
    }

    public void setImageProductId(int imageProductId) {
        this.imageProductId = imageProductId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}

