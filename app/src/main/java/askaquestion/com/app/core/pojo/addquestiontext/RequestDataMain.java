package askaquestion.com.app.core.pojo.addquestiontext;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RequestDataMain implements Serializable {

    @SerializedName("request_data")
    @Expose
    private RequestDataSub requestData;

    @SerializedName("question_image")
    @Expose
    public String question_image;

    public RequestDataSub getRequestData() {
        return requestData;
    }

    public void setRequestData(RequestDataSub requestData) {
        this.requestData = requestData;
    }

    public String getQuestion_image() {
        return question_image;
    }

    public void setQuestion_image(String question_image) {
        this.question_image = question_image;
    }
}
