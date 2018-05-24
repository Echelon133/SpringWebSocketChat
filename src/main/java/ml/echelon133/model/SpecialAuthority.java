package ml.echelon133.model;

import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;

@RedisHash("SpecialAuthority")
public class SpecialAuthority implements Serializable {

    private String id;

    @Indexed
    private String username;

    private String authority;

    public SpecialAuthority() {}
    public SpecialAuthority(String username) {
        this.username = username;
        this.authority = "ROLE_ADMIN";
    }
    public SpecialAuthority(String username, String authority) {
        this.username = username;
        this.authority = authority;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public String toString() {
        return authority;
    }
}
