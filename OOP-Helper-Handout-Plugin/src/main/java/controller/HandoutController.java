package controller;

import provider.LocalStorageDataProvider;

import java.io.File;

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
        FileHandleController.createNewFile(handoutLocalFile);
        downloadCurrentData();
    }

    private void downloadCurrentData(){
        File handoutRepoFile = LocalStorageDataProvider.getInitHandoutFileDirectory();
        FileHandleController.saveRepoFileInLocalFile(handoutRepoFile, handoutLocalFile);
    }

    public void updateLocalData() {
        downloadCurrentData();
    }
}
