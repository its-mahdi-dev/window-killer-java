package gradle.view;

import javax.swing.*;
import javax.swing.border.LineBorder;

import java.awt.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.json.simple.JSONObject;

import gradle.controller.JsonHelper;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingsPanel extends JPanel {
    Timer timer;
    private static SettingsPanel INSTANCE;
    JSONObject data;

    private SettingsPanel() {
        setOpaque(true);
        setBackground(Color.BLACK);
        setBorder(BorderFactory.createCompoundBorder(new LineBorder(Color.CYAN),
                BorderFactory.createEmptyBorder(2, 2, 2, 2)));

        setSize(new Dimension(MainPanel.getINSTANCE().getWidth() - 70, MainPanel.getINSTANCE().getHeight()));
        setLocation(-getWidth(), 0);
        setFocusable(false);
        setData();
        addItems();
        MainPanel.getINSTANCE().add(this);
    }

    public static SettingsPanel getINSTANCE() {
        if (INSTANCE == null)
            INSTANCE = new SettingsPanel();
        return INSTANCE;
    }

    private void setData() {
        data = JsonHelper.readJsonFromFile("app/src/main/resources/data/settings.json");
    }

    public void showPanel(boolean open) {
        setData();
        repaint();

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
        JPanel mainPanel = new JPanel(new GridLayout(7, 1));
        mainPanel.setBorder(BorderFactory.createCompoundBorder(new LineBorder(Color.yellow),
                BorderFactory.createEmptyBorder(60, 60, 60, 60)));
        mainPanel.setPreferredSize(new Dimension(getWidth() - getWidth() / 4, 500));
        mainPanel.setBackground(new Color(0, 0, 0, 0));
        // ComboBox
        JLabel comboBoxLabel = new JLabel("level: " + data.get("level").toString());
        comboBoxLabel.setForeground(Color.white);
        String[] comboBoxItems = { "easy", "medium", "high" };
        JComboBox<String> comboBox = new JComboBox<>(comboBoxItems);
        comboBox.setPreferredSize(new Dimension(100, 20));
        comboBox.setBackground(new Color(0, 0, 0, 0));
        comboBox.setForeground(Color.white);

        // Slider 1
        JLabel volume = new JLabel("volume: " + data.get("volume").toString());
        volume.setForeground(Color.white);
        JSlider slider1 = new JSlider(JSlider.HORIZONTAL, 0, 100, Integer.parseInt(data.get("volume").toString()));
        slider1.setMajorTickSpacing(20);
        slider1.setMinorTickSpacing(5);
        slider1.setPaintTicks(true);
        slider1.setPaintLabels(true);
        slider1.setPreferredSize(new Dimension(200, 50));
        slider1.setBackground(new Color(0, 0, 0, 0));

        // Slider 2
        JLabel sensitivity = new JLabel("sensitivity: " + data.get("sensitivity").toString());
        sensitivity.setForeground(Color.white);
        JSlider slider2 = new JSlider(JSlider.HORIZONTAL, 0, 10, Integer.parseInt(data.get("sensitivity").toString()));
        slider2.setMajorTickSpacing(20);
        slider2.setMinorTickSpacing(5);
        slider2.setPaintTicks(true);
        slider2.setPaintLabels(true);
        slider2.setPreferredSize(new Dimension(150, 20));
        slider2.setBackground(new Color(0, 0, 0, 0));

        JButton submitButton = new JButton("Submit");
        submitButton.setBackground(Color.yellow);
        submitButton.setForeground(Color.black);
        submitButton.setPreferredSize(new Dimension(mainPanel.getWidth() - mainPanel.getWidth() / 4, 40));
        submitButton.addActionListener(new ActionListener() {
            @SuppressWarnings("unchecked")
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedOption = (String) comboBox.getSelectedItem();
                int slider1Value = slider1.getValue();
                int slider2Value = slider2.getValue();

                data.put("level", selectedOption);
                data.put("volume", slider1Value);
                data.put("sensitivity", slider2Value);
                JsonHelper.writeJsonToFile(data, "app/src/main/resources/data/settings.json");
            }
        });

        mainPanel.add(comboBoxLabel);
        mainPanel.add(comboBox);
        mainPanel.add(volume);
        mainPanel.add(slider1);
        mainPanel.add(sensitivity);
        mainPanel.add(slider2);
        mainPanel.add(submitButton);

        add(mainPanel, BorderLayout.CENTER);
    }
}
