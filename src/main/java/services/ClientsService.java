package services;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.POST;
import javax.ws.rs.Consumes;
import java.util.ArrayList;
import java.util.List;
import model.Client;
import api.OpenGateway;

@Path("/clients")
public class ClientsService {
    private static List<Client> clients = new ArrayList<>();

    @POST
    @Path("/subscribe")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response subscribe(Client client) {
        try {
            // threshold is 21 as per project description
            boolean isVerified = OpenGateway.verifyAge(client.getPhoneNumber(), 21);
            
            if (!isVerified) {
                return Response.status(Response.Status.FORBIDDEN)
                            .entity("{\"error\": \"Age verification failed. Must be 21+.\"}")
                            .build();
            }

            clients.add(client);
            return Response.ok().build();
        } catch (Exception e) {
            return Response.status(500).entity("{\"error\": \"System error during verification\"}").build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getClients() {
        return Response.ok(clients).build();
    }

    @POST
    @Path("/removeStation/{phone}/{stationId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeStation(@javax.ws.rs.PathParam("phone") int phone, 
                                 @javax.ws.rs.PathParam("stationId") int stationId) {
        Client client = getClientByPhone(phone);
        
        if (client != null) {
            client.removeStationId(stationId);
            return Response.ok("{\"message\": \"Station removed successfully\"}").build();
        }
        
        return Response.status(Response.Status.NOT_FOUND)
                       .entity("{\"error\": \"Client not found\"}")
                       .build();
    }
    
    public static Client getClientByPhone(int phone) {
        for (Client c : clients) {
            if (c.getPhoneNumber() == phone) {
                return c;
            }
        }
        return null;
    }
}
