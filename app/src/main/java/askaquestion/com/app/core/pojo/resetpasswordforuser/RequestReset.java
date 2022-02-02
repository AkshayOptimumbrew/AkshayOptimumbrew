package askaquestion.com.app.core.pojo.resetpasswordforuser;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RequestReset implements Serializable {

    @SerializedName("email_id")
    public String email_id;

    @SerializedName("old_password")
    public String old_password;

    @SerializedName("new_password")
    public String new_password;

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getOld_password() {
        return old_password;
    }

    public void setOld_password(String old_password) {
        this.old_password = old_password;
    }

    public String getNew_password() {
        return new_password;
    }

    public void setNew_password(String new_password) {
        this.new_password = new_password;
    }
}
