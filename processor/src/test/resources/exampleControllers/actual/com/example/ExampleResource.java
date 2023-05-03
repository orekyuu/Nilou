package com.example;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import net.orekyuu.nilou.UriBuilder;

@Path("/hello")
@UriBuilder
public class ExampleResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("")
    public String hello() {
        return "Hello from RESTEasy Reactive";
    }
}
