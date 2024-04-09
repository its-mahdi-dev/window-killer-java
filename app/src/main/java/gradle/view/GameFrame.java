package gradle.view;

import javax.swing.*;

import gradle.controller.Constants;

import java.awt.*;

public class GameFrame extends JFrame {
    private static GameFrame INSTANCE;

    private GameFrame() throws HeadlessException {
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));
        setSize(Constants.GAME_FRAME_DIMENSION);
        setLocationRelativeTo(null);
        setVisible(true);
        setLayout(null);
    }

    public static GameFrame getINSTANCE() {
        if (INSTANCE == null)
            INSTANCE = new GameFrame();
        return INSTANCE;
    }

}
