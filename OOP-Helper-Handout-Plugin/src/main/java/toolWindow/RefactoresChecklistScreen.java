package toolWindow;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.wm.ToolWindow;
import controller.ChecklistController;
import gui.PluginWebViewFXPanel;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.scene.web.WebView;
import objects.Checklist;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import provider.LocalStorageDataProvider;
import provider.ParseChecklistJSON;
import webView.WebViewController;
import netscape.javascript.JSObject;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.MalformedURLException;

public class RefactoresChecklistScreen extends SimpleToolWindowPanel {
    private PluginWebViewFXPanel checklistContent;
    private ToolWindow checklistToolWindow;
    private JsonObject checklistJson;
    private JsonObject userChecklistJson;
    private String checklistStartPage;
    private static WebView webView;
    private WebViewController webViewController;
    private SimpleToolWindowPanel toolWindowPanel;
    private File file;
    private File userData;
    private ChecklistController checklistController;


    public RefactoresChecklistScreen(ToolWindow toolWindow) {
        super(true, true);
        checklistController = ChecklistController.getInstance();
        webViewController = new WebViewController();

        toolWindowPanel = new SimpleToolWindowPanel(true);
        checklistToolWindow = toolWindow;
        file = LocalStorageDataProvider.getLocalChecklistPredefinedData();
        userData = LocalStorageDataProvider.getChecklistUserData();
        //TODO Create new Method
        //https://stackoverflow.com/a/34486879
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            JsonParser parser = new JsonParser();
            checklistJson = parser.parse(br).getAsJsonObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //TODO IF AVAILALABLE -> CREATE TXT
        try {
            br = new BufferedReader(new FileReader(userData));
            JsonParser parser = new JsonParser();
            userChecklistJson = parser.parse(br).getAsJsonObject();
        } catch (FileNotFoundException | IllegalStateException e) {
            System.out.println("OHOH");
        }


        try {
            checklistStartPage = LocalStorageDataProvider.getLocalPredefinedChecklistFile().toURI().toURL().toString();
            System.out.println("checklistStartPage: " + checklistStartPage);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        createContent();
        initToolWindowMenu();
    }

    private void initToolWindowMenu() {
        //http://androhi.hatenablog.com/entry/2015/07/23/233932
        toolWindowPanel.setContent(checklistContent);
    }

    private void createContent() {
        checklistContent = new PluginWebViewFXPanel();
        Checklist checklist = ParseChecklistJSON.predefinedJsonToChecklistParser(checklistJson);
        checklistController.createTaskList("predefined", checklist);

        for (int i = 0; i < checklist.tasks.size(); i++) {
            //TODO fill pr
        }
        if (userChecklistJson != null) {
            Checklist userChecklist = ParseChecklistJSON.checklistJSONHandler(userChecklistJson);
            //checklistController.createTaskList("user", userChecklist);
            for (int i = 0; i < userChecklist.tasks.size(); i++) {
                //TODO fill user html --> PARSE MIST
            }
        }

        /* //https://stackoverflow.com/a/28671914
        final ObservableList<ChecklistTableTask> predefinedData = FXCollections.observableArrayList(param -> new Observable[]{param.checked});
        for (int i = 0; i < checklist.tasks.size(); i++) {
            String taskName = checklist.tasks.get(i).taskDescription;
            Boolean checked = checklist.tasks.get(i).checked;
            String id = checklist.tasks.get(i).id;
            ChecklistTableTask newTask = new ChecklistTableTask.TasksTableBuilder(taskName, checked)
                    .id(id).build();
            predefinedData.add(newTask);
            System.out.println("ID WHEN CREATING: " + predefinedData.get(i).id);
        }
        predefinedData.addListener((ListChangeListener<? super ChecklistTableTask>) change -> {
            System.out.println("Chenge PREDEFINED DATA: ");
            while (change.next()) {
                if (change.wasUpdated()) {
                    System.out.println("PREDEFINED " + predefinedData.get(change.getFrom()).getChecked() + " changed value to " + predefinedData.get(change.getFrom()).checked);
                    System.out.println("ID BY LISTENER: " + predefinedData.get(0).id);
                    //TODO CHANGE TO PREFEFINED
                    checklistController.savePredefinedDataInFile(predefinedData);
                }
            }
        });

        //https://stackoverflow.com/a/28671914
        final ObservableList<ChecklistTableTask> userData = FXCollections.observableArrayList(param -> new Observable[]{param.checked});
        if (userChecklistJson != null) {
            Checklist userChecklist = ParseChecklistJSON.checklistJSONHandler(userChecklistJson);
            for (int i = 0; i < userChecklist.tasks.size(); i++) {
                String taskName = userChecklist.tasks.get(i).taskDescription;
                Boolean checked = userChecklist.tasks.get(i).checked;
                ChecklistTableTask newTask = new ChecklistTableTask.TasksTableBuilder(taskName, checked).build();
                userData.add(newTask);
            }
        }
        userData.addListener((ListChangeListener<? super ChecklistTableTask>) change -> {
            System.out.println("CHECNGE USER DATA: ");
            checklistController.saveUserDataInFile(userData);
        });

*/
        Platform.setImplicitExit(false);
        Platform.runLater(() -> {
            webView = webViewController.createWebView(checklistStartPage);
            checklistContent.showChecklist(checklistStartPage, webView);
            initAddTaskButtonListener();
            initDeleteTaskButtonListener();
            initCheckTaskButtonListener();
        });

    }

    private void initCheckTaskButtonListener() {
        System.out.println("initCheckTaskButtonListener");
        JSObject window = (JSObject) webView.getEngine().executeScript("window");
        window.setMember("clickTask", ChecklistController.getInstance());
    }

    private void initDeleteTaskButtonListener() {
        System.out.println("initAddTaskButtonListener");
        JSObject window = (JSObject) webView.getEngine().executeScript("window");
        window.setMember("checklistController", ChecklistController.getInstance());
    }

    private void initAddTaskButtonListener() {
        //https://stackoverflow.com/a/34547416
        //create listener for "add task" button in webView
        System.out.println("initAddTaskButtonListener");
        //noteAddingFrame = NoteAddingFrame.getInstance();
        //noteAddingFrame.setNotesScreen(this);
        //JSObject window = (JSObject) webView.getEngine().executeScript("window");
        //window.setMember("noteAddingFrame", NoteAddingFrame.getInstance());
    }

    public JPanel getContent() {
        return toolWindowPanel;
    }
}