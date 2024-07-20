package gradle.model;

import java.awt.geom.Point2D;
import java.util.*;

import gradle.controller.Constants;
import gradle.controller.GameSettings;
import gradle.view.GameFrame;
import gradle.view.charecretsView.EpsilonView;
import gradle.view.charecretsView.View;

public class EpsilonModel extends Entity {
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
        anchor = new Point2D.Double(GameFrame.getINSTANCE().getWidth() / 2,GameFrame.getINSTANCE().getHeight() / 2);
        w = Constants.EPSILON_DIAMETER;
        h = Constants.EPSILON_DIAMETER;
        max_speed = Constants.MOVE_SPEED + GameSettings.sensitivity * 2 / 10.0;
        isMoving = false;
        HP = 100;
        XP = 250;
        impact_speed = 1 + GameSettings.sensitivity / 100.0;
    }

    @Override
    public List<Model> getItems() {
        return items;
    }

    @Override
    public List<Model> getRemovedItems() {
        return removedItems;
    }

}
