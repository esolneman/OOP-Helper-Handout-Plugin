package provider.helper;
import net.lingala.zip4j.ZipFile;

import java.io.*;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class DownloadTask {

    private String path;

    public DownloadTask(String path) {
        this.path = path;
    }

    public void run(File output) {
        System.out.println("Start downloading [" + this.path + "] to " + output.getAbsolutePath());
        try (BufferedInputStream in = new BufferedInputStream(new URL(this.path).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(output)) {
            byte dataBuffer[] = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
            System.out.println("Finished download file");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void hashFiles(File newVersion, File oldVersion){
        //TODO Add Listener
    }

    //https://stackoverflow.com/a/14656534
    public void unzipFile(File in, File out) {
        try {
            ZipFile zipFile = new ZipFile(in);
            zipFile.extractAll(out.getPath());
            /*ZipInputStream zis = new ZipInputStream(new FileInputStream(in));
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                File newFile = new File(out, String.valueOf(zipEntry));
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                zipEntry = zis.getNextEntry();
            }
            zis.closeEntry();
            zis.close();*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
