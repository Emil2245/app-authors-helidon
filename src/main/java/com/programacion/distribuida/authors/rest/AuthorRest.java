package com.programacion.distribuida.authors.rest;

import com.programacion.distribuida.authors.model.Author;
import com.programacion.distribuida.authors.repository.AuthorRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/authors")
public class AuthorRest {
    @Inject
    AuthorRepository authorRepository;

    AtomicInteger index = new AtomicInteger(0);

    @GET
    public List<Author> findAll() {
        return authorRepository.listAll();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Integer id) {

        return authorRepository.findByIdOptional(id)
                .map(obj -> {
                    obj.setName(obj.getName() );
                    return obj;
                })
                .map(Response::ok)
                .orElse(Response.status(Response.Status.NOT_FOUND))
                .build();
    }

    @GET
    @Path("/find/{isbn}")
    public List<Author> findByBook(@PathParam("isbn") String isbn) {
        System.out.println(isbn);

        return authorRepository.findByBook(isbn).stream()
                .map(obj -> {
                    var newName = String.format("%s", obj.getName());
                    obj.setName(newName);
                    return obj;
                }).toList();
    }

    @GET
    @Path("/test")
    public String test() {
        Config config = ConfigProvider.getConfig();
        config.getConfigSources()
                .forEach(obj -> System.out.printf("%d ->s \n", obj.getOrdinal(), obj.getName()));

        return "ok";
    }
}
