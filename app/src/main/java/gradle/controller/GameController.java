package gradle.controller;

import gradle.model.EnemyModel;
import gradle.model.EpsilonModel;
import gradle.view.GameFrame;
import gradle.view.GamePanel;
import gradle.view.MainPanel;
import gradle.view.SettingsPanel;
import gradle.view.StorePanel;
import gradle.view.charecretsView.EnemyView;
import gradle.view.charecretsView.EpsilonView;

import java.awt.GraphicsEnvironment;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import javax.swing.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class GameController {

    public static int waveNumber = 0;

    public static void startGame() {
        SwingUtilities.invokeLater(() -> {
            GameSettings.isPause = false;
            GameSettings.isGameRun = true;
            setSettings();
            setSkillTree();
            SkillTreeController.skillsTime.put("ares", System.currentTimeMillis() - 6 * 60000);
            SkillTreeController.skillsTime.put("aceso", System.currentTimeMillis() - 6 * 60000);
            SkillTreeController.skillsTime.put("proteus", System.currentTimeMillis() - 6 * 60000);
            GameFrame.getINSTANCE().remove(SettingsPanel.getINSTANCE());
            // GameFrame.getINSTANCE().remove(MainPanel.getINSTANCE());
            GamePanel.getINSTANCE();

            GameFrame.getINSTANCE().add(GamePanel.getINSTANCE());
            StorePanel.getINSTANCE();
            GamePanel.getINSTANCE().setSize(Constants.GAME_FRAME_DIMENSION);
            GamePanel.getINSTANCE().setLocationToCenter(GameFrame.getINSTANCE());

            GamePanel.getINSTANCE().repaint();
            new EpsilonModel();
            createWave();

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

    private static void setSettings() {
        JSONObject settings = JsonHelper.readJsonFromFile("app/src/main/resources/data/settings.json");
        setLevel(settings.get("level").toString());
        int volume = Integer.parseInt(settings.get("volume").toString());
        float gain = (float) (Math.log(volume / 100.0) / Math.log(10.0) * 20.0);
        GameSettings.volume = gain;
        GameSettings.sensitivity = Integer.parseInt(settings.get("sensitivity").toString());
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

    public static void createWave() {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        waveNumber++;
        EnemyController.isCreating = true;
        executor.schedule(() -> {
            EnemyController.createEnemyWaves((6 + (int) GameSettings.level) * waveNumber);
        }, 5, TimeUnit.SECONDS);

        executor.shutdown();
    }

    private static void setSkillTree() {
        JSONObject data = JsonHelper.readJsonFromFile("app/src/main/resources/data/skillTree.json");
        JSONArray skills = (JSONArray) data.get("skills");
        for (int i = 0; i < skills.size(); i++) {
            SkillTreeController.skills.put(((JSONObject) skills.get(i)).get("slog").toString(),
                    Boolean.parseBoolean(((JSONObject) skills.get(i)).get("enabled").toString()));
        }
    }

    @SuppressWarnings("unchecked")
    public static void GameOver() {

        // JOptionPane.showMessageDialog(null, "Game Over");
        Utils.playMusic("gameOver", false);
        GameSettings.isPause = true;
        GameSettings.isGameRun = false;
        waveNumber = 0;
        Update.timer1.stop();
        Update.timer2.stop();
        GameFrame.getINSTANCE().add(SettingsPanel.getINSTANCE());
        GameFrame.getINSTANCE().remove(GamePanel.getINSTANCE());
        GameFrame.getINSTANCE().remove(StorePanel.getINSTANCE());

        JSONObject data = JsonHelper.readJsonFromFile("app/src/main/resources/data/data.json");
        data.put("xp", EpsilonModel.getINSTANCE().XP + Integer.parseInt(data.get("xp").toString()));
        JsonHelper.writeJsonToFile(data, "app/src/main/resources/data/data.json");
        SkillTreeController.enemy_hp_decrease = 0;
        SkillTreeController.epsilon_hp_increase = 0;
        StoreController.shotsNumber = 1;
        EpsilonModel.items.remove(0);
        EpsilonView.items.remove(0);
        EnemyController.removeAll();
        EpsilonController.removeAllCollectible();
        ShotController.removeAll();
        EnemyController.isCreating = false;
        MainPanel.getINSTANCE().setVisible(true);
        MainPanel.getINSTANCE().repaint();
        GameFrame.getINSTANCE().repaint();

    }
}
