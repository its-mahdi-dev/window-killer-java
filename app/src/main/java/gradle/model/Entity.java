package gradle.model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Timer;

import gradle.controller.Constants;
import gradle.controller.Utils;
import gradle.interfaces.Rotation;
import gradle.model.enemies.WyrmEnemy;

public abstract class Entity extends Model {

    public boolean isMoving = true;
    public boolean ableMove = true;
    public double impact_time;
    public double impact_speed;
    public boolean isImpacting;
    public boolean visible = true;
    public boolean rigid;
    public double speed = 0;
    public int HP;
    public double HP_time;
    public double max_speed;
    public double velocity;
    public boolean hovering;
    Timer timer;

    public Map<String, Long> times = new HashMap<>();
    public Point2D direction = new Point2D.Double(0, 0);
    public Map<String, Timer> timers = new HashMap<>();

    public Entity() {
        saveIgnore.add("timer");
        saveIgnore.add("timers");
    }

    @Override
    public void addItem(Model item) {
        velocity = max_speed / Constants.ACCELERATION;
        super.addItem(item);
    }

    public void setDirection(Point2D direction) {
        if (!isImpacting)
            this.direction = direction;
    }

    public void move(Point2D direction, double speed) {
        if (ableMove)
            anchor = new Point2D.Double(anchor.getX() + direction.getX() * speed,
                    anchor.getY() + direction.getY() * speed);
    }

    public void move() {
        if (timer == null) {
            timer = new Timer(50, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (isImpacting) {
                        if (speed > max_speed) {
                            speed -= velocity;
                        }
                    } else if (speed <= max_speed && speed >= 0) {
                        if (isMoving)
                            speed += velocity;
                        else
                            speed -= velocity;

                        if (speed < 0)
                            speed = 0;
                        if (speed > max_speed)
                            speed = max_speed;
                    }
                }
            });
            timer.start();
        }
        if (System.currentTimeMillis() - impact_time > 50 && speed <= max_speed) {
            isImpacting = false;
        }

        move(direction, speed);
        if (this instanceof Rotation && isImpacting && ableMove) {
            Rotation entity = (Rotation) this;
            entity.moveRotaion(speed);
        }
    }

    public void setImpact(Point2D point2d, boolean isCollision) {
        setImpact(point2d, isCollision, false);

    }

    public void setImpact(Point2D point2d, boolean isCollision, boolean correctDirection) {

        setImpact(point2d, max_speed * impact_speed, correctDirection);
        if (isCollision)
            setEnemyImpacts();

    }

    public void setImpact(Point2D point2d) {
        setImpact(point2d, true);

    }

    public void setImpact(Point2D point2d, double speed) {
        setImpact(point2d, speed, false);
    }

    public void setImpact(Point2D point2d, double speed, boolean correctDirection) {
        if (hovering)
            return;
        if (this instanceof EnemyModel && ((EnemyModel) this).type == EnemyType.wyrm) {
            // if () {
            if (System.currentTimeMillis() - impact_time > 1000) {
                ((WyrmEnemy) this).angleMove = 0;
                ((WyrmEnemy) this).clockwise = !((WyrmEnemy) this).clockwise;
                System.out.println(((WyrmEnemy) this).clockwise);
            }
            // }
        } else {
            if (isMoving && !correctDirection) {
                direction = new Point2D.Double(point2d.getX() * direction.getX(), point2d.getY() * direction.getY());
            } else {
                direction = point2d;
            }
        }
        // anchor = new Point2D.Double(anchor.getX() + (direction.getX() * 5),
        // anchor.getY() + (direction.getY() * 5));
        impact_time = System.currentTimeMillis();
        isImpacting = true;
        this.speed = speed;

    }

    public void setImpact() {
        setImpact(true);
    }

    public void setImpact(boolean isCollision) {
        setImpact(new Point2D.Double(-1, -1), isCollision);
    }

    public void setEnemyImpacts(double max_distance, double increaseSpeed) {
        for (Model newModel : getAllEntities()) {
            Entity entity = (Entity) newModel;
            double distance = Utils.getDistance(anchor, entity.anchor);
            if (!entity.getId().equals(getId()) && distance < max_distance) {
                double newSpeed = entity.impact_speed * entity.max_speed
                        * ((max_distance - distance) / max_distance) * increaseSpeed;
                Point2D newDirection = Utils.getDirection(anchor, entity.anchor);
                // if (newDirection.getX() * entity.direction.getX() <= 0
                // && newDirection.getY() * entity.direction.getY() <= 0)
                if (entity instanceof EnemyModel) {
                    EnemyModel enemyModel = (EnemyModel) entity;
                    if (enemyModel.type != EnemyType.wyrm)
                        entity.setImpact(newDirection, newSpeed, true);
                } else
                    entity.setImpact(newDirection, newSpeed, true);
            }
        }
    }

    public void setEnemyImpacts() {
        setEnemyImpacts(Constants.MAX_DISTANCE_IMPACT, 1);
    }

    public static List<Model> getAllEntities() {

        List<Model> all = new ArrayList<>();

        // all.addAll(EnemyModel.items);
        all.add(EpsilonModel.getINSTANCE());

        return all;
    }

    public void decreasHp(Integer hp) {
        if (System.currentTimeMillis() - HP_time > 200)
            HP -= hp;
    }
}
