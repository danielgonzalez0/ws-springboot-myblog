package org.wildcodeschool.MyBlog.mapper;

import org.springframework.stereotype.Component;
import org.wildcodeschool.MyBlog.dto.article.ArticleAuthorDTO;
import org.wildcodeschool.MyBlog.dto.author.AuthorCreateDTO;
import org.wildcodeschool.MyBlog.dto.author.AuthorDTO;
import org.wildcodeschool.MyBlog.model.Author;

import java.util.stream.Collectors;

@Component
public class AuthorMapper {

    public AuthorDTO convertToDTO(Author author) {
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setId(author.getId());
        authorDTO.setFirstName(author.getFirstName());
        authorDTO.setLastName(author.getLastName());
        if(author.getArticleAuthors() != null) {
            authorDTO.setArticlesAuthor(author.getArticleAuthors().stream()
                    .map(articleAuthor ->{
                        ArticleAuthorDTO articleAuthorDTO = new ArticleAuthorDTO();
                        articleAuthorDTO.setId(articleAuthor.getId());
                        articleAuthorDTO.setAuthorId(articleAuthor.getAuthor().getId());
                        articleAuthorDTO.setArticleId(articleAuthor.getArticle().getId());
                        articleAuthorDTO.setContribution(articleAuthor.getContribution());
                        return articleAuthorDTO;
                    }).collect(Collectors.toList()));
        }
        return authorDTO;
    }

    public Author convertToEntity(AuthorCreateDTO authorCreateDTO) {
        Author author = new Author();
        author.setFirstName(authorCreateDTO.getFirstName());
        author.setLastName(authorCreateDTO.getLastName());
        return author;
    }
}
