package gradle.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import gradle.controller.Constants;
import gradle.view.charecretsView.EpsilonVertexView;
import gradle.view.charecretsView.ShotView;

public class EpsilonVertexModel extends Model {
    public static final List<Model> items = new ArrayList<>();
    public static final List<Model> removedItems = new ArrayList<>();

    public static double max_angle;

    public EpsilonVertexModel() {

    }

    public static EpsilonVertexModel create() {
        EpsilonVertexModel epsilonVertexModel = new EpsilonVertexModel();
        EpsilonVertexView epsilonVertexView = new EpsilonVertexView(epsilonVertexModel.getId());

        epsilonVertexModel.h = Constants.EPSILON_VERTEX_DIAMETER;
        epsilonVertexModel.w = Constants.EPSILON_VERTEX_DIAMETER;

        epsilonVertexModel.anchor = EpsilonModel.items.get(0).anchor;

        Random rand = new Random();
        double angle = rand.nextDouble() * 2 * Math.PI;
        epsilonVertexModel.angle = 0.0;
        epsilonVertexView.addItem(epsilonVertexView);
        epsilonVertexModel.addItem(epsilonVertexModel);
        epsilonVertexView.setUtil(epsilonVertexModel);

        return epsilonVertexModel;
    }

    @Override
    protected List<Model> getItems() {
        return items;
    }

    @Override
    protected List<Model> getRemovedItems() {
        return removedItems;
    }

    public static Model findById(String Id) {
        return Model.findModel(Id, items);
    }

}
