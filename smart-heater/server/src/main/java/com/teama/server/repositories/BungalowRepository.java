package com.teama.server.repositories;

import com.teama.server.models.Bungalow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BungalowRepository extends JpaRepository<Bungalow, Long> {
}
