package gradle.controller;

import java.awt.Component;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.*;
import gradle.model.EnemyModel;
import gradle.model.EnemyType;
import gradle.model.ShotModel;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Utils {
    public static Point2D getDirection(Point2D point1, Point2D point2) {
        double angle = Math.atan2(point2.getY() - point1.getY(), point2.getX() - point1.getX());
        double xVelocity = Math.cos(angle);
        double yVelocity = Math.sin(angle);
        return new Point2D.Double(xVelocity, yVelocity);
    }

    public static double getDistance(Point2D point1, Point2D point2, Point2D point3) {
        double deltaX = point2.getX() - point1.getX();
        if (deltaX == 0) {
            return Math.abs(point3.getX() - point1.getX());
        } else {
            double m = (point2.getY() - point1.getY()) / deltaX;
            double c = point1.getY() - m * point1.getX();
            double distance = Math.abs(m * point3.getX() - point3.getY() + c) / Math.sqrt(m * m + 1);

            return distance;
        }
    }

    public static double getDistance(Point2D point1, Point2D point2) {
        double distance = Math
                .sqrt(Math.pow(point1.getX() - point2.getX(), 2) + Math.pow(point1.getY() - point2.getY(), 2));
        return distance;
    }

    public static Point2D[] getNearestPoints(double[] xPoints, double[] yPoints, Point2D point) {
        Point2D[] nearestPoints = new Point2D[2];
        double minDistance = 999999999;
        int index1 = 0;
        for (int i = 0; i < xPoints.length; i++) {
            double distance = Math
                    .sqrt(Math.pow(point.getX() - xPoints[i], 2) + Math.pow(point.getY() - yPoints[i], 2));
            if (distance < minDistance) {
                minDistance = distance;
                nearestPoints[0] = new Point2D.Double(xPoints[i], yPoints[i]);
                index1 = i;
            }
        }
        minDistance = 999999999;
        for (int i = 0; i < xPoints.length; i++) {
            double distance = Math
                    .sqrt(Math.pow(point.getX() - xPoints[i], 2) + Math.pow(point.getY() - yPoints[i], 2));
            if (distance < minDistance && i != index1) {
                minDistance = distance;
                nearestPoints[1] = new Point2D.Double(xPoints[i], yPoints[i]);
            }
        }
        return nearestPoints;

    }

    public static boolean isPerpendicular(Point2D point1, Point2D point2, Point2D point3) {
        double deltaX = point2.getX() - point1.getX();
        double deltaY = point2.getY() - point1.getY();
        if (deltaY == 0) {
            if ((point3.getX() > point1.getX() && point3.getX() < point2.getX())
                    || (point3.getX() < point1.getX() && point3.getX() > point2.getX())) {
                return true;
            }
        }
        double m = deltaY / deltaX;
        double k = -1 / m;
        double c1 = point1.getY() - k * point1.getX();
        double c2 = point2.getY() - k * point2.getX();
        double distance1 = Math.abs(k * point3.getX() - point3.getY() + c1) / Math.sqrt(k * k + 1);
        double distance2 = Math.abs(k * point3.getX() - point3.getY() + c2) / Math.sqrt(k * k + 1);
        double distance3 = Math.abs(c1 - c2) / Math.sqrt(k * k + 1);
        if (distance1 + distance2 <= distance3)
            return true;

        return false;
    }

    public static Point2D getRelatedPoint(Point2D point2d, Component component) {
        Point2D panelLocation = component.getLocation();
        return new Point2D.Double(point2d.getX() - panelLocation.getX(), point2d.getY() - panelLocation.getY());
    }

    public static void playMusic(String url, boolean loop) {
        try {
            File soundFile = new File("app/src/main/java/gradle/assets/musics/1/" + url + ".wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            FloatControl control = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            control.setValue(GameSettings.volume);
            if (loop)
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            else
                clip.start();
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException ex) {
            ex.printStackTrace();
        }
    }

}
