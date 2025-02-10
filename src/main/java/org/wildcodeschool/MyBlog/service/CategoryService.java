package org.wildcodeschool.MyBlog.service;

import org.springframework.stereotype.Service;
import org.wildcodeschool.MyBlog.dto.CategoryDTO;
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
            return null;
        }
        return categories.stream().map(this.categoryMapper::convertToDTO).collect(Collectors.toList());
    }

    public CategoryDTO getCategoryById(Long id){
        Category category = this.categoryRepository.findById(id).orElse(null);
        if(category == null){
            return null;
        }
        return this.categoryMapper.convertToDTO(category);
    }

    public CategoryDTO createCategory(Category category){
        Category newCategory = this.categoryRepository.save(category);
        if(newCategory == null){
            return null;
        }
        return this.categoryMapper.convertToDTO(newCategory);
    }

    public CategoryDTO updateCategory(Long id, Category categoryDetails){
        Category category = this.categoryRepository.findById(id).orElse(null);
        if(category == null){
            return null;
        }
        category.setName(categoryDetails.getName());
        Category updatedCategory = this.categoryRepository.save(category);
        return this.categoryMapper.convertToDTO(updatedCategory);
    }

    public boolean deleteCategory(Long id){
        Category category = this.categoryRepository.findById(id).orElse(null);
        if(category == null){
            return false;
        }
        this.categoryRepository.delete(category);
        return true;
    }


}
