package askaquestion.com.app.core.pojo.getquestion;

import java.io.Serializable;
import java.util.ArrayList;

public class DataViewQuestion implements Serializable {
    public ArrayList<Result> result;

    public ArrayList<Result> getResult() {
        return result;
    }

    public void setResult(ArrayList<Result> result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return String.valueOf(result);
    }
}
