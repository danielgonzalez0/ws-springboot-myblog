package org.wildcodeschool.MyBlog.dto;

import java.util.List;

public class CategoryDTO {
    private Long id;
    private  String name;
    private List<ArticleDTO> articles;

    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ArticleDTO> getArticles() {
        return this.articles;
    }

    public void setArticles(List<ArticleDTO> articles) {
        this.articles = articles;
    }
}
