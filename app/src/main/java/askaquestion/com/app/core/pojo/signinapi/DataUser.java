package askaquestion.com.app.core.pojo.signinapi;

public class DataUser {
    public String token;
    public UserDetails user_details;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserDetails getUser_details() {
        return user_details;
    }

    public void setUser_details(UserDetails user_details) {
        this.user_details = user_details;
    }
}
