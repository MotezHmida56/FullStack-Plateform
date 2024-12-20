package org.mdw31.tp4SOA.repositories;

import org.mdw31.tp4SOA.entitys.Message;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
