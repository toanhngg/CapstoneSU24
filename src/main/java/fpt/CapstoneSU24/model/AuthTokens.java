package fpt.CapstoneSU24.model;

import jakarta.persistence.*;

@Entity
@Table(name = "auth_tokens")
public class AuthTokens {
    @Id
    @Column(name = "auth_tokens_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int authTokenId;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userAuth;
    @Column(name = "jwt_hash")
    private String jwtHash;
public AuthTokens(){}
    public AuthTokens(int authTokenId, User userAuth, String jwtHash) {
        this.authTokenId = authTokenId;
        this.userAuth = userAuth;
        this.jwtHash = jwtHash;
    }

    public int getAuthTokenId() {
        return authTokenId;
    }

    public void setAuthTokenId(int authTokenId) {
        this.authTokenId = authTokenId;
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
