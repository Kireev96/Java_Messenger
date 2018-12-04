package com.server;

import com.data.Message;

import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

public class Receiver implements Runnable{
    private static final Logger LOGGER = Logger.getLogger(Receiver.class.getName());

    Socket clientSocket;
    private static ObjectInputStream in;

    private Message message;
    private RecipientMessages recipientMessages;

    private Lock lock = new ReentrantLock();

    public Receiver(Socket clientSocket){
        this.clientSocket = clientSocket;
        recipientMessages = new RecipientMessages();
    }

    @Override
    public synchronized void run() {
        try {
            try {
                lock.lock();
                System.out.println("Start - " + Thread.currentThread().getName());
                LOGGER.info("Work on server - " + Thread.currentThread().getName());
                in = new ObjectInputStream(clientSocket.getInputStream());
                message = (Message) in.readObject();
                recipientMessages.addMessageInQueue(message);
                System.out.println("End - " + Thread.currentThread().getName());
                lock.unlock();
            } finally {
                in.close();
                LOGGER.info("End work on server - " + Thread.currentThread().getName());
            }
        } catch (Exception exception) {
            LOGGER.severe("Error work on server: " + exception);
        }
    }
}
