package model;

public class Station {
    public int station_id;
    public int num_bikes_available;
    public int num_docks_available;

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

}
