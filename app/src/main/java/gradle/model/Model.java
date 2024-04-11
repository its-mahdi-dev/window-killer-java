package gradle.model;

import java.util.*;

public abstract class Model {
    private String Id;
    public int x;
    public int y;
    public int w;
    public int h;

    public Model() {
        Id = UUID.randomUUID().toString();
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public Model findModel(String Id) {
        List<Model> items = getItems();
        if (items != null) {
            for (Model item : items) {
                if (item.getId().equals(Id)) {
                    return item;
                }
            }
        }
        return null;
    }

    public void addItem(Model item) {
        List<Model> items = getItems();
        if (items != null) {
            items.add(item);
        }
    }

    protected abstract List<Model> getItems();
}
