package fpt.CapstoneSU24.model;

import jakarta.persistence.*;

@Entity
@Table(name = "category")
public class Category {

    @Id
    @Column(name = "category_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int categoryId;
    @Column(name = "category_name", columnDefinition = "nvarchar(50)")
    private String name;
    @Column(name = "description", columnDefinition = "nvarchar(255)")
    private String description;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Category() {

    }

    public Category(int categoryId, String name, String description, User user) {
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.user = user;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}