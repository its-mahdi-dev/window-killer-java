package gradle.view;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.LineBorder;

import gradle.controller.Constants;
import gradle.controller.KeyController;
import gradle.controller.MouseController;
import gradle.view.charecretsView.EnemyView;
import gradle.view.charecretsView.EpsilonView;
import gradle.view.charecretsView.ShotView;
import gradle.view.charecretsView.View;

public class GamePanel extends JPanel {
    private static GamePanel INSTANCE;

    private GamePanel() {
        setOpaque(true);
        setBackground(new Color(0, 0, 0, 210));
        setBorder(new LineBorder(Color.yellow));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setSize(Constants.PANEL_SIZE);
        setLocationToCenter(GameFrame.getINSTANCE());
        setFocusable(true);
        this.addKeyListener(new KeyController());

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
    }

}
