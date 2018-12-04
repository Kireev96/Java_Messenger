package com.server;

import com.data.Message;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Logger;

public class RecipientMessages implements Runnable{
    private static final Logger LOGGER = Logger.getLogger(RecipientMessages.class.getName());

    //Относительный путь не работает
    final private static String FILE_HISTORY_PATH = "/home/kireev/Java_Messenger/Server/src/com/server";
    final private static String FILE_HISTORY_NAME = "History";
    final protected static String FILE_CURRENT_PATH = "/home/kireev/Java_Messenger/Server/src/com/server";
    final protected static String FILE_CURRENT_NAME = "Current State";

    protected volatile static ArrayList<Message> messageFields;
    private volatile static Queue<Message> queueMessages;
    private Message tempMessage;

    public RecipientMessages() {
        queueMessages = new LinkedList<>();
    }

    @Override
    public void run() {
            while (true) {
                if(queueMessages.isEmpty()) {
                    try {
                        Thread.currentThread().sleep(1000);
                    } catch (InterruptedException e) {
                        LOGGER.warning("Thread can`t sleep: " + e);
                    }
                    continue;
                }

                try {
                    tempMessage = queueMessages.poll();
                } catch (Exception exception) {
                    LOGGER.severe("Error take from queue: " + exception);
                }
                recipient();
            }
    }

    public void addMessageInQueue(Message message) {
        queueMessages.add(message);
    }

    public void recipient() {
        System.out.println("Start recipient - " + Thread.currentThread().getName());
        LOGGER.info("Work with current " + Thread.currentThread().getName());
        ArrayList<Message> newMessage = new ArrayList<>();
        newMessage.add(this.tempMessage);
        if (!writeToFile(newMessage, false, FILE_HISTORY_PATH, FILE_HISTORY_NAME))
            LOGGER.warning("New message not write to history file");
        current();
        System.out.println("End recipient - " + Thread.currentThread().getName());
    }

    private boolean writeToFile(ArrayList<Message> messages, boolean overwriteFile,
                                String filePath, String fileName) {
        ArrayList<String> messagesToFile = new ArrayList<>();
        for (int i = 0; i<messages.size(); i++)
            messagesToFile.add(messages.get(i).toString());

        boolean isWrite = new FileIO().fileInput(messagesToFile, overwriteFile,
                filePath, fileName);

        if (!isWrite) {
            LOGGER.warning("Not write to file");
            return false;
        }
        return true;
    }

    private void current() {
        if (this.messageFields.size() == 0) {
            LOGGER.info("File current is empty");
            this.messageFields.add(this.tempMessage);
            if (!writeToFile(messageFields, true, FILE_CURRENT_PATH, FILE_CURRENT_NAME))
                LOGGER.warning("New message not write to current file");
            return;
        }
        boolean idIsFound = false;
        for (int indexMessageFields = 0; indexMessageFields<this.messageFields.size(); indexMessageFields++) {
            if (this.messageFields.get(indexMessageFields).getId() == this.tempMessage.getId()) {
                idIsFound = true;
                int keysNewMessage[] = this.tempMessage.getKeys();
                int keysMessageFields[] = this.messageFields.get(indexMessageFields).getKeys();

                for (int indexNewKeys = 0; indexNewKeys<keysNewMessage.length; indexNewKeys++) {
                    boolean keyIsFound = false;
                    for (int indexKeyFields = 0; indexKeyFields<keysMessageFields.length; indexKeyFields++) {
                        if (keysMessageFields[indexKeyFields] == keysNewMessage[indexNewKeys]) {
                            this.messageFields.get(indexMessageFields)
                                    .getMessageFields()
                                    .replace(keysMessageFields[indexKeyFields],
                                            this.tempMessage.getMessageFields().get(keysNewMessage[indexNewKeys]));
                            keyIsFound = true;
                        }
                        if (indexKeyFields == keysMessageFields.length - 1 && !keyIsFound)
                            messageFields.get(indexMessageFields)
                                    .getMessageFields().
                                    put(keysNewMessage[indexNewKeys],
                                            this.tempMessage.getMessageFields().get(keysNewMessage[indexNewKeys]));
                    }
                }
            }
            if (indexMessageFields == messageFields.size() - 1 && !idIsFound)
                this.messageFields.add(this.tempMessage);
        }
        if (!writeToFile(messageFields, true, FILE_CURRENT_PATH, FILE_CURRENT_NAME))
            LOGGER.warning("New currents not write to current file");
    }
}
