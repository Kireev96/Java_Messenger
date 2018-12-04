package com.client;

import com.data.Message;
import com.data.Template;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Logger;

public class Creator {
    //Относительный путь не работает
    final private static String FILE_TEMPLATES_PATH = "/home/kireev/Java_Messenger/Client/src/com/client";
    final private static String FILE_TEMPLATES_NAME = "Templates";

    private static final Logger LOGGER = Logger.getLogger(Creator.class.getName());

    private static Template templates[];

    /**
     * Описание парсинга файла.
     * Вызывается метод fileOutput, который возвращает ArrayList состоящий из String.
     * Каждая строка это один Template.
     * Далее по очереди парсится каждая строка и присвается полям класса Template.
     * Первый набор символов в троке это id, дальнейшие наборы являются ключами key.
     * Сначала для каждого поля формируются String, а затем записываются в поле с преобразование типа.
     */
    public static boolean parsingTemplates() {
        ArrayList<String> list;
        list = new FileIO().fileOutput(FILE_TEMPLATES_PATH, FILE_TEMPLATES_NAME);

        if (list == null){
            LOGGER.severe("No templates");
            return false;
        }

        templates = new Template[list.size()];
        try {
            for (int indexTemplates = 0; indexTemplates < list.size(); indexTemplates++) {
                templates[indexTemplates] = new Template();
                String str = new String();
                int indexString = 0;
                while (list.get(indexTemplates).charAt(indexString) != ' ') {
                    str += list.get(indexTemplates).charAt(indexString);
                    indexString++;
                }
                templates[indexTemplates].setId(Integer.parseInt(str));
                str = "";
                indexString++;

                ArrayList<Integer> keys = new ArrayList();
                while (indexString < list.get(indexTemplates).length()) {
                    while (indexString < list.get(indexTemplates).length() &&
                            list.get(indexTemplates).charAt(indexString) != ' ') {
                        str += list.get(indexTemplates).charAt(indexString);
                        indexString++;
                    }
                    indexString++;
                    keys.add(Integer.parseInt(str));
                    str = "";
                }
                int keysToTemplate[] = new int[keys.size()];
                for (int indexKeys = 0; indexKeys < keysToTemplate.length; indexKeys++) {
                    keysToTemplate[indexKeys] = keys.get(indexKeys);
                }
                templates[indexTemplates].setKeys(keysToTemplate);
            }
        } catch (Exception exception) {
            LOGGER.severe("Error parsing: " + exception);
            return false;
        }
        LOGGER.info("Templates file successfully parsing");
        return true;
    }

    public static Message generatorMessage() {
        Message message;
        int indexTemplate = choiceTemplate();
        String stringMessage[] = new String[templates[indexTemplate].getKeys().length];
        for (int indexString = 0; indexString<stringMessage.length; indexString++) {
            stringMessage[indexString] = getValueMassage();
        }
        message = new Message(templates[indexTemplate].getId(), templates[indexTemplate].getKeys(), stringMessage);
        LOGGER.info("Message is generate");
        return message;
    }

    private static int choiceTemplate() {
        return new Random().nextInt(templates.length);
    }

    private static String getValueMassage() {
        return String.valueOf(new Random().nextInt(10) + 1);
    }
}
