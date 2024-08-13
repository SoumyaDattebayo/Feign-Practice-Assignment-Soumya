package com.openfienpractise.controller;

import com.openfienpractise.model.Book;
import com.openfienpractise.dto.BookDTO;
import com.openfienpractise.model.Author;
import com.openfienpractise.repository.BookRepository;
import com.openfienpractise.client.AuthorClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorClient authorClient;

    @GetMapping("/books")
    public List<BookDTO> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/books/{id}")
    public BookDTO getBookById(@PathVariable Long id) {
        Book book = bookRepository.findById(id);
        if (book == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found");
        }
        return convertToDTO(book);
    }

    private BookDTO convertToDTO(Book book) {
        Author author = authorClient.getAuthorById(book.getAuthorId());
        if (author == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found");
        }
        return new BookDTO(book.getId(), book.getTitle(), author);
    }
}
