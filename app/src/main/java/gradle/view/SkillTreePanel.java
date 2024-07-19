package gradle.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.LineBorder;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

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

        setLayout(new BorderLayout());

        // Back button
        JButton backButton = new JButton("back");
        backButton.setForeground(Color.white);
        backButton.setBackground(new Color(0, 0, 0, 0));
        backButton.setBorder(new LineBorder(Color.white, 2));
        backButton.setPreferredSize(new Dimension(70, 30));
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showPanel(false);
            }
        });

        // Panel for back button at the top
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
        topPanel.setBackground(Color.BLACK);
        topPanel.add(Box.createHorizontalGlue());
        topPanel.add(backButton);
        topPanel.add(Box.createHorizontalGlue());

        // Content panel
        contentPanel = new JPanel(new GridLayout(3, 3));
        contentPanel.setBackground(Color.BLACK);
        contentPanel.setPreferredSize(new Dimension(getWidth() - 100, getHeight() - 150)); // Adjust dimensions as
                                                                                           // needed

        addItems2();

        // Main layout
        add(topPanel, BorderLayout.NORTH);
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

    private void addItems2() {
        for (int i = 0; i < skills.size(); i++) {
            JSONObject skillObject = (JSONObject) skills.get(i);
            JPanel mypanel = new JPanel();
            mypanel.setBackground(new Color(0, 0, 0, 0));
            mypanel.setLayout(new BoxLayout(mypanel, BoxLayout.Y_AXIS));
            mypanel.setBorder(BorderFactory.createLineBorder(Color.WHITE));

            mypanel.setAlignmentX(Component.CENTER_ALIGNMENT);
            mypanel.setAlignmentY(Component.CENTER_ALIGNMENT);

            JLabel imageLabel = new JLabel(
                    new ImageIcon("app/src/main/java/gradle/assets/icons/" + skillObject.get("image").toString()));
            imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel xpLabel = new JLabel(skillObject.get("XP").toString() + " XP");
            xpLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            xpLabel.setForeground(Color.green);
            xpLabel.setFont(new Font("Consolas", Font.PLAIN, 11));

            JLabel nameLabel = new JLabel(skillObject.get("name").toString());
            nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            nameLabel.setForeground(Color.white);

            nameLabel.setFont(new Font("Raleway ExtraBold", Font.BOLD, 16));

            boolean enabled = Boolean.parseBoolean(skillObject.get("enabled").toString());
            Component buy = null;
            if (enabled) {
                buy = new JLabel("purchased");
                JLabel buyLabel = (JLabel) buy;
                buyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                buyLabel.setForeground(Color.white);
                buyLabel.setFont(new Font("Raleway ExtraBold", Font.BOLD, 14));
                buyLabel.setForeground(Color.green);
            } else {
                buy = new JButton("buy");
                JButton buyButton = (JButton) buy;
                buyButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                buyButton.setBackground(Color.yellow);
                buyButton.setPreferredSize(new Dimension(100, 30));
                buyButton.setBorder(new LineBorder(Color.RED, 2));
                buyButton.setForeground(Color.black);
                buyButton.setFont(new Font("Raleway ExtraBold", Font.BOLD, 14));
                buyButton.addActionListener(new ActionListener() {
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
                                message = "greate , you have now " + skillObject.get("name").toString();
                            } else
                                message = "you don't have enough XP";
                        }
                        JOptionPane.showMessageDialog(null, message);
                    }
                });
            }

            mypanel.add(Box.createVerticalGlue());
            mypanel.add(imageLabel);
            mypanel.add(Box.createVerticalStrut(10)); // Space between image and XP label
            mypanel.add(xpLabel);
            mypanel.add(Box.createVerticalStrut(5)); // Space between XP label and name label
            mypanel.add(nameLabel);
            mypanel.add(Box.createVerticalStrut(10)); // Space between name label and button
            mypanel.add(buy);

            mypanel.add(Box.createVerticalGlue());

            contentPanel.add(mypanel);
        }
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
        add(backButton, BorderLayout.WEST);
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
            imageLabel.setSize(new Dimension(50, 50));
            // aresPanel.add(imageLabel, BorderLayout.WEST);

            // Create flex column panel
            JPanel flexColumnPanel = new JPanel();
            flexColumnPanel.setBackground(new Color(0, 0, 0, 0));
            flexColumnPanel.setLayout(new BoxLayout(flexColumnPanel, BoxLayout.Y_AXIS));
            flexColumnPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
            JLabel label = new JLabel(skillObject.get("name").toString());
            label.setForeground(Color.white);
            label.setFont(new Font("Raleway ExtraBold", Font.BOLD, 14));
            flexColumnPanel.add(label);
            JLabel descriptionTextArea = new JLabel(skillObject.get("description").toString());
            descriptionTextArea.setForeground(Color.white);
            flexColumnPanel.add(descriptionTextArea);

            JLabel XPLabel = new JLabel(skillObject.get("XP").toString() + " XP");
            XPLabel.setBorder(BorderFactory.createEmptyBorder(3, 0, 0, 0));
            XPLabel.setForeground(Color.green);
            XPLabel.setFont(new Font("Consolas", Font.PLAIN, 11));
            flexColumnPanel.add(XPLabel);

            // Create button panel
            JPanel buttonPanel = new JPanel();
            buttonPanel.setBackground(new Color(0, 0, 0, 0));
            buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
            boolean enabled = Boolean.parseBoolean(skillObject.get("enabled").toString());
            JButton button = new JButton(enabled ? "Purchased" : "Buy");
            button.setBackground(enabled ? Color.green : Color.red);
            button.setPreferredSize(new Dimension(70, 15));
            button.setBorder(new LineBorder(Color.RED, 2));
            button.setForeground(Color.black);
            button.setFont(new Font("Raleway ExtraBold", Font.BOLD, 9));
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
