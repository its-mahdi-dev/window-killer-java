package gradle.controller;

import java.util.*;
import java.awt.event.*;

public class SkillTreeController {
    public static final Map<String, Long> skillsTime = new HashMap<>();
    public static final Map<String, Boolean> skills = new HashMap<>();

    static {
        skillsTime.put("ares", System.currentTimeMillis() - 6 * 60000);
        skillsTime.put("aceso", System.currentTimeMillis() - 6 * 60000);
        skillsTime.put("proteus", System.currentTimeMillis() - 6 * 60000);

        skills.put("ares", false);
        skills.put("aceso", false);
        skills.put("proteus", false);
    }

    public static final long MIN_SKILL_TIME = 5 * 60000;

    public static void keyControl(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_F:
                setAres();
                break;

            default:
                break;
        }
    }

    private static void setAres() {
        if (System.currentTimeMillis() - skillsTime.get("ares") > MIN_SKILL_TIME) {
            skillsTime.put("ares", System.currentTimeMillis());
            skills.replace("ares", true);
        }
    }

    public static void checkSkillsTime() {
        for (Map.Entry<String, Long> entry : skillsTime.entrySet()) {
            if (System.currentTimeMillis() - entry.getValue() > MIN_SKILL_TIME) {
                skills.replace(entry.getKey(), false);
            }
        }
    }

}
