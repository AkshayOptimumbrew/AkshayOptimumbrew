package askaquestion.com.app.core.pojo.analysisgender;

public class ResultGender {
    public Option1 option1;
    public Option2 option2;

    public Option1 getOption1() {
        return option1;
    }

    public void setOption1(Option1 option1) {
        this.option1 = option1;
    }

    public Option2 getOption2() {
        return option2;
    }

    public void setOption2(Option2 option2) {
        this.option2 = option2;
    }

    @Override
    public String toString() {
        return "ResultGender{" +
                "option1=" + option1 +
                ", option2=" + option2 +
                '}';
    }
}
