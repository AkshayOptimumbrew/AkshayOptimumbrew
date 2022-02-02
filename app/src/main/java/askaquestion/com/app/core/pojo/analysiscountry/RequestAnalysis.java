package askaquestion.com.app.core.pojo.analysiscountry;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RequestAnalysis implements Serializable {
    @SerializedName("question_id")
    @Expose
    public int question_id;

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }
}
