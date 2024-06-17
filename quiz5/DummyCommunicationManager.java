package quiz5;

import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class DummyCommunicationManager {
    private Map<Integer, JSONObject> messages = new HashMap<>();
    private int messageIdCounter = 1;

    public JSONObject sendMessage(JSONObject messageData) {
        int messageId = messageIdCounter++;
        messages.put(messageId, messageData);
        return new JSONObject().put("status", "success").put("message_id", messageId);
    }

    public JSONObject receiveMessage(int messageId) {
        if (messages.containsKey(messageId)) {
            return messages.get(messageId);
        } else {
            return new JSONObject().put("status", "error").put("message", "Message not found");
        }
    }
}