package org.wildcodeschool.MyBlog.service;

import org.springframework.stereotype.Service;
import org.wildcodeschool.MyBlog.dto.image.ImageCreateDTO;
import org.wildcodeschool.MyBlog.dto.image.ImageDTO;
import org.wildcodeschool.MyBlog.exception.BadRequestException;
import org.wildcodeschool.MyBlog.exception.ResourceNotFoundException;
import org.wildcodeschool.MyBlog.mapper.ImageMapper;
import org.wildcodeschool.MyBlog.model.Image;
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
          throw new ResourceNotFoundException("No image found");
        }
        return images.stream()
                .map(this.imageMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    public ImageDTO getImageById(Long id){
        Image image = this.imageRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Image not found with id : " + id));
        return this.imageMapper.convertToDTO(image);
    }

    public ImageDTO createImage(ImageCreateDTO imageCreateDTO){
        Image image = this.imageMapper.convertToEntity(imageCreateDTO);
        Image savedImage = this.imageRepository.save(image);
        if (savedImage == null){
            throw  new BadRequestException("Image not saved");
        }
        return imageMapper.convertToDTO(savedImage);
    }

    public ImageDTO updateImage(Long id, ImageDTO imageDetails){
        Image image = this.imageMapper.convertToEntity(imageDetails);
        Image imageUpdated = this.imageRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Image not found with id : " + id));
        imageUpdated.setUrl(imageDetails.getUrl());
        Image updatedImage = this.imageRepository.save(imageUpdated);
        return this.imageMapper.convertToDTO(updatedImage);
    }

    public boolean deleteImage(Long id){
        Image image = this.imageRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Image not found with id : " + id));
        this.imageRepository.delete(image);
        return true;
    }

}
