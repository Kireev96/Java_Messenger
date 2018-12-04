package com.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Template implements Serializable {
    private static final long serialVersionUID = 1L;
    protected int id;
    protected HashMap messageFields;

    public Template() {
        messageFields = new HashMap<Integer, String>();
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setKeys(int key[]) {
        for (int i = 0; i<key.length; i++)
            messageFields.put(key[i], "");

    }

    public int getId() {
        return id;
    }

    public int[] getKeys() {
        int keys[] = new int[messageFields.size()];
        Set<Map.Entry<Integer, String>> set = messageFields.entrySet();
        int i = 0;
        for(Map.Entry<Integer, String> s : set) {
            keys[i] = s.getKey();
            i++;
        }
        return keys;
    }
}