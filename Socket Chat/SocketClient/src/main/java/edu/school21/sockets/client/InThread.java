package edu.school21.sockets.client;

import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.ObjectInputStream;

public class InThread extends Thread {

    private final String DISCONNECT = "21";
    private ObjectInputStream inputStream;

    public InThread(ObjectInputStream inputStream) {
        this.inputStream = inputStream;
        start();
    }

    @Override
    public void run() {
        try {
            while (true) {
                String message = receiveMessage();
                if (message.equals(DISCONNECT)) break;
                System.out.println(message);
            }
        } catch (IOException e) {
            System.err.printf("Error: %s\n", e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Unknown format");
        }
    }

    private String receiveMessage() throws IOException, ClassNotFoundException {
        JSONObject jsonObject = (JSONObject) inputStream.readObject();
        String user = (String) jsonObject.get("user");
        String body = (String) jsonObject.get("message");
        if (body == null)
            throw new IOException("Unknown response");
        if (user == null)
            return body;
        return String.format("%s : %s", user, body);
    }
}
