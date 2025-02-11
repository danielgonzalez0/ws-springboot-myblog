package org.wildcodeschool.MyBlog.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.wildcodeschool.MyBlog.dto.AuthorDTO;
import org.wildcodeschool.MyBlog.exception.BadRequestException;
import org.wildcodeschool.MyBlog.exception.ResourceNotFoundException;
import org.wildcodeschool.MyBlog.mapper.AuthorMapper;
import org.wildcodeschool.MyBlog.model.Author;
import org.wildcodeschool.MyBlog.repository.AuthorRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    private final AuthorMapper authorMapper;
    private final AuthorRepository authorRepository;

    public AuthorService(AuthorMapper authorMapper, AuthorRepository authorRepository) {
        this.authorMapper = authorMapper;
        this.authorRepository = authorRepository;
    }

    public List<AuthorDTO> getAllAuthors(){
        List<Author> authors = this.authorRepository.findAll();
        if(authors.isEmpty()) {
            throw new ResourceNotFoundException("No author found");
        }
        return authors.stream()
                .map(this.authorMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    public AuthorDTO getAuthorById(Long id){
        Author author = this.authorRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Author not found with id : " + id));
        return this.authorMapper.convertToDTO(author);
    }

    public AuthorDTO createAuthor(Author author){
        Author savedAuthor = this.authorRepository.save(author);
        if (!(savedAuthor instanceof Author) ){
            throw new BadRequestException("Author not saved");
        }
        return authorMapper.convertToDTO(savedAuthor);
    }

    public AuthorDTO updateAuthor(Long id, Author authorDetails){
        Author author = this.authorRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Author not found with id : " + id));
        author.setFirstName(authorDetails.getFirstName());
        author.setLastName(authorDetails.getLastName());
        Author updatedAuthor = this.authorRepository.save(author);
        return this.authorMapper.convertToDTO(updatedAuthor);
    }

    public Boolean deleteAuthor(Long id){
        Author author = this.authorRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Author not found with id : " + id));
        this.authorRepository.delete(author);
        return true;
    }
}
