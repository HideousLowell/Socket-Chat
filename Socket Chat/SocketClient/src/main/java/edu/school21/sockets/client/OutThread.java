package edu.school21.sockets.client;

import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;

public class OutThread extends Thread {

    ObjectOutputStream outputStream;
    BufferedReader reader;

    public OutThread(ObjectOutputStream objectOutputStream) {
        this.outputStream = objectOutputStream;
        reader = new BufferedReader(new InputStreamReader(System.in));
        start();
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                if (!reader.ready()) continue;
                String text = reader.readLine();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("message", text);
                outputStream.writeObject(jsonObject);
                outputStream.flush();
            }
        } catch (IOException e) {
            System.out.printf("Error: %s\n", e.getMessage());
        }
    }

}
