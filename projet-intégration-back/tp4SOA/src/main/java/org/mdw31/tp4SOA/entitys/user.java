package org.mdw31.tp4SOA.entitys;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class user {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String telephone;
    private String password;
    @Column(name = "photo_path")
    private String photoPath;
    public user() {
    }

    // Autres constructeurs si nécessaire
    public user(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public user(String username, String telephone, String password, String photoPath) {
        this.username = username;
        this.telephone = telephone;
        this.password = password;
        this.photoPath = photoPath;
    }





    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }




    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

}
