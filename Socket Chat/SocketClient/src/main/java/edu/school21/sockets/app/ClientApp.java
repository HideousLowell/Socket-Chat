package edu.school21.sockets.app;

import edu.school21.sockets.client.Client;

public class ClientApp {

    public static void main(String[] args) {

        if (args.length != 1 || !args[0].startsWith("--port="))
            exitWithError("Usage: socket-client --port=PORT");

        int port = 0;
        try {
            port = Integer.parseInt(args[0].substring("--port=".length()));
        } catch (NumberFormatException e) {
            exitWithError("Error: Port is not a number");
        }

        Client client = new Client();

        if (client.connect(port))
            client.run();
        else
            exitWithError("Failed to connect to the server on port " + port);

    }

    private static void exitWithError(String msg) {
        System.err.println(msg);
        System.exit(-1);
    }
}





