package gradle.model;

import java.util.ArrayList;
import java.util.List;

public abstract class Model {
    private String Id;
    public static final List<Model> items = new ArrayList<>();

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public Model findModel(String Id) {
        for (Model item : items) {
            if (item.getId().equals(Id)) {
                return item;
            }
        }
        return null;
    }

    public static void addItem(Model item) {
        items.add(item);
    }
}
