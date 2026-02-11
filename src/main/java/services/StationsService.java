package services;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/stations")
public class StationsService {
    private static Data cachedData;
    private static long lastUpdate = 0;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStations() {
        long now = System.currentTimeMillis();
        // 120 seconds = 120,000 ms
        if (cachedData == null || (now - lastUpdate) > 120000) {
            cachedData = BicingClient.fetchFromAPI();
            lastUpdate = now;
        }
        return Response.ok(cachedData).build();
    }
}