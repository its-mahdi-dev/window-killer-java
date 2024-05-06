package gradle.view;

import javax.swing.*;

import gradle.controller.Constants;
import gradle.controller.MouseController;

import java.awt.*;

public class GameFrame extends JFrame {
    private static GameFrame INSTANCE;

    private GameFrame() throws HeadlessException {
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 2));
        setSize(Constants.GAME_FRAME_DIMENSION);
        setLocationRelativeTo(null);
        this.addMouseListener(new MouseController());
        this.addMouseMotionListener(new MouseController());
        setVisible(true);
        setLayout(null);
    }

    public static GameFrame getINSTANCE() {
        if (INSTANCE == null)
            INSTANCE = new GameFrame();
        return INSTANCE;
    }
    

}
