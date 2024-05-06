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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.google.common.primitives.Booleans;

import gradle.controller.Constants;
import gradle.controller.JsonHelper;
import gradle.controller.SkillTreeController;

public class SkillTreePanel extends JPanel {
    Timer timer;
    JPanel contentPanel;
    GridBagConstraints gbc;
    JSONObject skillsData = JsonHelper.readJsonFromFile("app/src/main/resources/data/skillTree.json");
    JSONArray skills = (JSONArray) skillsData.get("skills");

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
        if (open)
            MainPanel.getINSTANCE().removeItems();
        else
            MainPanel.getINSTANCE().showItems();
        if (timer != null)
            timer.stop();
        timer = new Timer(7, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int change = open ? 20 : -20;
                if ((open && getX() < -10) || (!open && getX() > -getWidth()))
                    setLocation(getX() + change, getY());

            }
        });
        timer.start();
    }

    private void addItems() {
        JButton backButton = new JButton("back");
        backButton.setForeground(Color.white);
        backButton.setBackground(new Color(0, 0, 0, 0));
        backButton.setBorder(new LineBorder(Color.white, 2));
        backButton.setBounds(20, 10, 70, 30);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showPanel(false);
            }
        });
        add(backButton , BorderLayout.WEST);
        for (int i = 0; i < skills.size(); i++) {
            JSONObject skillObject = (JSONObject) skills.get(i);
            gbc.insets = new Insets(10, 5, 20, 5);
            gbc.gridx = 0;
            gbc.gridy++;

            JPanel aresPanel = new JPanel();
            aresPanel.setBackground(new Color(0, 0, 0, 0));
            aresPanel.setLayout(new BorderLayout());

            String imagePath = "app/src/main/java/gradle/assets/icons/" + skillObject.get("image").toString();
            ImageIcon icon = new ImageIcon(imagePath);
            JLabel imageLabel = new JLabel(icon);
            aresPanel.add(imageLabel, BorderLayout.WEST);

            // Create flex column panel
            JPanel flexColumnPanel = new JPanel();
            flexColumnPanel.setBackground(new Color(0, 0, 0, 0));
            flexColumnPanel.setLayout(new BoxLayout(flexColumnPanel, BoxLayout.Y_AXIS));
            flexColumnPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
            JLabel label = new JLabel(skillObject.get("name").toString());
            label.setForeground(Color.white);
            label.setFont(new Font("Raleway ExtraBold", Font.BOLD, 20));
            flexColumnPanel.add(label);
            JLabel descriptionTextArea = new JLabel(skillObject.get("description").toString());
            descriptionTextArea.setForeground(Color.white);
            flexColumnPanel.add(descriptionTextArea);

            JLabel XPLabel = new JLabel(skillObject.get("XP").toString() + " XP");
            XPLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
            XPLabel.setForeground(Color.green);
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
            button.addActionListener(new ActionListener() {
                @SuppressWarnings("unchecked")
                @Override
                public void actionPerformed(ActionEvent e) {

                    String message;
                    boolean enabled = Boolean.parseBoolean(skillObject.get("enabled").toString());
                    if (enabled)
                        message = "Skill Purchased";
                    else {
                        int xp = Integer.parseInt(skillObject.get("XP").toString());
                        boolean xpEnable = SkillTreeController.buySkill(xp);
                        if (xpEnable) {
                            skillObject.put("enabled", true);
                            JsonHelper.writeJsonToFile(skillsData, "app/src/main/resources/data/skillTree.json");
                            message = "greate , you have now " + skillObject.get("XP").toString() + " XP";
                        } else
                            message = "you don't have enough XP";
                    }
                    JOptionPane.showMessageDialog(null, message);
                }
            });
            buttonPanel.add(button);

            // Add components to row panel
            aresPanel.add(flexColumnPanel, BorderLayout.CENTER);
            aresPanel.add(buttonPanel, BorderLayout.EAST);

            contentPanel.add(aresPanel, gbc);
        }

    }

}
