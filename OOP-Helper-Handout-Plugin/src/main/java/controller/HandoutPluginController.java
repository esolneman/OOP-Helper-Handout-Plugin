package controller;

import com.intellij.notification.NotificationType;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import eventHandling.OnGitEventListener;
import gui.InformChangesPanel;
import gui.InformCommitChangesDialog;
import provider.HandoutContentDataProviderInterface;
import provider.RepoLocalStorageDataProvider;
import toolWindow.HandoutToolWindowFactory;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;

public class HandoutPluginController implements HandoutPluginControllerInterface, OnGitEventListener {
    private HandoutContentDataProviderInterface handoutDataProvider;
    private HandoutToolWindowFactory handoutToolWindowFactory;
    private ToolWindowController toolWindowController;
    private Project project;

    public HandoutPluginController(Project project) {
        this.project = project;
        RepoLocalStorageDataProvider.setUserProjectDirectory(this.project);
        handoutDataProvider = ServiceManager.getService(project, HandoutContentDataProviderInterface.class);
        handoutDataProvider.addListener(this);
        toolWindowController = ToolWindowController.getInstance();
        updateHandoutContent();
        ArrayList<String> test = new ArrayList<>();
        test.add("dfghj");
        test.add("zghjkl");
        test.add("jhgfd");
        test.add("AARGHHGHGH");
        InformCommitChangesDialog informCommitChangesDialog = new InformCommitChangesDialog(test);
        informCommitChangesDialog.show();
    }

    public void updateHandoutContent() {
        handoutDataProvider.updateHandoutData();
    }

    //TODO MANAGE UPDATE VIEWS....
    //TODO update Webview and other content of toolWindows
    //TODO enable / disable Actions
    public void onCloningRepositoryEvent(String notificationMessage, NotificationType messageType) {
        System.out.println("Performing callback after Asynchronous Task");
        File repoFile = new File(RepoLocalStorageDataProvider.getRepoLocalFile());
        System.out.println(repoFile.getAbsolutePath());
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
    }


    @Override
    //TODO add strings to message constants
    public void onUpdatingRepositoryEvent(ArrayList<String> commitMessages) {
        BalloonPopupController.showNotification(project, "Handout Daten wurden runtergeladen." + commitMessages.toString(), NotificationType.INFORMATION);
        InformChangesPanel informChangesPanel = new InformChangesPanel(commitMessages);
        informChangesPanel.showPanel();
    }

    @Override
    public void onNotUpdatingRepositoryEvent(String notificationMessage, NotificationType messageType) {
        System.out.println("onNotUpdatingRepositoryEvent");
        BalloonPopupController.showNotification(project, notificationMessage, messageType);
    }


}
