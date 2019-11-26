package controller;

import de.ur.mi.pluginhelper.User.User;
import de.ur.mi.pluginhelper.logger.Log;
import de.ur.mi.pluginhelper.logger.LogDataType;
import de.ur.mi.pluginhelper.logger.LogManager;
import de.ur.mi.pluginhelper.logger.SyncProgressListener;

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
        //TODO user.gestSessionID oder getID
        log = LogManager.openLog(user.getID(), "MA-Wolfes");
        saveDataInLogger(LogDataType.CUSTOM, "startIDE", "Opened IDE");
    }

    // Nutzer erstellen oder laden
    private User getCurrentUser(){
        user = de.ur.mi.pluginhelper.User.User.getLocalUser();
        return user;
    }

    private void syncLoggingData(Log log){
        String serverUrl = "http://regensburger-forscher.de:9999/upload/";
        // Log synchronisieren
        LogManager.syncLog(log, user, serverUrl, new SyncProgressListener() {
            @Override
            public void onFinished() {
                System.out.println("Upload finished");
            }
            @Override
            public void onFailed() {
                System.out.println("Upload failed");
            }
        });
    }

    public void saveDataInLogger(LogDataType type, String label, String payload){
        // Daten im Log speichern
        log.log(user.getSessionID(), type, label, payload);
    }
}
