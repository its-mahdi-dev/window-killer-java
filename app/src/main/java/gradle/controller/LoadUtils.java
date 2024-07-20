package gradle.controller;

import java.awt.geom.Point2D;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.simple.*;

import gradle.model.EnemyModel;
import gradle.model.EnemyType;
import gradle.model.Model;
import gradle.view.charecretsView.EnemyView;

public class LoadUtils {
    public static <T extends Model> List<T> createEnemiesFromJson(JSONObject jsonObject, String key,
            Class<T> enemyClass) {
        List<T> enemies = new ArrayList<>();
        JSONArray jsonArray = (JSONArray) jsonObject.get(key);
        for (Object obj : jsonArray) {
            JSONObject enemyJson = (JSONObject) obj;
            try {
                T enemy = enemyClass.getDeclaredConstructor().newInstance();
                populateEnemyFromJson(enemy, enemyJson);
                enemies.add(enemy);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return enemies;
    }

    public static void populateEnemyFromJson(Object enemy, JSONObject jsonObject) {
        Class<?> objClass = enemy.getClass();

        while (objClass != null) {
            Field[] fields = objClass.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                Object value = jsonObject.get(field.getName());
                if (value != null) {
                    try {
                        if (field.getType().equals(Point2D.class) && value instanceof JSONObject) {
                            JSONObject pointJson = (JSONObject) value;
                            double x = convertToDouble(pointJson.get("x"));
                            double y = convertToDouble(pointJson.get("y"));
                            field.set(enemy, new Point2D.Double(x, y));
                        } else if (field.getType().equals(List.class) && value instanceof JSONArray) {
                            JSONArray jsonArray = (JSONArray) value;
                            List<Point2D> pointList = (List<Point2D>) field.get(enemy);
                            for (Object item : jsonArray) {
                                if (item instanceof JSONObject) {
                                    JSONObject pointJson = (JSONObject) item;
                                    double x = convertToDouble(pointJson.get("x"));
                                    double y = convertToDouble(pointJson.get("y"));
                                    pointList.add(new Point2D.Double(x, y));
                                } else {
                                    if (item instanceof Point2D)
                                        pointList.add((Point2D) item);
                                }
                            }
                        } else if (field.getType().equals(Map.class) && value instanceof JSONObject) {
                            Map<Object, Object> map = (Map<Object, Object>) field.get(enemy);
                            JSONObject jsonMap = (JSONObject) value;
                            for (Object key : jsonMap.keySet()) {
                                Object mapValue = jsonMap.get(key);
                                map.put(key, convertToAppropriateType(mapValue));
                            }
                        } else {
                            setFieldValue(field, enemy, convertToAppropriateType(value));
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            objClass = objClass.getSuperclass();
        }
    }

    private static Object convertToAppropriateType(Object value) {
        if (value instanceof Number) {
            Number number = (Number) value;
            if (number instanceof Integer) {
                return number.longValue(); // Convert Integer to Long
            } else if (number instanceof Long) {
                return number;
            } else if (number instanceof Double) {
                return number;
            } else if (number instanceof Float) {
                return number.doubleValue();
            }
        } else if (value instanceof Boolean) {
            return value; // Return Boolean as is
        } else if (value instanceof String) {
            return value; // Return String as is
        }
        return value; // Default return value
    }

    private static double convertToDouble(Object value) {
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        } else if (value instanceof String) {
            try {
                return Double.parseDouble((String) value);
            } catch (NumberFormatException e) {
                e.printStackTrace(); // Handle the exception
            }
        }
        return 0.0; // Default return value for unexpected types
    }

    private static void setFieldValue(Field field, Object obj, Object value) throws IllegalAccessException {
        Class<?> fieldType = field.getType();

        if (fieldType.equals(int.class) && value instanceof Number) {
            field.setInt(obj, ((Number) value).intValue());
        } else if (fieldType.equals(double.class) && value instanceof Number) {
            field.setDouble(obj, ((Number) value).doubleValue());
        } else if (fieldType.equals(long.class) && value instanceof Number) {
            field.setLong(obj, ((Number) value).longValue());
        } else if (fieldType.equals(float.class) && value instanceof Number) {
            field.setFloat(obj, ((Number) value).floatValue());
        } else if (fieldType.equals(boolean.class) && value instanceof Boolean) {
            field.setBoolean(obj, (Boolean) value);
        } else {
            field.set(obj, value);
        }
    }

    private static boolean hasSetIdMethod(Class<?> cls, Object value) {
        try {
            Method method = cls.getMethod("setId", String.class);
            return method.getReturnType() == Void.TYPE; // Assuming setId returns void
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    public static <T extends Model, V extends EnemyView> void setupEnemies(
            JSONObject jsonObject, String key, Class<T> enemyClass, EnemyType type, Class<V> viewClass) {
        List<T> enemies = createEnemiesFromJson(jsonObject, key, enemyClass);
        for (T enemy : enemies) {
            EnemyModel enemyModel = (EnemyModel) enemy;
            enemyModel.type = type;
            enemyModel.setRelativePoints();
            try {
                V view = viewClass.getDeclaredConstructor(String.class, EnemyType.class)
                        .newInstance(enemyModel.getId(), type);
                view.addItem(view);
                view.setUtil(enemyModel);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            Field itemsField = enemyClass.getField("items");
            List<T> items = (List<T>) itemsField.get(null);
            items.addAll(enemies);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
