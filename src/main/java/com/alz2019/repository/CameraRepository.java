package com.alz2019.repository;

import com.alz2019.model.Camera;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CameraRepository extends JpaRepository<Camera, Integer> {
    Optional<Camera> findByOriginalId(Integer originalId);
}
