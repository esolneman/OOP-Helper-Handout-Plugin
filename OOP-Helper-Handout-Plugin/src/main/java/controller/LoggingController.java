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

    private LoggingController() {
    }

    public void startLogging() {
        user = getCurrentUser();
        // Session Log für das Experiment mit dem Titel "Test" erstellen oder öffnen
        log = LogManager.openLog(user.getID(), EXPERIMENT_NAME);
        saveDataInLogger(LogDataType.IDE, IDE_VISIBILITY, IDE_OPENED);
    }

    // Nutzer erstellen oder laden
    public User getCurrentUser() {
        user = de.ur.mi.pluginhelper.User.User.getLocalUser();
        return user;
    }

    public void syncLoggingData() {
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
    }

    //save data in log file
    public void saveDataInLogger(LogDataType type, String label, String payload) {
        log.log(user.getSessionID(), type, label, payload);
    }
}
