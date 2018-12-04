package com.client;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class Client {
    private static Creator creator = new Creator();
    private static final Logger LOGGER = Logger.getLogger(Client.class.getName());

    private static ExecutorService executorService;

    public static void main (String argv[]) {
        if (!creator.parsingTemplates()) {
            LOGGER.severe("Can`t start client");
            return;
        }
        LOGGER.info("Client is worked");


        //Меню для отправки сообщений
        int choice = 0;
        int quantityMessages;
        executorService = Executors.newCachedThreadPool();
        while (choice != 2) {
            System.out.println("1) Send messages");
            System.out.println("2) Exit");
            Scanner scanner = new Scanner(System.in);
            choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    System.out.println("Quantity messages: ");
                    Scanner scannerQuantityMessages = new Scanner(System.in);
                    quantityMessages = scannerQuantityMessages.nextInt();
                    int counter = 0;
                    while (counter<quantityMessages) {
                        executorService.submit(new Provider());
                        counter++;
                    }

                    break;
                case 2:
                    executorService.shutdown();
                    System.out.println("Exit");
                    break;
                default:
                    System.out.println("Error");
            }
        }
        //



    }
}
