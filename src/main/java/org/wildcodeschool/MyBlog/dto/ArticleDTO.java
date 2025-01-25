package org.wildcodeschool.MyBlog.dto;

import org.wildcodeschool.MyBlog.model.ArticleAuthor;
import org.wildcodeschool.MyBlog.model.Author;

import java.time.LocalDateTime;
import java.util.List;

public class ArticleDTO {

    private Long id;
    private String title;
    private String content;
    private LocalDateTime updatedAt;
    private String categoryName;
    private List<String> imageUrls;
    private List<ArticleAuthorDTO> authors;

    public List<ArticleAuthorDTO> getAuthors() {
        return authors;
    }

    public void setAuthors(List<ArticleAuthorDTO> authors) {
        this.authors = authors;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCategoryName() {
        return this.categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    public List<String> getImageUrls() {
        return this.imageUrls;
    }
    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
}
