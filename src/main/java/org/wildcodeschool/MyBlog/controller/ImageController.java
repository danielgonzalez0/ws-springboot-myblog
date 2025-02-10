package org.wildcodeschool.MyBlog.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wildcodeschool.MyBlog.dto.ImageDTO;
import org.wildcodeschool.MyBlog.model.Image;
import org.wildcodeschool.MyBlog.service.ImageService;


import java.util.List;

@RestController
@RequestMapping("/images")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService
    ) {
        this.imageService = imageService;
    }

    @GetMapping
    public ResponseEntity<List<ImageDTO>> getAllImages() {
        List<ImageDTO> images = this.imageService.getAllImages();
        if (images.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(images);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ImageDTO> getImageById(@PathVariable Long id) {
        ImageDTO image = this.imageService.getImageById(id);
        if (image == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(image);
    }

    @PostMapping
    public ResponseEntity<ImageDTO> createImage(@RequestBody Image image) {
    ImageDTO savedImage = this.imageService.createImage(image);
        if (savedImage == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(201).body(savedImage);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ImageDTO> updateImage(@PathVariable Long id, @RequestBody Image imageDetails) {
        ImageDTO updatedImage = this.imageService.updateImage(id, imageDetails);
        if (updatedImage == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedImage);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long id) {
        if (!this.imageService.deleteImage(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
