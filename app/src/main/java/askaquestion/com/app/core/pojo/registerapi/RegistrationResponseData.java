package askaquestion.com.app.core.pojo.registerapi;

public class RegistrationResponseData {
    public RequestData request_data;
    public String profile_img;

    public RequestData getRequest_data() {
        return request_data;
    }

    public void setRequest_data(RequestData request_data) {
        this.request_data = request_data;
    }

    public String getProfile_img() {
        return profile_img;
    }

    public void setProfile_img(String profile_img) {
        this.profile_img = profile_img;
    }

    @Override
    public String toString() {
        return "{" + request_data + '}';
    }
}
