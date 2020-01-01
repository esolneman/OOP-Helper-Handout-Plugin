package controller;

import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.project.ProjectManagerListener;
import de.ur.mi.pluginhelper.logger.LogDataType;
import eventHandling.OnGitEventListener;
import gui.CommitChangesDialog;
import org.jetbrains.annotations.NotNull;
import provider.HandoutContentDataProvider;
import provider.HandoutContentDataProviderInterface;
import provider.RepoLocalStorageDataProvider;

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
        BalloonPopupController.showNotification(project, notificationMessage, messageType);
    }

    private void initHtmlFiles() {
        System.out.println("initHtmlFiles");
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
        CommitChangesDialog commitChangesDialog = new CommitChangesDialog(commitMessages);
        BalloonPopupController.showNotification(project, "Handout Daten wurden runtergeladen." + commitMessages.toString(), NotificationType.INFORMATION);
        commitChangesDialog.showPanel();
    }

    @Override
    public void onNotUpdatingRepositoryEvent(String notificationMessage, NotificationType messageType) {
        BalloonPopupController.showNotification(project, notificationMessage, messageType);
    }

}
