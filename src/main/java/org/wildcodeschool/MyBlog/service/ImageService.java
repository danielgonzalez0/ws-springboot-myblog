package org.wildcodeschool.MyBlog.service;

import org.springframework.stereotype.Service;
import org.wildcodeschool.MyBlog.dto.ImageDTO;
import org.wildcodeschool.MyBlog.mapper.ImageMapper;
import org.wildcodeschool.MyBlog.model.Image;
import org.wildcodeschool.MyBlog.repository.ArticleRepository;
import org.wildcodeschool.MyBlog.repository.ImageRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ImageService {

    private final ImageRepository imageRepository;
    private final ImageMapper imageMapper;

    public ImageService(ImageRepository imageRepository,
                        ImageMapper imageMapper) {
        this.imageRepository = imageRepository;
        this.imageMapper = imageMapper;
    }

    public List<ImageDTO> getAllImages(){
        List<Image> images = this.imageRepository.findAll();
        if(images.isEmpty()){
            return null;
        }
        return images.stream()
                .map(this.imageMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    public ImageDTO getImageById(Long id){
        Image image = this.imageRepository.findById(id).orElse(null);
        if(image == null){
            return null;
        }
        return this.imageMapper.convertToDTO(image);
    }

    public ImageDTO createImage(Image image){
        Image savedImage = this.imageRepository.save(image);
        if (savedImage == null){
            return null;
        }
        return imageMapper.convertToDTO(savedImage);
    }

    public ImageDTO updateImage(Long id, Image imageDetails){
        Image image = this.imageRepository.findById(id).orElse(null);
        if(image == null){
            return null;
        }
        image.setUrl(imageDetails.getUrl());
        Image updatedImage = this.imageRepository.save(image);
        return this.imageMapper.convertToDTO(updatedImage);
    }

    public boolean deleteImage(Long id){
        Image image = this.imageRepository.findById(id).orElse(null);
        if(image == null){
            return false;
        }
        this.imageRepository.delete(image);
        return true;
    }

}
