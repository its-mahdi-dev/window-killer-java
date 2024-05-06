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
    public static Timer timer1;
    public static Timer timer2;

    public Update() {
        timer1 = new Timer((int) Constants.FRAME_UPDATE_TIME, e -> updateView()) {
            {
                setCoalesce(true);
            }
        };
        timer2 = new Timer((int) Constants.MODEL_UPDATE_TIME, e -> updateModel()) {
            {
                setCoalesce(true);
            }
        };
        timer1.start();
        timer2.start();
    }

    public void updateView() {
        EpsilonView.items.get(0).setUtil(EpsilonModel.getINSTANCE());

        for (int i = 0; i < EpsilonVertexView.items.size(); i++) {
            EpsilonVertexView epsilonView = (EpsilonVertexView) EpsilonVertexView.items.get(i);
            EpsilonVertexModel epsilonModel = (EpsilonVertexModel) EpsilonVertexModel.findById(epsilonView.getId());
            epsilonView.setUtil(epsilonModel);
        }
        if (!EnemyController.isCreating && EnemyModel.items.size() > 0) {
            for (int i = 0; i < EnemyView.items.size(); i++) {
                EnemyView enemyView = (EnemyView) EnemyView.items.get(i);

                EnemyModel enemyModel = (EnemyModel) EnemyModel.findById(enemyView.getId());
                if (enemyModel != null)
                    enemyView.setUtil(enemyModel);
                else
                    System.out.println(enemyView);
            }
        }
        for (int i = 0; i < ShotView.items.size(); i++) {
            ShotView shotView = (ShotView) ShotView.items.get(i);
            ShotModel shotModel = (ShotModel) ShotModel.findById(shotView.getId());
            if (shotModel != null)
                shotView.setUtil(shotModel);
        }
        for (int i = 0; i < CollectibleView.items.size(); i++) {
            CollectibleView collectibleView = (CollectibleView) CollectibleView.items.get(i);
            CollectibleModel collectibleModel = (CollectibleModel) CollectibleModel.findById(collectibleView.getId());
            if (collectibleModel != null)
                collectibleView.setUtil(collectibleModel);
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
            EpsilonModel.getINSTANCE().move();
            EpsilonController.checkWallImpact();
            EpsilonController.updateVertextAnchor();
            if (!EnemyController.isCreating)
                EnemyController.checkCollision();
            ShotController.checkCollision();

            for (int i = 0; i < CollectibleModel.items.size(); i++) {
                EpsilonController.checkCollectibleCollision((CollectibleModel) CollectibleModel.items.get(i));
            }

            StoreController.checkItemsTimes();
            SkillTreeController.checkSkillsTime();

            if (!EnemyController.isCreating && EnemyModel.items.size() == 0)
                GameController.createWave();

            if (EpsilonModel.getINSTANCE().HP <= 0 && GameSettings.isGameRun)
                GameController.GameOver();
        }
        upsCount++;
    }

}
