package api;

import io.github.cdimascio.dotenv.Dotenv;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import model.Data;

public class Bicing {
    // Load environment variables
    private static final Dotenv dotenv = Dotenv.load();
    private static final String TOKEN = dotenv.get("OPEN_DATA_API_TOKEN");
    private static final String API_URL = dotenv.get("OPEN_DATA_API_URL");

    public static Data fetchFromAPI() {
        return ClientBuilder.newClient()
                .target(API_URL)
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", TOKEN)
                .get(Data.class);
    }
}
