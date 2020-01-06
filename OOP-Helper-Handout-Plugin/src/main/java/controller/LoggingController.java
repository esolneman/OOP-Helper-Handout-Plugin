package controller;

import de.ur.mi.pluginhelper.User.User;
import de.ur.mi.pluginhelper.logger.Log;
import de.ur.mi.pluginhelper.logger.LogDataType;
import de.ur.mi.pluginhelper.logger.LogManager;
import de.ur.mi.pluginhelper.logger.SyncProgressListener;

import static environment.LoggingMessageConstants.*;

public class LoggingController {
    private static LoggingController single_instance = null;
    private de.ur.mi.pluginhelper.User.User user;
    private Log log;

    public static LoggingController getInstance() {
        if (single_instance == null) {
            single_instance = new LoggingController();
        }
        return single_instance;
    }

    private LoggingController() {}

    public void startLogging() {
        user = getCurrentUser();
        // Session Log für das Experiment mit dem Titel "Test" erstellen oder öffnen
        log = LogManager.openLog(user.getID(), EXPERIMENT_NAME);
        saveDataInLogger(LogDataType.IDE, IDE_VISIBILITY, IDE_OPENED);
    }

    // Nutzer erstellen oder laden
    public User getCurrentUser(){
        user = de.ur.mi.pluginhelper.User.User.getLocalUser();
        return user;
    }

    public void syncLoggingData(){
        //TODO UI?
        //UserResponse response = UserDialogManager.showConfirmationDialog("Möchten Sie die Logdatei hochladen?", "Freigegeben der Logdatei");
        //if (response == UserResponse.ACCEPT) {
            LogManager.syncLog(log, user, UPLOAD_SERVER_URL, new SyncProgressListener() {
                @Override
                public void onFinished() {
                    System.out.println("Upload finished");
                }

                @Override
                public void onFailed() {
                    System.out.println("Upload failed");
                }
            });
        //}
    }

    public void saveDataInLogger(LogDataType type, String label, String payload){
        // Daten im Log speichern
        log.log(user.getSessionID(), type, label, payload);
    }
}
