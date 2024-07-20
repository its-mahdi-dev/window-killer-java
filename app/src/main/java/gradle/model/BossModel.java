package gradle.model;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Timer;

import org.locationtech.jts.geom.Dimension;

import gradle.controller.BossController;
import gradle.controller.Constants;
import gradle.controller.Utils;
import gradle.view.GameFrame;
import gradle.view.GamePanel;
import gradle.view.Panels;
import gradle.view.charecretsView.SmileyView;

public abstract class BossModel extends Entity {

    public GamePanel panel;
    public double minRadius;
    public double angleMove;
    public double angleChange;
    public Point2D firstAnchor;
    public boolean isInFirstAnchor;
    public boolean ableDecrease;

    public void setAngleMove() {
        Point2D center = EpsilonModel.getINSTANCE().anchor;
        double deltaX = anchor.getX() - center.getX();
        double deltaY = anchor.getY() - center.getY();

        // Calculate the angle between the original and new anchor points
        angleMove = Math.atan2(deltaY, deltaX);
    }

    public void moveRotation() {
        angleMove += angleChange; // Clockwise rotation

        // Calculate the new position using the angleMove and radius
        double x = EpsilonModel.getINSTANCE().anchor.getX() + minRadius * Math.cos(angleMove);
        double y = EpsilonModel.getINSTANCE().anchor.getY() + minRadius * Math.sin(angleMove);

        // Update the anchor position
        anchor = new Point2D.Double(x, y);
    }

    public static List<Model> getAllBossEntities() {
        List<Model> entities = new ArrayList<>();
        entities.addAll(SmileyHandsModel.items);
        entities.add(SmileyModel.getINSTANCE());
        if (BossController.isFist)
            entities.add(SmileyFistModel.getINSTANCE());

        return entities;
    }

    public void goToFirstAnchor() {
        Point2D newDirection = new Point2D.Double(0, 0);
        if (Utils.getDistance(anchor, firstAnchor) <= 2)
            isInFirstAnchor = true;
        else
            isInFirstAnchor = false;
        if (!isInFirstAnchor) {
            newDirection = Utils.getDirection(anchor, firstAnchor);
        }

        setDirection(newDirection);
    }

}
