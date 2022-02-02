package askaquestion.com.app.core.pojo.reportquestion;

import java.io.Serializable;

public class RequestReport implements Serializable {

    int question_id;

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }
}
