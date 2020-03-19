package controller;

import eventHandling.OnLocalDataUpdatedListener;

import java.io.FileNotFoundException;

//controller class for updating handout data
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
        try {
            ChecklistController.getInstance().comparePredefinedChecklistVersions();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        HandoutController.getInstance().updateLocalData();
        callLocalUpdatedListener();
    }

    private void callLocalUpdatedListener() {
        if (onLocalDataUpdatedListener != null) {
            onLocalDataUpdatedListener.OnLocalDataUpdatedEvent();
        }
    }
}
