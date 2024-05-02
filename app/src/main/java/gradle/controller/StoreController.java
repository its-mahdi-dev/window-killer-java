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
                increaseHP(10);
                break;
            case 1:
                increaseShots();
                break;
            case 2:
                wave();
                break;
            default:
                break;
        }

    }

    private static void increaseHP(int HP) {
        EpsilonModel epsilonModel = (EpsilonModel) EpsilonModel.items.get(0);
        epsilonModel.HP = Math.min(100, epsilonModel.HP + HP);
    }

    private static void increaseShots() {
        shotsNumber = 3;
        shotTime = System.currentTimeMillis();
    }

    private static void wave() {
        EpsilonModel.items.get(0).setEnemyImpacts(Constants.MAX_DISTANCE_IMPACT * 10, 1.4);
    }

    public static void checkItemsTimes() {
        if (System.currentTimeMillis() - shotTime > 10000) {
            shotsNumber = 1;
        }
    }
}
