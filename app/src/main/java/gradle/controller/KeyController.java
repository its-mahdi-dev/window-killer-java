package gradle.controller;

import java.awt.event.*;

import gradle.view.StorePanel;

public class KeyController implements KeyListener {

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        System.out.println(keyCode);
        if (GameSettings.isGameRun) {
            if (keyCode == KeyEvent.VK_W) {
                EpsilonController.pressed.replace("up", true);
            } else if (keyCode == KeyEvent.VK_A) {
                EpsilonController.pressed.replace("left", true);
            } else if (keyCode == KeyEvent.VK_S) {
                EpsilonController.pressed.replace("down", true);
            } else if (keyCode == KeyEvent.VK_D) {
                EpsilonController.pressed.replace("right", true);
            }

            if (keyCode == KeyEvent.VK_P)
                GameSettings.isPause = !GameSettings.isPause;
            else if (keyCode == KeyEvent.VK_K)
                GameSettings.isStore = !GameSettings.isStore;
            else
                SkillTreeController.keyControl(e);
            EpsilonController.updateMovement();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();

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
