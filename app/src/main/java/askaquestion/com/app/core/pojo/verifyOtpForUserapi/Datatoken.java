package askaquestion.com.app.core.pojo.verifyOtpForUserapi;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Datatoken implements Serializable {

    @SerializedName("token")
    public String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
