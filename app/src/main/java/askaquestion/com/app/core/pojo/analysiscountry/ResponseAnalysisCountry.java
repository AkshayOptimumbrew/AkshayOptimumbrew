package askaquestion.com.app.core.pojo.analysiscountry;

public class ResponseAnalysisCountry {
    public int code;
    public String message;
    public String cause;
    public DataAnalysisCountry data;

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

    public DataAnalysisCountry getData() {
        return data;
    }

    public void setData(DataAnalysisCountry data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseAnalysisCountry{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", cause='" + cause + '\'' +
                ", data=" + data +
                '}';
    }
}
