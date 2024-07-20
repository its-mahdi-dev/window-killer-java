package gradle.view;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.geom.Point2D;
import java.util.UUID;

import gradle.controller.Constants;
import gradle.controller.KeyController;
import gradle.model.EpsilonModel;
import gradle.threads.GamePanelThread;
import gradle.view.charecretsView.SmileyHandsView;
import gradle.view.charecretsView.SmileyView;
import gradle.view.charecretsView.CollectibleView;
import gradle.view.charecretsView.EnemyView;
import gradle.view.charecretsView.EpsilonCerbView;
import gradle.view.charecretsView.EpsilonVertexView;
import gradle.view.charecretsView.EpsilonView;
import gradle.view.charecretsView.NavbarView;
import gradle.view.charecretsView.ShotView;
import gradle.view.charecretsView.SmileyFistView;
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
    public boolean rigid;
    public boolean isometric;
    double velocity = Constants.CHANGE_FRAME_SPEED / Constants.ACCELERATION;
    public GamePanelThread panelThread;
    private String Id;

    public Dimension availableDimension = Constants.PANEL_SIZE;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public GamePanel() {
        setOpaque(true);
        setBackground(new Color(0, 0, 0, 255));
        setBorder(BorderFactory.createCompoundBorder(new LineBorder(Color.cyan),
                BorderFactory.createEmptyBorder(2, 2, 2, 2)));
        setSize(Constants.PANEL_SIZE);
        setLocationToCenter(GameFrame.getINSTANCE());
        setFocusable(true);
        setLayout(null);
        Id = UUID.randomUUID().toString();
    }

    public void setLocationToCenter(GameFrame gameFrame) {
        setLocation(gameFrame.getWidth() / 2 - getWidth() / 2, gameFrame.getHeight() / 2 - getHeight() / 2);
    }

    // public static GamePanel getINSTANCE() {
    // if (INSTANCE == null)
    // INSTANCE = new GamePanel();
    // return INSTANCE;
    // }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (View epsilonView : EpsilonView.items) {
            epsilonView.draw(g, this);
        }
        for (View epsilonView : EpsilonVertexView.items) {
            epsilonView.draw(g, this);
        }
        for (View epsilonView : EpsilonCerbView.items) {
            epsilonView.draw(g, this);
        }
        for (View shotView : ShotView.items) {
            shotView.draw(g, this);
        }

        for (View enemyView : EnemyView.getEnemyViews()) {
            if (enemyView.visible)
                enemyView.draw(g, this);
        }

        for (View collectibleView : CollectibleView.items) {
            collectibleView.draw(g, this);
        }

        if (EpsilonModel.getINSTANCE().currentPanels.get(0).equals(this))
            NavbarView.getINSTANCE().draw(g);

        if (SmileyView.items.size() > 0)
            SmileyView.items.get(0).draw(g, this);
        for (View bossHandsView : SmileyHandsView.items) {
            bossHandsView.draw(g, this);
        }
        if (SmileyFistView.items.size() > 0)
            SmileyFistView.items.get(0).draw(g, this);
    }

    public void showMsg(String msg) {
        JOptionPane.showMessageDialog(null, msg);
    }

    public void changeSize(Point2D location, Point2D size) {
        Dimension newSize = new Dimension((int) (getWidth() + (size.getX() * speed)),
                (int) (getHeight() + size.getY() * speed));
        Point newLocation = new Point((int) (getX() + location.getX() * speed),
                (int) (getY() + location.getY() * speed));
        for (GamePanel gamePanel : Panels.getINSTANCE().getRigidPanels()) {
            if (isCollision(gamePanel, newSize, newLocation)) {
                // System.out.println("here");
                return;
            }
        }
        setSize(newSize);
        setLocation(newLocation);

    }

    public void changeSize() {
        if (isChanging) {
            speed -= velocity;
        } else {
            if (getWidth() > availableDimension.getWidth()
                    && getHeight() > availableDimension.getHeight()) {
                speed = Constants.CHANGE_FRAME_SPEED;
                location = new Point2D.Double(1, 1);
                size = new Point2D.Double(-1, -1);
            } else if (getHeight() > availableDimension.getHeight()) {
                speed = Constants.CHANGE_FRAME_SPEED;
                location = new Point2D.Double(0, 1);
                size = new Point2D.Double(0, -1);
            } else if (getWidth() > availableDimension.getWidth()) {
                speed = Constants.CHANGE_FRAME_SPEED;
                location = new Point2D.Double(1, 0);
                size = new Point2D.Double(-1, 0);
            } else if (getWidth() < availableDimension.getWidth()) {
                speed = Constants.CHANGE_FRAME_SPEED;
                location = new Point2D.Double(1, 0);
                size = new Point2D.Double(+1, 0);
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

    private boolean isCollision(GamePanel panel1, Dimension newSize, Point newLocation) {
        boolean containsY = false;
        boolean containsX = false;
        if (panel1.getX() < newLocation.getX() + newSize.getWidth() &&
                panel1.getX() + panel1.getWidth() > newLocation.getX())
            containsX = true;
        if (panel1.getY() < newLocation.getY() + newSize.getHeight() &&
                panel1.getY() + panel1.getHeight() > newLocation.getY())
            containsY = true;
        if (containsX && containsY)
            return true;
        return false;
    }

}
