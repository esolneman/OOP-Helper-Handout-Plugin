package controller;

import com.intellij.notification.NotificationType;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.project.ProjectManagerListener;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindowManager;
import de.ur.mi.pluginhelper.logger.LogDataType;
import eventHandling.OnGitEventListener;
import gui.CommitChangesDialog;
import io.woo.htmltopdf.HtmlToPdf;
import io.woo.htmltopdf.HtmlToPdfObject;
import org.jetbrains.annotations.NotNull;
import provider.HandoutContentDataProvider;
import provider.HandoutContentDataProviderInterface;
import provider.LocalStorageDataProvider;
import provider.RepoLocalStorageDataProvider;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Objects;

import static environment.FileConstants.HANDOUT_PDF_FILE_NAME;
import static environment.FileConstants.URL_BEGIN_FOR_FILE;
import static environment.Messages.FILES_SELECTING_DESCRIPTION;
import static environment.Messages.FILES_SELECTING_TEXT;

//TODO HIDE ANT TOOL WINDOW
public class HandoutPluginController implements HandoutPluginControllerInterface, OnGitEventListener {
    private HandoutContentDataProviderInterface handoutDataProvider;
    private ToolWindowController toolWindowController;
    private Project project;
    private LoggingController loggingController;

    public HandoutPluginController(Project project) {
        this.project = project;
        createProjectListener();
        //TODO NOT TO REPOLOCALSTORAGE OFOFOFOFOF
        RepoLocalStorageDataProvider.setUserProjectDirectory(this.project);
        handoutDataProvider = HandoutContentDataProvider.getInstance();
        handoutDataProvider.addListener(this);
        toolWindowController = ToolWindowController.getInstance();
    }

    private void createProjectListener() {
        ProjectManagerListener projectClosedListener = new ProjectManagerListener() {
            @Override
            public void projectClosed(@NotNull Project project) {
                System.out.println("PROJECT LISTENER name: " + project.getName());
                loggingController.saveDataInLogger(LogDataType.IDE, "IDE VISIBILITY", "closed IDE");
                //TODO UNCOMMENT
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
        System.out.println("Performing callback after Asynchronous Task");
        toolWindowController.updateContent();
        BalloonPopupController.showNotification(project, notificationMessage, messageType);

        NotesController notesController = NotesController.getInstance();
        notesController.createNotesFile();

        ChecklistController checklistController = ChecklistController.getInstance();
        checklistController.createChecklistFiles();
        HandoutController handoutController = HandoutController.getInstance();
        handoutController.createHandoutFile();
        QuestionnaireController.getInstance().saveProjectCreationDate();
    }


    @Override
    //TODO add strings to message constants
    public void onUpdatingRepositoryEvent(ArrayList<String> commitMessages) {
        System.out.println("onUpdatingRepositoryEvent");

        ChecklistController checklistController = ChecklistController.getInstance();
        BalloonPopupController.showNotification(project, "Handout Daten wurden runtergeladen." + commitMessages.toString(), NotificationType.INFORMATION);
        CommitChangesDialog commitChangesDialog = new CommitChangesDialog(commitMessages);
        toolWindowController.updateContent();

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
