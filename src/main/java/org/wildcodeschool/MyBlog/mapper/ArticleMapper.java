package org.wildcodeschool.MyBlog.mapper;

import org.springframework.stereotype.Component;
import org.wildcodeschool.MyBlog.dto.ArticleAuthorDTO;
import org.wildcodeschool.MyBlog.dto.ArticleDTO;
import org.wildcodeschool.MyBlog.model.Article;
import org.wildcodeschool.MyBlog.model.Image;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ArticleMapper {

    //méthodes mapper pour convertir un article en articleDTO
    public ArticleDTO convertToDTO(Article article) {
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setId(article.getId());
        articleDTO.setTitle(article.getTitle());
        articleDTO.setContent(article.getContent());
        articleDTO.setUpdatedAt(article.getUpdatedAt());
        if (article.getCategory() != null) {
            articleDTO.setCategoryName(article.getCategory().getName());
        }
        if (article.getImages() != null) {
            articleDTO.setImageUrls(article.getImages().stream().map(Image::getUrl).collect(Collectors.toList()));
        }
        //gestion author
        if(article.getArticleAuthors() != null) {
            articleDTO.setAuthors(article.getArticleAuthors().stream()
                    .filter(articleAuthor -> articleAuthor.getAuthor().getId() != null)
                    .map(articleAuthor -> {
                        ArticleAuthorDTO articleAuthorDTO = new ArticleAuthorDTO();
                        articleAuthorDTO.setId(articleAuthor.getId()); // Associer l'ID de ArticleAuthor
                        articleAuthorDTO.setAuthorId(articleAuthor.getAuthor().getId()); // Associer l'ID de l'auteur
                        articleAuthorDTO.setArticleId(articleAuthor.getArticle().getId()); // Associer l'ID de l'article
                        articleAuthorDTO.setContribution(articleAuthor.getContribution()); // Ajout de la contribution
                        return articleAuthorDTO; // Retourner l'objet ArticleAuthorDTO
                    })
                    .collect(Collectors.toList()));
        }
        return articleDTO;
    }

    public List<ArticleDTO> convertToDTOList(List<Article> articles) {
        List<ArticleDTO> articlesDTO = articles.stream()
                .map(this::convertToDTO)
                //                    même chose que la ligne ci-dessous, "::"
                // Le double deux-points (::) est utilisé pour faire une référence de méthode en Java.
//                    .map(article -> this.convertToDTO(article))
                .collect(Collectors.toList());
        return articlesDTO;
    }
}
