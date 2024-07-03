package gradle.controller;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.awt.Polygon;

import gradle.interfaces.UPSController;
import gradle.model.EnemyModel;
import gradle.model.EpsilonModel;
import gradle.model.ShotModel;
import gradle.model.ShotType;
import gradle.view.GameFrame;
import gradle.view.GamePanel;
import gradle.view.charecretsView.ShotView;

public class ShotController implements UPSController {

    @Override
    public void check() {
        for (int i = 0; i < ShotModel.items.size(); i++) {
            ShotModel shotModel = (ShotModel) ShotModel.items.get(i);
            shotModel.move();

            if (shotModel.shotType == ShotType.epsilon) {
                checkShotWithPanel(shotModel);
                for (int j = 0; j < EnemyModel.items.size(); j++) {
                    EnemyModel enemyModel = (EnemyModel) EnemyModel.items.get(j);
                    if (checkEpsilonShot(enemyModel, shotModel)) {
                        enemyModel.HP -= shotModel.power + (SkillTreeController.enemy_hp_decrease);
                        if (enemyModel.HP >= 0)
                            // Utils.playMusic("app/src/main/java/gradle/assets/musics/ah.wav");
                            if (ShotModel.items.contains(shotModel)) {
                                remove(shotModel.getId());
                            }
                        if (enemyModel.isImpacting) {
                            enemyModel.impact_speed *= 1.05;
                            enemyModel.setImpact(new Point2D.Double(1, 1), false);
                        } else
                            enemyModel.setImpact(false);
                    }
                    if (enemyModel.HP <= 0) {
                        EnemyController.remove(enemyModel.getId());
                    }

                }
            } else if (shotModel.shotType == ShotType.enemy) {
                if (Utils.getDistance(shotModel.anchor,
                        EpsilonModel.getINSTANCE().anchor) <= EpsilonModel.getINSTANCE().w / 2) {
                    EpsilonModel.getINSTANCE().HP -= shotModel.power;
                    if (ShotModel.items.contains(shotModel)) {
                        remove(shotModel.getId());
                    }
                } else {
                    if (ShotModel.items.contains(shotModel)) {
                        checkEnemyShots(shotModel);
                    }
                }
            }
        }
        for (int i = 0; i < ShotView.items.size(); i++) {
            ShotView shotView = (ShotView) ShotView.items.get(i);
            ShotModel shotModel = (ShotModel) ShotModel.findById(shotView.getId());
            if (shotModel != null)
                shotView.setUtil(shotModel);
        }
    }

    public static void checkShotWithPanel(ShotModel shotModel) {
        int dx = 0;
        int dy = 0;
        int dw = 0;
        int dh = 0;
        ArrayList<GamePanel> currentPanels = EpsilonModel.getINSTANCE().currentPanels;
        int maxY = currentPanels.get(0).getY() + currentPanels.get(0).getHeight();
        int maxX = currentPanels.get(0).getX() + currentPanels.get(0).getWidth();
        int minX = currentPanels.get(0).getX();
        int minY = currentPanels.get(0).getY();
        for (GamePanel panel : currentPanels) {
            int panelY = panel.getY();
            int panelX = panel.getX();
            if (panelY < minY)
                minY = panelY;
            if (panelX < minX)
                minX = panelX;
            if (panelY + panel.getHeight() > maxY)
                maxY = panelY + panel.getHeight();
            if (panelX + panel.getWidth() > maxX)
                maxX = panelX + panel.getWidth();
        }
        if (shotModel.anchor.getX() <= minX) {
            dx = -1;
            dw = 1;
        } else if (shotModel.anchor.getX() > maxX) {
            dw = 1;
            dx = 1;
        } else if (shotModel.anchor.getY() > maxY) {
            dh = 1;
            dy = 1;
        } else if (shotModel.anchor.getY() < minY) {
            dy = -1;
            dh = 1;
        }
        if (dx != 0 || dy != 0)
            EpsilonModel.getINSTANCE().currentPanels.get(0).location = new Point2D.Double(dx, dy);
        if (dh != 0 || dw != 0)
            EpsilonModel.getINSTANCE().currentPanels.get(0).size = new Point2D.Double(dw, dh);

        if (dy != 0 || dx != 0 || dh != 0 || dw != 0) {
            remove(shotModel.getId());
            EpsilonModel.getINSTANCE().currentPanels.get(0).setChanging();
        }

    }

    public static void remove(String Id) {
        ShotModel shotModel = (ShotModel) ShotModel.findById(Id);
        if (shotModel != null) {
            ShotModel.removedItems.add(shotModel);
            ShotView.removedItems.add(ShotView.findById(shotModel.getId()));
            ShotModel.items.remove(shotModel);
            ShotView.items.removeIf(shot -> shot.getId() == shotModel.getId());
        }

    }

    private static void checkEnemyShots(ShotModel shotModel) {
        if (shotModel.anchor.getX() < 0 || shotModel.anchor.getX() > GameFrame.getINSTANCE().getWidth()
                || shotModel.anchor.getY() < 0 || shotModel.anchor.getY() > GameFrame.getINSTANCE().getHeight())
            remove(shotModel.getId());
    }

    private static boolean checkEpsilonShot(EnemyModel enemyModel, ShotModel shotModel) {
        Polygon polygon = new Polygon(enemyModel.getXpointsInt(), enemyModel.getYpointsInt(),
                enemyModel.getEnemyPointsNumber());
        if (polygon.contains(shotModel.anchor))
            return true;
        return false;
    }

    public static void removeAll() {
        for (int i = ShotModel.items.size() - 1; i >= 0; i--) {
            remove(ShotModel.items.get(i).getId());
        }
    }
}
