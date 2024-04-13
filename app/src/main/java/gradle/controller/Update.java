package gradle.controller;

import java.awt.geom.Point2D;

import javax.swing.*;

import gradle.model.EnemyModel;
import gradle.model.EpsilonModel;
import gradle.model.Model;
import gradle.model.ShotModel;
import gradle.view.GameFrame;
import gradle.view.charecretsView.EnemyView;
import gradle.view.charecretsView.EpsilonView;
import gradle.view.charecretsView.ShotView;

public class Update {

    public int upsCount = 0;
    public int fpsCount = 0;

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
        EpsilonView epsilonView = (EpsilonView) EpsilonView.items.get(0);
        EpsilonModel epsilonModel = (EpsilonModel) EpsilonModel.items.get(0);
        epsilonView.setUtil(epsilonModel);
        for (int i = 0; i < EnemyView.items.size(); i++) {
            EnemyView shotView = (EnemyView) EnemyView.items.get(i);
            EnemyModel shotModel = (EnemyModel) EnemyModel.findById(shotView.getId());
            shotView.setUtil(shotModel);
        }
        for (int i = 0; i < ShotView.items.size(); i++) {
            ShotView shotView = (ShotView) ShotView.items.get(i);
            ShotModel shotModel = (ShotModel) ShotModel.findById(shotView.getId());
            shotView.setUtil(shotModel);
        }
        for (int i = 0; i < EnemyView.items.size(); i++) {
            EnemyView enemyView = (EnemyView) EnemyView.items.get(i);
            EnemyModel enemyModel = (EnemyModel) EnemyModel.findById(enemyView.getId());
            enemyView.setUtil(enemyModel);
        }

        GameFrame.getINSTANCE().repaint();
        fpsCount++;
    }

    public void updateModel() {
        EpsilonModel epsilonModel = (EpsilonModel) EpsilonModel.items.get(0);

        epsilonModel.move();
        EpsilonController.checkWallImpact();
        for (Model model : EnemyModel.items) {
            EnemyModel enemyModel = (EnemyModel) model;
            enemyModel.setDirection(Utils.getDirection(enemyModel.anchor,
                    EpsilonModel.items.get(0).anchor));
            enemyModel.move();
            EnemyController.setPoints(enemyModel);

            EnemyController.checkEpsilonColision(enemyModel);
        }
        for (int i = 0; i < ShotModel.items.size(); i++) {
            ShotModel shotModel = (ShotModel) ShotModel.items.get(i);
            shotModel.move();
            for (int j = 0; j < EnemyModel.items.size(); j++) {
                EnemyModel enemyModel = (EnemyModel) EnemyModel.items.get(j);
                if (Utils.checkEpsilonShot(enemyModel, shotModel)) {
                    enemyModel.HP--;
                    if (ShotModel.items.contains(shotModel)) {
                        ShotModel.removedItems.add(shotModel);
                        ShotView.removedItems.add(ShotView.findById(shotModel.getId()));
                        ShotModel.items.remove(shotModel);
                        ShotView.items.removeIf(shot -> shot.getId() == shotModel.getId());
                    }
                }
                if (enemyModel.HP <= 0) {
                    EnemyModel.removedItems.add(enemyModel);
                    EnemyView.removedItems.add(EnemyView.findById(enemyModel.getId()));
                    EnemyModel.items.remove(j);
                    EnemyView.items.removeIf(enemy -> enemy.getId() == enemyModel.getId());
                }

            }
            if (shotModel.anchor.getX() > Constants.PANEL_SIZE.getWidth()
                    || shotModel.anchor.getX() < 0 ||
                    shotModel.anchor.getY() > Constants.PANEL_SIZE.getHeight()
                    || shotModel.anchor.getY() < 0) {
                ShotModel.removedItems.add(shotModel);
                ShotView.removedItems.add(ShotView.findById(shotModel.getId()));
                ShotModel.items.remove(i);
                ShotView.items.removeIf(shot -> shot.getId() == shotModel.getId());
            }
        }
        upsCount++;
    }

}
