package toolWindow;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.wm.ToolWindow;
import gui.ChecklistTreeView;
import gui.HandoutPluginFXPanel;
import objects.Checklist;
import objects.Checklist.Tasks;
import provider.LocalStorageDataProvider;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

public class ChecklistScreen extends SimpleToolWindowPanel {
    private HandoutPluginFXPanel handoutContent;
    private ToolWindow handoutToolWindow;
    private JPanel checklistContent;
    private JTextPane repoChecklist;
    private JsonObject checklistJson;
    private ChecklistTreeView cbt;
    private SimpleToolWindowPanel toolWindowPanel;
    private File file;

    public ChecklistScreen(ToolWindow toolWindow) {
        super(true, true);
        toolWindowPanel = new SimpleToolWindowPanel(true);
        handoutToolWindow = toolWindow;
        file = LocalStorageDataProvider.getChecklistData();

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
        Checklist checklist = cheklistJSONHandler(checklistJson);
        //TODO replace with message Cosntant
        DefaultMutableTreeNode initNode = new DefaultMutableTreeNode("Angabe");
        for (int i = 0; i < checklist.getTasks().size(); i++) {
            Tasks task = checklist.getTasks().get(i);
            DefaultMutableTreeNode newParentNode = new DefaultMutableTreeNode(task.getTask());
            initNode.add(newParentNode);
            for (String childTask : task.getChildTasks()) {
                DefaultMutableTreeNode newChildNote = new DefaultMutableTreeNode(childTask);
                newParentNode.add(newChildNote);
            }
        }
        //https://stackoverflow.com/a/21851201
        ChecklistTreeView cbt = new ChecklistTreeView();
        DefaultTreeModel model = new DefaultTreeModel(initNode);
        cbt.setModel(model);
        checklistContent.add(cbt);
        cbt.addCheckChangeEventListener(event -> {
            TreePath[] paths = cbt.getCheckedPaths();
            for (TreePath tp : paths) {
                for (Object pathPart : tp.getPath()) {
                    System.out.print(pathPart + ",");
                }
                System.out.println();
            }
        });
    }

    //TODO WRITE PARSER CLASS
    //https://stackoverflow.com/a/34510715
    private Checklist cheklistJSONHandler(JsonObject checklistJson) {
        JsonArray checklist = ((JsonArray) checklistJson.get("checklist"));
        ArrayList<Tasks> tasks = new ArrayList<>();
        System.out.println(checklist.get(0).getClass());
        for (JsonElement jsonElement : checklist) {
            final ArrayList<String> childTasks = new ArrayList<>();
            JsonElement childtasksEle = jsonElement.getAsJsonObject().get("childtasks");
            childtasksEle.getAsJsonArray().forEach(jsonElement1 -> childTasks.add(jsonElement1.toString()));
            Tasks newTask = new Tasks(jsonElement.getAsJsonObject().get("task").getAsString(), childTasks);
            tasks.add(newTask);
        }
        Checklist realChecklist = new Checklist();
        realChecklist.setTasks(tasks);
        return realChecklist;
    }

    private JComponent createToolbarPanel() {
        final DefaultActionGroup checklistActionGroup = new DefaultActionGroup();
        //checklistActionGroup.add(ActionManager.getInstance().getAction("Handout.Download"));
        checklistActionGroup.add(ActionManager.getInstance().getAction("Checklist.EditContent"));
        final ActionToolbar checklistActionToolbar = ActionManager.getInstance().createActionToolbar("Checklisttool", checklistActionGroup, true);
        return checklistActionToolbar.getComponent();
    }

    public JPanel getContent() {
        return toolWindowPanel;
    }
}
