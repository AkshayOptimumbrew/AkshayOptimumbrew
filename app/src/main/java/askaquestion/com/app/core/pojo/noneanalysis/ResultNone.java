package askaquestion.com.app.core.pojo.noneanalysis;

public class ResultNone {
    public String option1;
    public String option2;

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
        return "ResultNone{" +
                "option1='" + option1 + '\'' +
                ", option2='" + option2 + '\'' +
                '}';
    }
}
