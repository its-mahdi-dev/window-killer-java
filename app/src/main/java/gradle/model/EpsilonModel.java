package gradle.model;

import java.awt.geom.Point2D;
import java.util.*;

import gradle.controller.Constants;
import gradle.controller.GameSettings;
import gradle.interfaces.Entity;
import gradle.view.charecretsView.EpsilonView;
import gradle.view.charecretsView.View;

public class EpsilonModel extends Model implements Entity {
    public static final List<Model> items = new ArrayList<>();
    public static final List<Model> removedItems = new ArrayList<>();
    public boolean shotStore;
    public int XP;

    private EpsilonModel() {
        init();
        addItem(this);
        View view = new EpsilonView(getId());
        view.setUtil(this);
    }

    public static EpsilonModel getINSTANCE() {
        if (items.size() == 0) {
            return new EpsilonModel();
        } else
            return (EpsilonModel) items.get(0);
    }

    public void init() {
        anchor = new Point2D.Double(700, 500);
        w = Constants.EPSILON_DIAMETER;
        h = Constants.EPSILON_DIAMETER;
        max_speed = Constants.MOVE_SPEED + GameSettings.sensitivity * 2 / 10.0;
        isMoving = false;
        HP = 4;
        impact_speed = 1 + GameSettings.sensitivity / 100.0;
    }

    @Override
    protected List<Model> getItems() {
        return items;
    }

    @Override
    protected List<Model> getRemovedItems() {
        return removedItems;
    }

}
