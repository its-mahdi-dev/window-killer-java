package gradle.controller;

import javax.swing.*;

import gradle.model.EnemyModel;
import gradle.model.Model;
import gradle.model.ShotModel;
import gradle.view.GameFrame;
import gradle.view.charecretsView.EnemyView;
import gradle.view.charecretsView.EpsilonView;
import gradle.view.charecretsView.ShotView;

import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

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
        for (int i = 0; i < ShotView.items.size(); i++) {
            ShotView shotView = (ShotView) ShotView.items.get(i);
            ShotModel shotModel = (ShotModel) ShotModel.findById(shotView.getId());
            shotView.setUtil(shotModel);
        }
        GameFrame.getINSTANCE().repaint();
    }

    public void updateModel() {
        for (int i = 0; i < ShotModel.items.size(); i++) {
            ShotModel shotModel = (ShotModel) ShotModel.items.get(i);
            shotModel.move();
            for (int j = 0; j < EnemyModel.items.size(); j++) {
                EnemyModel enemyModel = (EnemyModel) EnemyModel.items.get(j);
                if (Utils.checkEpsilonShot(enemyModel, shotModel)) {
                    EnemyModel.items.remove(j);
                    EnemyView.items.removeIf(enemy -> enemy.getId() == enemyModel.getId());
                }

            }
            if (shotModel.anchor.getX() > Constants.PANEL_SIZE.getWidth()
                    || shotModel.anchor.getX() < 0 ||
                    shotModel.anchor.getY() > Constants.PANEL_SIZE.getHeight()
                    || shotModel.anchor.getY() < 0) {
                ShotModel.items.remove(i);
                ShotView.items.removeIf(shot -> shot.getId() == shotModel.getId());
            }
        }
    }

}
