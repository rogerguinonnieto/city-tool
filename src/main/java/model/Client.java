package model;
import java.util.List;

public class Client {
    public int phoneNumber;
    public List<Integer> stationIds;
    public String botToken;
    public String chatId;

    public Client() {
    }

    public Client(int phoneNumber, List<Integer> stationIds, String botToken, String chatId) {
        this.phoneNumber = phoneNumber;
        this.stationIds = stationIds;
        this.botToken = botToken;
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

    public String getBotToken() {
        return botToken;
    }

    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }
}
