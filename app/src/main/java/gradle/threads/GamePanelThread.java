package gradle.threads;

import gradle.controller.Constants;
import gradle.controller.GameSettings;
import gradle.controller.StoreController;
import gradle.model.EpsilonModel;
import gradle.view.GamePanel;
import gradle.view.charecretsView.EnemyView;

import gradle.controller.StoreController.StoreTypes;
public class GamePanelThread implements Runnable {
    private final GamePanel gamePanel;
    private volatile boolean running = true;

    public GamePanelThread(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void run() {
        while (running) {
            long startTime = System.nanoTime();
            if (!GameSettings.isPause) {
                gamePanel.repaint();
                if (EpsilonModel.getINSTANCE().currentPanels.size() <= 1 &&
                        !GameSettings.isPause && !gamePanel.isometric && !StoreController.itemsActive.get(StoreTypes.hypnos))
                    gamePanel.changeSize();
            }

            long elapsedTime = System.nanoTime() - startTime;
            long sleepTime = ((long) Constants.FRAME_UPDATE_TIME * 1_000_000L - elapsedTime) / 1_000_000L;

            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public void stopRunning() {
        running = false;
    }
}