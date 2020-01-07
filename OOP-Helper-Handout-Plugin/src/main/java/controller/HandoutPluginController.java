package controller;

import com.intellij.notification.NotificationType;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.project.ProjectManagerListener;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import de.ur.mi.pluginhelper.logger.LogDataType;
import eventHandling.OnGitEventListener;
import gui.ContentDataChangesDialog;
import org.jetbrains.annotations.NotNull;
import provider.HandoutContentDataProvider;
import provider.HandoutContentDataProviderInterface;
import provider.RepoLocalStorageDataProvider;

import java.io.File;
import java.util.ArrayList;

import static environment.LoggingMessageConstants.IDE_CLOSED;
import static environment.LoggingMessageConstants.IDE_VISIBILITY;

//TODO HIDE ANT TOOL WINDOW
public class HandoutPluginController implements HandoutPluginControllerInterface, OnGitEventListener {
    private HandoutContentDataProviderInterface handoutDataProvider;
    private UpdateHandoutDataController updateHandoutDataController;
    private Project project;
    private LoggingController loggingController;

    public HandoutPluginController(Project project) {
        this.project = project;
        createProjectListener();
        //TODO NOT TO REPOLOCALSTORAGE OFOFOFOFOF
        RepoLocalStorageDataProvider.setUserProjectDirectory(this.project);
        handoutDataProvider = HandoutContentDataProvider.getInstance();
        handoutDataProvider.addListener(this);
        updateHandoutDataController = UpdateHandoutDataController.getInstance();
    }

    private void createProjectListener() {
        ProjectManagerListener projectClosedListener = new ProjectManagerListener() {
            @Override
            public void projectClosed(@NotNull Project project) {
                loggingController.saveDataInLogger(LogDataType.IDE, IDE_VISIBILITY, IDE_CLOSED);
                loggingController.syncLoggingData();
            }

            @Override
            public void projectOpened(@NotNull Project project) {
                initLogger();
                updateHandoutContent();
            }
        };
        //https://intellij-support.jetbrains.com/hc/en-us/community/posts/206792155/comments/206204315
        ProjectManager.getInstance().addProjectManagerListener(project, projectClosedListener );
    }

    private void initLogger() {
        loggingController = LoggingController.getInstance();
        loggingController.startLogging();
    }

    public void updateHandoutContent() {
        handoutDataProvider.updateHandoutData();
    }

    //TODO MANAGE UPDATE VIEWS....
    //TODO update Webview and other content of toolWindows
    //TODO enable / disable Actions


    public void onCloningRepositoryEvent(String notificationMessage, NotificationType messageType)   {
        initHtmlFiles();
        updateContentData();
        QuestionnaireController.getInstance().saveProjectCreationDate();
        BalloonPopupController.showBalloonNotification( Balloon.Position.above, notificationMessage, "Status des Handouts", MessageType.INFO);
        loggingController.saveDataInLogger(LogDataType.HANDOUT, "Download Repo", "Cloned Repo");
    }

    private void initHtmlFiles() {
        NotesController.getInstance().createNotesFile();
        ChecklistController.getInstance().createChecklistFiles();
        HandoutController.getInstance().createHandoutFile();
    }

    private void updateContentData() {
        updateHandoutDataController.updateLocalStorageData();
    }


    @Override
    //TODO add strings to message constants
    public void onUpdatingRepositoryEvent(ArrayList<String> commitMessages) {
        updateContentData();
        ApplicationManager.getApplication().invokeLater(() -> {
            ContentDataChangesDialog.main(commitMessages);
        });
        BalloonPopupController.showBalloonNotification( Balloon.Position.above, "Handout Daten wurden runtergeladen.", "Status des Handouts", MessageType.INFO);
        loggingController.saveDataInLogger(LogDataType.HANDOUT, "Download Repo", "Updated Repo");

    }

    @Override
    public void onNotUpdatingRepositoryEvent(String notificationMessage, MessageType messageType) {
        BalloonPopupController.showBalloonNotification( Balloon.Position.above, notificationMessage, "Status des Handouts", messageType);
        loggingController.saveDataInLogger(LogDataType.HANDOUT, "Download Repo", "Not Updated Repo");
    }

    @Override
    public void onCloneCanceledRepositoryEvent(String notificationMessage, MessageType messageType, File file) {
        BalloonPopupController.showBalloonNotification( Balloon.Position.above, notificationMessage, "Status des Handouts", messageType);
        FileHandleController.deleteFile(file);
        loggingController.saveDataInLogger(LogDataType.HANDOUT, "Download Repo", "Canceled");
    }
}
