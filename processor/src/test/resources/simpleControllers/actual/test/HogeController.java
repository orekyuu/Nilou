package test;
import net.orekyuu.nilou.UriBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/hoge")
@UriBuilder
public class HogeController {

  @Path("")
  @GET
  public void all() {}

  @Path("{id}")
  @GET
  public void get(@PathParam("id") String id) {}
}