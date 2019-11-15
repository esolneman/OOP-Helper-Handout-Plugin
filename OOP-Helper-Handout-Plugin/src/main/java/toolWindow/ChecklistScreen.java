package toolWindow;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import gui.ChecklistCheckbox;
import gui.ChecklistTable;
import gui.ChecklistTreeView;
import objects.Checklist;
import provider.LocalStorageDataProvider;
import provider.ParseChecklistJSON;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class ChecklistScreen extends SimpleToolWindowPanel {
    private ToolWindow handoutToolWindow;
    private JPanel checklistContent;
    private JTable predefinedChecklistTable;
    private JTable userChecklistTable;
    private JSeparator seperator;
    private JsonObject checklistJson;
    private JsonObject userChecklistJson;

    private SimpleToolWindowPanel toolWindowPanel;
    private File file;
    private File userData;
    private Boolean editPanelVisible;

    public ChecklistScreen(ToolWindow toolWindow) {
        super(true, true);
        editPanelVisible = false;
        toolWindowPanel = new SimpleToolWindowPanel(true);
        predefinedChecklistTable = new JBTable();
        userChecklistTable = new JBTable();
        handoutToolWindow = toolWindow;
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
        checklistContent = new JPanel();
        Checklist checklist = ParseChecklistJSON.checklistJSONHandler(checklistJson);


        for (Checklist.Tasks task : checklist.tasks) {
            JCheckBox checkbox = new JCheckBox(task.taskDescription);
            checklistContent.add(checkbox);
        }

        if(userChecklistJson!= null){
            Checklist userChecklsit = ParseChecklistJSON.checklistJSONHandler(userChecklistJson);
            for (Checklist.Tasks task : userChecklsit.tasks) {
                ChecklistCheckbox checkbox = new ChecklistCheckbox(task.taskDescription);
                checklistContent.add(checkbox);
            }
        }
        seperator = new JSeparator();
        checklistContent.add(seperator);
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
