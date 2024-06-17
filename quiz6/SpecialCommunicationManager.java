package quiz6;

import org.json.JSONObject;
import quiz5.CommunicationManager;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class SpecialCommunicationManager extends CommunicationManager {
    private String commonServiceUrl;
    private String specialServiceUrl;

    public SpecialCommunicationManager(String commonServiceUrl, String specialServiceUrl) {
        super(commonServiceUrl);
        this.commonServiceUrl = commonServiceUrl;
        this.specialServiceUrl = specialServiceUrl;
    }

    @Override
    public JSONObject sendMessage(JSONObject messageData) throws Exception {
        String serviceUrl = commonServiceUrl;
        if (messageData.getString("message").contains("help")) {
            serviceUrl = specialServiceUrl;
        }
        URL url = new URL(serviceUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; utf-8");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);

        try(OutputStream os = conn.getOutputStream()) {
            byte[] input = messageData.toString().getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        try(Scanner scanner = new Scanner(conn.getInputStream(), "UTF-8")) {
            String responseBody = scanner.useDelimiter("\\A").next();
            return new JSONObject(responseBody);
        }
    }

    public JSONObject receiveMessage(String messageId, boolean special) throws Exception {
        String serviceUrl = special ? specialServiceUrl : commonServiceUrl;
        URL url = new URL(serviceUrl + "/" + messageId);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        try(Scanner scanner = new Scanner(conn.getInputStream(), "UTF-8")) {
            String responseBody = scanner.useDelimiter("\\A").next();
            return new JSONObject(responseBody);
        }
    }
}