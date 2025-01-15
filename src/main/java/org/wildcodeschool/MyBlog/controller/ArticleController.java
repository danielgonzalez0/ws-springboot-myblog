    package org.wildcodeschool.MyBlog.controller;

    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;
    import org.wildcodeschool.MyBlog.dto.ArticleDTO;
    import org.wildcodeschool.MyBlog.model.Article;
    import org.wildcodeschool.MyBlog.model.Category;
    import org.wildcodeschool.MyBlog.model.Image;
    import org.wildcodeschool.MyBlog.repository.ArticleRepository;
    import org.wildcodeschool.MyBlog.repository.CategoryRepository;
    import org.wildcodeschool.MyBlog.repository.ImageRepository;

    import java.time.LocalDateTime;
    import java.util.ArrayList;
    import java.util.List;
    import java.util.stream.Collectors;

    @RestController
    @RequestMapping("/articles")
    public class ArticleController {

        private final ArticleRepository articleRepository;
        private final CategoryRepository categoryRepository;
        private final ImageRepository imageRepository;


        public ArticleController(ArticleRepository articleRepository, CategoryRepository categoryRepository, ImageRepository imageRepository) {
            this.articleRepository = articleRepository;
            this.categoryRepository = categoryRepository;
            this.imageRepository = imageRepository;
        }
        //méthodes mapper pour convertir un article en articleDTO
        private ArticleDTO convertToDTO(Article article) {
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
            return articleDTO;
        }

        private List<ArticleDTO> convertToDTOList(List<Article> articles) {
            List<ArticleDTO> articleDTOs = articles.stream()
                    .map(this::convertToDTO)
 //                    même chose que la ligne ci-dessous, "::"
                    // Le double deux-points (::) est utilisé pour faire une référence de méthode en Java.
//                    .map(article -> this.convertToDTO(article))
                    .collect(Collectors.toList());
            return articleDTOs;
        }

        //méthodes CRUD à venir

        @GetMapping
        public ResponseEntity<List<ArticleDTO>> getAllArticles() {
            List<Article> articles = this.articleRepository.findAll();
            if (articles.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(this.convertToDTOList(articles));
        }

        @GetMapping("/{id}")
        public ResponseEntity<ArticleDTO> getArticleById(@PathVariable Long id) {
            Article article = this.articleRepository.findById(id).orElse(null);
            if (article == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(this.convertToDTO(article));
        }

        @PostMapping
        public ResponseEntity<ArticleDTO> createArticle(@RequestBody Article article) {
            article.setCreatedAt(LocalDateTime.now());
            article.setUpdatedAt(LocalDateTime.now());

            //ajout catégorie
            if(article.getCategory() != null){
                Category category = this.categoryRepository.findById(article.getCategory().getId()).orElse(null);
                if(category == null){
                    return ResponseEntity.badRequest().body(null);
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
                            return ResponseEntity.badRequest().body(null);
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
            return ResponseEntity.status(HttpStatus.CREATED).body(this.convertToDTO(savedArticle));
        }

        @PutMapping("/{id}")
        public ResponseEntity<ArticleDTO> updateArticle(@PathVariable Long id, @RequestBody Article articleDetails) {

            Article article = this.articleRepository.findById(id).orElse(null);
            if (article == null) {
                return ResponseEntity.notFound().build();
            }

            article.setTitle(articleDetails.getTitle());
            article.setContent(articleDetails.getContent());
            article.setUpdatedAt(LocalDateTime.now());

            // Mise à jour de la catégorie
            if (articleDetails.getCategory() != null) {
                Category category = this.categoryRepository.findById(articleDetails.getCategory().getId()).orElse(null);
                if (category == null) {
                    return ResponseEntity.badRequest().body(null);
                }
                article.setCategory(category);
            }

            if (articleDetails.getImages() != null) {
                List<Image> validImages = new ArrayList<>();
                for (Image image : articleDetails.getImages()) {
                    if (image.getId() != null) {
                        // Vérification des images existantes
                        Image existingImage = imageRepository.findById(image.getId()).orElse(null);
                        if (existingImage != null) {
                            validImages.add(existingImage);
                        } else {
                            return ResponseEntity.badRequest().build(); // Image non trouvée, retour d'une erreur
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
                // Si aucune image n'est fournie, on nettoie la liste des images associées
                article.getImages().clear();
            }

            Article updatedArticle = this.articleRepository.save(article);
            return ResponseEntity.ok(this.convertToDTO(updatedArticle));
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {

            Article article = this.articleRepository.findById(id).orElse(null);
            if (article == null) {
                return ResponseEntity.notFound().build();
            }

            this.articleRepository.delete(article);
            return ResponseEntity.noContent().build();
        }

        @GetMapping("/search-title")
        public ResponseEntity<List<ArticleDTO>> getArticlesByTitle(@RequestParam String searchTerms) {
            List<Article> articles = this.articleRepository.findByTitle(searchTerms);
            if (articles.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(this.convertToDTOList(articles));
        }

        @GetMapping("/search-content")
        public ResponseEntity<List<ArticleDTO>> getArticlesByContent(@RequestParam String searchTerms) {
            List<Article> articles = this.articleRepository.findByContent(searchTerms);
            if (articles.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(this.convertToDTOList(articles));
        }

        @GetMapping("/created-after")
        public ResponseEntity<List<ArticleDTO>> getArticlesCreateAfter(@RequestParam LocalDateTime createdAt) {
            List<Article> articles = this.articleRepository.findByCreatedAtAfter(createdAt);
            if (articles.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(this.convertToDTOList(articles));
        }

        @GetMapping("/last-created")
        public ResponseEntity<List<ArticleDTO>> getFiveLastArticles(){
            List<Article> articles = this.articleRepository.findTop5ByOrderByCreatedAtDesc();
            if (articles.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(this.convertToDTOList(articles));
        }
    }
