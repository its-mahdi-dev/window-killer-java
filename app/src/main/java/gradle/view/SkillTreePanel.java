package gradle.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import gradle.controller.Constants;

public class SkillTreePanel extends JPanel {
    Timer timer;
    JPanel contentPanel;
    GridBagConstraints gbc;

    public SkillTreePanel() throws HeadlessException {
        setOpaque(true);
        setBackground(Color.BLACK);
        setBorder(BorderFactory.createCompoundBorder(new LineBorder(Color.CYAN),
                BorderFactory.createEmptyBorder(2, 2, 2, 2)));

        setSize(new Dimension(MainPanel.getINSTANCE().getWidth() - 70, MainPanel.getINSTANCE().getHeight()));
        setLocation(-getWidth(), 0);
        setFocusable(false);
        // setVisible(false);
        contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.BLACK);
        gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridy = 1;

        addItems();
        setLayout(new BorderLayout());

        add(contentPanel, BorderLayout.CENTER);
        MainPanel.getINSTANCE().add(this);
    }

    public void showPanel(boolean open) {
        MainPanel.getINSTANCE().removeItems();
        timer = new Timer(7, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (getX() < -10) {
                    int change = open ? 20 : -20;
                    setLocation(getX() + change, getY());
                    for (JButton button : MainPanel.buttonMap.values()) {
                        MainPanel.getINSTANCE().remove(button);
                    }
                }

            }
        });
        timer.start();
    }

    private void addItems() {
        gbc.insets = new Insets(10, 5, 20, 5);
        gbc.gridx = 0;
        gbc.gridy++;

        JPanel aresPanel = new JPanel();
        aresPanel.setBackground(new Color(0, 0, 0, 0));
        aresPanel.setLayout(new BorderLayout());

        ImageIcon icon = new ImageIcon("app/src/main/java/gradle/assets/icons/ares.png");
        JLabel imageLabel = new JLabel(icon);
        aresPanel.add(imageLabel, BorderLayout.WEST);

        // Create flex column panel
        JPanel flexColumnPanel = new JPanel();
        flexColumnPanel.setBackground(new Color(0, 0, 0, 0));
        flexColumnPanel.setLayout(new BoxLayout(flexColumnPanel, BoxLayout.Y_AXIS));
        flexColumnPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        JLabel label = new JLabel("Writ of Ares");
        label.setForeground(Color.white);
        label.setFont(new Font("Raleway ExtraBold", Font.BOLD, 20));
        flexColumnPanel.add(label);
        JLabel descriptionTextArea = new JLabel("Epsilon's attacks deal two more damage to the enemy.");
        descriptionTextArea.setForeground(Color.white);
        flexColumnPanel.add(descriptionTextArea);

        JLabel XPLabel = new JLabel("750 XP");
        XPLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        XPLabel.setForeground(Color.white);
        XPLabel.setFont(new Font("Consolas", Font.PLAIN, 17));
        flexColumnPanel.add(XPLabel);

        // Create button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(0, 0, 0, 0));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JButton button = new JButton("Buy");
        button.setBackground(Color.yellow);
        button.setPreferredSize(new Dimension(100, 30));
        button.setBorder(new LineBorder(Color.RED, 2));
        button.setForeground(Color.black);
        button.setFont(new Font("Raleway ExtraBold", Font.BOLD, 14));
        buttonPanel.add(button);

        // Add components to row panel
        aresPanel.add(flexColumnPanel, BorderLayout.CENTER);
        aresPanel.add(buttonPanel, BorderLayout.EAST);

        contentPanel.add(aresPanel, gbc);

    }
}
