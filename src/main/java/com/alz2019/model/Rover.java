package com.alz2019.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "rovers")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Rover {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    Integer id;

    @Column(nullable = false, unique = true)
    Integer originalId;

    @Column(nullable = false)
    String name;

    @Column(nullable = false)
    LocalDate landingDate;

    @Column(nullable = false)
    LocalDate launchDate;

    @Enumerated(EnumType.STRING)
    Status status;

    @OneToMany(mappedBy = "rover", cascade = CascadeType.PERSIST)
    @Setter(AccessLevel.PRIVATE)
    @Builder.Default
    List<Photo> photos = new ArrayList<>();

    public void addPicture(Photo photo) {
        photo.setRover(this);
        photos.add(photo);
    }
}
