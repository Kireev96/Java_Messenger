package com.server;

import com.data.Message;

import java.util.ArrayList;
import java.util.logging.Logger;

public class ParsingCurrent extends RecipientMessages{
    private static final Logger LOGGER = Logger.getLogger(ParsingCurrent.class.getName());

    private int indexString = 0;
    private int indexList = 0;
    private ArrayList<String> list;

    protected ParsingCurrent() {
        super();
        messageFields = new ArrayList<>();
        list = new FileIO().fileOutput(FILE_CURRENT_PATH, FILE_CURRENT_NAME);

    }
    protected boolean parsing() {
        if (list == null) {
            LOGGER.info("Current file is empty");
            return true;
        }

        try {
            while (indexList<list.size()) {
                indexString = 0;
                Message tempMessage = new Message();
                tempMessage.setId(Integer.parseInt(getString()));
                indexList++;
                while (indexList < list.size() && !list.get(indexList).equals("")) {
                    indexString= 0;
                    tempMessage.getMessageFields().put(Integer.parseInt(getString()), getString());
                    indexList++;
                }
                messageFields.add(tempMessage);
                indexList++;
            }
        } catch (Exception exception) {
            LOGGER.severe("Error parsing: " + exception);
            return false;
        }
        LOGGER.info("Current file successfully parsing");
        return true;
    }

    private String getString() {
        String string = new String();
        while (list.get(indexList).charAt(indexString) != '=')
            indexString++;
        indexString++;
        while (list.get(indexList).charAt(indexString) != ';') {
            string += list.get(indexList).charAt(indexString);
            indexString++;
        }
        return string;
    }
}
