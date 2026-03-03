package services;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import model.Client;
import model.NotifierRequest;
import model.Station;
import model.Data;
import api.Telegram;
import api.IpApi;
import api.AirQuality;

@Path("/notifier")
public class NotifierService {

    @POST
    @Path("/slots")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response notifySlots(NotifierRequest request) {
        // 1. Find the client
        Client client = ClientsService.getClientByPhone(request.getPhone());
        if (client == null) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("{\"error\":\"Client not found\"}").build();
        }

        // 2. Fetch current Bicing data safely
        StationsService stationsService = new StationsService();
        Response stationResponse = stationsService.getStations();
        Data bicingData = (Data) stationResponse.getEntity();

        if (bicingData == null || bicingData.getData() == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("{\"error\":\"Could not retrieve Bicing data\"}").build();
        }

        List<Station> allStations = bicingData.getData().getStations();
        List<Integer> subscribedIds = client.getStationIds();

        // 3. Build the Telegram message
        StringBuilder message = new StringBuilder("🚲 *Your Bicing Update* 🚲\n\n");
        boolean hasStations = false;

        for (Station station : allStations) {
            if (subscribedIds.contains(station.getStation_id())) {
                message.append("📍 Station ").append(station.getStation_id())
                       .append(station.getName() != null ? " (" + station.getName() + ")" : "")
                       .append("\nFree Bikes: ").append(station.getNum_bikes_available())
                       .append("\nFree Docks: ").append(station.getNum_docks_available())
                       .append("\n\n");
                hasStations = true;
            }
        }

        if (!hasStations) {
            message.append("No data available for your subscribed stations.");
        }

        // 4. Send via Telegram
        Telegram.sendMessage(message.toString());

        return Response.ok("{\"status\":\"success\", \"message\":\"Slots notification sent\"}").build();
    }

    @POST
    @Path("/airquality")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response notifyAirQuality(NotifierRequest request, @Context HttpServletRequest remoteRequest) {
        // 1. Find the client
        Client client = ClientsService.getClientByPhone(request.getPhone());
        if (client == null) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("{\"error\":\"Client not found\"}").build();
        }

        // 2. Determine IP from Headers
        String ipAddress = remoteRequest.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = remoteRequest.getRemoteAddr();
        }
        
        // If multiple IPs in X-Forwarded-For, take the first one
        if (ipAddress != null && ipAddress.contains(",")) {
            ipAddress = ipAddress.split(",")[0].trim();
        }

        // Fallback to your demo default if local/null
        if (ipAddress == null || ipAddress.equals("0:0:0:0:0:0:0:1") || ipAddress.equals("127.0.0.1")) {
            System.out.println("Localhost IP detected, using default Barcelona IP for demo.");
            ipAddress = "213.195.118.92"; 
        }
        
        // If the frontend didn't send an IP, use a default Barcelona IP so the API doesn't crash during your demo
        if (ipAddress == null || ipAddress.isEmpty()) {
            System.out.println("No IP detected, using default Barcelona IP for demo.");
            ipAddress = "213.195.118.92"; 
        }

        // 3. Translate IP to City
        String city = IpApi.getCityByIp(ipAddress);
        if (city == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("{\"error\":\"Could not determine city from IP\"}").build();
        }

        // 4. Translate City to Air Quality Index
        Integer aqiNumber = AirQuality.getAqiByCity(city);
        if (aqiNumber == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("{\"error\":\"Could not fetch AQI for city\"}").build();
        }

        // 5. Translate numerical AQI to conceptual level and send message
        String aqiDescription = AirQuality.translateAqi(aqiNumber);
        String message = "🌍 *Air Quality Update* 🌍\n\n" +
                         "City: " + city + "\n" +
                         "AQI: " + aqiNumber + "\n" +
                         "Status: " + aqiDescription;

        Telegram.sendMessage(message);

        return Response.ok("{\"status\":\"success\", \"message\":\"Air quality notification sent\"}").build();
    }
}
