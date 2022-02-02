package askaquestion.com.app.core.sharedprefs;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {

    public static final String USER_DATA="User_Data";
    public static final String EMAIL="email";
    public static final String PASSWORD="password";
    public static final String NAME="name";
    public static final String LOGIN_STATUS="login_status";
    public static final String USER_REGISTER_TEMP_ID="user_reg_temp_id";
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Context context;

    public PrefManager(Context context) {
        this.context=context;
        preferences= context.getSharedPreferences(USER_DATA,Context.MODE_PRIVATE);
        editor=preferences.edit();
    }
    public void setEmail(String email){
        editor.putString(EMAIL,email);
        editor.commit();

    }
    public String getEmail(){
        return preferences.getString(EMAIL,"");

    }
    public void setPassword(String password){
        editor.putString(PASSWORD,password);
        editor.commit();

    }
    public String getPassword(){
        return preferences.getString(PASSWORD,"");

    }
    public void setName(String name){
        editor.putString(NAME,name);
        editor.commit();

    }
    public String getName(){
        return preferences.getString(NAME,"");
    }

    public void setTempid(int regId){
        editor.putInt(USER_REGISTER_TEMP_ID,regId);
        editor.commit();

    }
    public int getTempid(){
        return preferences.getInt(USER_REGISTER_TEMP_ID,0);
    }

    public void setLoginStatus(boolean status){
        editor.putBoolean(LOGIN_STATUS,status);
        editor.commit();
    }
    public boolean getLoginStatus(){
        return preferences.getBoolean(LOGIN_STATUS,false);
    }

    public void clearData(){
        editor.clear();
        editor.commit();
    }

}
