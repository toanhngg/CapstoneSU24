package fpt.CapstoneSU24.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Category")
public class Category {

    @Id
    @Column(name = "Id_Category")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int categoryId;
    @Column(name = "Name_Category", columnDefinition = "nvarchar(50)")
    private String name;
    @Column(name = "Description", columnDefinition = "nvarchar(255)")
    private String description;

    public Category() {

    }

    public Category(int categoryId, String name, String description) {
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}