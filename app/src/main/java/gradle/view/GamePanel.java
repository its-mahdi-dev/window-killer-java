package gradle.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.CubicCurve2D;

import javax.swing.*;

import gradle.controller.Constants;

public class GamePanel extends JPanel {
    private static GamePanel INSTANCE;

    private GamePanel() {
        setOpaque(true);
        setBackground(Color.gray);
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setSize(Constants.PANEL_SIZE);
        setLocationToCenter(GameFrame.getINSTANCE());
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
    }
}
