package fpt.CapstoneSU24.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Item") // Đặt tên bảng trong cơ sở dữ liệu
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String info;
    public Item(){
    }

    public Item(Integer id, String info) {
        this.id = id;
        this.info = info;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
