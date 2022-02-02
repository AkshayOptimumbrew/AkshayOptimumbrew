package askaquestion.com.app.core.pojo.verifyOtpForUserapi;

public class Rootverifyresponse {
    public int code;
    public String message;
    public String cause;
    public Datatoken data;

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

    public Datatoken getData() {
        return data;
    }

    public void setData(Datatoken data) {
        this.data = data;
    }
}
