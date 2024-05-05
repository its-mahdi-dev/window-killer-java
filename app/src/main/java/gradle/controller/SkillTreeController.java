package gradle.controller;

import java.util.*;
import java.awt.event.*;
import javax.swing.Timer;

import org.json.simple.JSONObject;

import gradle.model.EpsilonModel;
import gradle.model.EpsilonVertexModel;
import gradle.model.Model;

public class SkillTreeController {
    public static final Map<String, Long> skillsTime = new HashMap<>();
    public static int enemy_hp_decrease;
    public static int epsilon_hp_increase;
    static {
        skillsTime.put("ares", System.currentTimeMillis() - 6 * 60000);
        skillsTime.put("aceso", System.currentTimeMillis() - 6 * 60000);
        skillsTime.put("proteus", System.currentTimeMillis() - 6 * 60000);
    }

    public static final long MIN_SKILL_TIME = 5 * 60000;

    static Timer timer;

    public static void keyControl(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_F:
                setAres();
                break;
            case KeyEvent.VK_H:
                setAceso();
                break;
            case KeyEvent.VK_J:
                setProteus();
                break;
            default:
                break;
        }
    }

    private static void setAres() {
        if (System.currentTimeMillis() - skillsTime.get("ares") > MIN_SKILL_TIME) {
            skillsTime.put("ares", System.currentTimeMillis());
            enemy_hp_decrease += 2;
        }
    }

    private static void setAceso() {
        if (System.currentTimeMillis() - skillsTime.get("aceso") > MIN_SKILL_TIME) {
            skillsTime.replace("aceso", System.currentTimeMillis());
            epsilon_hp_increase += 1;
            timer = new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (System.currentTimeMillis() - skillsTime.get("aceso") < MIN_SKILL_TIME)
                        EpsilonModel.items.get(0).HP = Math.min(100,
                                EpsilonModel.items.get(0).HP + epsilon_hp_increase);
                }
            });
            timer.start();
        }
    }

    private static void setProteus() {
        if (System.currentTimeMillis() - skillsTime.get("proteus") > MIN_SKILL_TIME) {
            skillsTime.put("proteus", System.currentTimeMillis());
            EpsilonVertexModel.create();
        }
    }

    public static void checkSkillsTime() {

    }

    public static boolean buySkill(int xp) {
        JSONObject data = JsonHelper.readJsonFromFile("app/src/main/resources/data/data.json");
        int userXP = Integer.parseInt(data.get("xp").toString());
        if (userXP >= xp)
            return true;

        return false;

    }

}
