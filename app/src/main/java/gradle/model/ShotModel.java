package gradle.model;

import java.util.ArrayList;
import java.util.List;

import gradle.view.charecretsView.ShotView;

public class ShotModel extends Model {
    public static final List<Model> items = new ArrayList<>();

    public ShotModel() {
        addItem(this);
    }

    public ShotModel(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        addItem(this);
        ShotView.items.add(new ShotView());
    }

    @Override
    protected List<Model> getItems() {
        return items;
    }
}
