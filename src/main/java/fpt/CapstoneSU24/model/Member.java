package fpt.CapstoneSU24.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Nationalized;

@Entity
@Table(name = "member") // Đặt tên bảng trong cơ sở dữ liệu
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String fullName;

    private Integer age;

    public Member(){

    }

    public Member(Integer id, String fullName, Integer age) {
        this.id = id;
        this.fullName = fullName;
        this.age = age;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    // Constructors, getters, and setters
}