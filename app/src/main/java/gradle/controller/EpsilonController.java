package gradle.controller;

import java.awt.event.*;

import gradle.model.EpsilonModel;
import gradle.view.charecretsView.EpsilonView;

public class EpsilonController implements KeyListener {

    private boolean upPressed = false;
    private boolean downPressed = false;
    private boolean leftPressed = false;
    private boolean rightPressed = false;

    public EpsilonController() {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_W) {
            upPressed = true;
        } else if (keyCode == KeyEvent.VK_A) {
            leftPressed = true;
        } else if (keyCode == KeyEvent.VK_S) {
            downPressed = true;
        } else if (keyCode == KeyEvent.VK_D) {
            rightPressed = true;
        }

        updateMovement();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_W) {
            upPressed = false;
        } else if (keyCode == KeyEvent.VK_A) {
            leftPressed = false;
        } else if (keyCode == KeyEvent.VK_S) {
            downPressed = false;
        } else if (keyCode == KeyEvent.VK_D) {
            rightPressed = false;
        }

        updateMovement();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not needed for movement
    }

    private void updateMovement() {
        int dx = 0;
        int dy = 0;

        if (upPressed && !downPressed) {
            dy -= Constants.MOVE_SPEED;
        } else if (!upPressed && downPressed) {
            dy += Constants.MOVE_SPEED;
        }

        if (leftPressed && !rightPressed) {
            dx -= Constants.MOVE_SPEED;
        } else if (!leftPressed && rightPressed) {
            dx += Constants.MOVE_SPEED;
        }

        if (upPressed && rightPressed) {
            dy -= Constants.MOVE_SPEED;
            dx += Constants.MOVE_SPEED;
        } else if (upPressed && leftPressed) {
            dy -= Constants.MOVE_SPEED;
            dx -= Constants.MOVE_SPEED;
        } else if (downPressed && rightPressed) {
            dy += Constants.MOVE_SPEED;
            dx += Constants.MOVE_SPEED;
        } else if (downPressed && leftPressed) {
            dy += Constants.MOVE_SPEED;
            dx -= Constants.MOVE_SPEED;
        }

        EpsilonModel.items.get(0).x += dx;
        EpsilonModel.items.get(0).y += dy;

        EpsilonView.items.get(0).setUtil(EpsilonModel.items.get(0));
    }
}
