package controller;

import eventHandling.OnGitEventListener;
import eventHandling.OnLocalDataUpdatedListener;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class UpdateHandoutDataController  {
    private static UpdateHandoutDataController single_instance = null;
    private OnLocalDataUpdatedListener onLocalDataUpdatedListener;

    public static UpdateHandoutDataController getInstance() {
        if (single_instance == null) {
            single_instance = new UpdateHandoutDataController();
        }
        return single_instance;
    }

    private UpdateHandoutDataController(){
    }

    public void addListener(OnLocalDataUpdatedListener listener) {
        this.onLocalDataUpdatedListener = listener;
    }


    public void updateLocalStorageData(){
        System.out.println("UpdateHandoutDataController: updateLocalStorageData");
        try {
            ChecklistController.getInstance().comparePredefinedChecklistVersions();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        HandoutController.getInstance().updateLocalData();
        callLocalUpdatedListener();
    }

    private void callLocalUpdatedListener() {
        System.out.println("listener call callLocalUpdatedListener");
        if (onLocalDataUpdatedListener != null) {
            System.out.println("listener call callLocalUpdatedListener not null");
            onLocalDataUpdatedListener.OnLocalDataUpdatedEvent();
        }
    }
}
