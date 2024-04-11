package gradle.model;

import java.awt.geom.Point2D;
import java.util.*;

import gradle.controller.Constants;
import gradle.view.charecretsView.EpsilonView;

public class EpsilonModel extends Model {
    public static final List<Model> items = new ArrayList<>();

    public EpsilonModel() {
        anchor = new Point2D.Double(100, 100);
        w = Constants.EPSILON_DIAMETER;
        h = Constants.EPSILON_DIAMETER;
        addItem(this);
        new EpsilonView(getId());
    }

    @Override
    protected List<Model> getItems() {
        return items;
    }
}
