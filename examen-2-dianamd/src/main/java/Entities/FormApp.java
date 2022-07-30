package Entities;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class FormApp {
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
    private String name;
    private String Sector;
    private String educationLevel;
    @ManyToOne()
    private UserApp userForm;
    private String latitud;
    private String longitud;
    private Boolean isDeleted=Boolean.FALSE;

    public FormApp() {
    }

    public FormApp(String name, String sector, String educationLevel, UserApp userForm) {
        this.name = name;
        Sector = sector;
        this.educationLevel = educationLevel;
        this.userForm = userForm;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSector() {
        return Sector;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public void setSector(String sector) {
        Sector = sector;
    }

    public String getEducationLevel() {
        return educationLevel;
    }

    public void setEducationLevel(String educationLevel) {
        this.educationLevel = educationLevel;
    }

    public UserApp getUserForm() {
        return userForm;
    }

    public void setUserForm(UserApp userForm) {
        this.userForm = userForm;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
