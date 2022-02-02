package askaquestion.com.app.core.pojo.addquestionimage;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RootImage implements Serializable {
    @SerializedName("request_data")
    public RequestDataImage request_data;

    @SerializedName("question_image")
    public String question_image;

    @SerializedName("option1")
    public String option1;

    @SerializedName("option2")
    public String option2;

    public RequestDataImage getRequest_data() {
        return request_data;
    }

    public void setRequest_data(RequestDataImage request_data) {
        this.request_data = request_data;
    }

    public String getQuestion_image() {
        return question_image;
    }

    public void setQuestion_image(String question_image) {
        this.question_image = question_image;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    @Override
    public String toString() {
        return "RootImage{" +
                "request_data=" + request_data +
                ", question_image='" + question_image + '\'' +
                ", option1='" + option1 + '\'' +
                ", option2='" + option2 + '\'' +
                '}';
    }
}
