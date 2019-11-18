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
        TableView<ChecklistTableTask> table = new TableView<>();
        Checklist checklist = ParseChecklistJSON.checklistJSONHandler(checklistJson);
        Object[] columnNames = {"Aufgabe",  "Erledigt"};
        final ObservableList<ChecklistTableTask> data = FXCollections.observableArrayList();
        for (int i = 0; i < checklist.tasks.size(); i++) {
            String taskName = checklist.tasks.get(i).taskDescription;
            Boolean checked = checklist.tasks.get(i).checked;
            ChecklistTableTask newTask = new ChecklistTableTask(taskName, checked);
            data.add(newTask);
            System.out.println("OBSERVERABLE: " + data.get(i).taskDescription);
        }

        Platform.setImplicitExit(false);
        Platform.runLater(() -> {
            checklistContent.showPredefinedChecklistTable(data, table);
        });


/*        //https://stackoverflow.com/a/7392163
        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        //TODO INIT USER LOCATION FILE WHEN START IDE
        //predefinedTable.setPreferredScrollableViewportSize(predefinedTable.getMaximumSize());
        predefinedChecklistTable = new JTable(model) {
            private static final long serialVersionUID = 1L;
            @Override
            public Class getColumnClass(int column) {
                switch (column) {
                    case 0:
                        return String.class;
                    default:
                        return Boolean.class;
                }
            }

            public boolean isCellEditable(int row, int column) {
                return column == 1;
            }
        };
        predefinedChecklistTable.setEditingColumn(1);
        predefinedChecklistTable.setPreferredScrollableViewportSize(predefinedChecklistTable.getPreferredSize());
        predefinedChecklistTable.setFillsViewportHeight(true);
        JScrollBar scrollBar = new JScrollBar();
        scrollBar.add(predefinedChecklistTable);
        checklistContent.add(predefinedChecklistTable);*/


        //create checklist for user data
        if(userChecklistJson!= null){
            Checklist userChecklsit = ParseChecklistJSON.checklistJSONHandler(userChecklistJson);
            for (Checklist.Tasks task : userChecklsit.tasks) {
               // ChecklistCheckbox checkbox = new ChecklistCheckbox(task.taskDescription);
               // checklistContent.add(checkbox);
            }
        }
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
