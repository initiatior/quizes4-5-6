package quiz5;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONObject;

public class CommunicationManager {
    private String serviceUrl;

    public CommunicationManager(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public JSONObject sendMessage(JSONObject messageData) throws Exception {
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

    public JSONObject receiveMessage(String messageId) throws Exception {
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