package gradle.view.charecretsView;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.Point2D;
import java.net.URL;

import javax.swing.ImageIcon;

import gradle.controller.Constants;
import gradle.controller.MouseController;
import gradle.controller.Utils;
import gradle.model.EpsilonModel;
import gradle.view.GamePanel;

public class NavbarView {

    private int XP;
    private int HP;
    private static NavbarView INSTANCE;

    private double mouseMovedTime;

    private NavbarView() {

    }

    public static NavbarView getINSTANCE() {
        if (INSTANCE == null)
            INSTANCE = new NavbarView();
        return INSTANCE;
    }

    public void drawNavbar(Graphics g) {

        g.setFont(new Font("Consolas", Font.PLAIN, 20));
        g.setColor(new Color(0, 255, 255, 70));
        g.fillRect(0, GamePanel.getINSTANCE().getHeight() - Constants.NAVBAR_HEIGHT, GamePanel.getINSTANCE().getWidth(),
                Constants.NAVBAR_HEIGHT);

        g.setColor(Color.white);
        // g.setFont(new Font("Console", Font.CENTER_BASELINE, 18));

        FontMetrics fm = g.getFontMetrics();
        int textWidth = 10;
        String HP_text = "HP: ";
        g.drawString(HP_text, textWidth, GamePanel.getINSTANCE().getHeight() - Constants.NAVBAR_HEIGHT / 2);

        textWidth += fm.stringWidth(HP_text) + 10;
        g.setColor(Color.red);
        g.drawRect(textWidth,
                GamePanel.getINSTANCE().getHeight() - Constants.NAVBAR_HEIGHT + Constants.NAVBAR_HEIGHT / 4, 100,
                Constants.NAVBAR_HEIGHT / 4);
        g.fillRect(textWidth,
                GamePanel.getINSTANCE().getHeight() - Constants.NAVBAR_HEIGHT + Constants.NAVBAR_HEIGHT / 4, HP,
                Constants.NAVBAR_HEIGHT / 4);
        g.setColor(Color.white);
        g.setFont(new Font("Consolas", Font.PLAIN, 14));
        Image healthIcon = new ImageIcon("app/src/main/java/gradle/assets/health.png").getImage();
        g.drawImage(healthIcon, HP + textWidth - 13, GamePanel.getINSTANCE().getHeight() -
                Constants.NAVBAR_HEIGHT, 26, 26,
                null);

        g.setFont(new Font("Consolas", Font.PLAIN, 12));
        int HP_width = fm.stringWidth(String.valueOf(HP));
        g.drawString(String.valueOf(HP), HP + textWidth - HP_width / 2 + 2,
                GamePanel.getINSTANCE().getHeight() - Constants.NAVBAR_HEIGHT  + 13);
        textWidth += 130;
        g.setFont(new Font("Consolas", Font.PLAIN, 20));
        g.setColor(Color.GREEN);
        String XP_text = "XP: " + String.valueOf(XP);
        g.drawString(XP_text, textWidth + 20, GamePanel.getINSTANCE().getHeight() -
                Constants.NAVBAR_HEIGHT / 2);
        textWidth += fm.stringWidth(XP_text);
    }

    public void draw(Graphics g) {
        Point2D mousePoint = Utils.getRelatedPoint(MouseController.mousePos, GamePanel.getINSTANCE());
        if (GamePanel.getINSTANCE().getHeight() - mousePoint.getY() < Constants.NAVBAR_HEIGHT) {
            drawNavbar(g);
            mouseMovedTime = System.currentTimeMillis();
        } else if (System.currentTimeMillis() - mouseMovedTime < 2000)
            drawNavbar(g);
    }

    public void setUtil() {
        EpsilonModel epsilonModel = (EpsilonModel) EpsilonModel.items.get(0);
        HP = epsilonModel.HP;
        XP = epsilonModel.XP;
    }
}
