package model;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Data {
    private Stations data;

    public Stations getData() { return data; }
    public void setData(Stations data) { this.data = data; }

    public static class Stations{
        private List<Station> stations;
        public List<Station> getStations() { return stations; }
        public void setStations(List<Station> stations) { this.stations = stations; }
    }
}