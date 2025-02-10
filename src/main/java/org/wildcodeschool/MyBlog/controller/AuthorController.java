package org.wildcodeschool.MyBlog.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wildcodeschool.MyBlog.dto.ArticleAuthorDTO;
import org.wildcodeschool.MyBlog.dto.AuthorDTO;
import org.wildcodeschool.MyBlog.model.ArticleAuthor;
import org.wildcodeschool.MyBlog.model.Author;
import org.wildcodeschool.MyBlog.repository.AuthorRepository;
import org.wildcodeschool.MyBlog.service.AuthorService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    public final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;

    }

    @GetMapping
    public ResponseEntity<List<AuthorDTO>> getAllAuthors() {
        List<AuthorDTO> authors = this.authorService.getAllAuthors();
        if(authors.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(authors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorDTO> getAuthorById(@PathVariable long id) {
        AuthorDTO author = this.authorService.getAuthorById(id);
        if(author == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(author);
    }

    @PostMapping
    public ResponseEntity<AuthorDTO> createAuthor(@RequestBody Author author) {
       AuthorDTO savedAuthor = this.authorService.createAuthor(author);
        if(savedAuthor == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(201).body(savedAuthor);
    }

    @PutMapping("{id}")
    public ResponseEntity<AuthorDTO> updateAuthor(@PathVariable long id, @RequestBody Author author) {
        AuthorDTO updatedAuthor = this.authorService.updateAuthor(id, author);
        if(updatedAuthor == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedAuthor);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable long id) {

        if(!this.authorService.deleteAuthor(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
