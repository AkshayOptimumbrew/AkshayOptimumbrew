package askaquestion.com.app.core.sharedprefs;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedManagerForVolley {
    public static final String STATUS = "Status";
    public static final String LOGIN_STATUS = "login_status";
    public static final String EMAIL = "email";
    public static final String TOKEN = "token";
    public static final String IMAGE_PATH = "image_path";
    public static final String PASSWORD = "password";

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Context context;

    public SharedManagerForVolley(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(STATUS, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void setLoginStatus(boolean status) {
        editor.putBoolean(LOGIN_STATUS, status);
        editor.commit();
    }

    public boolean getLoginStatus() {
        return preferences.getBoolean(LOGIN_STATUS, false);
    }

    public void setEmail(String email) {
        editor.putString(EMAIL, email);
        editor.commit();
    }

    public String getEmail() {
        return preferences.getString(EMAIL, "");
    }

    public void setPassword(String password) {
        editor.putString(PASSWORD, password);
        editor.commit();
    }

    public String getPassword() {
        return preferences.getString(PASSWORD, "");
    }

    public void setToken(String token) {
        editor.putString(TOKEN, token);
        editor.commit();
    }

    public String getToken() {
        return preferences.getString(TOKEN, "");
    }
    public void setImagePath(String imagePath){
        editor.putString(IMAGE_PATH, imagePath);
        editor.commit();
    }
    public String getImagePath(){
        return preferences.getString(IMAGE_PATH,"");
    }
    public void clearData() {
        editor.clear();
        editor.commit();
    }
}
