package org.wildcodeschool.MyBlog.dto;

import org.wildcodeschool.MyBlog.model.ArticleAuthor;
import org.wildcodeschool.MyBlog.model.Author;

import java.util.List;

public class AuthorDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private List<ArticleAuthorDTO> articlesAuthor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<ArticleAuthorDTO> getArticlesAuthor() {
        return articlesAuthor;
    }

    public void setArticlesAuthor(List<ArticleAuthorDTO> articlesAuthor) {
        this.articlesAuthor = articlesAuthor;
    }
}
