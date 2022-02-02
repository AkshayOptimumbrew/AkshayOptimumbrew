package askaquestion.com.app.core.pojo.viewcategories;

public class ResponseOfViewCategories {
    public int code;
    public String message;
    public String cause;
    public DataCategories data;

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

    public DataCategories getData() {
        return data;
    }

    public void setData(DataCategories data) {
        this.data = data;
    }
}
