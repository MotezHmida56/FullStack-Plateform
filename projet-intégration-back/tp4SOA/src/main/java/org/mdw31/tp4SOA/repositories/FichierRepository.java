package org.mdw31.tp4SOA.repositories;

import org.mdw31.tp4SOA.entitys.Fichier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FichierRepository extends JpaRepository<Fichier, Long> {
}
