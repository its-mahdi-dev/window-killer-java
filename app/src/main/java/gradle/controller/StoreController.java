package gradle.controller;

import java.util.HashMap;
import java.util.Map;

import gradle.interfaces.UPSController;
import gradle.model.EnemyModel;
import gradle.model.EpsilonModel;
import gradle.model.Model;
import gradle.view.StorePanel;

public class StoreController implements UPSController {

    public static double shotTime = System.currentTimeMillis();
    public static int shotsNumber = 1;
    public static final Map<StoreTypes, Integer> itemsXP = new HashMap<>();
    public static final Map<StoreTypes, String> itemNames = new HashMap<>();
    public static final Map<StoreTypes, Boolean> itemsActive = new HashMap<>();
    public static final Map<StoreTypes, Store> itemsInterface = new HashMap<>();
    public static final Map<StoreTypes, Long> itemsTimes = new HashMap<>();
    public static final Map<StoreTypes, Integer> itemsActiveTimes = new HashMap<>();
    public static final Map<Integer, StoreTypes> itemsID = new HashMap<>();

    static {
        itemsID.put(0, StoreTypes.hephaestus);
        itemsID.put(1, StoreTypes.athena);
        itemsID.put(2, StoreTypes.apollo);
        itemsID.put(3, StoreTypes.deimos);
        itemsID.put(4, StoreTypes.hypnos);
        itemsID.put(5, StoreTypes.phonoi);

        for (StoreTypes type : itemsID.values()) {
            itemsActive.put(type, false);
            itemsTimes.put(type, 0L);
        }
        itemNames.put(StoreTypes.hephaestus, "Hephaestus");
        itemNames.put(StoreTypes.athena, "Athena");
        itemNames.put(StoreTypes.apollo, "Apollo");
        itemNames.put(StoreTypes.deimos, "Deimos");
        itemNames.put(StoreTypes.hypnos, "Hypnos");
        itemNames.put(StoreTypes.phonoi, "Phonoi");

        itemsActiveTimes.put(StoreTypes.hephaestus, 10000);
        itemsActiveTimes.put(StoreTypes.athena, 10000);
        itemsActiveTimes.put(StoreTypes.apollo, 10000);
        itemsActiveTimes.put(StoreTypes.deimos, 10000);
        itemsActiveTimes.put(StoreTypes.hypnos, 10000);
        itemsActiveTimes.put(StoreTypes.phonoi, 10000);

        itemsXP.put(StoreTypes.hephaestus, 100);
        itemsXP.put(StoreTypes.athena, 75);
        itemsXP.put(StoreTypes.apollo, 50);
        itemsXP.put(StoreTypes.deimos, 120);
        itemsXP.put(StoreTypes.hypnos, 120);
        itemsXP.put(StoreTypes.phonoi, 200);

        itemsInterface.put(StoreTypes.hephaestus, new Store() {
            @Override
            public void store() {
                EpsilonModel.getINSTANCE().setEnemyImpacts(Constants.MAX_DISTANCE_IMPACT * 10, 1.4);
                System.out.println("wave");
            }

            @Override
            public void remove() {

            }
        });
        itemsInterface.put(StoreTypes.athena, new Store() {
            @Override
            public void store() {
                shotsNumber = 3;
            }

            @Override
            public void remove() {
                shotsNumber = 1;
            }
        });
        itemsInterface.put(StoreTypes.apollo, new Store() {
            @Override
            public void store() {
                EpsilonModel epsilonModel = EpsilonModel.getINSTANCE();
                epsilonModel.HP = Math.min(100, epsilonModel.HP + 10);
                System.out.println("here");
            }

            @Override
            public void remove() {

            }
        });
        itemsInterface.put(StoreTypes.deimos, new Store() {
            @Override
            public void store() {

            }

            @Override
            public void remove() {

            }
        });
        itemsInterface.put(StoreTypes.hypnos, new Store() {
            @Override
            public void store() {
                for(Model model : EnemyModel.getAllEnemies()){
                    EnemyModel enemyModel = (EnemyModel) model;
                    if(enemyModel.timers.get("shotTimer") != null) enemyModel.timers.get("shotTimer").stop();
                }
            }

            @Override
            public void remove() {
                for(Model model : EnemyModel.getAllEnemies()){
                    EnemyModel enemyModel = (EnemyModel) model;
                    if(enemyModel.timers.get("shotTimer") != null) enemyModel.timers.get("shotTimer").start();
                }
            }
        });
        itemsInterface.put(StoreTypes.phonoi, new Store() {
            @Override
            public void store() {

            }

            @Override
            public void remove() {

            }
        });

    }

    public enum StoreTypes {
        hephaestus, athena, apollo, deimos, hypnos, phonoi
    }

    interface Store {
        void store();

        void remove();
    }

    public static String handleStore(int index) {
        StoreTypes type = itemsID.get(index);
        if (type == null)
            return "item not found";

        if (EpsilonModel.getINSTANCE().XP >= itemsXP.get(type)) {
            itemsInterface.get(type).store();
            itemsTimes.put(type, System.currentTimeMillis());
            itemsActive.replace(type, true);
            EpsilonModel.getINSTANCE().XP -= itemsXP.get(type);
            return "success";
        } else {
            return String.format("you don't have enough XP for %s", itemNames.get(type));
        }
    }


    @Override
    public void check() {
        for (StoreTypes type : itemsActive.keySet()) {
            // System.out.println("ddd");
            if (System.currentTimeMillis() - itemsTimes.get(type) >= itemsActiveTimes.get(type)
                    && itemsActive.get(type)) {
                itemsActive.replace(type, false);
                itemsInterface.get(type).remove();
            }
        }

    }
}
