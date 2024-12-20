package org.mdw31.tp4SOA.entitys;

import jakarta.persistence.*;

@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contenu;
    private String auteur;

    // Chat auquel le message appartient


    public Message() {

    }

    // Timestamps ou toute autre information nécessaire
    private Long dateEnvoi;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }



    public Long getDateEnvoi() {
        return dateEnvoi;
    }

    public void setDateEnvoi(Long dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }
}
