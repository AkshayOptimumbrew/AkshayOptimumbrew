package askaquestion.com.app.core.pojo.verifyOtpForUserapi;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Rootverifyotp implements Serializable {

    @SerializedName("email_id")
    public String email_id;

    @SerializedName("token")
    public int token;

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public int getToken() {
        return token;
    }

    public void setToken(int token) {
        this.token = token;
    }
}
