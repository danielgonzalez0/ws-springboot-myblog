    package org.wildcodeschool.MyBlog.controller;

    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;
    import org.wildcodeschool.MyBlog.model.Article;
    import org.wildcodeschool.MyBlog.model.Category;
    import org.wildcodeschool.MyBlog.repository.ArticleRepository;
    import org.wildcodeschool.MyBlog.repository.CategoryRepository;

    import java.time.LocalDateTime;
    import java.util.List;

    @RestController
    @RequestMapping("/articles")
    public class ArticleController {

        private final ArticleRepository articleRepository;
        private final CategoryRepository categoryRepository;

        public ArticleController(ArticleRepository articleRepository, CategoryRepository categoryRepository) {
            this.articleRepository = articleRepository;
            this.categoryRepository = categoryRepository;

        }

        //méthodes CRUD à venir

        @GetMapping
        public ResponseEntity<List<Article>> getAllArticles() {
            List<Article> articles = this.articleRepository.findAll();
            if (articles.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(articles);
        }

        @GetMapping("/{id}")
        public ResponseEntity<Article> getArticleById(@PathVariable Long id) {
            Article article = this.articleRepository.findById(id).orElse(null);
            if (article == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(article);
        }

        @PostMapping
        public ResponseEntity<Article> createArticle(@RequestBody Article article) {
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

            Article savedArticle = this.articleRepository.save(article);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedArticle);
        }

        @PutMapping("/{id}")
        public ResponseEntity<Article> updateArticle(@PathVariable Long id, @RequestBody Article articleDetails) {

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

            Article updatedArticle = this.articleRepository.save(article);
            return ResponseEntity.ok(updatedArticle);
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
        public ResponseEntity<List<Article>> getArticlesByTitle(@RequestParam String searchTerms) {
            List<Article> articles = this.articleRepository.findByTitle(searchTerms);
            if (articles.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(articles);
        }

        @GetMapping("/search-content")
        public ResponseEntity<List<Article>> getArticlesByContent(@RequestParam String searchTerms) {
            List<Article> articles = this.articleRepository.findByContent(searchTerms);
            if (articles.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(articles);
        }

        @GetMapping("/created-after")
        public ResponseEntity<List<Article>> getArticlesCreateAfter(@RequestParam LocalDateTime createdAt) {
            List<Article> articles = this.articleRepository.findByCreatedAtAfter(createdAt);
            if (articles.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(articles);
        }

        @GetMapping("/last-created")
        public ResponseEntity<List<Article>> getFiveLastArticles(){
            List<Article> articles = this.articleRepository.findTop5ByOrderByCreatedAtDesc();
            if (articles.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(articles);
        }
    }
