package gradle.view;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import gradle.controller.Constants;
import gradle.controller.EpsilonController;
// import gradle.interfaces.KeyListener;
import gradle.view.charecretsView.EpsilonView;
import gradle.view.charecretsView.View;

public class GamePanel extends JPanel {
    private static GamePanel INSTANCE;

    // private KeyListener kl;

    private GamePanel() {
        setOpaque(true);
        setBackground(Color.gray);
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setSize(Constants.PANEL_SIZE);
        setLocationToCenter(GameFrame.getINSTANCE());
        setFocusable(true);
        this.addKeyListener(new EpsilonController());
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
    }

    // public void setKeyListener(KeyListener kl) {
    // // this.kl = kl;
    // }

    public class AL extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            // kl.keyPressed(e);
        }

        public void ketReleased(KeyEvent e) {
        }
    }

}
