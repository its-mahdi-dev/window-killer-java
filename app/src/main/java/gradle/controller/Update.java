package gradle.controller;

import javax.swing.*;

import gradle.view.GameFrame;
import gradle.view.charecretsView.EpsilonView;

import java.util.ArrayList;

public class Update {

    public Update() {
        new Timer((int) Constants.FRAME_UPDATE_TIME, e -> updateView()) {
            {
                setCoalesce(true);
            }
        }.start();
        new Timer((int) Constants.MODEL_UPDATE_TIME, e -> updateModel()) {
            {
                setCoalesce(true);
            }
        }.start();
    }

    public void updateView() {

        GameFrame.getINSTANCE().repaint();
    }

    public void updateModel() {
    }
}
