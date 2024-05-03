package gradle.controller;

import java.awt.Dimension;
import java.awt.geom.Point2D;

import javax.swing.*;

import gradle.model.CollectibleModel;
import gradle.model.EnemyModel;
import gradle.model.EpsilonModel;
import gradle.model.EpsilonVertexModel;
import gradle.model.Model;
import gradle.model.ShotModel;
import gradle.view.GameFrame;
import gradle.view.GamePanel;
import gradle.view.StorePanel;
import gradle.view.charecretsView.CollectibleView;
import gradle.view.charecretsView.EnemyView;
import gradle.view.charecretsView.EpsilonVertexView;
import gradle.view.charecretsView.EpsilonView;
import gradle.view.charecretsView.NavbarView;
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
        EpsilonView.items.get(0).setUtil(EpsilonModel.items.get(0));

        for (int i = 0; i < EpsilonVertexView.items.size(); i++) {
            EpsilonVertexView shotView = (EpsilonVertexView) EpsilonVertexView.items.get(i);
            EpsilonVertexModel shotModel = (EpsilonVertexModel) EpsilonVertexModel.findById(shotView.getId());
            shotView.setUtil(shotModel);
        }
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
        for (int i = 0; i < CollectibleView.items.size(); i++) {
            CollectibleView shotView = (CollectibleView) CollectibleView.items.get(i);
            CollectibleModel shotModel = (CollectibleModel) CollectibleModel.findById(shotView.getId());
            shotView.setUtil(shotModel);
        }

        NavbarView.getINSTANCE().setUtil();
        GameFrame.getINSTANCE().repaint();
        StorePanel.getINSTANCE().showOrHidePanel();
        if (GameSettings.isStore)
            StorePanel.getINSTANCE().repaint();
        if (!GameSettings.isPause)
            GamePanel.getINSTANCE().changeSize();
        fpsCount++;
    }

    public void updateModel() {
        if (!GameSettings.isPause) {
            EpsilonModel.items.get(0).move();
            EpsilonController.checkWallImpact();
            EpsilonController.updateVertextAnchor();
            EnemyController.checkCollision();
            ShotController.checkCollision();

            for (int i = 0; i < CollectibleModel.items.size(); i++) {
                EpsilonController.checkCollectibleCollision((CollectibleModel) CollectibleModel.items.get(i));
            }

            StoreController.checkItemsTimes();
            SkillTreeController.checkSkillsTime();
        }
        upsCount++;
    }

}
