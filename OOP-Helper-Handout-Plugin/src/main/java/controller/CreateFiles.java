package controller;

import java.io.*;

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
}
