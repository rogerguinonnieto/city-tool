package api;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import model.IpInfo;

public class IpApi {

    // Base URL for the IP-API JSON endpoint
    private static final String API_URL = "http://ip-api.com/json/";

    public static String getCityByIp(String ipAddress) {
        System.out.println("=== Fetching City by IP ===");
        System.out.println("IP Address: " + ipAddress);

        // JAX-RS client
        Client client = ClientBuilder.newClient();

        // Make GET request
        IpInfo ipInfo = client.target(API_URL + ipAddress)
                .request(MediaType.APPLICATION_JSON)
                .get(IpInfo.class);

        // Check if request was successful
        if (ipInfo != null && "success".equals(ipInfo.getStatus())) {
            System.out.println("City found: " + ipInfo.getCity());
            System.out.println("=== Lookup Complete ===\n");
            return ipInfo.getCity();
        } else {
            System.out.println("Lookup failed. Reason: " + (ipInfo != null ? ipInfo.getMessage() : "Unknown"));
            System.out.println("=== Lookup Complete ===\n");
            return null;
        }
    }
}