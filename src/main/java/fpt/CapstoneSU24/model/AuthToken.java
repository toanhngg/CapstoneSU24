package fpt.CapstoneSU24.model;

import jakarta.persistence.*;

@Entity
@Table(name = "auth_tokens")
public class AuthToken {
    @Id
    @Column(name = "user_id")
    private int id;
    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User userAuth;
    @Column(name = "jwt_hash")
    private String jwtHash;
    public AuthToken(){}

    public AuthToken(int id, User userAuth, String jwtHash) {
        this.id = id;
        this.userAuth = userAuth;
        this.jwtHash = jwtHash;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUserAuth() {
        return userAuth;
    }

    public void setUserAuth(User userAuth) {
        this.userAuth = userAuth;
    }

    public String getJwtHash() {
        return jwtHash;
    }

    public void setJwtHash(String jwtHash) {
        this.jwtHash = jwtHash;
    }
}
