package gradle.view;

import javax.swing.*;

import gradle.controller.Constants;
import gradle.controller.KeyController;

import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class MainPanel extends JPanel {
    private static MainPanel INSTANCE;
    static SkillTreePanel skillTreePanel = new SkillTreePanel();
    static final Map<String, JButton> buttonMap = Map.of(
            "start", new JButton("Start"),
            "exit", new JButton("Exit"),
            "settings", new JButton("Settings"),
            "skillTree", new JButton("Skill Tree"));

    static {
        buttonMap.get("skillTree").addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                skillTreePanel.showPanel(true);
            }
        });
    }
    Graphics graphics;

    private MainPanel() {
        setOpaque(true);
        setBackground(new Color(0, 0, 0, 240));
        // setBorder(BorderFactory.createCompoundBorder(new LineBorder(Color.cyan),
        // BorderFactory.createEmptyBorder(2, 2, 2, 2)));
        setSize(Constants.MAIN_PANEL_DIMENSION);
        setLocationToCenter(GameFrame.getINSTANCE());
        setFocusable(true);
        setLayout(null);
        GameFrame.getINSTANCE().add(this);

    }

    public void setLocationToCenter(GameFrame gameFrame) {
        setLocation(gameFrame.getWidth() / 2 - getWidth() / 2, gameFrame.getHeight() / 2 - getHeight() / 2);
    }

    public static MainPanel getINSTANCE() {
        if (INSTANCE == null)
            INSTANCE = new MainPanel();
        return INSTANCE;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Image image = new ImageIcon("app/src/main/java/gradle/assets/images/menu-bg.jpg").getImage();
        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
        showItems();

    }

    protected void showItems() {
        buttonMap.get("start").setBounds(50, 50, 150, 40);
        buttonMap.get("start").setFocusPainted(false);
        buttonMap.get("start").setBackground(new Color(0, 0, 0, 100));
        buttonMap.get("start").setBorder(new LineBorder(Color.GREEN, 2));
        buttonMap.get("start").setForeground(Color.GREEN);
        buttonMap.get("start").setFont(new Font("Raleway ExtraBold", Font.BOLD, 20));

        buttonMap.get("skillTree").setBounds(300, 200, 150, 40);
        buttonMap.get("skillTree").setFocusPainted(false);
        buttonMap.get("skillTree").setBackground(new Color(0, 0, 0, 100));
        buttonMap.get("skillTree").setBorder(new LineBorder(Color.CYAN, 2));
        buttonMap.get("skillTree").setForeground(Color.CYAN);
        buttonMap.get("skillTree").setFont(new Font("Raleway ExtraBold", Font.BOLD, 20));
        for (Map.Entry<String, JButton> entry : buttonMap.entrySet()) {

            JButton button = entry.getValue();
            add(button);
        }
        repaint();
    }

    protected void removeItems() {
        for (JButton button : buttonMap.values()) {
            remove(button);
        }
        repaint();
    }
}
