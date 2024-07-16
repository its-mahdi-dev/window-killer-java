package gradle.view;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.border.LineBorder;
import gradle.controller.Constants;
import gradle.controller.KeyController;
import gradle.threads.GamePanelThread;

public class Panels extends JPanel {
    private static Panels INSTANCE;
    private ArrayList<GamePanel> panels = new ArrayList<>();

    public ArrayList<GamePanel> getPanels() {
        return panels;
    }

    private Panels() {
        setOpaque(true);
        setBackground(new Color(0, 0, 0, 0));
        // setBorder(BorderFactory.createCompoundBorder(new LineBorder(Color.cyan),
        // BorderFactory.createEmptyBorder(2, 2, 2, 2)));
        // setBackground(Color.red);
        setSize(Constants.GAME_FRAME_DIMENSION);
        setLocationToCenter(GameFrame.getINSTANCE());
        setFocusable(true);
        this.addKeyListener(new KeyController());
        setLayout(null);
    }

    public void setLocationToCenter(GameFrame gameFrame) {
        setLocation(gameFrame.getWidth() / 2 - getWidth() / 2, gameFrame.getHeight() / 2 - getHeight() / 2);
    }

    public static Panels getINSTANCE() {
        if (INSTANCE == null)
            INSTANCE = new Panels();
        return INSTANCE;
    }

    public void addPanel(GamePanel gamePanel) {
        GamePanelThread threadPanel = new GamePanelThread(gamePanel);
        Thread thread = new Thread(threadPanel);
        gamePanel.panelThread = threadPanel;
        thread.start();
        panels.add(gamePanel);
        this.add(gamePanel);
    }

    public void removePanel(GamePanel gamePanel) {
        gamePanel.panelThread.stopRunning();
        panels.remove(gamePanel);
        this.remove(gamePanel);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    public List<GamePanel> getRigidPanels() {
        List<GamePanel> rigidPanels = getPanels().stream().filter(p -> p.rigid).collect(Collectors.toList());
        return rigidPanels;
    }

}
