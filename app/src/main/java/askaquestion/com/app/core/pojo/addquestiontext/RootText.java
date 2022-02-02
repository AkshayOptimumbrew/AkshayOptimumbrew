package askaquestion.com.app.core.pojo.addquestiontext;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RootText  implements Serializable {

    @SerializedName("request_data")
    public RequestDataText request_data;

    @SerializedName("question_image")
    public String question_image;

    public RequestDataText getRequest_data() {
        return request_data;
    }

    public void setRequest_data(RequestDataText request_data) {
        this.request_data = request_data;
    }

    public String getQuestion_image() {
        return question_image;
    }

    public void setQuestion_image(String question_image) {
        this.question_image = question_image;
    }

   /* @Override
    public String toString() {
        return "{" + request_data + "}";
    }*/
}
