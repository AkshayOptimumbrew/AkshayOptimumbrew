package askaquestion.com.app.core.pojo.signinapi;

public class LoginData {
    public String email_id;
    public String password;
    public DeviceInfoLogin device_info;

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public DeviceInfoLogin getDevice_info() {
        return device_info;
    }

    public void setDevice_info(DeviceInfoLogin device_info) {
        this.device_info = device_info;
    }
}
