package provider;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javafx.collections.ObservableList;
import objects.Checklist;
import objects.ChecklistTableTask;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import java.util.ArrayList;

public class ParseChecklistJSON {

    public static JsonObject getJsonFromTreeModel (TreeModel treeModel){
        JsonObject checklistJson = new JsonObject();
        JsonArray tasks = new JsonArray();
        checklistJson.add("checklist", tasks);
        JsonObject task;
        for (int i = 0; i < treeModel.getChildCount(treeModel.getRoot()); i++) {
            task = new JsonObject();
            DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) treeModel.getChild(treeModel.getRoot(), i);
            System.out.println("Parent: " + parentNode.toString());
            task.addProperty("task", parentNode.toString());
            JsonArray childtasks = new JsonArray();
            for (int j = 0; j < parentNode.getChildCount() ; j++) {
                DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) parentNode.getChildAt(j);
                System.out.println("CHILD  : " + childNode.toString());
                childtasks.add(childNode.toString());
            }
            task.add("childtasks", childtasks);
            tasks.add(task);
        }
/*
        JsonObject task = new JsonObject();
        checklistJson.add("checklist", tasks);
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
        tasks.add(task);*/
        return checklistJson;
    }

    public static DefaultTreeModel getTreeModelFromJson (JsonObject jsonObject){
/*        Checklist checklist = checklistJSONHandler(jsonObject);
        DefaultMutableTreeNode initNode = new DefaultMutableTreeNode("Angabe");
        for (int i = 0; i < checklist.getTasks().size(); i++) {
            Checklist.Tasks task = checklist.getTasks().get(i);
            DefaultMutableTreeNode newParentNode = new DefaultMutableTreeNode(task.getTask());
            initNode.add(newParentNode);
            for (String childTask : task.getChildTasks()) {
                DefaultMutableTreeNode newChildNote = new DefaultMutableTreeNode(childTask);
                newParentNode.add(newChildNote);
            }
        }
        DefaultTreeModel model = new DefaultTreeModel(initNode);
        return model;*/

        return null;

    }

    //TODO Combine Methods
    //https://stackoverflow.com/a/34510715
    public static Checklist checklistJSONHandler(JsonObject checklistJson) {
        JsonArray checklist = ((JsonArray) checklistJson.get("checklist"));
        ArrayList<Checklist.Tasks> tasks = new ArrayList<>();
        for (JsonElement task : checklist) {
            String taskName = task.getAsJsonObject().get("taskDescription").toString();
            System.out.println("taskName: " + taskName);

            Boolean checked = task.getAsJsonObject().get("checked").getAsBoolean();
            System.out.println("checked: " + checked);

            //TODO Quelle: Efefective Java Page 13-14 Kapitel 2.2 - Thema 2
            Checklist.Tasks newTask = new Checklist.Tasks.TasksBuilder(taskName, checked).build();
            tasks.add(newTask);
        }
        Checklist realChecklist = new Checklist();
        realChecklist.setTasks(tasks);
        return realChecklist;
    }

    public static Checklist predefinedJsonToChecklistParser(JsonObject checklistJson) {
        JsonArray checklist = ((JsonArray) checklistJson.get("checklist"));
        ArrayList<Checklist.Tasks> tasks = new ArrayList<>();
        for (JsonElement task : checklist) {
            String taskName = task.getAsJsonObject().get("task").toString();
            System.out.println("taskName: " + taskName);

            Boolean checked = task.getAsJsonObject().get("checked").getAsBoolean();
            System.out.println("checked: " + checked);

            String id = task.getAsJsonObject().get("id").toString();
            System.out.println("id: " + id);
            //TODO Quelle: Efefective Java Page 13-14 Kapitel 2.2 - Thema 2
            Checklist.Tasks newTask = new Checklist.Tasks.TasksBuilder(taskName, checked)
                    .id(id).build();
            tasks.add(newTask);
        }
        Checklist realChecklist = new Checklist();
        realChecklist.setTasks(tasks);
        return realChecklist;
    }


    public static Checklist getJsonFromChecklistTableData(ObservableList<ChecklistTableTask> userData) {
        Checklist updatedChecklist;
        ArrayList<Checklist.Tasks> tasksArrayList = new ArrayList<>();
        for (int i = 0; i < userData.size(); i++) {
            String description = userData.get(i).taskDescription.getValue();
            Boolean checked = userData.get(i).checked.getValue();
            Checklist.Tasks newTask = new Checklist.Tasks.TasksBuilder(description, checked).build();
            tasksArrayList.add(newTask);
            System.out.println("NEW CHEKLCIST description: " + description);
            System.out.println("NEW CHEKLCIST: " + tasksArrayList.get(i).taskDescription);

        }
        updatedChecklist = new Checklist();
        updatedChecklist.setTasks(tasksArrayList);
        return updatedChecklist;
    }
}
