package com.alz2019.controller;

import com.alz2019.dto.RequestDto;
import com.alz2019.service.PhotoService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/pictures")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PhotoController {
    PhotoService photoService;

    @GetMapping(value = "/largest", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getLargestPhoto() {
        return photoService.loadLargestPhoto();
    }

    @GetMapping(value = "/smallest", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getSmallestPhoto() {
        return photoService.loadSmallestPhoto();
    }

    @PostMapping
    public void downloadPhotos(@RequestBody RequestDto requestDto) {
        photoService.downloadPhotosBySol(requestDto.sol());
    }
}
