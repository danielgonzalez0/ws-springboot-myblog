package org.wildcodeschool.MyBlog.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.wildcodeschool.MyBlog.dto.ArticleDTO;
import org.wildcodeschool.MyBlog.mapper.ArticleMapper;
import org.wildcodeschool.MyBlog.model.*;
import org.wildcodeschool.MyBlog.repository.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private final AuthorRepository authorRepository;
    private final ArticleAuthorRepository articleAuthorRepository;

    public ArticleService(ArticleRepository articleRepository, ArticleMapper articleMapper, CategoryRepository categoryRepository, ImageRepository imageRepository, AuthorRepository authorRepository, ArticleAuthorRepository articleAuthorRepository) {
        this.articleRepository = articleRepository;
        this.articleMapper = articleMapper;
        this.categoryRepository = categoryRepository;
        this.imageRepository = imageRepository;
        this.authorRepository = authorRepository;
        this.articleAuthorRepository = articleAuthorRepository;
    }

    public List<ArticleDTO> getAllArticles() {
        List<Article> articles = this.articleRepository.findAll();
        return articles.stream().map(this.articleMapper::convertToDTO).collect(Collectors.toList());
    }

    public ArticleDTO getArticleById(Long id){
        Article article = this.articleRepository.findById(id).orElse(null);
        if(article == null){
            return null;
        }
        return this.articleMapper.convertToDTO(article);
    }

    public ArticleDTO createArticle(Article article){
        article.setCreatedAt(LocalDateTime.now());
        article.setUpdatedAt(LocalDateTime.now());

        //ajout catégorie
        if(article.getCategory() != null){
            Category category = this.categoryRepository.findById(article.getCategory().getId()).orElse(null);
            if(category == null){
                return null;
            }
            article.setCategory(category);
        }

        //Modif image
        if (article.getImages() != null && !article.getImages().isEmpty()) {
            List<Image> validImages = new ArrayList<>();
            for (Image image : article.getImages()) {
                if (image.getId() != null) {
                    // Vérification des images existantes
                    Image existingImage = this.imageRepository.findById(image.getId()).orElse(null);
                    if (existingImage != null) {
                        validImages.add(existingImage);
                    } else {
                        return null;
                    }
                } else {
                    // Création de nouvelles images
                    Image savedImage = this.imageRepository.save(image);
                    validImages.add(savedImage);
                }
            }
            article.setImages(validImages);
        }
        Article savedArticle = this.articleRepository.save(article);

        /// gestion author
        if(article.getArticleAuthors() != null){
            for(ArticleAuthor articleAuthor : article.getArticleAuthors()){
                Author author = articleAuthor.getAuthor();
                author = this.authorRepository.findById(author.getId()).orElse(null);
                if(author == null){
                    return null;
                }
                articleAuthor.setAuthor(author);
                articleAuthor.setArticle(savedArticle);
                articleAuthor.setContribution(articleAuthor.getContribution());
                this.articleAuthorRepository.save(articleAuthor);
            }
        }
        return this.articleMapper.convertToDTO(savedArticle);
    }

    public ArticleDTO updateArticle(Long id , Article articleDetails) {
        Article article = this.articleRepository.findById(id).orElse(null);
        if(article == null){
            return null;
        }
        article.setTitle(articleDetails.getTitle());
        article.setContent(articleDetails.getContent());
        article.setUpdatedAt(LocalDateTime.now());

        // Mise à jour de la catégorie
        if (articleDetails.getCategory() != null) {
            Category category = this.categoryRepository.findById(articleDetails.getCategory().getId()).orElse(null);
            if (category == null) {
                return null;
            }
            article.setCategory(category);
        }

        // Mise à jour des images
        if (articleDetails.getImages() != null) {
            List<Image> validImages = new ArrayList<>();
            for (Image image : articleDetails.getImages()) {
                if (image.getId() != null) {
                    // Vérification des images existantes
                    Image existingImage = imageRepository.findById(image.getId()).orElse(null);
                    if (existingImage != null) {
                        validImages.add(existingImage);
                    } else {
                        return null; // Image non trouvée, retour d'une erreur
                    }
                } else {
                    // Création de nouvelles images
                    Image savedImage = imageRepository.save(image);
                    validImages.add(savedImage);
                }
            }
            // Mettre à jour la liste des images associées
            article.setImages(validImages);
        } else {
            // Si aucune image n'est fournie, on nettoie la liste des images associées (choix projet)
            article.getImages().clear();
        }
        /// update author
        if(articleDetails.getArticleAuthors() != null){
            //supprimer manuellement les anciens ArticleAuthor
            for(ArticleAuthor oldArticleAuthor : article.getArticleAuthors()){
                article.getArticleAuthors().remove(oldArticleAuthor);
            }
            List<ArticleAuthor> updatedArticleAuthors = new ArrayList<>();
            for (ArticleAuthor articleAuthorDetails : articleDetails.getArticleAuthors()) {
                Author author = articleAuthorDetails.getAuthor();
                author = this.authorRepository.findById(author.getId()).orElse(null);
                if (author == null) {
                    return null;
                }
                // Créer et associer la nouvelle relation ArticleAuthor
                ArticleAuthor newArticleAuthor = new ArticleAuthor();
                newArticleAuthor.setAuthor(author);
                newArticleAuthor.setArticle(article);
                newArticleAuthor.setContribution(articleAuthorDetails.getContribution());
                updatedArticleAuthors.add(newArticleAuthor);
            }
            for (ArticleAuthor articleAuthor : updatedArticleAuthors) {
                this.articleAuthorRepository.save(articleAuthor);
            }
            article.setArticleAuthors(updatedArticleAuthors);
        }
        /// end update author

        Article updatedArticle = this.articleRepository.save(article);
        return this.articleMapper.convertToDTO(updatedArticle);
    };

    public boolean deleteArticle(Long id) {
        Article article = this.articleRepository.findById(id).orElse(null);
        if(article == null){
            return false;
        }

        // Supprimer les associations ArticleAuthor manuellement
        if (article.getArticleAuthors() != null) {
            for (ArticleAuthor articleAuthor : article.getArticleAuthors()) {
                this.articleAuthorRepository.delete(articleAuthor);
            }
        }
        this.articleRepository.delete(article);
        return true;
    }

    //méthodes personnalisées

    public List<ArticleDTO> getArticlesByTitle(String searchTerms){
        List<Article> articles = this.articleRepository.findByTitle(searchTerms);
        if (articles.isEmpty()) {
            return null;
        }
        return this.articleMapper.convertToDTOList(articles);
    }

    public List<ArticleDTO> getArticlesByContent(String searchTerms){
        List<Article> articles = this.articleRepository.findByContent(searchTerms);
        if (articles.isEmpty()) {
            return null;
        }
        return this.articleMapper.convertToDTOList(articles);
    }

    public List<ArticleDTO> getArticlesCreateAfter(LocalDateTime createdAt){
        List<Article> articles = this.articleRepository.findByCreatedAtAfter(createdAt);
        if (articles.isEmpty()) {
            return null;
        }
        return this.articleMapper.convertToDTOList(articles);
    }

    public List<ArticleDTO> getFiveLastArticles(){
        List<Article> articles = this.articleRepository.findTop5ByOrderByCreatedAtDesc();
        if (articles.isEmpty()) {
            return null;
        }
        return this.articleMapper.convertToDTOList(articles);
    }


}
