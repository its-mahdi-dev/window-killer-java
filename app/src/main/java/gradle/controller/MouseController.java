package gradle.controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import gradle.model.EpsilonModel;
import gradle.view.GameFrame;

public class MouseController implements MouseListener, MouseMotionListener {

    public static final Point2D.Double mousePos = new Point2D.Double();

    public MouseController() {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        System.out.println(GameSettings.isStore);
        if (GameSettings.massedUp) {
            Random random = new Random();
            int randomX = random.nextInt(GameFrame.getINSTANCE().getWidth());
            int randomY = random.nextInt(GameFrame.getINSTANCE().getHeight());

            MouseEvent randomMouseEvent = new MouseEvent(
                    GameFrame.getINSTANCE(), // source component
                    MouseEvent.MOUSE_CLICKED, // event type
                    System.currentTimeMillis(), // when event occurred
                    MouseEvent.BUTTON1_DOWN_MASK, // mouse button mask
                    randomX, // x-coordinate
                    randomY, // y-coordinate
                    1, // number of clicks
                    false // whether the event is a popup trigger
            );
            EpsilonController.mousePressed(randomMouseEvent);
        } else if (!GameSettings.isPause && GameSettings.isGameRun)
            EpsilonController.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (!GameSettings.isPause && GameSettings.isGameRun) {
            mousePos.setLocation(e.getX(), e.getY());

        }
    }

}
