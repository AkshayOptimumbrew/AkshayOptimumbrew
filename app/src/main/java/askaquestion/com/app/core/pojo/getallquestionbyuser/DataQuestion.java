package askaquestion.com.app.core.pojo.getallquestionbyuser;

import java.util.ArrayList;
import java.util.Objects;

import askaquestion.com.app.core.pojo.getquestion.Result;

public class DataQuestion {
    public ArrayList<ResultWithQuestionId> result;

    public ArrayList<ResultWithQuestionId> getResult() {
        return result;
    }

    public void setResult(ArrayList<ResultWithQuestionId> result) {
        this.result = result;
    }
}
