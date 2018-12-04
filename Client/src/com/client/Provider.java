package com.client;

import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.logging.Logger;

public class Provider implements Runnable{
    private static final Logger LOGGER = Logger.getLogger(Provider.class.getName());

    private static final int PORT = 4444;

    private Socket clientSocket = new Socket();
    private ObjectOutputStream out;

    @Override
    public void run() {
        try {
            try {
                clientSocket.connect(new InetSocketAddress(InetAddress.getLocalHost(),PORT), 10000);
                out = new ObjectOutputStream(clientSocket.getOutputStream());
                out.writeObject(new Creator().generatorMessage());
                out.flush();
                LOGGER.info("Message is send");
            } finally {
                clientSocket.close();
                out.close();
                LOGGER.info("Client socket is close");
            }
        } catch (Exception exception) {
            LOGGER.severe("Send message error: " + exception);
        }
    }
}
