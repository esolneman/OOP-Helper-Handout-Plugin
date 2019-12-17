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
        handoutLocalFile.getParentFile().mkdirs();
        try {
            handoutLocalFile.createNewFile();
        } catch (IOException e) {
            //TODO CATACH
            System.out.println(e);
        }
        File notesRepoFile = LocalStorageDataProvider.getInitHandoutFileDirectory();
        CreateFiles.saveRepoFileInLocalFile(notesRepoFile, handoutLocalFile);
    }
}
