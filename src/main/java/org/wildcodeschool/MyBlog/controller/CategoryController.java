package org.wildcodeschool.MyBlog.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.wildcodeschool.MyBlog.dto.category.CategoryCreateDTO;
import org.wildcodeschool.MyBlog.dto.category.CategoryDTO;
import org.wildcodeschool.MyBlog.model.Category;
import org.wildcodeschool.MyBlog.service.CategoryService;
import java.util.List;


@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    //méthodes CRUD

    @PreAuthorize("permitAll()")
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories(){
        List<CategoryDTO> categories = this.categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id){
        CategoryDTO category = this.categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CategoryDTO> createArticle(@Valid @RequestBody CategoryCreateDTO categoryCreateDTO) {
        CategoryDTO newCategory = this.categoryService.createCategory(categoryCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCategory);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryCreateDTO categoryDetails) {
        CategoryDTO category = this.categoryService.updateCategory(id, categoryDetails);
        return ResponseEntity.ok(category);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        this.categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
