package com.server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Logger;

public class FileIO  {
    private static final Logger LOGGER = Logger.getLogger(FileIO.class.getName());

    public boolean fileInput(ArrayList<String> list, boolean overwriteFile, String filePath, String fileName) {
        try {
            File file = new File(filePath, fileName);
            try (FileWriter writer = new FileWriter(file, !overwriteFile)) {
                for (int i = 0; i<list.size(); i++)
                    writer.append(list.get(i) + "\n");
                writer.flush();
            } catch (IOException ioException) {
                LOGGER.warning("Write to file error: " + ioException);
                return false;
            }
        } catch (Exception exception) {
            LOGGER.warning("File not found: " + exception);
            return false;
        }
        return true;
    }

    public ArrayList<String> fileOutput(String filePath, String fileName) {
        ArrayList<String> list = new ArrayList<>();
        try {
            File file = new File(filePath, fileName);
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextLine())
                    list.add(scanner.nextLine());
            } catch (IOException ioException) {
                LOGGER.severe("Read from file error: " + ioException);
                list = null;
            }
        } catch (Exception exception) {
            LOGGER.severe("File not found: " + exception);
            list = null;
        }
        return list;
    }
}
