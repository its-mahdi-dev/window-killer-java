package gradle.model;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Timer;

import org.locationtech.jts.geom.Dimension;

import gradle.controller.Constants;
import gradle.view.GameFrame;
import gradle.view.GamePanel;
import gradle.view.Panels;
import gradle.view.charecretsView.BossView;

public abstract class BossModel extends Entity {

    public Map<String,Timer> timers = new HashMap<>();
    public GamePanel panel;
    public double minRadius;
    public double angleMove;
    public double angleChange;

    public void moveRotation(){
        angleMove += angleChange; // Clockwise rotation

        // Calculate the new position using the angleMove and radius
        double x = EpsilonModel.getINSTANCE().anchor.getX() + minRadius * Math.cos(angleMove);
        double y = EpsilonModel.getINSTANCE().anchor.getY() + minRadius * Math.sin(angleMove);

        // Update the anchor position
        anchor = new Point2D.Double(x, y);
    }

}
