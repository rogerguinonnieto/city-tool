package model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Station {
    // Api url1
    private int station_id;
    private int num_bikes_available;
    private int num_docks_available;
    private long last_reported;
    private boolean is_charging_station;
    private String status;

    // Api url2
    private String name;
    private float lat;
    private float lon;

    public Station() {
    }

    public Station(int station_id, int num_bikes_available, int num_docks_available) {
        this.station_id = station_id;
        this.num_bikes_available = num_bikes_available;
        this.num_docks_available = num_docks_available;
    }

    public int getStation_id() {
        return station_id;
    }

    public void setStation_id(int station_id) {
        this.station_id = station_id;
    }

    public int getNum_bikes_available() {
        return num_bikes_available;
    }

    public void setNum_bikes_available(int num_bikes_available) {
        this.num_bikes_available = num_bikes_available;
    }

    public int getNum_docks_available() {
        return num_docks_available;
    }

    public void setNum_docks_available(int num_docks_available) {
        this.num_docks_available = num_docks_available;
    }

    public long getLast_reported() {
        return last_reported;
    }

    public void setLast_reported(long last_reported) {
        this.last_reported = last_reported;
    }

    public boolean isIs_charging_station() {
        return is_charging_station;
    }

    public void setIs_charging_station(boolean is_charging_station) {
        this.is_charging_station = is_charging_station;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    @Override
    public String toString() {
        return "Station{" +
                "station_id=" + station_id +
                ", num_bikes_available=" + num_bikes_available +
                ", num_docks_available=" + num_docks_available +
                ", last_reported=" + last_reported +
                ", is_charging_station=" + is_charging_station +
                ", status='" + status + '\'' +
                ", name='" + name + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                '}';
    }

}
