package com.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Message extends Template {
    public Message() {}

    public Message(int id, int keys[], String stringMessage[]) {
        super.id = id;
        this.messageFields = new HashMap(keys.length);
        for(int i = 0; i<keys.length; i++)
            messageFields.put(keys[i], stringMessage[i]);
    }

    public HashMap getMessageFields() {
        return messageFields;
    }

    public String[] getAllValues() {
        String values[] = new String[messageFields.size()];
        Set<Map.Entry<Integer, String>> set = messageFields.entrySet();
        int i = 0;
        for(Map.Entry<Integer, String> s : set) {
            values[i] = s.getValue();
            i++;
        }
        return values;
    }

    @Override
    public String toString() {
        String string = new String();

        string += "Id=" + id + ";\n";
        Set<Map.Entry<Integer, String>> set = messageFields.entrySet();
        for(Map.Entry<Integer, String> s : set)
            string += "Key=" + s.getKey() + "; Value=" + s.getValue() + ";\n";
        return string;
    }
}