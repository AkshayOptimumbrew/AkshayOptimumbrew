package askaquestion.com.app.core.pojo.adapter_model;

public class CountryList {
    String country;

    public CountryList(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return country;
    }
}
