package edu.school21.sockets.client;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    final Socket socket = new Socket();

    public boolean connect(int port) {
        try {
            socket.connect(new InetSocketAddress(port));
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public void run() {
        try (ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
            Thread outThread = new OutThread(out);
            Thread inThread = new InThread(in);
            inThread.join();
            outThread.interrupt();
            socket.close();
        } catch (IOException e) {
            System.out.println("ERROR: " +  e.getMessage());
        } catch (InterruptedException ignore) {
        }
    }

}