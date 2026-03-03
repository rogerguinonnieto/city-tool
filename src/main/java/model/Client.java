package model;
import java.util.List;

public class Client {
    public int phoneNumber;
    public List<Integer> stationIds;
    public String chatId;

    public Client() {
    }

    public Client(int phoneNumber, List<Integer> stationIds, String chatId) {
        this.phoneNumber = phoneNumber;
        this.stationIds = stationIds;
        this.chatId = chatId;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<Integer> getStationIds() {
        return stationIds;
    }

    public void setStationIds(List<Integer> stationIds) {
        this.stationIds = stationIds;
    }

    public void addStationId(int stationId) {
        this.stationIds.add(stationId);
    }

    public void removeStationId(int stationId) {
        this.stationIds.remove(Integer.valueOf(stationId));
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    @Override
    public String toString() {
        return "Client{" +
                "phoneNumber=" + phoneNumber +
                ", stationIds=" + stationIds +
                ", chatId='" + chatId + '\'' +
                '}';
    }
}
