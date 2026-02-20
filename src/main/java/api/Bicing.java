package api;

import io.github.cdimascio.dotenv.Dotenv;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import model.Data;
import model.Station;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bicing {
    // Load environment variables
    private static final Dotenv dotenv = Dotenv.load();
    private static final String TOKEN = dotenv.get("OPEN_DATA_API_TOKEN");
    private static final String API_URL = dotenv.get("OPEN_DATA_API_URL1");
    private static final String API_URL2 = dotenv.get("OPEN_DATA_API_URL2");

    public static Data fetchFromAPI() {
        // Fetch data from URL1 (availability/status data)
        Data data1 = ClientBuilder.newClient()
                .target(API_URL)
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", TOKEN)
                .get(new GenericType<Data>() {});

        // Fetch data from URL2 (station info: name, location)
        Data data2 = ClientBuilder.newClient()
                .target(API_URL2)
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", TOKEN)
                .get(new GenericType<Data>() {});

        // Merge data from URL2 into URL1 stations
        mergeStationData(data1, data2);

        return data1;
    }

    private static void mergeStationData(Data data1, Data data2) {
        List<Station> stations1 = data1.getData().getStations();
        List<Station> stations2 = data2.getData().getStations();

        // Create a map for quick lookup by station_id
        Map<Integer, Station> stationMap = new HashMap<>();
        for (Station station : stations2) {
            stationMap.put(station.getStation_id(), station);
        }

        // Merge data from URL2 into stations from URL1
        System.out.println("=== Merging Station Data ===");
        for (Station station : stations1) {
            Station infoStation = stationMap.get(station.getStation_id());
            if (infoStation != null) {
                station.setName(infoStation.getName());
                station.setLat(infoStation.getLat());
                station.setLon(infoStation.getLon());
                System.out.println("Station ID: " + station.getStation_id() + 
                        " | Name: " + station.getName() + 
                        " | Lat: " + station.getLat() + 
                        " | Lon: " + station.getLon() + 
                        " | Bikes: " + station.getNum_bikes_available() + 
                        " | Docks: " + station.getNum_docks_available());
            }
        }
        System.out.println("Total stations merged: " + stations1.size());
        System.out.println("=== Merge Complete ===\n");
    }
}
