package org.mdw31.tp4SOA.services;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.mdw31.tp4SOA.entitys.Message;
import org.mdw31.tp4SOA.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:8081") // Autorisation des requêtes CORS
@RequestMapping("/chat") // Point de terminaison pour le chat
public class ChatRS {




    @Autowired
    private MessageRepository messageRepository;

    // Gérer les requêtes préalables CORS (OPTIONS)
    @OPTIONS
    @RequestMapping(value = "/{any:.*}", method = RequestMethod.OPTIONS)
    public Response handlePreflight() {
        return Response.ok()
                .header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:8081")
                .header(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "GET, POST, PUT, DELETE, OPTIONS")
                .header(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "Content-Type, Authorization")
                .header(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true")
                .build();
    }



    // Ajouter un message dans un chat

    @PostMapping("/sendMessage")
    public ResponseEntity<Message> sendMessage(@RequestBody Message message) {
        // Enregistrer le message dans la base de données
        messageRepository.save(message);

        // Retourne une réponse OK avec le message créé
        return ResponseEntity.ok()
                .header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:8081") // CORS
                .body(message);
    }
    @GetMapping("/getMessages")
    public ResponseEntity<List<Message>> getMessages() {
        // Récupérer tous les messages de la base de données
        List<Message> messages = messageRepository.findAll();
        return ResponseEntity.ok(messages);
    }


    // Supprimer un message spécifique

    @DeleteMapping("/deleteMessage/{messageId}")
    public ResponseEntity<String> deleteMessage(@PathVariable("messageId") Long messageId) {
        Optional<Message> messageOpt = messageRepository.findById(messageId);
        if (messageOpt.isPresent()) {
            messageRepository.deleteById(messageId);
            return ResponseEntity.ok("Message deleted successfully.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Message not found.");
    }

}
