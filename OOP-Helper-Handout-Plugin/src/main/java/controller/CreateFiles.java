package controller;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class CreateFiles {


    public static void createNewFile(File file){
        try {
            //TODO SEARCH FOR DUPLICATED CODE
            file.getParentFile().mkdirs();
            file.createNewFile();
        } catch (IOException e) {
            //TODO CATACH
            System.out.println(e);
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

    //TODO REMOVE
    public static void replaceFile(File repoFile, File localFile) {
        System.out.println("replaceFile: ");
        System.out.println("repo: " + repoFile.getPath());
        System.out.println("localFile: " + localFile.getPath());
        Path from = repoFile.toPath(); //convert from File to Path
        Path to = localFile.toPath(); //convert from String to Path
        try {
            Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
