package test;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import net.orekyuu.nilou.UriBuilder;

import javax.validation.constraints.NotNull;

@Path("notNull")
@UriBuilder
public class NotNullController {

  @GET
  public void all(@NotNull @QueryParam("id") String id) {}

  @GET
  @Path("{id}")
  public void all(@PathParam("id") String id, @NotNull @QueryParam("mode") String mode) {}
}