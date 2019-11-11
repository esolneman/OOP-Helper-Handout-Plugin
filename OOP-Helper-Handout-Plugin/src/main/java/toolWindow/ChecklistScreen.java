package toolWindow;

import com.google.gson.*;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.treeStructure.Tree;
import gui.ChecklistTreeView;
import gui.HandoutPluginFXPanel;
import objects.Checklist;
import objects.Checklist.*;

import provider.LocalStorageDataProvider;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.io.File;
import java.util.ArrayList;


public class ChecklistScreen extends SimpleToolWindowPanel {
    private HandoutPluginFXPanel handoutContent;
    private ToolWindow handoutToolWindow;
    private JPanel checklistContent;
    private JTextPane repoChecklist;
    private JsonObject checklistJson;

    private SimpleToolWindowPanel toolWindowPanel;
    private File file;

    public ChecklistScreen(ToolWindow toolWindow) {
        super(true, true);
        toolWindowPanel = new SimpleToolWindowPanel(true);
        handoutToolWindow = toolWindow;
         file = LocalStorageDataProvider.getChecklistData();

        checklistJson = new JsonObject();
        JsonArray tasks = new JsonArray();
        checklistJson.add("checklist", tasks);

        JsonObject task = new JsonObject();
        task.addProperty("task", "Create *Player* class");
        JsonArray childtasks = new JsonArray();
        childtasks.add("Create *update* method");
        childtasks.add("Create *setOrbitMovementType* method");
        childtasks.add("Create *setRangeMovementType* method");
        task.add("childtasks", childtasks);
        tasks.add(task);

        task = new JsonObject();


        task.addProperty("task", "Create *Projectile* class");
        JsonArray c = new JsonArray();
        task.add("childtasks", c);
        tasks.add(task);

        task = new JsonObject();

        task.addProperty("task", "collision detection between player and projecttile");
        JsonArray c1 = new JsonArray();
        task.add("childtasks", c1);
        tasks.add(task);

        //checklistJson.put("tasks", task);

       // Files.write(Paths.get(filename), checklistJson.toJSONString().getBytes());


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
        DefaultMutableTreeNode initNode = new DefaultMutableTreeNode("Angabe");
        for (int i = 0; i < checklist.getTasks().size(); i++) {
            Tasks task = checklist.getTasks().get(i);
            if (i == 0){
                DefaultMutableTreeNode newParentNode = new DefaultMutableTreeNode(task.getTask());
                initNode.add(newParentNode);
            }else{
                DefaultMutableTreeNode newParentNode = new DefaultMutableTreeNode(task.getTask());
                for (String childTask : task.getChildTasks()) {
                    DefaultMutableTreeNode newChildNote = new DefaultMutableTreeNode(childTask);
                    newParentNode.add(newChildNote);
                }
            }
        }


        JTree jt = new Tree(initNode);
        checklistContent.add(jt);

        DefaultMutableTreeNode style=new DefaultMutableTreeNode("Style");
        DefaultMutableTreeNode color=new DefaultMutableTreeNode("color");
        DefaultMutableTreeNode font=new DefaultMutableTreeNode("font");
        style.add(color);
        style.add(font);
        DefaultMutableTreeNode red=new DefaultMutableTreeNode("red");
        DefaultMutableTreeNode blue=new DefaultMutableTreeNode("blue");
        DefaultMutableTreeNode black=new DefaultMutableTreeNode("black");
        DefaultMutableTreeNode green=new DefaultMutableTreeNode("green");
        color.add(red); color.add(blue); color.add(black); color.add(green);
        JTree jt2 = new Tree(style);
        checklistContent.add(jt2);
        //https://stackoverflow.com/a/21851201
        final ChecklistTreeView cbt = new ChecklistTreeView(checklistJson);
        checklistContent.add(cbt);
        cbt.addCheckChangeEventListener(event -> {
            System.out.println("event");
            TreePath[] paths = cbt.getCheckedPaths();
            for (TreePath tp : paths) {
                for (Object pathPart : tp.getPath()) {
                    System.out.print(pathPart + ",");
                }
                System.out.println();
            }
        });
        //this.setDefaultCloseOperation(EXIT_ON_CLOSE);

    }

    //TODO WRITE PARSER CLASS
    private Checklist cheklistJSONHandler(JsonObject checklistJson) {
        System.out.println("JSON ARRAY: " + checklistJson.toString());
        JsonArray checklist = ((JsonArray) checklistJson.get("checklist"));
        ArrayList<Tasks> tasks = new ArrayList<>();
        String task;
        ArrayList<String> childTasks = new ArrayList<>();
        System.out.println(checklist.get(0).getClass());
        for (JsonElement jsonElement : checklist) {
            Tasks newTask = new Tasks();
            System.out.println("jsonElement TASK : " + jsonElement.getAsJsonObject().get("task"));
            System.out.println("jsonElement TASK : " + jsonElement.getAsJsonObject().get("childTasks"));

        }
        Checklist realChecklist = new Checklist();
        realChecklist.setTasks();
        return null;

    }

    private JComponent createToolbarPanel() {
        final DefaultActionGroup checklistActionGroup = new DefaultActionGroup();
        //checklistActionGroup.add(ActionManager.getInstance().getAction("Handout.Download"));
        checklistActionGroup.add(ActionManager.getInstance().getAction("Checklist.EditContent"));
        final ActionToolbar checklistActionToolbar = ActionManager.getInstance().createActionToolbar("Checklisttool", checklistActionGroup, true);
        return checklistActionToolbar.getComponent();
    }

    public JPanel getContent() {
        System.out.println("Getting Content for checklist");
        return toolWindowPanel;
    }

}
