package org.mdw31.tp4SOA.repositories;

import org.mdw31.tp4SOA.entitys.Tache;
import org.mdw31.tp4SOA.entitys.user;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TacheRepository extends JpaRepository<Tache, Long> {


}
