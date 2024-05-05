package gradle.controller;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import netscape.javascript.JSObject;

public class JsonHelper {

    @SuppressWarnings("unchecked")

    public static JSONObject readJsonFromFile(String path) {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = new JSONObject();
        try (FileReader reader = new FileReader(path)) {
            Object obj = jsonParser.parse(reader);
            jsonObject = (JSONObject) obj;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static void writeJsonToFile(JSONObject jsonObject, String path) {
        try (FileWriter file = new FileWriter(path)) {
            file.write(jsonObject.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
