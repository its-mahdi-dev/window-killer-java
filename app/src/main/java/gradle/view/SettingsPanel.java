package gradle.view;

import javax.swing.*;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingsPanel extends JPanel {
    Timer timer;
    private JComboBox<String> levelComboBox;
    private JTextField nameTextField;
    private JButton startButton;
    private Color selectedColor = Color.BLACK;
    private JPanel colorPreviewPanel;

    public SettingsPanel() {
        setOpaque(true);
        setBackground(Color.BLACK);
        setBorder(BorderFactory.createCompoundBorder(new LineBorder(Color.CYAN),
                BorderFactory.createEmptyBorder(2, 2, 2, 2)));

        setSize(new Dimension(MainPanel.getINSTANCE().getWidth() - 70, MainPanel.getINSTANCE().getHeight()));
        setLocation(-getWidth(), 0);
        setFocusable(false);

        addItems();
        GameFrame.getINSTANCE().add(this);
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
        // Title Label
        JLabel titleLabel = new JLabel("Game Ready");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(new Color(240, 240, 240));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Level Selection
        JLabel levelLabel = new JLabel("Select Difficulty:");
        levelComboBox = new JComboBox<>(new String[] { "Easy", "Medium", "Hard" });
        formPanel.add(levelLabel, gbc);

        gbc.gridy++;
        formPanel.add(levelComboBox, gbc);

        // Color Selection Label
        gbc.gridy++;
        JLabel colorSelectionLabel = new JLabel("Select Color:");
        formPanel.add(colorSelectionLabel, gbc);

        // Color Selection Button
        gbc.gridx = 1;
        gbc.gridy++;
        JButton colorButton = new JButton("Choose Color");
        colorButton.setBackground(selectedColor);
        colorButton.setForeground(Color.white);
        colorButton.setPreferredSize(new Dimension(120, 30));
        colorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color newColor = JColorChooser.showDialog(null, "Choose Color", selectedColor);
                if (newColor != null) {
                    selectedColor = newColor;
                    colorButton.setBackground(selectedColor);
                    colorButton.setForeground(selectedColor);
                    colorPreviewPanel.setBackground(selectedColor);
                }
            }
        });
        formPanel.add(colorButton, gbc);

        // Color Preview Panel
        gbc.gridx = 0;
        gbc.gridy++;
        colorPreviewPanel = new JPanel();
        colorPreviewPanel.setBackground(selectedColor);
        colorPreviewPanel.setPreferredSize(new Dimension(30, 30));
        // formPanel.add(colorPreviewPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel nameLabel = new JLabel("Enter Your Name:");
        formPanel.add(nameLabel, gbc);

        // Name Input Field
        gbc.gridx = 0;
        gbc.gridy++;
        nameTextField = new JTextField();
        nameTextField.setPreferredSize(new Dimension(200, 30));
        formPanel.add(nameTextField, gbc);

        // Start Button
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        startButton = new JButton("Start Game");
        startButton.setFont(new Font("Arial", Font.BOLD, 16));
        startButton.setBackground(new Color(100, 200, 100));
        startButton.setForeground(Color.WHITE);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String level = (String) levelComboBox.getSelectedItem();
                String name = nameTextField.getText();

                System.out.println("Game Started: " + level + ", " + name + ", " + selectedColor);
            }
        });
        formPanel.add(startButton, gbc);
        add(formPanel, BorderLayout.CENTER);
    }
}
