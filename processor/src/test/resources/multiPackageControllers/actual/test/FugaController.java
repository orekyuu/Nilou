package test;
import net.orekyuu.nilou.UriBuilder;

import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@Path("/fuga")
@UriBuilder
public class FugaController {

  @Path("")
  @GET
  public void all(@NotNull @QueryParam("mode") String mode) {}
}