package controller;

import com.intellij.notification.NotificationType;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.project.ProjectManagerListener;
import eventHandling.OnGitEventListener;
import gui.CommitChangesDialog;
import org.jetbrains.annotations.NotNull;
import provider.HandoutContentDataProviderInterface;
import provider.RepoLocalStorageDataProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class HandoutPluginController implements HandoutPluginControllerInterface, OnGitEventListener {
    private HandoutContentDataProviderInterface handoutDataProvider;
    private ToolWindowController toolWindowController;
    private Project project;
    private LoggingController loggingController;

    public HandoutPluginController(Project project) {
        this.project = project;
        initLogger();
        createProjectListener();
        //TODO NOT TO REPOLOCALSTORAGE OFOFOFOFOF
        RepoLocalStorageDataProvider.setUserProjectDirectory(this.project);
        handoutDataProvider = ServiceManager.getService(project, HandoutContentDataProviderInterface.class);
        handoutDataProvider.addListener(this);
        toolWindowController = ToolWindowController.getInstance();
    }

    private void createProjectListener() {
        ProjectManagerListener projectClosedListener = new ProjectManagerListener() {
            @Override
            public void projectClosed(@NotNull Project project) {
                System.out.println("PROJECT LISTENER name: " + project.getName());
                //TODO UNCOMMENT
                //loggingController.syncLoggingData();
            }

            @Override
            public void projectOpened(@NotNull Project project) {
                System.out.println("PROJECT Openend name: " + project.getName());
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
        System.out.println("Performing callback after Asynchronous Task");
        File repoFile = new File(RepoLocalStorageDataProvider.getRepoLocalFile());
        repoFile.setExecutable(false);
        repoFile.setReadable(true);
        repoFile.setWritable(false);
        repoFile.setReadOnly();
        toolWindowController.updateContent();
        BalloonPopupController.showNotification(project, notificationMessage, messageType);

        //update toolWindow
        //
        /*Path file = Paths.get(repoFile.getAbsolutePath());
        try {
            Files.setAttribute(file, "dos:hidden", true);
        } catch (IOException e) {
            System.out.print(e);
        }
        System.out.println("RepoFile hidden: "+ repoFile.isHidden());*/

        NotesController notesController = NotesController.getInstance();
        notesController.createNotesFile();

        ChecklistController checklistController = ChecklistController.getInstance();
        System.out.println("checklistController: " + checklistController);
        //TODO IS NOTCALLED
        checklistController.createChecklistFiles();
    }


    @Override
    //TODO add strings to message constants
    public void onUpdatingRepositoryEvent(ArrayList<String> commitMessages) {
        ChecklistController checklistController = ChecklistController.getInstance();
        BalloonPopupController.showNotification(project, "Handout Daten wurden runtergeladen." + commitMessages.toString(), NotificationType.INFORMATION);
        CommitChangesDialog commitChangesDialog = new CommitChangesDialog(commitMessages);
        try {
            checklistController.comparePredefinedChecklistVersions();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        commitChangesDialog.showPanel();
    }

    @Override
    public void onNotUpdatingRepositoryEvent(String notificationMessage, NotificationType messageType) {
        System.out.println("onNotUpdatingRepositoryEvent");
        BalloonPopupController.showNotification(project, notificationMessage, messageType);
    }
}
