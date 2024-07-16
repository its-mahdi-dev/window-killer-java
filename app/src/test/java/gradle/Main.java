package gradle;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;

class Circle {
    Point2D center;
    int radius;

    public Circle(Point2D center, int radius) {
        this.center = center;
        this.radius = radius;
    }

    public Point2D getCenter() {
        return center;
    }

    public int getRadius() {
        return radius;
    }
}

class DrawingPanel extends JPanel {
    private Circle circle1;
    private Circle circle2;

    public DrawingPanel(Circle circle1, Circle circle2) {
        this.circle1 = circle1;
        this.circle2 = circle2;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Draw circles
        g2d.setColor(Color.BLUE);
        drawCircle(g2d, circle1);
        drawCircle(g2d, circle2);

        // Draw thin rectangle (line)
        g2d.setColor(Color.RED);
        drawThinRectangleBetweenCircles(g2d, circle1, circle2);
    }

    private void drawCircle(Graphics2D g2d, Circle circle) {
        int x = (int) (circle.getCenter().getX() - circle.getRadius());
        int y = (int) (circle.getCenter().getY() - circle.getRadius());
        int diameter = circle.getRadius() * 2;
        g2d.fillOval(x, y, diameter, diameter);
    }

    private void drawThinRectangleBetweenCircles(Graphics2D g2d, Circle circle1, Circle circle2) {
        Point2D center1 = circle1.getCenter();
        Point2D center2 = circle2.getCenter();

        // Calculate the direction vector from center1 to center2
        double dx = center2.getX() - center1.getX();
        double dy = center2.getY() - center1.getY();
        double length = Math.sqrt(dx * dx + dy * dy);

        // Normalize the direction vector
        double ux = dx / length;
        double uy = dy / length;

        // Calculate the perpendicular vector
        double px = -uy;
        double py = ux;

        // Rectangle height (width of the thin rectangle)
        double height = 10.0;

        // Calculate the four corners of the rectangle
        int[] xpoints = new int[4];
        int[] ypoints = new int[4];

        xpoints[0] = (int) (center1.getX() + height * px / 2);
        ypoints[0] = (int) (center1.getY() + height * py / 2);

        xpoints[1] = (int) (center1.getX() - height * px / 2);
        ypoints[1] = (int) (center1.getY() - height * py / 2);

        xpoints[2] = (int) (center2.getX() - height * px / 2);
        ypoints[2] = (int) (center2.getY() - height * py / 2);

        xpoints[3] = (int) (center2.getX() + height * px / 2);
        ypoints[3] = (int) (center2.getY() + height * py / 2);

        // Draw the polygon (rectangle)
        g2d.fillPolygon(xpoints, ypoints, 4);
    }
}

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Create circles
            Circle circle1 = new Circle(new Point2D.Double(100, 100), 40);
            Circle circle2 = new Circle(new Point2D.Double(300, 200), 40);

            // Create the panel
            DrawingPanel drawingPanel = new DrawingPanel(circle1, circle2);

            // Set up the frame
            JFrame frame = new JFrame("Circles and Thin Rectangle");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(drawingPanel);
            frame.setSize(400, 300);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}

