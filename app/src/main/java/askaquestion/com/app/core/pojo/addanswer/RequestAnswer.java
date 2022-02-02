package askaquestion.com.app.core.pojo.addanswer;

public class RequestAnswer {
    public int question_id;
    public int selected_answer;

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    public int getSelected_answer() {
        return selected_answer;
    }

    public void setSelected_answer(int selected_answer) {
        this.selected_answer = selected_answer;
    }
}
