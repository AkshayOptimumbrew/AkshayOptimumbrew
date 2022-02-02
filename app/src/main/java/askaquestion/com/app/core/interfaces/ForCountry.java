package askaquestion.com.app.core.interfaces;

import android.view.View;

import askaquestion.com.app.core.pojo.adapter_model.CountryName;

public interface ForCountry {
    void setClick(View view, CountryName countryName, String str);
}