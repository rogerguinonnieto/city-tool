package api;

import io.github.cdimascio.dotenv.Dotenv;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import model.Message;

public class Telegram {
    private static final Dotenv dotenv = Dotenv.load();
    private static final String TELEGRAM_API_URL = "https://api.telegram.org";
    private static final String BOT_TOKEN = dotenv.get("TELEGRAM_BOT_TOKEN");
    private static final String CHAT_ID = dotenv.get("TELEGRAM_CHAT_ID");

    public static String sendMessage(String text) {
        Message message = new Message(Long.parseLong(CHAT_ID), text);

        Client client = ClientBuilder.newClient();
        
        // Target the Telegram API endpoint and path
        WebTarget targetSendMessage = client.target(TELEGRAM_API_URL)
                .path("/bot" + BOT_TOKEN + "/sendMessage");

        System.out.println("=== Sending Telegram Message ===");
        System.out.println("Chat ID: " + CHAT_ID + " | Message: " + text);

        // Send the HTTP POST request with the Message object converted to JSON
        String response = targetSendMessage.request()
                .post(Entity.entity(message, MediaType.APPLICATION_JSON), String.class);

        System.out.println("Telegram API Response: " + response);
        System.out.println("=== Message Sent ===\n");

        return response;
    }
}