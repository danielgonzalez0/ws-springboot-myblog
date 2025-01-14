package org.wildcodeschool.MyBlog.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wildcodeschool.MyBlog.model.Category;
import org.wildcodeschool.MyBlog.repository.CategoryRepository;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    //méthodes CRUD

    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories(){
        List<Category> categories = this.categoryRepository.findAll();
        if(categories.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id){
        Category category = this.categoryRepository.findById(id).orElse(null);
        if(category == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(category);
    }

    @PostMapping
    public ResponseEntity<Category> createArticle(@RequestBody Category category) {
        Category savedCategory = this.categoryRepository.save(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
    }

    @PutMapping("{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody Category categoryDetails) {
        Category category = this.categoryRepository.findById(id).orElse(null);
        if (category == null) {
            return ResponseEntity.notFound().build();
        }
        category.setName(categoryDetails.getName());

        Category updatedCategory = this.categoryRepository.save(category);
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        Category category = this.categoryRepository.findById(id).orElse(null);
        if (category == null) {
            return ResponseEntity.notFound().build();
        }
        this.categoryRepository.delete(category);
        return ResponseEntity.noContent().build();
    }
}
