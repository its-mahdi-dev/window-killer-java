package gradle.model;

import java.util.*;

import gradle.controller.Constants;
import gradle.view.charecretsView.EpsilonView;

public class EpsilonModel extends Model {
    public static final List<Model> items = new ArrayList<>();
    public EpsilonModel() {
        x = 100;
        y = 100;
        w = Constants.EPSILON_DIAMETER;
        h = Constants.EPSILON_DIAMETER;
        addItem(this);
        EpsilonView.items.add(new EpsilonView());
    }

    @Override
    protected List<Model> getItems() {
        return items;
    }
}
