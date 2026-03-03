package model;

public class NotifierRequest {
    private String phone;
    private String ip; 

    public NotifierRequest() {}

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getIp() { return ip; }
    public void setIp(String ip) { this.ip = ip; }
}