package gradle.model;

import java.awt.geom.Point2D;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.Timer;
import java.util.List;

import gradle.controller.Constants;
import gradle.controller.Controller;
import gradle.controller.Utils;
import gradle.interfaces.Entity;
import gradle.interfaces.Rotation;
import gradle.view.GamePanel;
import gradle.view.charecretsView.EpsilonView;
import gradle.view.charecretsView.View;

public abstract class Model {
    private String Id;
    public Point2D anchor;
    public int w;
    public int h;
    public double[] xPoints;
    public double[] yPoints;
    Timer timer;
    public boolean isMoving = true;
    public double impact_time;
    public double impact_speed;
    public boolean isImpacting;
    public Point2D direction = new Point2D.Double(0, 0);
    public double angle;
    public int HP;
    public double HP_time;

    public double speed = 0;
    public double max_speed;
    public double velocity;

    public Model() {
        Id = UUID.randomUUID().toString();
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public static Model findModel(String Id, List<? extends Model> items) {
        for (Model item : items) {
            if (item.getId().equals(Id)) {
                return item;
            }
        }
        return null;
    }

    public void addItem(Model item) {
        List<Model> items = getItems();
        if (items != null) {
            items.add(item);
            velocity = max_speed / Constants.ACCELERATION;
        }
    }

    public void setDirection(Point2D direction) {
        if (!isImpacting)
            this.direction = direction;
    }

    public void move(Point2D direction, double speed) {
        anchor = new Point2D.Double(anchor.getX() + direction.getX() * speed, anchor.getY() + direction.getY() * speed);
    }

    public void moveRotaion(double deg) {
        double rotationAngle = Math.toRadians(deg);
        angle += rotationAngle * speed;
        double x = anchor.getX();
        double y = anchor.getY();
        if (xPoints.length == 4) {
            xPoints = new double[] {
                    (x - w / 2 * Math.cos(angle) + h / 2 * Math.sin(angle)),
                    (x + w / 2 * Math.cos(angle) + h / 2 * Math.sin(angle)),
                    (x + w / 2 * Math.cos(angle) - h / 2 * Math.sin(angle)),
                    (x - w / 2 * Math.cos(angle) - h / 2 * Math.sin(angle))
            };
            yPoints = new double[] {
                    (y - w / 2 * Math.sin(angle) - h / 2 * Math.cos(angle)),
                    (y + w / 2 * Math.sin(angle) - h / 2 * Math.cos(angle)),
                    (y + w / 2 * Math.sin(angle) + h / 2 * Math.cos(angle)),
                    (y - w / 2 * Math.sin(angle) + h / 2 * Math.cos(angle))
            };
        } else if (xPoints.length == 3) {
            double d = Math.sqrt(3) / 2 * h;
            xPoints = new double[] {
                    (x + d * Math.cos(angle)),
                    (x + d * Math.cos(angle - Math.PI * 2 / 3)),
                    (x + d * Math.cos(angle + Math.PI * 2 / 3))
            };
            yPoints = new double[] {
                    (y + d * Math.sin(angle)),
                    (y + d * Math.sin(angle - Math.PI * 2 / 3)),
                    (y + d * Math.sin(angle + Math.PI * 2 / 3))
            };
        }
    }

    public void move() {
        if (timer == null) {
            timer = new Timer(50, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (isImpacting) {
                        if (isMoving && speed > max_speed) {
                            speed -= velocity;
                        }
                    } else if (speed <= max_speed && speed >= 0) {
                        if (isMoving)
                            speed += velocity;
                        else
                            speed -= velocity;

                        if (speed < 0)
                            speed = 0;
                        if (speed > max_speed)
                            speed = max_speed;
                    }
                }
            });
            timer.start();
        }
        if (System.currentTimeMillis() - impact_time > 50 && speed <= max_speed) {
            isImpacting = false;
        }

        move(direction, speed);
        if (this instanceof Rotation && isImpacting)
            moveRotaion(speed * 1.5);
    }

    public static void addAnchorToEntities(Point2D point2d) {
        System.out.println("saa");
    }

    public int[] getXpointsInt() {
        int[] points = new int[xPoints.length];
        for (int i = 0; i < xPoints.length; i++) {
            points[i] = (int) xPoints[i];
        }
        return points;
    }

    public int[] getYpointsInt() {
        int[] points = new int[yPoints.length];
        for (int i = 0; i < yPoints.length; i++) {
            points[i] = (int) yPoints[i];
        }
        return points;
    }

    public Map<String, int[]> getPanelPoints() {
        Point2D[] points = new Point2D[xPoints.length];
        int[] newXpoints = new int[xPoints.length];
        int[] newYpoints = new int[xPoints.length];
        for (int i = 0; i < xPoints.length; i++) {
            points[i] = Utils.getRelatedPoint(new Point2D.Double(xPoints[i], yPoints[i]),
                    GamePanel.getINSTANCE());
            newXpoints[i] = (int) points[i].getX();
            newYpoints[i] = (int) points[i].getY();
        }
        return Map.of("xPoints", newXpoints, "yPoints", newYpoints);
    }

    public void setImpact(Point2D point2d, boolean isCollision) {
        setImpact(point2d, max_speed * impact_speed);

        if (isCollision)
            setEnemyImpacts();
    }

    public void setImpact(Point2D point2d) {
        setImpact(point2d, true);

    }

    public void setImpact(Point2D point2d, double speed) {
        if (isMoving)
            direction = new Point2D.Double(point2d.getX() * direction.getX(), point2d.getY() * direction.getY());
        else
            direction = point2d;
        anchor = new Point2D.Double(anchor.getX() + (direction.getX() * 15),
                anchor.getY() + (direction.getY() * 15));
        impact_time = System.currentTimeMillis();
        isImpacting = true;
        this.speed = speed;

    }

    public void setImpact() {
        setImpact(true);
    }

    public void setImpact(boolean isCollision) {
        setImpact(new Point2D.Double(-1, -1), isCollision);
    }

    public Point2D getPanelAnchor() {
        return Utils.getRelatedPoint(anchor, GamePanel.getINSTANCE());
    }

    public void setEnemyImpacts(double max_distance, double increaseSpeed) {
        for (Model newModel : getAllEntities()) {
            double distance = Utils.getDistance(anchor, newModel.anchor);
            if (!newModel.getId().equals(getId()) && distance < max_distance) {
                double newSpeed = newModel.impact_speed * newModel.max_speed
                        * ((max_distance - distance) / max_distance) * increaseSpeed;
                Point2D newDirection = Utils.getDirection(anchor, newModel.anchor);
                if (newDirection.getX() * newModel.direction.getX() <= 0
                        && newDirection.getY() * newModel.direction.getY() <= 0)
                    newModel.setImpact(newDirection, newSpeed);
            }
        }
    }

    public void setEnemyImpacts() {
        setEnemyImpacts(Constants.MAX_DISTANCE_IMPACT, 1);
    }

    public static List<Model> getAllEntities() {

        List<Model> all = new ArrayList<>();

        all.addAll(EnemyModel.items);
        all.add(EpsilonModel.items.get(0));

        return all;
    }

    protected abstract List<Model> getItems();

    protected abstract List<Model> getRemovedItems();
}
