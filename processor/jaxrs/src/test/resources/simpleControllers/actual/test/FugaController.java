package test;
import net.orekyuu.nilou.UriBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/fuga")
@UriBuilder
public class FugaController {

  @Path("")
  @GET
  public void all() {}
}