package gradle.controller;

import gradle.model.EpsilonModel;
import gradle.view.GameFrame;
import gradle.view.GamePanel;
import gradle.view.MainPanel;
import gradle.view.StorePanel;

import java.awt.GraphicsEnvironment;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import javax.swing.*;

public class GameController {
    public static void startGame() {
        SwingUtilities.invokeLater(() -> {

            GameFrame.getINSTANCE();
            GamePanel.getINSTANCE();
            StorePanel.getINSTANCE();
            GamePanel.getINSTANCE().repaint();
            new EpsilonModel();
            ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

            // Schedule the task to run after 10 seconds
            executor.schedule(() -> {
                EnemyController.createEnemyWaves(8);
            }, 5, TimeUnit.SECONDS);

            executor.shutdown();
            Update update = new Update();

            GameFrame.getINSTANCE().repaint();
            GameSettings.isPause = false;
            GameSettings.isGameRun = true;
            MainPanel.getINSTANCE().setVisible(false);
            new Timer().scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    // Print and reset counts
                    System.out.println("UPS: " + update.upsCount + ", FPS: " + update.fpsCount);
                    update.upsCount = 0;
                    update.fpsCount = 0;
                }
            }, 1000, 1000);
        });
    }
}
