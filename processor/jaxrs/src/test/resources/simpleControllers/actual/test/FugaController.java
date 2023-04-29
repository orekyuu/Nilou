package test;
import net.orekyuu.nilou.UriBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/fuga")
@UriBuilder
public class FugaController {

  @Path("")
  @GET
  public void all() {}
}