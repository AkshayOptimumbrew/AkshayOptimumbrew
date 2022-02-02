package askaquestion.com.app.core.pojo.uploadimage;

import askaquestion.com.app.core.pojo.verifyuser.DataVerify;

public class ResponseUpload {
    public int code;
    public String message;
    public String cause;
    public DataVerify data;

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

    public DataVerify getData() {
        return data;
    }

    public void setData(DataVerify data) {
        this.data = data;
    }
}
