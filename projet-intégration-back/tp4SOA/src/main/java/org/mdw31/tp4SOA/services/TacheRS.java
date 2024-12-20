package org.mdw31.tp4SOA.services;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.mdw31.tp4SOA.entitys.Tache;
import org.mdw31.tp4SOA.entitys.projet;
import org.mdw31.tp4SOA.entitys.user;
import org.mdw31.tp4SOA.repositories.TacheRepository;
import org.mdw31.tp4SOA.repositories.UserRepository;
import org.mdw31.tp4SOA.repositories.ProjetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:8081") // Permet les requêtes CORS globales
@RequestMapping("/Tache")
public class TacheRS {

    @Autowired
    private TacheRepository tacheRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ProjetRepository projetRepo;

    // Gérer les requêtes préalables CORS (OPTIONS)
    @OPTIONS
    @Path("{any:.*}")
    public Response handlePreflight() {
        return Response.ok()
                .header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:8081")
                .header(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "GET, POST, PUT, DELETE, OPTIONS")
                .header(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "Content-Type, Authorization")
                .header(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true")
                .build();
    }

    @PostMapping("/create")
    public ResponseEntity<String> createTache(@RequestBody Tache tache) {

        tacheRepo.save(tache);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Projet créé avec succès.");
    }


    // Obtenir toutes les tâches
    @GetMapping("/all")
    public ResponseEntity<List<Tache>> getAllProjets() {
        List<Tache> teches = tacheRepo.findAll();
        return ResponseEntity.ok(teches);
    }

    // Supprimer une tâche par son ID

    @DeleteMapping("/delete/{tacheId}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteTache(@PathVariable("tacheId") Long tacheId) {
        Optional<Tache> tache = tacheRepo.findById(tacheId);
        if (tache.isPresent()) {
            tacheRepo.deleteById(tacheId);
            return Response.ok("Tache deleted successfully")
                    .header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:8081")
                    .build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Tache not found")
                    .header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:8081")
                    .build();
        }
    }


}
