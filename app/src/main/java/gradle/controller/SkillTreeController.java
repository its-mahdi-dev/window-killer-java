package gradle.controller;

import java.util.*;
import java.awt.event.*;
import javax.swing.Timer;

import org.json.simple.JSONObject;

import gradle.interfaces.UPSController;
import gradle.model.EpsilonModel;
import gradle.model.EpsilonVertexModel;

public class SkillTreeController implements UPSController {

    public static final long MIN_SKILL_TIME = 5 * 60000;

    public static int enemy_hp_decrease;
    public static int enemy_hp_collision_decrease;
    public static int epsilon_hp_increase;

    public static final Map<SkillTypes, Long> skillsTime = new HashMap<>();
    public static final Map<SkillTypes, Boolean> skills = new HashMap<>();
    public static final Map<Integer, SkillTypes> skillKeys = new HashMap<>();
    public static final Map<SkillTypes, Skill> skillInterfaces = new HashMap<>();
    public static final Map<SkillTypes, Timer> skillTimers = new HashMap<>();
    public static final Map<SkillTypes, Boolean> activeSkills = new HashMap<>();

    public static final List<SkillTypes> skillNames = List.of(
            SkillTypes.ares,
            SkillTypes.aceso,
            SkillTypes.proteus,
            SkillTypes.astrape,
            SkillTypes.cerberus,
            SkillTypes.melampus,
            SkillTypes.chiron,
            SkillTypes.empusa,
            SkillTypes.dolus);

    static {
        for (SkillTypes skillType : skillNames) {
            skills.put(skillType, false);
            activeSkills.put(skillType, false);
            skillsTime.put(skillType, System.currentTimeMillis() - MIN_SKILL_TIME * 2);
        }

        skillKeys.put(KeyEvent.VK_1, SkillTypes.ares);
        skillKeys.put(KeyEvent.VK_2, SkillTypes.aceso);
        skillKeys.put(KeyEvent.VK_3, SkillTypes.proteus);
        skillKeys.put(KeyEvent.VK_4, SkillTypes.astrape);
        skillKeys.put(KeyEvent.VK_5, SkillTypes.cerberus);
        skillKeys.put(KeyEvent.VK_6, SkillTypes.melampus);
        skillKeys.put(KeyEvent.VK_7, SkillTypes.chiron);
        skillKeys.put(KeyEvent.VK_8, SkillTypes.empusa);
        skillKeys.put(KeyEvent.VK_9, SkillTypes.dolus);

        skillInterfaces.put(SkillTypes.ares, new Skill() {
            @Override
            public void skill() {
                enemy_hp_decrease += 2;
            }
        });
        skillInterfaces.put(SkillTypes.aceso, new Skill() {
            @Override
            public void skill() {
                epsilon_hp_increase += 1;
                Timer timer = new Timer(1000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (System.currentTimeMillis() - skillsTime.get(SkillTypes.aceso) < MIN_SKILL_TIME)
                            EpsilonModel.getINSTANCE().HP = Math.min(100,
                                    EpsilonModel.getINSTANCE().HP + epsilon_hp_increase);
                    }
                });
                timer.start();
                skillTimers.put(SkillTypes.aceso, timer);
            }
        });
        skillInterfaces.put(SkillTypes.proteus, new Skill() {
            @Override
            public void skill() {
                EpsilonVertexModel.create();
            }
        });
        skillInterfaces.put(SkillTypes.astrape, new Skill() {
            @Override
            public void skill() {
                enemy_hp_collision_decrease += 2;
            }
        });
        skillInterfaces.put(SkillTypes.cerberus, new Skill() {
            @Override
            public void skill() {

            }
        });
        skillInterfaces.put(SkillTypes.melampus, new Skill() {
            @Override
            public void skill() {

            }
        });
        skillInterfaces.put(SkillTypes.chiron, new Skill() {
            @Override
            public void skill() {

            }
        });
        skillInterfaces.put(SkillTypes.empusa, new Skill() {
            @Override
            public void skill() {

            }
        });
        skillInterfaces.put(SkillTypes.dolus, new Skill() {
            @Override
            public void skill() {

            }
        });
    }

    interface Skill {
        void skill();
    }

    enum SkillTypes {
        ares, aceso, proteus, astrape, cerberus, melampus, chiron, empusa, dolus
    }

    public static void keyControl(KeyEvent e) {
        int keyCode = e.getKeyCode();
        SkillTypes skillType = skillKeys.get(keyCode);

        if (skillType != null) {
            if (skills.get(skillType)) {
                if (skillsTime.get(skillType) < System.currentTimeMillis() - MIN_SKILL_TIME) {
                    skillInterfaces.get(skillType).skill();
                    skillsTime.put(skillType, System.currentTimeMillis());
                    activeSkills.put(skillType, true);
                }
            }
        }
    }

    @Override
    public void check() {
    }

    public static boolean buySkill(int xp) {
        JSONObject data = JsonHelper.readJsonFromFile("app/src/main/resources/data/data.json");
        int userXP = Integer.parseInt(data.get("xp").toString());
        if (userXP >= xp) {
            return true;
        }

        return false;

    }

    public static SkillTypes stringToSkillTypes(String slog) {
        switch (slog) {
            case "ares":
                return SkillTypes.ares;
            case "aceso":
                return SkillTypes.aceso;
            case "proteus":
                return SkillTypes.proteus;
            case "astrape":
                return SkillTypes.astrape;
            case "cerberus":
                return SkillTypes.cerberus;
            case "melampus":
                return SkillTypes.melampus;
            case "chiron":
                return SkillTypes.chiron;
            case "empusa":
                return SkillTypes.empusa;
            case "dolus":
                return SkillTypes.dolus;
            default:
                return null;
        }

    }

}
