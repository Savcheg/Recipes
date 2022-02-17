package recipes.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.micrometer.core.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "USERNAME")
    @JsonProperty("email")
    @Pattern(regexp = ".+@.+\\..+")
    private String username;

    @Column(name = "PASSWORD", nullable = false)
    @Size(min = 8)
    @NotBlank
    private String password;

    @Column(name = "ROLE")
    @Nullable
    private String role;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User() {
        this.role = "ROLE_USER";
    }

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        if (!getUsername().equals(user.getUsername())) return false;
        return getRole().equals(user.getRole());
    }

    @Override
    public int hashCode() {
        int result = getUsername().hashCode();
        result = 31 * result + getRole().hashCode();
        return result;
    }
}
