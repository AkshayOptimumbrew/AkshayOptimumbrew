package askaquestion.com.app.core.pojo.adapter_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class CountryName implements Serializable {
    @SerializedName("country")
    @Expose
    public String country;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

}
