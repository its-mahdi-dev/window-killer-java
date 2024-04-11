package gradle.model;

import java.awt.geom.Point2D;
import java.util.*;

public abstract class Model {
    private String Id;
    public Point2D anchor;
    public int w;
    public int h;
    public int[] xPoints;
    public int[] yPoints;

    public Model() {
        Id = UUID.randomUUID().toString();
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public static Model findModel(String Id, List<? extends Model> items) {
        for (Model item : items) {
            if (item.getId().equals(Id)) {
                return item;
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
