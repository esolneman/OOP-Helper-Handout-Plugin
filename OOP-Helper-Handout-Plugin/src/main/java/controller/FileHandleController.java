package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.*;

public class FileHandleController {


    public static void createNewFile(File file){
        try {
            file.getParentFile().mkdirs();
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //TODO SOUREC
    public static void saveRepoFileInLocalFile (File repoFile, File localFile) {
        BufferedReader inputStream = null;
        BufferedWriter outputStream = null;
        try {
            inputStream = new BufferedReader(new FileReader(repoFile));
            File UIFile = localFile;
            // if File doesnt exists, then create it
            if (!UIFile.exists()) {
                UIFile.createNewFile();
            }
            FileWriter filewriter = new FileWriter(UIFile.getAbsoluteFile());
            outputStream = new BufferedWriter(filewriter);
            String count;
            while ((count = inputStream.readLine()) != null) {
                outputStream.write(count);
            }
            outputStream.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void saveJsonObjectInFile(JsonObject jsonObject, File outputFile) {
        //https://stackoverflow.com/a/29319491
        try (Writer writer = new FileWriter(outputFile)) {
            Gson gson = new GsonBuilder().create();
            gson.toJson(jsonObject, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
