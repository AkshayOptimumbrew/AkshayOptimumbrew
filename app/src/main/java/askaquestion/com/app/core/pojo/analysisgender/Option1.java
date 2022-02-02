package askaquestion.com.app.core.pojo.analysisgender;

public class Option1 {
    public double male;
    public double female;

    public double getMale() {
        return male;
    }

    public void setMale(double male) {
        this.male = male;
    }

    public double getFemale() {
        return female;
    }

    public void setFemale(double female) {
        this.female = female;
    }

    @Override
    public String toString() {
        return "male=" + male +
                " female=" + female;
    }
}
