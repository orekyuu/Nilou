package test;
import jakarta.ws.rs.*;
import net.orekyuu.nilou.UriBuilder;

@Path("/method/{id}")
@UriBuilder
public class MethodOnlyController {

  @GET
  public void get(@PathParam("id") String id) {}

  @POST
  public void post(@PathParam("id") String id) {}

  @PUT
  public void put(@PathParam("id") String id) {}

  @PATCH
  public void patch(@PathParam("id") String id) {}

  @OPTIONS
  public void options(@PathParam("id") String id) {}

  @HEAD
  public void head(@PathParam("id") String id) {}

  @DELETE
  public void delete(@PathParam("id") String id) {}
}