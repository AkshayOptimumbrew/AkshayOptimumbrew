package askaquestion.com.app.core.pojo.getquestion;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ResponseForGetQuestion implements Serializable {
    @SerializedName("code")
    public int code;
    @SerializedName("message")
    public String message;
    @SerializedName("cause")
    public String cause;
    @SerializedName("data")
    public DataViewQuestion data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public DataViewQuestion getData() {
        return data;
    }

    public void setData(DataViewQuestion data) {
        this.data = data;
    }
}
