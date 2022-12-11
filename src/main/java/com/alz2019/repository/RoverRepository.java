package com.alz2019.repository;

import com.alz2019.model.Rover;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoverRepository extends JpaRepository<Rover, Integer> {
    Optional<Rover> findByOriginalId(Integer originalId);
}
