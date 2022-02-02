package askaquestion.com.app.core.pojo.noneanalysis;

public class ResponseNone {
    public int code;
    public String message;
    public String cause;
    public DataNone data;

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

    public DataNone getData() {
        return data;
    }

    public void setData(DataNone data) {
        this.data = data;
    }


}
