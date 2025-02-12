package org.wildcodeschool.MyBlog.service;

import org.springframework.stereotype.Service;
import org.wildcodeschool.MyBlog.dto.AuthorContributionDTO;
import org.wildcodeschool.MyBlog.dto.ImageDTO;
import org.wildcodeschool.MyBlog.dto.article.ArticleCreateDTO;
import org.wildcodeschool.MyBlog.dto.article.ArticleDTO;
import org.wildcodeschool.MyBlog.dto.article.ArticleUpdateDTO;
import org.wildcodeschool.MyBlog.exception.BadRequestException;
import org.wildcodeschool.MyBlog.exception.ResourceNotFoundException;
import org.wildcodeschool.MyBlog.mapper.ArticleMapper;
import org.wildcodeschool.MyBlog.mapper.ImageMapper;
import org.wildcodeschool.MyBlog.model.*;
import org.wildcodeschool.MyBlog.repository.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private final ImageMapper imageMapper;
    private final AuthorRepository authorRepository;
    private final ArticleAuthorRepository articleAuthorRepository;

    public ArticleService(ArticleRepository articleRepository,
                          ArticleMapper articleMapper,
                          CategoryRepository categoryRepository,
                          ImageRepository imageRepository,
                          ImageMapper imageMapper,
                          AuthorRepository authorRepository,
                          ArticleAuthorRepository articleAuthorRepository) {
        this.articleRepository = articleRepository;
        this.articleMapper = articleMapper;
        this.categoryRepository = categoryRepository;
        this.imageRepository = imageRepository;
        this.imageMapper = imageMapper;
        this.authorRepository = authorRepository;
        this.articleAuthorRepository = articleAuthorRepository;
    }

    public List<ArticleDTO> getAllArticles() {
        List<Article> articles = this.articleRepository.findAll();
        if (articles.isEmpty()) {
            throw new ResourceNotFoundException("No article found");
        }
        return articles.stream().map(this.articleMapper::convertToDTO).collect(Collectors.toList());
    }

    public ArticleDTO getArticleById(Long id){
        Article article = this.articleRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Article not found with id : " + id));
        return this.articleMapper.convertToDTO(article);
    }

    public ArticleDTO createArticle(ArticleCreateDTO articleCreateDTO){
        Article article = this.articleMapper.convertToEntity(articleCreateDTO);
        article.setCreatedAt(LocalDateTime.now());
        article.setUpdatedAt(LocalDateTime.now());

        //ajout catégorie
        if(articleCreateDTO.getCategoryId() != null){
            Category category = this.categoryRepository.findById(articleCreateDTO.getCategoryId())
                    .orElseThrow(()-> new BadRequestException("No existing Category with id : " + articleCreateDTO.getCategoryId()));
            article.setCategory(category);
        }

        //Modif image
        if (articleCreateDTO.getImages() != null && !articleCreateDTO.getImages().isEmpty()) {
            List<Image> validImages = new ArrayList<>();
            for (ImageDTO image : articleCreateDTO.getImages()) {
                if (image.getId() != null) {
                    // Vérification des images existantes
                    Image existingImage = this.imageRepository.findById(image.getId())
                            .orElseThrow(()-> new BadRequestException("No existing image with id : " + image.getId()));
                    if (existingImage != null) {
                        validImages.add(existingImage);
                    } else {
                        throw new BadRequestException("No existing image with id : " + image.getId());
                    }
                } else {
                    // Création de nouvelles images
                    Image savedImage = this.imageRepository.save(this.imageMapper.convertToEntity(image));
                    validImages.add(savedImage);
                }
            }
            article.setImages(validImages);
        }
        Article savedArticle = this.articleRepository.save(article);

        /// gestion author
        if(articleCreateDTO.getAuthors() != null){
            for(AuthorContributionDTO authorContributionDTO : articleCreateDTO.getAuthors()){
                Long authorId = authorContributionDTO.getAuthorId();
                Author finalAuthor = this.authorRepository.findById(authorId)
                        .orElseThrow(()-> new BadRequestException("No existing Author with id : " + authorId));
                ArticleAuthor articleAuthor = new ArticleAuthor();
                articleAuthor.setAuthor(finalAuthor);
                articleAuthor.setArticle(savedArticle);
                articleAuthor.setContribution(authorContributionDTO.getContribution());
                this.articleAuthorRepository.save(articleAuthor);
            }
        }
        return this.articleMapper.convertToDTO(savedArticle);
    }

    public ArticleDTO updateArticle(Long id , ArticleUpdateDTO articleDetails) {

        Article article = this.articleRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Article not found with id : " + id));
        article.setTitle(articleDetails.getTitle());
        article.setContent(articleDetails.getContent());
        article.setUpdatedAt(LocalDateTime.now());

        // Mise à jour de la catégorie
        if (articleDetails.getCategoryId() != null) {
            Category category = this.categoryRepository.findById(articleDetails.getCategoryId())
                    .orElseThrow(()-> new BadRequestException("No existing Category with id : " + articleDetails.getCategoryId()));
            article.setCategory(category);
        }

        // Mise à jour des images
        if (articleDetails.getImages() != null) {
            List<Image> validImages = new ArrayList<>();
            for (ImageDTO image : articleDetails.getImages()) {
                if (image.getId() != null) {
                    // Vérification des images existantes
                    Image existingImage = imageRepository.findById(image.getId())
                            .orElseThrow(()-> new BadRequestException("No existing image with id : " + image.getId()));
                    if (existingImage != null) {
                        validImages.add(existingImage);
                    } else {
                        throw new BadRequestException("No existing image with id : " + image.getId());
                    }
                } else {
                    // Création de nouvelles images
                    Image savedImage = this.imageRepository.save(this.imageMapper.convertToEntity(image));
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
        if(articleDetails.getAuthors() != null){
//            //supprimer manuellement les anciens ArticleAuthor

            Iterator<ArticleAuthor> iterator = article.getArticleAuthors().iterator();
            while (iterator.hasNext()) {
                ArticleAuthor articleAuthor = iterator.next();
                iterator.remove();
                this.articleAuthorRepository.delete(articleAuthor);
            }


            List<ArticleAuthor> updatedArticleAuthors = new ArrayList<>();
            for (AuthorContributionDTO authorContributionDTO : articleDetails.getAuthors()) {
                Long authorId = authorContributionDTO.getAuthorId();
                Author finalAuthor = this.authorRepository.findById(authorId)
                        .orElseThrow(()-> new BadRequestException("No existing Author with id : " + authorId));
                // Créer et associer la nouvelle relation ArticleAuthor
                ArticleAuthor newArticleAuthor = new ArticleAuthor();
                newArticleAuthor.setAuthor(finalAuthor);
                newArticleAuthor.setArticle(article);
                newArticleAuthor.setContribution(authorContributionDTO.getContribution());
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
        Article article = this.articleRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Article not found with id : " + id));


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
