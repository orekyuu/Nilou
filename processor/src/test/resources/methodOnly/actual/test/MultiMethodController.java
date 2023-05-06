package test;
import jakarta.ws.rs.*;
import net.orekyuu.nilou.UriBuilder;

@Path("/method/{id}")
@UriBuilder
public class MultiMethodController {

  @GET
  @POST
  public void getAndPost(@PathParam("id") String id) {}
}