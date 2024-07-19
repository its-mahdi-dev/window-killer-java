package gradle.controller;

import java.awt.event.*;
import java.util.Random;

public class KeyController implements KeyListener {

    public static String[] directionsString = { "up", "left", "down", "right" };

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (GameSettings.isGameRun) {
            if (GameSettings.massedUp) {
                if (keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_S
                        || keyCode == KeyEvent.VK_D) {

                    Random random = new Random();
                    int randomIndex = random.nextInt(directionsString.length);
                    String randomString = directionsString[randomIndex];
                    EpsilonController.pressed.replace(randomString, true);
                }

            } else {
                if (keyCode == KeyEvent.VK_W) {
                    EpsilonController.pressed.replace("up", true);
                } else if (keyCode == KeyEvent.VK_A) {
                    EpsilonController.pressed.replace("left", true);
                } else if (keyCode == KeyEvent.VK_S) {
                    EpsilonController.pressed.replace("down", true);
                } else if (keyCode == KeyEvent.VK_D) {
                    EpsilonController.pressed.replace("right", true);
                }
            }

            if (keyCode == KeyEvent.VK_P)
                GameSettings.isPause = !GameSettings.isPause;
            else if (keyCode == KeyEvent.VK_K) {
                GameSettings.isStore = !GameSettings.isStore;
                GameSettings.isPause = !GameSettings.isPause;
            } else
                SkillTreeController.keyControl(e);
            EpsilonController.updateMovement();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (GameSettings.massedUp) {
            for (String key : EpsilonController.pressed.keySet()) {
                EpsilonController.pressed.replace(key, false);
            }
        }
        if (keyCode == KeyEvent.VK_W) {
            EpsilonController.pressed.replace("up", false);
        } else if (keyCode == KeyEvent.VK_A) {
            EpsilonController.pressed.replace("left", false);
        } else if (keyCode == KeyEvent.VK_S) {
            EpsilonController.pressed.replace("down", false);
        } else if (keyCode == KeyEvent.VK_D) {
            EpsilonController.pressed.replace("right", false);
        }

        EpsilonController.updateMovement();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not needed for movement
    }
}
