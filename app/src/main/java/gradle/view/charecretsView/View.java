package gradle.view.charecretsView;

import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.util.*;

import gradle.model.Model;

public abstract class View {

    public Point2D anchor;
    public int w;
    public int h;

    private String Id;

    public View(String Id) {
        this.Id = Id;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public void addItem(View item) {
        List<View> items = getItems();
        if (items != null) {
            items.add(item);
        }
    }

    public View findView(String Id) {
        List<View> items = getItems();
        if (items != null) {
            for (View item : items) {
                if (item.getId().equals(Id)) {
                    return item;
                }
            }
        }
        return null;
    }

    public abstract void draw(Graphics g);

    public abstract void setUtil(Model model);

    public abstract List<View> getItems();
}
