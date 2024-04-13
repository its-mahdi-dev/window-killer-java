package gradle.model;

import java.awt.geom.Point2D;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.Timer;
import java.util.List;

import gradle.controller.Constants;

public abstract class Model {
    private String Id;
    public Point2D anchor;
    public int w;
    public int h;
    public double[] xPoints;
    public double[] yPoints;
    Timer timer;
    public boolean isMoving = true;

    public Point2D direction = new Point2D.Double(0, 0);

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
        this.direction = direction;
    }

    public void move(Point2D direction, double speed) {
        anchor = new Point2D.Double(anchor.getX() + direction.getX() * speed, anchor.getY() + direction.getY() * speed);
    }

    public void move() {
        if (timer == null) {
            System.out.println("new timer");
            timer = new Timer(50, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (speed <= max_speed && speed >= 0) {
                        if (isMoving)
                            speed += velocity;
                        else
                            speed -= velocity;

                        if (speed < 0)
                            speed = 0;
                        if (speed > max_speed)
                            speed = max_speed;
                        // System.out.println("speed: " + speed);
                        // System.out.println("max speed: " + max_speed);
                        // System.out.println("velocity: " + velocity);
                    }
                }
            });
            timer.start();
        }
        move(direction, speed);
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

    protected abstract List<Model> getItems();

    protected abstract List<Model> getRemovedItems();
}
