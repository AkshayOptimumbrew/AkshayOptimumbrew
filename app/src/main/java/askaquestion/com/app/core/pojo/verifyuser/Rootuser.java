package askaquestion.com.app.core.pojo.verifyuser;

public class Rootuser {
    public int user_reg_temp_id;
    public int token;
    public DeviceInfosignup device_info;

    public int getUser_reg_temp_id() {
        return user_reg_temp_id;
    }

    public void setUser_reg_temp_id(int user_reg_temp_id) {
        this.user_reg_temp_id = user_reg_temp_id;
    }

    public int getToken() {
        return token;
    }

    public void setToken(int token) {
        this.token = token;
    }

    public DeviceInfosignup getDevice_info() {
        return device_info;
    }

    public void setDevice_info(DeviceInfosignup device_info) {
        this.device_info = device_info;
    }
}
