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
import provider.ParseChecklistJSON;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
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
    private JPanel EditButtons;
    private JButton OKButton;
    private JButton abbrechenButton;
    private ChecklistTreeView predeterminedChecklistTree;
    private ChecklistTreeView userChecklistTree;
    private JsonObject checklistJson;
    private JsonObject userChecklistJson;

    private ChecklistTreeView cbt;
    private SimpleToolWindowPanel toolWindowPanel;
    private File file;
    private File userData;
    private Boolean editPanelVisible;

    public ChecklistScreen(ToolWindow toolWindow) {
        super(true, true);
        editPanelVisible = false;
        toolWindowPanel = new SimpleToolWindowPanel(true);
        handoutToolWindow = toolWindow;
        predeterminedChecklistTree = new ChecklistTreeView();
        userChecklistTree = new ChecklistTreeView();
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
        DefaultTreeModel model = new DefaultTreeModel(initNode);
        predeterminedChecklistTree.setModel(model);
        checklistContent.add(predeterminedChecklistTree);
        predeterminedChecklistTree.addCheckChangeEventListener(event -> {
            TreePath[] paths = predeterminedChecklistTree.getCheckedPaths();
            for (TreePath tp : paths) {
                for (Object pathPart : tp.getPath()) {
                    System.out.print(pathPart + ",");
                }
                System.out.println();
            }
        });


        if(userChecklistJson!= null){
            Checklist userChecklsit = ParseChecklistJSON.checklistJSONHandler(userChecklistJson);
            DefaultMutableTreeNode userinitNode = new DefaultMutableTreeNode("Eigene Checkliste");
            userinitNode.add((MutableTreeNode) ParseChecklistJSON.getTreeModelFromJson(userChecklistJson).getRoot());
            DefaultTreeModel userModel = new DefaultTreeModel(userinitNode);
            userChecklistTree.setModel(userModel);
            checklistContent.add(userChecklistTree);
            userChecklistTree.addCheckChangeEventListener(event -> {
                TreePath[] paths = userChecklistTree.getCheckedPaths();
                for (TreePath tp : paths) {
                    for (Object pathPart : tp.getPath()) {
                        System.out.print(pathPart + ",");
                    }
                    System.out.println();
                }
            });
        }
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
