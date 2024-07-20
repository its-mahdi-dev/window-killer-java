package gradle.controller;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.awt.geom.Point2D;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class PropertyUtil {
    @SuppressWarnings("unchecked")
    public static JSONObject getPropertiesAsJSON(Object obj, List<String> ignore) {
        JSONObject jsonObject = new JSONObject();
        Class<?> objClass = obj.getClass();

        while (objClass != null) {
            Field[] fields = objClass.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true); // Allows access to private fields
                if (ignore.contains(field.getName()))
                    continue;
                try {
                    Object value = field.get(obj);
                    if (value != null && isSupportedType(value)) {
                        if (value instanceof Point2D) {
                            jsonObject.put(field.getName(), convertPoint2D((Point2D) value));
                        } else if (value instanceof List) {
                            jsonObject.put(field.getName(), convertList((List<?>) value));
                        } else if (hasGetIdMethod(value)) {
                            jsonObject.put(field.getName(), invokeGetId(value));
                        } else {
                            jsonObject.put(field.getName(), value);
                        }
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            objClass = objClass.getSuperclass(); // Move to the superclass
        }

        return jsonObject;
    }

    private static boolean isSupportedType(Object value) {
        return value instanceof Boolean ||
                value instanceof Integer ||
                value instanceof String ||
                value instanceof Double ||
                value instanceof Long ||
                value instanceof ArrayList ||
                value instanceof List ||
                value instanceof Map ||
                value instanceof double[] ||
                value instanceof int[] ||
                value instanceof Point2D;
    }

    private static boolean hasGetIdMethod(Object obj) {
        try {
            Method method = obj.getClass().getMethod("getId");
            return method.getReturnType() == String.class; // Assuming getId returns a String
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    private static String invokeGetId(Object obj) {
        try {
            Method method = obj.getClass().getMethod("getId");
            return (String) method.invoke(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static JSONObject convertPoint2D(Point2D point) {
        JSONObject pointJSON = new JSONObject();
        pointJSON.put("x", point.getX());
        pointJSON.put("y", point.getY());
        return pointJSON;
    }

    private static JSONArray convertList(List<?> list) {
        JSONArray jsonArray = new JSONArray();
        for (Object item : list) {
            if (item instanceof Point2D) {
                jsonArray.add(convertPoint2D((Point2D) item));
            } else if (hasGetIdMethod(item)) {
                jsonArray.add(invokeGetId(item));
            } else {
                jsonArray.add(item);
            }
        }
        return jsonArray;
    }

}