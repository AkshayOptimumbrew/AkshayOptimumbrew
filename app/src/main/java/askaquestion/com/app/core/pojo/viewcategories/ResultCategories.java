package askaquestion.com.app.core.pojo.viewcategories;

public class ResultCategories {
    public int id;
    public String category_name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    @Override
    public String toString() {
        return category_name;
    }
}
