package model;

public class NotifierRequest {
    private int phone;
    private String ip;

    public NotifierRequest() {}

    public int getPhone() { return phone; }
    public void setPhone(int phone) { this.phone = phone; }

    public String getIp() { return ip; }
    public void setIp(String ip) { this.ip = ip; }
}
