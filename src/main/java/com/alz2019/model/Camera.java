package com.alz2019.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "cameras")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Camera {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    Integer id;

    @Column(nullable = false, unique = true)
    Integer originalId;

    @Column(nullable = false)
    String name;

    @Column(nullable = false, insertable = false, updatable = false)
    LocalDateTime createdAt;

    @OneToMany(mappedBy = "camera", cascade = CascadeType.PERSIST)
    @Setter(AccessLevel.PRIVATE)
    @Builder.Default
    List<Photo> photos = new ArrayList<>();

    public void addPicture(Photo photo) {
        photo.setCamera(this);
        photos.add(photo);
    }
}
