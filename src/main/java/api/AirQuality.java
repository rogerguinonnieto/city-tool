package api;

import io.github.cdimascio.dotenv.Dotenv;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import model.AqiResponse;

public class AirQuality {

    // Load environment variables
    private static final Dotenv dotenv = Dotenv.load();
    private static final String TOKEN = dotenv.get("AQI_API_TOKEN");
    private static final String API_URL = "https://api.waqi.info/feed/";

    public static Integer getAqiByCity(String city) {
        System.out.println("=== Fetching Air Quality ===");
        System.out.println("City: " + city);

        // JAX-RS client
        Client client = ClientBuilder.newClient();

        // Make GET request
        AqiResponse response = client.target(API_URL + city + "/")
                .queryParam("token", TOKEN)
                .request(MediaType.APPLICATION_JSON)
                .get(AqiResponse.class);

        // Check if the request was successful and contains data
        if (response != null && "ok".equals(response.getStatus()) && response.getData() != null) {
            int aqi = response.getData().getAqi();
            System.out.println("AQI found: " + aqi);
            System.out.println("=== AQI Lookup Complete ===\n");
            return aqi;
        } else {
            System.out.println("AQI lookup failed.");
            System.out.println("=== AQI Lookup Complete ===\n");
            return null;
        }
    }

    public static String translateAqi(int aqi) {
        if (aqi <= 50) {
            return "Bona";
        } else if (aqi <= 100) {
            return "Moderada";
        } else if (aqi <= 150) {
            return "Insalubre per a grups sensibles";
        } else if (aqi <= 200) {
            return "Insalubre";
        } else if (aqi <= 300) {
            return "Molt insalubre";
        } else {
            return "Perillós";
        }
    }
}
