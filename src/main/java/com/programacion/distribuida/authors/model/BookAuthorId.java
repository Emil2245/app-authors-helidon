package com.programacion.distribuida.authors.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class BookAuthorId {
    @Column(name="books_isbn")
    private String bookIsbn;

    @Column(name="autors_id")
    private Integer authorId;
}
