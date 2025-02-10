    package org.wildcodeschool.MyBlog.controller;

    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;
    import org.wildcodeschool.MyBlog.dto.ArticleAuthorDTO;
    import org.wildcodeschool.MyBlog.dto.ArticleDTO;
    import org.wildcodeschool.MyBlog.dto.AuthorDTO;
    import org.wildcodeschool.MyBlog.model.*;
    import org.wildcodeschool.MyBlog.repository.*;
    import org.wildcodeschool.MyBlog.service.ArticleService;

    import java.time.LocalDateTime;
    import java.util.ArrayList;
    import java.util.List;
    import java.util.stream.Collectors;

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
            if (articles.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(articles);
        }

        @GetMapping("/{id}")
        public ResponseEntity<ArticleDTO> getArticleById(@PathVariable Long id) {
            ArticleDTO article = this.articleService.getArticleById(id);
            if (article == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(article);
        }

        @PostMapping
        public ResponseEntity<ArticleDTO> createArticle(@RequestBody Article article) {
            ArticleDTO articleDTO = this.articleService.createArticle(article);
            if (articleDTO == null) {
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(articleDTO);
        }

        @PutMapping("/{id}")
        public ResponseEntity<ArticleDTO> updateArticle(@PathVariable Long id, @RequestBody Article articleDetails) {
ArticleDTO articleDTO = this.articleService.updateArticle(id, articleDetails);
            if (articleDTO == null) {
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.ok(articleDTO);
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
            if (articleService.deleteArticle(id)) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
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
