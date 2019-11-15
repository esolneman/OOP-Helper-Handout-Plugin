package toolWindow;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.uiDesigner.core.GridLayoutManager;
import objects.Checklist;
import provider.LocalStorageDataProvider;
import provider.ParseChecklistJSON;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class ChecklistScreen extends SimpleToolWindowPanel {
    private ToolWindow handoutToolWindow;
    private JPanel checklistContent;
    private JPanel predefinedDataPanel;
    private JPanel userDataPanel;
    private JList predefinedChecklistList;
    private JTable predefinedChecklistTable;
    private JTable userChecklistTable;
    private JTextField predefiniedDataHeader;
    private JScrollPane predefieniedDataScrollPane;
    private JsonObject checklistJson;
    private JsonObject userChecklistJson;

    private SimpleToolWindowPanel toolWindowPanel;
    private File file;
    private File userData;
    private Boolean editPanelVisible;

    public ChecklistScreen(ToolWindow toolWindow) {
        super(true, true);
        GridLayoutManager contentLayout = new GridLayoutManager(2,1);
        checklistContent = new JPanel();
        //checklistContent.setLayout(contentLayout);
        predefinedDataPanel = new JPanel();
        editPanelVisible = false;
        toolWindowPanel = new SimpleToolWindowPanel(true);
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
        checklistContent.add(predefiniedDataHeader);
    }

    private void initToolWindowMenu() {
        //http://androhi.hatenablog.com/entry/2015/07/23/233932
        toolWindowPanel.setToolbar(createToolbarPanel());
        toolWindowPanel.setContent(checklistContent);
    }

    private void createContent() {
        Checklist checklist = ParseChecklistJSON.checklistJSONHandler(checklistJson);
        Object[] columnNames = {"Type",  "Boolean"};
        Object[][] data;
        data = new Object[checklist.tasks.size()][2];
        for (int i = 0; i < checklist.tasks.size(); i++) {
            data[i][0] = checklist.tasks.get(i).taskDescription;
            data [i] [1] = checklist.tasks.get(i).checked;
        }
        JTable predefinedTable;
        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        predefinedTable = new JTable(model) {

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


        //TODO INIT USER LOCATION FILE WHEN START IDE
        //predefinedTable.setPreferredScrollableViewportSize(predefinedTable.getMaximumSize());
        predefinedChecklistTable = predefinedTable;
        predefinedDataPanel.add(predefiniedDataHeader);
        predefinedDataPanel.add(predefinedChecklistTable);
        checklistContent.add(predefinedDataPanel);


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
