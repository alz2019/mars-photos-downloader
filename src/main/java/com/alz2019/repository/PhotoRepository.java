package com.alz2019.repository;

import com.alz2019.model.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    Optional<Photo> findByOriginalId(Long originalId);
}
