package org.example.hogwarts.repository;

import org.example.hogwarts.model.Avatar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvatarRepository extends JpaRepository<Avatar, Long> {
}
