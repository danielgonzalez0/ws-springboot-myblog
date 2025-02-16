package org.wildcodeschool.MyBlog.service;

import org.springframework.stereotype.Service;
import org.wildcodeschool.MyBlog.dto.category.CategoryCreateDTO;
import org.wildcodeschool.MyBlog.dto.category.CategoryDTO;
import org.wildcodeschool.MyBlog.exception.BadRequestException;
import org.wildcodeschool.MyBlog.exception.ResourceNotFoundException;
import org.wildcodeschool.MyBlog.mapper.CategoryMapper;
import org.wildcodeschool.MyBlog.model.Category;
import org.wildcodeschool.MyBlog.repository.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryRepository categoryRepository,
                           CategoryMapper categoryMapper){
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    public List<CategoryDTO>getAllCategories(){
        List<Category> categories = this.categoryRepository.findAll();
        if (categories.isEmpty()){
            throw new ResourceNotFoundException("No category found");
        }
        return categories.stream().map(this.categoryMapper::convertToDTO).collect(Collectors.toList());
    }

    public CategoryDTO getCategoryById(Long id){
        Category category = this.categoryRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Category not found with id : " + id));
        return this.categoryMapper.convertToDTO(category);
    }

    public CategoryDTO createCategory(CategoryCreateDTO categoryCreateDTO){
        Category category = this.categoryMapper.convertToEntity(categoryCreateDTO);
        Category newCategory = this.categoryRepository.save(category);
        if(newCategory == null){
            throw new BadRequestException("Category not saved");
        }
        return this.categoryMapper.convertToDTO(newCategory);
    }

    public CategoryDTO updateCategory(Long id, CategoryCreateDTO categoryDetails){
        Category category = this.categoryRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Category not found with id : " + id));
        category.setName(categoryDetails.getName());
        Category updatedCategory = this.categoryRepository.save(category);
        return this.categoryMapper.convertToDTO(updatedCategory);
    }

    public boolean deleteCategory(Long id){
        Category category = this.categoryRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Category not found with id : " + id));
        this.categoryRepository.delete(category);
        return true;
    }


}
