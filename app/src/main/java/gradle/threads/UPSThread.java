package gradle.threads;

import gradle.controller.Constants;
import gradle.controller.GameController;
import gradle.controller.GameSettings;
import gradle.interfaces.UPSController;
import gradle.view.StorePanel;

public class UPSThread implements Runnable {
    private final UPSController controller;
    private volatile boolean running = true;

    public UPSThread(UPSController controller) {
        this.controller = controller;
    }

    @Override
    public void run() {
        while (running) {
            long startTime = System.nanoTime();

            if (!GameSettings.isPause || controller instanceof GameController || controller instanceof StorePanel)
                controller.check();

            long elapsedTime = System.nanoTime() - startTime;
            long sleepTime = ((long) Constants.MODEL_UPDATE_TIME * 1_000_000L - elapsedTime) / 1_000_000L;

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