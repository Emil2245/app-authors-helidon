package com.programacion.distribuida.authors.model;

import jakarta.persistence.*;

@Entity
@Table(name = "book_author")
public class BookAuthor {
    @EmbeddedId
    private BookAuthorId id;

    @ManyToOne
    @MapsId("authorId")
    @JoinColumn(name = "authors_id", nullable = false)
    private Author author;
}
