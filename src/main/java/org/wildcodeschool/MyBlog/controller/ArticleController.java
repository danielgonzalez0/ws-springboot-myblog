    package org.wildcodeschool.MyBlog.controller;

    import jakarta.validation.Valid;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;
    import org.wildcodeschool.MyBlog.dto.article.ArticleCreateDTO;
    import org.wildcodeschool.MyBlog.dto.article.ArticleDTO;
    import org.wildcodeschool.MyBlog.model.*;
    import org.wildcodeschool.MyBlog.service.ArticleService;

    import java.time.LocalDateTime;
    import java.util.List;

    @RestController
    @RequestMapping("/articles")
    public class ArticleController {

        private final ArticleService articleService;

        public ArticleController(ArticleService articleService) {
            this.articleService = articleService;
        }

        //méthodes CRUD à venir

        @GetMapping
        public ResponseEntity<List<ArticleDTO>> getAllArticles() {
            List<ArticleDTO> articles = this.articleService.getAllArticles();
            return ResponseEntity.ok(articles);
        }

        @GetMapping("/{id}")
        public ResponseEntity<ArticleDTO> getArticleById(@PathVariable Long id) {
            ArticleDTO article = this.articleService.getArticleById(id);
            return ResponseEntity.ok(article);
        }

        @PostMapping
        public ResponseEntity<ArticleDTO> createArticle(@Valid @RequestBody ArticleCreateDTO articleCreateDTO) {
            ArticleDTO articleDTO = this.articleService.createArticle(articleCreateDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(articleDTO);
        }

        @PutMapping("/{id}")
        public ResponseEntity<ArticleDTO> updateArticle(@PathVariable Long id, @RequestBody Article articleDetails) {
ArticleDTO articleDTO = this.articleService.updateArticle(id, articleDetails);
            return ResponseEntity.ok(articleDTO);
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
            this.articleService.deleteArticle(id);
            return ResponseEntity.noContent().build();
        }

        @GetMapping("/search-title")
        public ResponseEntity<List<ArticleDTO>> getArticlesByTitle(@RequestParam String searchTerms) {
            List<ArticleDTO> articles = this.articleService.getArticlesByTitle(searchTerms);
            if (articles == null) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(articles);
        }

        @GetMapping("/search-content")
        public ResponseEntity<List<ArticleDTO>> getArticlesByContent(@RequestParam String searchTerms) {
            List<ArticleDTO> articles = this.articleService.getArticlesByContent(searchTerms);
            if (articles.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(articles);
        }

        @GetMapping("/created-after")
        public ResponseEntity<List<ArticleDTO>> getArticlesCreateAfter(@RequestParam LocalDateTime createdAt) {
            List<ArticleDTO> articles = this.articleService.getArticlesCreateAfter(createdAt);
            if (articles.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(articles);
        }

        @GetMapping("/last-created")
        public ResponseEntity<List<ArticleDTO>> getFiveLastArticles(){
            List<ArticleDTO> articles = this.articleService.getFiveLastArticles();
            if (articles.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(articles);
        }
    }
