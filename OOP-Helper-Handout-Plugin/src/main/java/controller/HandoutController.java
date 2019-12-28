package controller;

import provider.LocalStorageDataProvider;

import java.io.File;
import java.io.IOException;

public class HandoutController {

    private static HandoutController single_instance = null;
    private File handoutLocalFile;


    public static HandoutController getInstance() {
        if (single_instance == null) {
            single_instance = new HandoutController();
        }
        return single_instance;
    }

    private HandoutController() {
        handoutLocalFile = LocalStorageDataProvider.getHandoutFileDirectory();
    }

    public void createHandoutFile() {
        //TODO TO FILE CONTROLLER MKDIRS
        handoutLocalFile.getParentFile().mkdirs();
        try {
            handoutLocalFile.createNewFile();
        } catch (IOException e) {
            //TODO CATACH
            System.out.println(e);
        }
        downloadCurrentData();
    }

    private void downloadCurrentData(){
        File handoutRepoFile = LocalStorageDataProvider.getInitHandoutFileDirectory();
        CreateFiles.replaceFile(handoutRepoFile, handoutLocalFile);
    }

    public void updateLocalData() {
        downloadCurrentData();
    }
}
