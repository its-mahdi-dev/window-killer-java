package gradle.view;

import javax.swing.*;
import javax.swing.border.LineBorder;

import java.awt.*;

import java.util.LinkedHashMap;
import java.util.Map;
import org.json.simple.JSONObject;

import gradle.controller.JsonHelper;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingsPanel extends JPanel {
    Timer timer;
    private static SettingsPanel INSTANCE;
    JSONObject data;
    JButton submitButton;
    JButton backButton;
    JPanel mainPanel;
    JLabel comboBoxLabel;
    static Map<String, JComponent> components = new LinkedHashMap<>();
    static {
        components.put("levelLabel", new JLabel("level: "));
        String[] comboBoxItems = { "easy", "medium", "high" };
        components.put("combo", new JComboBox<>(comboBoxItems));
        components.put("volumeLabel", new JLabel(" volume: "));
        components.put("volumeSlider", new JSlider());
        components.put("sensitivityLabel", new JLabel("sensitivity: "));
        components.put("sensitivitySlider", new JSlider());
        components.put("submitButton", new JButton());
    }

    private SettingsPanel() {
        setOpaque(true);
        setBackground(Color.BLACK);
        setBorder(BorderFactory.createCompoundBorder(new LineBorder(Color.CYAN),
                BorderFactory.createEmptyBorder(2, 2, 2, 2)));

        setSize(new Dimension(MainPanel.getINSTANCE().getWidth() - 70, MainPanel.getINSTANCE().getHeight()));
        setLocation(-getWidth(), 0);
        setFocusable(false);
        setData();

        backButton = new JButton("back");
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

        add(backButton);

        mainPanel = new JPanel(new GridLayout(7, 1));
        mainPanel.setBorder(BorderFactory.createCompoundBorder(new LineBorder(Color.yellow),
                BorderFactory.createEmptyBorder(60, 60, 60, 60)));
        mainPanel.setPreferredSize(new Dimension(getWidth() - getWidth() / 4, 500));
        mainPanel.setBackground(new Color(0, 0, 0, 0));

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

        if (open) {
            MainPanel.getINSTANCE().removeItems();
            addItems();
        } else {
            MainPanel.getINSTANCE().showItems();
            removeItems();
        }
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

        // ComboBox
        JLabel comboLabel = new JLabel("level: " + data.get("level").toString());
        components.replace("levelLabel", comboLabel);
        components.get("combo").setPreferredSize(new Dimension(100, 20));
        components.get("combo").setBackground(new Color(0, 0, 0, 0));
        components.get("combo").setForeground(Color.white);

        // Slider 1
        JLabel volume = new JLabel("volume: " + data.get("volume").toString());
        components.replace("volumeLabel", volume);
        JSlider slider1 = new JSlider(JSlider.HORIZONTAL, 0, 100, Integer.parseInt(data.get("volume").toString()));
        slider1.setMajorTickSpacing(20);
        slider1.setMinorTickSpacing(5);
        slider1.setPaintTicks(true);
        slider1.setPaintLabels(true);
        slider1.setPreferredSize(new Dimension(200, 50));
        slider1.setBackground(new Color(0, 0, 0, 0));

        components.replace("volumeSlider", slider1);

        // Slider 2
        JLabel sensitivity = new JLabel("sensitivity: " + data.get("sensitivity").toString());
        components.replace("sensitivityLabel", sensitivity);
        JSlider slider2 = new JSlider(JSlider.HORIZONTAL, 0, 10, Integer.parseInt(data.get("sensitivity").toString()));
        slider2.setMajorTickSpacing(20);
        slider2.setMinorTickSpacing(5);
        slider2.setPaintTicks(true);
        slider2.setPaintLabels(true);
        slider2.setPreferredSize(new Dimension(150, 20));
        slider2.setBackground(new Color(0, 0, 0, 0));

        components.replace("sensitivitySlider", slider2);

        JButton submitButton = new JButton("Submit");
        submitButton.setBackground(Color.yellow);
        submitButton.setForeground(Color.black);
        submitButton.setPreferredSize(new Dimension(mainPanel.getWidth() - mainPanel.getWidth() / 4, 40));
        submitButton.addActionListener(new ActionListener() {
            @SuppressWarnings("unchecked")
            @Override
            public void actionPerformed(ActionEvent e) {
                @SuppressWarnings("rawtypes")
                JComboBox<String> comboBox = (JComboBox) components.get("combo");
                String selectedOption = (String) comboBox.getSelectedItem();
                int slider1Value = slider1.getValue();
                int slider2Value = slider2.getValue();

                data.put("level", selectedOption);
                data.put("volume", slider1Value);
                data.put("sensitivity", slider2Value);
                JsonHelper.writeJsonToFile(data, "app/src/main/resources/data/settings.json");
                showPanel(false);
            }
        });

        components.replace("submitButton", submitButton);

        for (Map.Entry<String, JComponent> entry : components.entrySet()) {
            if (entry.getValue() instanceof JLabel)
                entry.getValue().setForeground(Color.white);
            mainPanel.add(entry.getValue());
        }

        add(mainPanel, BorderLayout.CENTER);
    }

    public void removeItems() {
        for (Map.Entry<String, JComponent> entry : components.entrySet()) {
            mainPanel.remove(entry.getValue());
        }

        remove(mainPanel);
    }
}
