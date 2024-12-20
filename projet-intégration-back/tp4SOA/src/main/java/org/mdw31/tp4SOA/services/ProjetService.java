package org.mdw31.tp4SOA.services;


import org.mdw31.tp4SOA.entitys.projet;
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
@RequestMapping("/projet") // Le chemin de base pour ce contrôleur
public class ProjetService {

    @Autowired
    private ProjetRepository repo;

    // Gérer les requêtes OPTIONS pour CORS (optionnel en Spring, mais je l'ai conservé pour le besoin de CORS)
    @RequestMapping(value = "/{any:.*}", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> handlePreflight() {
        return ResponseEntity.ok()
                .header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:8081")
                .header(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "GET, POST, PUT, DELETE, OPTIONS")
                .header(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "Content-Type, Authorization")
                .header(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true")
                .build();
    }

    // Création d'un projet
    @PostMapping("/create")
    public ResponseEntity<String> createProjet(@RequestBody projet projet) {
        repo.save(projet);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Projet créé avec succès.");
    }

    // Récupérer tous les projets
    @GetMapping("/all")
    public ResponseEntity<List<projet>> getAllProjets() {
        List<projet> projets = repo.findAll();
        return ResponseEntity.ok(projets);
    }
    @GetMapping("/count")
    public ResponseEntity<Long> getProjectCount() {
        long count = repo.count(); // Utilise count() pour récupérer le nombre total
        return ResponseEntity.ok(count);
    }

    // Supprimer un projet
    @DeleteMapping("/delete/{projetId}")
    public ResponseEntity<String> deleteProjet(@PathVariable("projetId") Long projetId) {
        Optional<projet> projet = repo.findById(projetId);
        if (projet.isPresent()) {
            repo.deleteById(projetId);
            return ResponseEntity.ok("Projet supprimé avec succès.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Projet non trouvé.");
        }
    }

    // Mettre à jour un projet
    @PutMapping("/update/{projetId}")
    public ResponseEntity<projet> updateProjet(@PathVariable("projetId") Long projetId, @RequestBody projet projet) {
        Optional<projet> existingProjetOpt = repo.findById(projetId);

        if (existingProjetOpt.isPresent()) {
            projet existingProjet = existingProjetOpt.get();

            // Mise à jour des champs du projet
            existingProjet.setNom(projet.getNom());
            existingProjet.setDescription(projet.getDescription());

            // Sauvegarde du projet mis à jour
            repo.save(existingProjet);

            return ResponseEntity.ok(existingProjet);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null); // Ou une erreur personnalisée si vous préférez
        }
    }
}
