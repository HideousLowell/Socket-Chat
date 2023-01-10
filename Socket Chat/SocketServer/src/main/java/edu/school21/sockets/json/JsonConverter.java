package edu.school21.sockets.json;

import edu.school21.sockets.models.Message;
import edu.school21.sockets.models.User;
import org.json.simple.JSONObject;

public class JsonConverter {

    public static JSONObject convert(String key, String value) {
        JSONObject obj = new JSONObject();
        obj.put(key, value);
        return obj;
    }

    public static JSONObject convert(User user, String message) {
        JSONObject obj = new JSONObject();
        obj.put("user", user.getName());
        obj.put("message", message);
        return obj;
    }

    public static JSONObject convert(Message message) {
        return convert(message.getUser(), message.getText());
    }

}
