package gradle.controller;

import gradle.interfaces.UPSController;
import gradle.model.EnemyModel;
import gradle.model.EnemyType;
import gradle.model.EpsilonModel;
import gradle.model.Model;
import gradle.model.enemies.ArchmireEnemy;
import gradle.model.enemies.WyrmEnemy;
import gradle.threads.GamePanelThread;
import gradle.threads.UPSThread;
import gradle.view.GameFrame;
import gradle.view.GamePanel;
import gradle.view.MainPanel;
import gradle.view.Panels;
import gradle.view.SettingsPanel;
import gradle.view.StorePanel;
import gradle.view.charecretsView.NavbarView;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class GameController implements UPSController {

    @Override
    public void check() {
        if (!EnemyController.isCreating && EnemyModel.getAllEnemies().size() == 0)
            GameController.createWave();

        if (EpsilonModel.getINSTANCE().HP <= 0 && GameSettings.isGameRun)
            GameController.GameOver();

        NavbarView.getINSTANCE().setUtil();
        StorePanel.getINSTANCE().showOrHidePanel();
        if (GameSettings.isStore)
            StorePanel.getINSTANCE().repaint();
        // Panels.getINSTANCE().repaint();
        GameFrame.getINSTANCE().repaint();
    }

    public static int waveNumber = 0;
    static final javax.swing.Timer winTimer = new javax.swing.Timer(10, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (EpsilonModel.getINSTANCE().w < EpsilonModel.getINSTANCE().currentPanels.get(0).getWidth()) {
                EpsilonModel.getINSTANCE().w += 2;
                EpsilonModel.getINSTANCE().h += 2;
            } else {
                resetGame();
            }
        }
    });

    public static void startGame() {

        if (winTimer != null) {
            winTimer.stop();
        }
        GameSettings.isPause = false;
        GameSettings.isGameRun = true;
        setSettings();
        setSkillTree();
        SkillTreeController.skillsTime.put("ares", System.currentTimeMillis() - 6 * 60000);
        SkillTreeController.skillsTime.put("aceso", System.currentTimeMillis() - 6 * 60000);
        SkillTreeController.skillsTime.put("proteus", System.currentTimeMillis() - 6 * 60000);
        MainPanel.getINSTANCE().remove(SettingsPanel.getINSTANCE());
        // GameFrame.getINSTANCE().remove(MainPanel.getINSTANCE());
        // GamePanel.getINSTANCE();

        StorePanel.getINSTANCE();
        GamePanel panel1 = new GamePanel();
        GamePanel panel2 = new GamePanel();
        // panel1.setSize(new Dimension(500,500));
        panel1.setLocation(100, 100);
        // panel2.setSize(new Dimension(500, 500));
        panel2.setLocation(700, 100);
        // panel1.setLocationToCenter(GameFrame.getINSTANCE());
        EpsilonModel.getINSTANCE();
        EpsilonModel.getINSTANCE().currentPanels.add(panel1);
        EpsilonModel.getINSTANCE().init();
        panel1.repaint();
        panel2.repaint();
        Thread threadPanel1 = new Thread(new GamePanelThread(panel1));
        Thread threadPanel2 = new Thread(new GamePanelThread(panel2));
        threadPanel1.start();
        threadPanel2.start();
        Panels.getINSTANCE();
        Panels.getINSTANCE().addPanel(panel1);
        Panels.getINSTANCE().addPanel(panel2);
        Panels.getINSTANCE().repaint();
        GameFrame.getINSTANCE().add(Panels.getINSTANCE());
        // createWave();
        // EnemyModel.create(new Point2D.Double(1100, 400), EnemyType.necropick);
        ArchmireEnemy.create(new Point2D.Double(600, 300));
        // EnemyModel.create(new Point2D.Double(800, 800), EnemyType.omenoct);
        EnemyController.isCreating = false;

        // Update.timer1.start();
        // Update.timer2.start();
        GameSettings.gameRunnig = true;
        startUPS();

        GameFrame.getINSTANCE().repaint();

        MainPanel.getINSTANCE().setVisible(false);
        // new Timer().scheduleAtFixedRate(new TimerTask() {
        // @Override
        // public void run() {
        // // Print and reset counts
        // System.out.println("UPS: " + Update.upsCount + ", FPS: " + Update.fpsCount);
        // Update.upsCount = 0;
        // Update.fpsCount = 0;
        // }
        // }, 1000, 1000);
        // });
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
        Utils.playMusic("nextLevel", false);
        if (waveNumber == 3) {
            win();
        } else {
            ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
            waveNumber++;
            EnemyController.isCreating = true;
            executor.schedule(() -> {
                EnemyController.createEnemyWaves((2 + (int) GameSettings.level) * waveNumber);
            }, 3, TimeUnit.SECONDS);

            executor.shutdown();
        }
    }

    private static void win() {
        Utils.playMusic("win", false);
        EpsilonModel.getINSTANCE().anchor = new Point2D.Double(
                EpsilonModel.getINSTANCE().currentPanels.get(0).getX()
                        + EpsilonModel.getINSTANCE().currentPanels.get(0).getWidth() / 2,
                EpsilonModel.getINSTANCE().currentPanels.get(0).getY()
                        + EpsilonModel.getINSTANCE().currentPanels.get(0).getHeight() / 2);
        winTimer.start();
    }

    private static void setSkillTree() {
        JSONObject data = JsonHelper.readJsonFromFile("app/src/main/resources/data/skillTree.json");
        JSONArray skills = (JSONArray) data.get("skills");
        for (int i = 0; i < skills.size(); i++) {
            SkillTreeController.skills.put(((JSONObject) skills.get(i)).get("slog").toString(),
                    Boolean.parseBoolean(((JSONObject) skills.get(i)).get("enabled").toString()));
        }
    }

    public static void GameOver() {

        // JOptionPane.showMessageDialog(null, "Game Over");
        Utils.playMusic("gameOver", false);
        resetGame();
    }

    @SuppressWarnings("unchecked")
    public static void resetGame() {
        if (winTimer != null) {
            winTimer.stop();
        }
        GameSettings.isPause = true;
        GameSettings.isGameRun = false;
        waveNumber = 0;
        Update.timer1.stop();
        Update.timer2.stop();
        MainPanel.getINSTANCE().add(SettingsPanel.getINSTANCE());
        GameFrame.getINSTANCE().remove(Panels.getINSTANCE());
        GameFrame.getINSTANCE().remove(StorePanel.getINSTANCE());

        JSONObject data = JsonHelper.readJsonFromFile("app/src/main/resources/data/data.json");
        data.put("xp", EpsilonModel.getINSTANCE().XP + Integer.parseInt(data.get("xp").toString()));
        JsonHelper.writeJsonToFile(data, "app/src/main/resources/data/data.json");
        SkillTreeController.enemy_hp_decrease = 0;
        SkillTreeController.epsilon_hp_increase = 0;
        StoreController.shotsNumber = 1;
        // EpsilonModel.items.remove(0);
        // EpsilonView.items.remove(0);
        EnemyController.removeAll();
        CollectibleController.removeAllCollectible();
        ShotController.removeAll();
        EnemyController.isCreating = false;
        MainPanel.getINSTANCE().setVisible(true);
        MainPanel.getINSTANCE().repaint();
        GameFrame.getINSTANCE().repaint();

    }

    public static void startUPS() {
        new Thread(new UPSThread(new GameController())).start();
        new Thread(new UPSThread(new EnemyController())).start();
        new Thread(new UPSThread(new EpsilonController())).start();
        new Thread(new UPSThread(new ShotController())).start();
        new Thread(new UPSThread(new SkillTreeController())).start();
        new Thread(new UPSThread(new StoreController())).start();
        new Thread(new UPSThread(new CollectibleController())).start();
    }
}
