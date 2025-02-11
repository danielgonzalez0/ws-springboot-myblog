package org.wildcodeschool.MyBlog.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wildcodeschool.MyBlog.dto.AuthorDTO;
import org.wildcodeschool.MyBlog.model.Author;
import org.wildcodeschool.MyBlog.service.AuthorService;

import java.util.List;

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
        return ResponseEntity.ok(authors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorDTO> getAuthorById(@PathVariable long id) {
        AuthorDTO author = this.authorService.getAuthorById(id);
        return ResponseEntity.ok(author);
    }

    @PostMapping
    public ResponseEntity<AuthorDTO> createAuthor(@RequestBody Author author) {
       AuthorDTO savedAuthor = this.authorService.createAuthor(author);
       return ResponseEntity.status(201).body(savedAuthor);
    }

    @PutMapping("{id}")
    public ResponseEntity<AuthorDTO> updateAuthor(@PathVariable long id, @RequestBody Author author) {
        AuthorDTO updatedAuthor = this.authorService.updateAuthor(id, author);
        return ResponseEntity.ok(updatedAuthor);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable long id) {
        this.authorService.deleteAuthor(id);
        return ResponseEntity.noContent().build();
    }
}
