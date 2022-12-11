package com.alz2019.service;

import com.alz2019.dto.PhotoDto;
import com.alz2019.model.Camera;
import com.alz2019.model.Photo;
import com.alz2019.model.Rover;
import com.alz2019.model.Status;
import com.alz2019.repository.CameraRepository;
import com.alz2019.repository.PhotoRepository;
import com.alz2019.repository.RoverRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.Comparator.comparing;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PhotoService {
    PhotoRepository photoRepository;
    CameraRepository cameraRepository;
    RoverRepository roverRepository;
    RestTemplate restTemplate;
    @Value("${nasa.api.url}")
    @NonFinal
    String nasaApiUrl;
    @Value("${nasa.api.key}")
    @NonFinal
    String nasaApiKey;

    @Transactional
    public void downloadPhotosBySol(int sol) {
        String uri = UriComponentsBuilder.fromHttpUrl(nasaApiUrl)
                .queryParam("api_key", nasaApiKey)
                .queryParam("sol", sol)
                .toUriString();
        JsonNode jsonResponse = restTemplate.getForObject(uri, JsonNode.class);
        processJsonResponse(jsonResponse);
    }

    private void processJsonResponse(JsonNode jsonNode) {
        for (JsonNode pictureJson : jsonNode.get("photos")) {
            Camera camera = resolveCamera(pictureJson.get("camera"));
            Rover rover = resolveRover(pictureJson.get("rover"));
            Photo picture = resolvePhoto(pictureJson);
            camera.addPicture(picture);
            rover.addPicture(picture);
        }
    }

    private Rover resolveRover(JsonNode roverJson) {
        int originalId = roverJson.get("id").asInt();
        return roverRepository.findByOriginalId(originalId).orElseGet(() -> saveNewRover(roverJson));
    }

    private Rover saveNewRover(JsonNode roverJson) {
        ObjectMapper mapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();
        Rover rover = Rover.builder()
                .originalId(roverJson.get("id").asInt())
                .name(roverJson.get("name").asText())
                .launchDate(mapper.convertValue(roverJson.get("launch_date"), LocalDate.class))
                .landingDate(mapper.convertValue(roverJson.get("landing_date"), LocalDate.class))
                .status(Status.valueOf(roverJson.get("status").asText().toUpperCase()))
                .build();
        return roverRepository.save(rover);
    }

    private Camera resolveCamera(JsonNode cameraJson) {
        int originalId = cameraJson.get("id").asInt();
        return cameraRepository.findByOriginalId(originalId).orElseGet(() -> saveNewCamera(cameraJson));
    }

    private Camera saveNewCamera(JsonNode cameraJson) {
        Camera camera = Camera.builder()
                .originalId(cameraJson.get("id").asInt())
                .name(cameraJson.get("name").asText())
                .build();
        return cameraRepository.save(camera);
    }

    private Photo resolvePhoto(JsonNode photoJson) {
        return photoRepository.findByOriginalId(photoJson.get("id").asLong())
                .orElseGet(() -> createPhoto(photoJson));
    }

    private Photo createPhoto(JsonNode photoJson) {
        return Photo.builder()
                .originalId(photoJson.get("id").asLong())
                .imgSrc(photoJson.get("img_src").asText())
                .build();
    }

    public byte[] loadLargestPhoto() {
        List<Photo> photos = photoRepository.findAll();
        PhotoDto largestPhoto = photos.stream().map(this::getSize).max(comparing(PhotoDto::size))
                .orElseThrow();
        return restTemplate.getForObject(largestPhoto.uri(), byte[].class);
    }

    public byte[] loadSmallestPhoto() {
        List<Photo> photos = photoRepository.findAll();
        PhotoDto smallestPhoto = photos.parallelStream().map(this::getSize).min(comparing(PhotoDto::size))
                .orElseThrow();
        return restTemplate.getForObject(smallestPhoto.uri(), byte[].class);
    }

    private PhotoDto getSize(Photo photo) {
        HttpHeaders httpHeaders = restTemplate.headForHeaders(photo.getImgSrc());
        URI location = httpHeaders.getLocation();
        HttpHeaders locationHeaders = restTemplate.headForHeaders(location);
        return PhotoDto.builder()
                .uri(location)
                .size(locationHeaders.getContentLength())
                .build();
    }
}
