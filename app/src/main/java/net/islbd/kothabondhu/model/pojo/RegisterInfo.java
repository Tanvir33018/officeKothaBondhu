package net.islbd.kothabondhu.model.pojo;

public class RegisterInfo {
    private String statusCode;
    private String descrption;

    public String getStatusCode() {
        return statusCode;
    }

    public String getDescription() {
        return descrption;
    }

    @Override
    public String toString() {
        return "RegisterInfo{" +
                "statusCode='" + statusCode + '\'' +
                ", description='" + descrption + '\'' +
                '}';
    }
}
