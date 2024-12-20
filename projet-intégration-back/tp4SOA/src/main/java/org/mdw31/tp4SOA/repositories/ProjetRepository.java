package org.mdw31.tp4SOA.repositories;

import org.mdw31.tp4SOA.entitys.projet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjetRepository extends JpaRepository<projet, Long> {
}
