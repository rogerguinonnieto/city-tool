package model;
import java.util.List;

public class Data {
    private StationsContainer data;

    public StationsContainer getData() { return data; }
    public void setData(StationsContainer data) { this.data = data; }

    public static class StationsContainer {
        private List<Station> stations;
        public List<Station> getStations() { return stations; }
        public void setStations(List<Station> stations) { this.stations = stations; }
    }
}