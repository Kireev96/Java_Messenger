package com.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class Server {
    private static final Logger LOGGER = Logger.getLogger(Server.class.getName());

    private static Socket clientSocket;
    private static ServerSocket serverSocket;
    final private static int PORT = 4444;

    private static ExecutorService executorService;

    private static ParsingCurrent parsingCurrent = new ParsingCurrent();

    public static void main (String argv[]) {
        if (!parsingCurrent.parsing()) {
            LOGGER.severe("Can`t start server");
            return;
        }

        try {
            try {
                serverSocket = new ServerSocket(PORT);
                executorService = Executors.newCachedThreadPool();
                LOGGER.info("Server is work");
                Thread thread = new Thread(new RecipientMessages());
                thread.setName("Recipient thread");
                thread.start();
                while (true) {
                    LOGGER.info("Wait client");
                    clientSocket = serverSocket.accept();
                    executorService.submit(new Receiver(clientSocket));
                }
            } finally {
                serverSocket.close();
                clientSocket.close();
                executorService.shutdown();
                LOGGER.info("Server is close");
            }
        } catch (Exception exception) {
            LOGGER.severe("Error work server: " + exception);
        }
    }
}