package askaquestion.com.app.core.pojo.addquestionimage;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RequestDataImage implements Serializable {

    @SerializedName("category_id")
    public int category_id;

    @SerializedName("country")
    public String country;

    @SerializedName("gender")
    public int gender;

    @SerializedName("description")
    public String description;

    @SerializedName("option_type")
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

    @Override
    public String toString() {
        return "RequestDataImage{" +
                "category_id=" + category_id +
                ", country='" + country + '\'' +
                ", gender=" + gender +
                ", description='" + description + '\'' +
                ", option_type=" + option_type +
                '}';
    }
}
