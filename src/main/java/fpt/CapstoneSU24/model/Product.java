package fpt.CapstoneSU24.model;


import jakarta.persistence.*;
import org.hibernate.Length;

@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private int productId;
    @Column(name = "product_name", columnDefinition = "nvarchar(255)")
    private String productName;
    //    người taọ ra sẳn phẩm này, người có role là manu
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @ManyToOne
    @JoinColumn(name = "origin_id")
    private Origin origin;
    @Column(name = "unit_price")
    private String unitPrice;
    @Column(name = "dimensions")
    private String dimensions;
    @Column(name = "material")
    private String material;
    @Column(name = "supporting_documents", columnDefinition = "nvarchar(255)")
    private String supportingDocuments;


}