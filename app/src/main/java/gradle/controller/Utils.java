package gradle.controller;

import java.awt.geom.Point2D;

public class Utils {
    public static Point2D relativeLocation(Point2D point,Point2D anchor){
        return new Point2D.Double(point.getX()-anchor.getX(),point.getY()-anchor.getY());
    }
    public static Point2D multiplyVector(Point2D point,double scalar){
        return new Point2D.Double(point.getX()*scalar,point.getY()*scalar);
    }
    public static Point2D addVectors(Point2D point1,Point2D point2){
        return new Point2D.Double(point1.getX()+point2.getX(),point1.getY()+point2.getY());
    }
    public static Point2D weightedAddVectors(Point2D point1,Point2D point2,double weight1,double weight2){
        return multiplyVector(addVectors(multiplyVector(point1,weight1),multiplyVector(point2,weight2)),1/(weight1+weight2));
    }
}
