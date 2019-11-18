package toolWindow;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.wm.ToolWindow;
import gui.HandoutPluginFXPanel;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import objects.Checklist;
import objects.ChecklistTableTask;
import provider.LocalStorageDataProvider;
import provider.ParseChecklistJSON;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class ChecklistScreen extends SimpleToolWindowPanel {
    private HandoutPluginFXPanel checklistContent;
    private ToolWindow checklistToolWindow;
    private JTable predefinedChecklistTable;
    private JsonObject checklistJson;
    private JsonObject userChecklistJson;

    private SimpleToolWindowPanel toolWindowPanel;
    private File file;
    private File userData;

    public ChecklistScreen(ToolWindow toolWindow) {
        super(true, true);
        toolWindowPanel = new SimpleToolWindowPanel(true);
        checklistToolWindow = toolWindow;
        file = LocalStorageDataProvider.getChecklistData();
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
        br = null;
        try {
            br = new BufferedReader(new FileReader(userData));
            JsonParser parser = new JsonParser();
            userChecklistJson = parser.parse(br).getAsJsonObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        createContent();
        initToolWindowMenu();
    }

    private void initToolWindowMenu() {
        //http://androhi.hatenablog.com/entry/2015/07/23/233932
        toolWindowPanel.setToolbar(createToolbarPanel());
        toolWindowPanel.setContent(checklistContent);
    }

    private void createContent() {
        checklistContent = new HandoutPluginFXPanel();
        Checklist checklist = ParseChecklistJSON.checklistJSONHandler(checklistJson);
        Object[] columnNames = {"Aufgabe", "Erledigt"};
        final ObservableList<ChecklistTableTask> predefinedData = FXCollections.observableArrayList();
        for (int i = 0; i < checklist.tasks.size(); i++) {
            String taskName = checklist.tasks.get(i).taskDescription;
            Boolean checked = checklist.tasks.get(i).checked;
            ChecklistTableTask newTask = new ChecklistTableTask(taskName, checked);
            predefinedData.add(newTask);
        }

        final ObservableList<ChecklistTableTask> userData = FXCollections.observableArrayList();
        if (userChecklistJson != null) {
            Checklist userChecklist = ParseChecklistJSON.checklistJSONHandler(userChecklistJson);
            for (int i = 0; i < userChecklist.tasks.size(); i++) {
                String taskName = checklist.tasks.get(i).taskDescription;
                Boolean checked = checklist.tasks.get(i).checked;
                ChecklistTableTask newTask = new ChecklistTableTask(taskName, checked);
                userData.add(newTask);
            }
        }


        Platform.setImplicitExit(false);
        Platform.runLater(() -> {
            checklistContent.showChecklistTable(predefinedData, userData);
        });

    }

    private JComponent createToolbarPanel() {
        final DefaultActionGroup checklistActionGroup = new DefaultActionGroup();
        checklistActionGroup.add(ActionManager.getInstance().getAction("Checklist.EditContent"));
        final ActionToolbar checklistActionToolbar = ActionManager.getInstance().createActionToolbar("Checklisttool", checklistActionGroup, true);
        return checklistActionToolbar.getComponent();
    }

    public JPanel getContent() {
        return toolWindowPanel;
    }
}
