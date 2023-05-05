package test;
import lombok.RequiredArgsConstructor;
import net.orekyuu.nilou.UriBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/hoge")
@UriBuilder
@RequiredArgsConstructor
public class HogeController {

  final String hoge;

  @Path("")
  @GET
  public void all() {}

  @Path("{id}")
  @GET
  public void get(@PathParam("id") String id) {}
}