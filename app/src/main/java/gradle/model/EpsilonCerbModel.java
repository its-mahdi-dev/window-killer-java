package gradle.model;

import java.util.ArrayList;
import java.util.List;

import gradle.controller.Constants;
import gradle.controller.MouseController;
import gradle.view.charecretsView.EpsilonCerbView;
import gradle.view.charecretsView.EpsilonVertexView;

public class EpsilonCerbModel extends Model {
    public static final List<Model> items = new ArrayList<>();
    public static final List<Model> removedItems = new ArrayList<>();

    public static double max_angle;

    public EpsilonCerbModel() {

    }

    public static EpsilonCerbModel create() {
        EpsilonCerbModel epsilonCerbModel = new EpsilonCerbModel();
        EpsilonCerbView epsilonCerbView = new EpsilonCerbView(epsilonCerbModel.getId());

        epsilonCerbModel.h = Constants.EPSILON_CERB_DIAMETER;
        epsilonCerbModel.w = Constants.EPSILON_CERB_DIAMETER;

        epsilonCerbModel.anchor = EpsilonModel.getINSTANCE().anchor;
        double angle = 0;
        if (items.size() == 0) {
            double dy = EpsilonModel.getINSTANCE().anchor.getY() - MouseController.mousePos
                    .getY();
            double dx = EpsilonModel.getINSTANCE().anchor.getX() - MouseController.mousePos
                    .getX();
            angle = Math.atan2(dy, dx);
        } else {
            angle = max_angle + 120;
        }
        max_angle = angle;
        epsilonCerbModel.angle = angle;
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

}
