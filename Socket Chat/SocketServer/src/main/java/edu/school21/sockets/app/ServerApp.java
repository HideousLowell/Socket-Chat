package edu.school21.sockets.app;

import edu.school21.sockets.config.SocketsApplicationConfig;
import edu.school21.sockets.server.Server;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ServerApp {

    public static void main(String[] args) {
        if (args.length != 1 || !args[0].startsWith("--server-port="))
            exitWithError("Usage: ServerApp --server-port=PORT");

        int port = 0;
        try {
            port = Integer.parseInt(args[0].substring("--server-port=".length()));
        } catch (NumberFormatException e) {
            exitWithError("Error: Port is not a number");
        }

        ApplicationContext context = new AnnotationConfigApplicationContext(SocketsApplicationConfig.class);
        Server server = context.getBean(Server.class);
        server.exec(port);
    }

    private static void exitWithError(String msg) {
        System.err.println(msg);
        System.exit(-1);
    }
}