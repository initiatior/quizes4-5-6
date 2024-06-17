package quiz5;

import org.json.JSONObject;

public class UserInteractionManager {
    private CommunicationManager commManager;

    public UserInteractionManager(boolean useDummy, String serviceUrl) {
        if (useDummy) {
            this.commManager = new DummyCommunicationManager();
        } else {
            this.commManager = new CommunicationManager(serviceUrl);
        }
    }

    public JSONObject interactWithUser(String userMessage) {
        try {
            JSONObject response = commManager.sendMessage(new JSONObject().put("message", userMessage));
            if (response.getString("status").equals("success")) {
                int messageId = response.getInt("message_id");
                return commManager.receiveMessage(messageId);
            } else {
                return new JSONObject().put("status", "error").put("message", "Failed to send message");
            }
        } catch (Exception e) {
            return new JSONObject().put("status", "error").put("message", e.getMessage());
        }
    }
}