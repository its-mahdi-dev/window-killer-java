package gradle.controller;

import gradle.model.EpsilonModel;
import gradle.view.StorePanel;

public class StoreController {

    public static double shotTime = System.currentTimeMillis();
    public static int shotsNumber = 1;

    public static void handleStore(int index) {
        System.out.println("index: " + index);
        switch (index) {
            case 0:
                increaseHP(10, StorePanel.xp[0]);
                break;
            case 1:
                increaseShots(StorePanel.xp[1]);
                break;
            default:
                break;
        }

    }

    private static void increaseHP(int HP, int xp) {
        EpsilonModel epsilonModel = (EpsilonModel) EpsilonModel.items.get(0);
        epsilonModel.HP = Math.min(100, epsilonModel.HP + HP);
        epsilonModel.XP -= xp;
    }

    private static void increaseShots(int xp) {
        shotsNumber = 3;
        shotTime = System.currentTimeMillis();
        EpsilonModel epsilonModel = (EpsilonModel) EpsilonModel.items.get(0);
        epsilonModel.XP -= xp;
    }

    public static void checkItemsTimes() {
        if (System.currentTimeMillis() - shotTime > 10000) {
            shotsNumber = 1;
        }
    }
}
