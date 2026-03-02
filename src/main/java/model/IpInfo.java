package model;

public class IpInfo {
    private String status;
    private String city;
    private String message; // Useful if the status is "fail"

    public IpInfo() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "IpInfo{" +
                "status='" + status + '\'' +
                ", city='" + city + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}