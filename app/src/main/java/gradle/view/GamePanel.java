package gradle.view;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.geom.Point2D;
import gradle.controller.Constants;
import gradle.controller.KeyController;
import gradle.controller.MouseController;
import gradle.view.charecretsView.CollectibleView;
import gradle.view.charecretsView.EnemyView;
import gradle.view.charecretsView.EpsilonView;
import gradle.view.charecretsView.NavbarView;
import gradle.view.charecretsView.ShotView;
import gradle.view.charecretsView.View;

public class GamePanel extends JPanel {
    private static GamePanel INSTANCE;
    public Point2D location = new Point2D.Double(0, 0);
    public Point2D size = new Point2D.Double(0, 0);
    public double speed;
    Timer timer;
    boolean isChanging;
    int changeCounter;
    double changingTime;
    double velocity = Constants.CHANGE_FRAME_SPEED / Constants.ACCELERATION;

    private GamePanel() {
        setOpaque(true);
        setBackground(new Color(0, 0, 0, 210));
        setBorder(BorderFactory.createCompoundBorder(new LineBorder(Color.cyan),
                BorderFactory.createEmptyBorder(2, 2, 2, 2)));
        setSize(Constants.GAME_FRAME_DIMENSION);
        setLocationToCenter(GameFrame.getINSTANCE());
        setFocusable(true);
        this.addKeyListener(new KeyController());
        setLayout(null);
        GameFrame.getINSTANCE().add(this);
    }

    public void setLocationToCenter(GameFrame gameFrame) {
        setLocation(gameFrame.getWidth() / 2 - getWidth() / 2, gameFrame.getHeight() / 2 - getHeight() / 2);
    }

    public static GamePanel getINSTANCE() {
        if (INSTANCE == null)
            INSTANCE = new GamePanel();
        return INSTANCE;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (View epsilonView : EpsilonView.items) {
            epsilonView.draw(g);
        }
        for (View shotView : ShotView.items) {
            shotView.draw(g);
        }

        for (View enemyView : EnemyView.items) {
            enemyView.draw(g);
        }

        for (View collectibleView : CollectibleView.items) {
            collectibleView.draw(g);
        }

        NavbarView.getINSTANCE().draw(g);
    }

    public void changeSize(Point2D location, Point2D size) {
        // System.out.println(speed + " -> "+ size);
        setSize((int) (getWidth() + (size.getX() * speed)),
                (int) (getHeight() + size.getY() * speed));
        setLocation((int) (getX() + location.getX() * speed),
                (int) (getY() + location.getY() * speed));
    }

    public void changeSize() {
        if (isChanging) {
            speed -= velocity;
        } else {
            if (getWidth() > Constants.PANEL_SIZE.getWidth()
                    && getHeight() > Constants.PANEL_SIZE.getHeight()) {
                speed = Constants.CHANGE_FRAME_SPEED;
                location = new Point2D.Double(1, 1);
                size = new Point2D.Double(-1, -1);
            } else if (getHeight() > Constants.PANEL_SIZE.getHeight()) {
                speed = Constants.CHANGE_FRAME_SPEED;
                location = new Point2D.Double(0, 1);
                size = new Point2D.Double(0, -1);
            } else if (getWidth() > Constants.PANEL_SIZE.getWidth()) {
                speed = Constants.CHANGE_FRAME_SPEED;
                location = new Point2D.Double(1, 0);
                size = new Point2D.Double(-1, 0);
            } else {
                speed = 0;
                location = new Point2D.Double(0, 0);
                size = new Point2D.Double(0, 0);
            }
            // }
            // }
            // });
            // timer.start();
        }

        if (System.currentTimeMillis() - changingTime > 100 && speed <= Constants.CHANGE_FRAME_SPEED && isChanging) {
            if (changeCounter > 0)
                changeCounter--;
        }

        if (changeCounter == 0)
            isChanging = false;

        changeSize(location, size);

    }

    public void setChanging() {
        isChanging = true;
        changingTime = System.currentTimeMillis();
        changeCounter++;
        speed = Constants.CHANGE_FRAME_SPEED * 2.5;
    }

}
