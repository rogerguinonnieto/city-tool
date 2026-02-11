package services;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/clients")
public class ClientsService {
    private static List<Client> clients = new ArrayList<>();

    @POST
    @Path("/subscribe")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response subscribe(Client client) {
        clients.add(client);
        return Response.ok().build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getClients() {
        return Response.ok(clients).build();
    }
}
