package com.programacion.distribuida.authors.repository;

import com.programacion.distribuida.authors.model.Author;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@Transactional
public class AuthorRepository {

    @PersistenceContext()
    private EntityManager em;

    public List<Author> findByBook(String isbn) {
        return em.createQuery("SELECT ba.author FROM BookAuthor ba WHERE ba.id.bookIsbn = :isbn", Author.class)
                .setParameter("isbn", isbn)
                .getResultList();
    }

    public Optional<Author> findByIdOptional(Integer id) {
        return em.createQuery("SELECT a FROM Author a WHERE a.id = :id", Author.class)
                .setParameter("id", id)
                .getResultStream()
                .findFirst();
    }
    public List<Author> listAll() {
        return em.createQuery("SELECT a FROM Author a", Author.class)
                .getResultList();
    }
}
