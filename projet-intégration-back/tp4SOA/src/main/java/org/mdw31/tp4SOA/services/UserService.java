package org.mdw31.tp4SOA.services;

import org.mdw31.tp4SOA.entitys.user;

import org.mdw31.tp4SOA.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:8081") // Autoriser CORS
@RequestMapping("/User")
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;  // Injecter le PasswordEncoder

    // Gérer les requêtes OPTIONS pour CORS (exemple pour un endpoint générique)
    @RequestMapping(value = "/{any:.*}", method = RequestMethod.OPTIONS)
    public ResponseEntity<?> handlePreflight() {
        return ResponseEntity.ok()
                .header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:8081")
                .header(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "GET, POST, PUT, DELETE, OPTIONS")
                .header(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "Content-Type, Authorization")
                .header(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true")
                .build();
    }

    // Méthode pour créer un nouvel utilisateur

    @PostMapping("/add")
    public ResponseEntity<String> createUser(
            @RequestParam("username") String username,
            @RequestParam("telephone") String telephone,
            @RequestParam("password") String password,
            @RequestParam(value = "photo", required = false) String photo) {

        // Encodage du mot de passe
        String encodedPassword = passwordEncoder.encode(password);

        // Créer l'utilisateur avec les informations reçues
        user newUser = new user(username, telephone, encodedPassword,photo);

        // Sauvegarde de l'utilisateur
        userRepository.save(newUser);

        // Gestion du fichier photo s'il est présent
        if (photo != null && !photo.isEmpty()) {
            // Vous pouvez ajouter une logique pour stocker la photo, si nécessaire.
            // Par exemple : fileStorageService.store(photo);
        }

        // Retour d'une réponse après ajout de l'utilisateur
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Utilisateur ajouté avec succès.");
    }

    // Récupérer tous les utilisateurs
    @GetMapping("/all")
    public ResponseEntity<List<user>> getAllUsers() {
        List<user> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    // Supprimer un utilisateur
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable("userId") Long userId) {
        Optional<user> user = userRepository.findById(userId);
        if (user.isPresent()) {
            userRepository.deleteById(userId);
            return ResponseEntity.ok("Utilisateur supprimé avec succès.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Utilisateur non trouvé.");
        }
    }

    // Méthode pour l'authentification des utilisateurs (login)
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody user loginUser) {
        Optional<user> existingUser = userRepository.findByUsername(loginUser.getUsername());

        if (existingUser.isPresent()) {
            user user = existingUser.get();
            if (passwordEncoder.matches(loginUser.getPassword(), user.getPassword())) {
                // Retourner une réponse JSON incluant ID et nom d'utilisateur
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Login réussi. Bienvenue " + user.getUsername());
                response.put("userId", user.getId());
                response.put("username", user.getUsername());

                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Collections.singletonMap("error", "Mot de passe incorrect."));
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", "Utilisateur non trouvé."));
        }


}
    // Méthode pour récupérer un utilisateur par son id
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable("userId") Long userId) {
        Optional<user> userOptional = userRepository.findById(userId);

        if (!userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Utilisateur non trouvé.");
        }

        user existingUser = userOptional.get();

        // Réponse avec les informations de l'utilisateur
        Map<String, Object> response = new HashMap<>();
        response.put("id", existingUser.getId());
        response.put("username", existingUser.getUsername());
        response.put("telephone", existingUser.getTelephone());
        response.put("photoPath", existingUser.getPhotoPath());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/username/{userId}")
    public ResponseEntity<?> getUsernameById(@PathVariable("userId") Long userId) {
        Optional<user> userOptional = userRepository.findById(userId);

        if (!userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Utilisateur non trouvé.");
        }

        user existingUser = userOptional.get();

        // Retourner uniquement le nom d'utilisateur
        Map<String, String> response = new HashMap<>();
        response.put("username", existingUser.getUsername());

        return ResponseEntity.ok(response);
    }

    // Méthode pour mettre à jour un utilisateur par son id
    @PutMapping("/update/{userId}")
    public ResponseEntity<String> updateUser(
            @PathVariable("userId") Long userId,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "telephone", required = false) String telephone,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "photo", required = false) String photo) {

        // Recherche de l'utilisateur par ID
        Optional<user> userOptional = userRepository.findById(userId);

        if (!userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Utilisateur non trouvé.");
        }

        user existingUser = userOptional.get();

        // Mise à jour des champs uniquement si les nouveaux valeurs sont fournies
        if (username != null && !username.isEmpty()) {
            existingUser.setUsername(username);
        }
        if (telephone != null && !telephone.isEmpty()) {
            existingUser.setTelephone(telephone);
        }
        if (password != null && !password.isEmpty()) {
            // Si un mot de passe est fourni, il est encodé avant de le mettre à jour
            String encodedPassword = passwordEncoder.encode(password);
            existingUser.setPassword(encodedPassword);
        }
        if (photo != null && !photo.isEmpty()) {
            existingUser.setPhotoPath(photo);
        }

        // Sauvegarde des modifications
        userRepository.save(existingUser);

        return ResponseEntity.status(HttpStatus.OK)
                .body("Utilisateur mis à jour avec succès.");
    }


    @GetMapping("/count")
    public ResponseEntity<Long> getUserCount() {
        long count = userRepository.count(); // Utilise count() pour récupérer le nombre total
        return ResponseEntity.ok(count);
    }
}
