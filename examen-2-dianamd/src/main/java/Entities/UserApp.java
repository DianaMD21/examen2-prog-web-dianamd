package Entities;

import Enums.RolesApp;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="users")
public class UserApp {
    @Id
    @GeneratedValue(
            strategy= GenerationType.AUTO,
            generator="native"
    )
    @GenericGenerator(
            name = "native",
            strategy = "native"
    )
    @Column(nullable = false,updatable = false)
    private long id;
    @Column(unique=true)
    private String username;
    private String password;
    @ElementCollection(targetClass = RolesApp.class,fetch = FetchType.EAGER)
    @JoinTable(name = "roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    Set<RolesApp> roles;
    private boolean isDeleted;

    public UserApp() {
        this.roles=new HashSet<>();
    }

    public UserApp(String username, String password) {
        this.username = username;
        this.password = password;
        this.roles=new HashSet<>();
    }
    public UserApp(String username, String password, Set<RolesApp> roles) {
        this.username = username;
        this.password = password;
        this.roles=roles;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public Set<RolesApp> getRoles() {
        return roles;
    }

    public void setRoles(Set<RolesApp> roles) {
        this.roles = roles;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
