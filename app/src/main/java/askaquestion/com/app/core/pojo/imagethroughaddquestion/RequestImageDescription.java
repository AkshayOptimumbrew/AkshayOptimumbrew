package askaquestion.com.app.core.pojo.imagethroughaddquestion;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RequestImageDescription implements Serializable {
    @SerializedName("category_id")
    @Expose
    public int category_id;

    @SerializedName("country")
    @Expose
    public String country;

    @SerializedName("gender")
    @Expose
    public int gender;

    @SerializedName("description")
    @Expose
    public String description;

    @SerializedName("option_type")
    @Expose
    public int option_type;

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getOption_type() {
        return option_type;
    }

    public void setOption_type(int option_type) {
        this.option_type = option_type;
    }
}
