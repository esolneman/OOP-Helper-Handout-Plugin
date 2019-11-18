package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import eventHandling.OnGitEventListener;
import gui.CommitChangesDialog;
import objects.Notes;
import provider.HandoutContentDataProviderInterface;
import provider.LocalStorageDataProvider;
import provider.RepoLocalStorageDataProvider;
import provider.contentHandler.ParseNotesJson;
import toolWindow.HandoutToolWindowFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

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
        // InformChangesPanel informChangesPanel = new InformChangesPanel(test);
        // informChangesPanel.showPanel();
    }

    public void updateHandoutContent() {
        handoutDataProvider.updateHandoutData();
    }

    //TODO MANAGE UPDATE VIEWS....
    //TODO update Webview and other content of toolWindows
    //TODO enable / disable Actions


    //TODO Create dummy file for user notes
    //TODO Create dummy file for checklist

    public void onCloningRepositoryEvent(String notificationMessage, NotificationType messageType) {
        System.out.println("Performing callback after Asynchronous Task");
        File repoFile = new File(RepoLocalStorageDataProvider.getRepoLocalFile());
        repoFile.setExecutable(false);
        repoFile.setReadable(true);
        repoFile.setWritable(false);
        repoFile.setReadOnly();
        toolWindowController.updateContent();
        BalloonPopupController.showNotification(project, notificationMessage, messageType);


        //TODO create in Notes controller
        File notesFile = LocalStorageDataProvider.getNotesFile();
        notesFile.getParentFile().mkdirs();
        try {
            notesFile.createNewFile();
        } catch (IOException e) {
            //TODO CATACH
            System.out.println(e);
        }


        //TODO create in Checklsit controller
        File checklistUserFile = LocalStorageDataProvider.getChecklistUserData();
        checklistUserFile.getParentFile().mkdirs();
        try {
            checklistUserFile.createNewFile();
            JsonObject checklistJson = new JsonObject();
            JsonArray tasks = new JsonArray();
            checklistJson.add("checklist", tasks);
            saveJsonObjectInFile(checklistJson,checklistUserFile);
        } catch (IOException ex) {
            System.out.println("FILE NOT Created");
        }

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
        CommitChangesDialog commitChangesDialog = new CommitChangesDialog(commitMessages);
        commitChangesDialog.showPanel();
    }

    @Override
    public void onNotUpdatingRepositoryEvent(String notificationMessage, NotificationType messageType) {
        System.out.println("onNotUpdatingRepositoryEvent");
        BalloonPopupController.showNotification(project, notificationMessage, messageType);
    }


    private void saveJsonObjectInFile(JsonObject  jsonObject, File file) {
        //https://stackoverflow.com/a/29319491
        try (Writer writer = new FileWriter(file)) {
            Gson gson = new GsonBuilder().create();
            gson.toJson(jsonObject, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
