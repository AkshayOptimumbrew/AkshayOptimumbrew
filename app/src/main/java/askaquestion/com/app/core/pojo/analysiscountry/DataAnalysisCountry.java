package askaquestion.com.app.core.pojo.analysiscountry;

import java.util.ArrayList;

public class DataAnalysisCountry {
    public ArrayList<ResultAnalysisCountry> result;

    public ArrayList<ResultAnalysisCountry> getResult() {
        return result;
    }

    public void setResult(ArrayList<ResultAnalysisCountry> result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return String.valueOf(result);
    }
}
