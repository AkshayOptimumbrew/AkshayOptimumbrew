package askaquestion.com.app.core.pojo.forgotpasswordapi;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RequestForgot implements Serializable {

    @SerializedName("email_id")
    public String email_id;

    @SerializedName("token")
    public String token;

    @SerializedName("password")
    public String password;

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
