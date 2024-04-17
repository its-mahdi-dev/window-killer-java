package gradle.controller;

import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;

import javax.swing.event.*;;

public class MouseController implements MouseListener, MouseMotionListener {

    public static final Point2D.Double mousePos = new Point2D.Double();

    public MouseController() {

    }

    @Override
    public void mouseClicked(java.awt.event.MouseEvent e) {
        EpsilonController.mousePressed(e);
    }

    @Override
    public void mousePressed(java.awt.event.MouseEvent e) {

    }

    @Override
    public void mouseReleased(java.awt.event.MouseEvent e) {
    }

    @Override
    public void mouseEntered(java.awt.event.MouseEvent e) {
    }

    @Override
    public void mouseExited(java.awt.event.MouseEvent e) {
    }

    @Override
    public void mouseDragged(java.awt.event.MouseEvent e) {
    }

    @Override
    public void mouseMoved(java.awt.event.MouseEvent e) {
        mousePos.setLocation(e.getX(), e.getY());
    }

}
