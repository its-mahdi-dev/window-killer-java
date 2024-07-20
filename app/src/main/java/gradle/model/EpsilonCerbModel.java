package gradle.model;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import gradle.controller.Constants;
import gradle.controller.MouseController;
import gradle.view.charecretsView.EpsilonCerbView;
import gradle.view.charecretsView.EpsilonVertexView;

public class EpsilonCerbModel extends Model {
    public static final List<Model> items = new ArrayList<>();
    public static final List<Model> removedItems = new ArrayList<>();
    public long lastAttack;
    public static double max_angle;

    public EpsilonCerbModel() {

    }

    public static EpsilonCerbModel create() {
        EpsilonCerbModel epsilonCerbModel = new EpsilonCerbModel();
        EpsilonCerbView epsilonCerbView = new EpsilonCerbView(epsilonCerbModel.getId());

        epsilonCerbModel.h = Constants.EPSILON_CERB_DIAMETER;
        epsilonCerbModel.w = Constants.EPSILON_CERB_DIAMETER;
        max_angle += 90;
        if (EpsilonCerbModel.items.size() == 3)
            max_angle += 20;
        epsilonCerbModel.angle = max_angle;
        epsilonCerbModel.lastAttack = System.currentTimeMillis() - 20000;
        epsilonCerbModel.setRealAnchor();
        epsilonCerbView.addItem(epsilonCerbView);
        epsilonCerbModel.addItem(epsilonCerbModel);
        epsilonCerbView.setUtil(epsilonCerbModel);
        return epsilonCerbModel;
    }

    @Override
    public List<Model> getItems() {
        return items;
    }

    @Override
    public List<Model> getRemovedItems() {
        return removedItems;
    }

    public static Model findById(String Id) {
        return Model.findModel(Id, items);
    }

    public void setRealAnchor() {
        EpsilonModel epsilonModel = EpsilonModel.getINSTANCE();
        double x = epsilonModel.anchor.getX()
                + (epsilonModel.w / 2 + w / 2) * Math.cos(angle);
        double y = epsilonModel.anchor.getY()
                + (epsilonModel.w / 2 + w / 2) * Math.sin(angle);

        anchor = new Point2D.Double(x, y);
    }

}
