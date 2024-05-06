package gradle.controller;

import gradle.model.EpsilonModel;
import gradle.view.GameFrame;
import gradle.view.GamePanel;
import gradle.view.MainPanel;
import gradle.view.SettingsPanel;
import gradle.view.StorePanel;

import java.awt.GraphicsEnvironment;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import javax.swing.*;

import org.json.simple.JSONObject;

public class GameController {
    public static void startGame() {
        SwingUtilities.invokeLater(() -> {
            GameSettings.isPause = false;
            GameSettings.isGameRun = true;
            JSONObject settings = JsonHelper.readJsonFromFile("app/src/main/resources/data/settings.json");
            setLevel(settings.get("level").toString());
            int volume = Integer.parseInt(settings.get("volume").toString());
            float gain = (float) (Math.log(volume / 100.0) / Math.log(10.0) * 20.0);
            GameSettings.volume = gain;
            GameSettings.sensitivity = Integer.parseInt(settings.get("sensitivity").toString());
            GameFrame.getINSTANCE().remove(SettingsPanel.getINSTANCE());
            // GameFrame.getINSTANCE().remove(MainPanel.getINSTANCE());
            GamePanel.getINSTANCE();
            StorePanel.getINSTANCE();
            GamePanel.getINSTANCE().repaint();
            new EpsilonModel();
            ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

            // Schedule the task to run after 10 seconds
            EnemyController.isCreating = true;
            executor.schedule(() -> {
                EnemyController.createEnemyWaves(8);
            }, 5, TimeUnit.SECONDS);

            executor.shutdown();
            Update update = new Update();

            GameFrame.getINSTANCE().repaint();

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

    private static void setLevel(String level) {
        switch (level) {
            case "easy":
                GameSettings.level = 1;
                break;
            case "medium":
                GameSettings.level = 2;
                break;
            case "high":
                GameSettings.level = 3;
                break;
            default:
                break;
        }

    }
}
