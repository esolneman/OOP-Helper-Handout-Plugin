package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import de.ur.mi.pluginhelper.logger.LogManager;
import eventHandling.OnGitEventListener;
import gui.CommitChangesDialog;
import provider.HandoutContentDataProviderInterface;
import provider.LocalStorageDataProvider;
import provider.RepoLocalStorageDataProvider;
import java.io.*;
import java.util.ArrayList;

public class HandoutPluginController implements HandoutPluginControllerInterface, OnGitEventListener {
    private HandoutContentDataProviderInterface handoutDataProvider;
    private ToolWindowController toolWindowController;
    private Project project;
    private de.ur.mi.pluginhelper.User.User user;

    public HandoutPluginController(Project project) {

        // Nutzer erstellen oder laden
        user = de.ur.mi.pluginhelper.User.User.getLocalUser();

        // Session Log für das Experiment mit dem Titel "Test" erstellen oder öffnen
        de.ur.mi.pluginhelper.logger.Log log = LogManager.openLog(user.getSessionID(), "MA-Wolfes");

        // Daten im Log speichern
        log.log(user.getSessionID(), de.ur.mi.pluginhelper.logger.LogDataType.CUSTOM, "demo", "Something happened");

        String serverUrl = "http://regensburger-forscher.de:9999/upload/";

/*        // Log synchronisieren
        LogManager.syncLog(log, user, serverUrl, new SyncProgressListener() {
            @Override
            public void onFinished() {
                System.out.println("Upload finished");
            }
            @Override
            public void onFailed() {
                System.out.println("Upload failed");
            }
        });*/

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

    public void onCloningRepositoryEvent(String notificationMessage, NotificationType messageType)   {
        System.out.println("Performing callback after Asynchronous Task");
        File repoFile = new File(RepoLocalStorageDataProvider.getRepoLocalFile());
        repoFile.setExecutable(false);
        repoFile.setReadable(true);
        repoFile.setWritable(false);
        repoFile.setReadOnly();
        toolWindowController.updateContent();
        BalloonPopupController.showNotification(project, notificationMessage, messageType);


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
        File localPredefinedChecklistFile = LocalStorageDataProvider.getLocalChecklistPredefinedData();
        File predefinedRepoChecklistFile = LocalStorageDataProvider.getChecklistData();

        //TODO create in Checklsit controller
        //https://stackoverflow.com/a/29965924
        Gson gson = new Gson();
        JsonReader reader = null;
        try {
            reader = new JsonReader(new FileReader(predefinedRepoChecklistFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        JsonObject repoChecklistData = gson.fromJson(reader, JsonObject.class);
        saveJsonObjectInFile(repoChecklistData, localPredefinedChecklistFile);

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
    }


    @Override
    //TODO add strings to message constants
    public void onUpdatingRepositoryEvent(ArrayList<String> commitMessages) {
        BalloonPopupController.showNotification(project, "Handout Daten wurden runtergeladen." + commitMessages.toString(), NotificationType.INFORMATION);
        CommitChangesDialog commitChangesDialog = new CommitChangesDialog(commitMessages);
        try {
            ChecklistController.comparePredefinedChecklistVersions();
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
