package provider;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import objects.Checklist;

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
        Checklist checklist = checklistJSONHandler(jsonObject);
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
        return model;
    }

    //TODO WRITE PARSER CLASS
    //https://stackoverflow.com/a/34510715
    public static Checklist checklistJSONHandler(JsonObject checklistJson) {
        JsonArray checklist = ((JsonArray) checklistJson.get("checklist"));
        ArrayList<Checklist.Tasks> tasks = new ArrayList<>();
        for (JsonElement jsonElement : checklist) {
            final ArrayList<String> childTasks = new ArrayList<>();
            JsonElement childtasksEle = jsonElement.getAsJsonObject().get("childtasks");
            childtasksEle.getAsJsonArray().forEach(jsonElement1 -> childTasks.add(jsonElement1.toString()));
            Checklist.Tasks newTask = new Checklist.Tasks(jsonElement.getAsJsonObject().get("task").getAsString(), childTasks);
            tasks.add(newTask);
        }
        Checklist realChecklist = new Checklist();
        realChecklist.setTasks(tasks);
        return realChecklist;
    }

}
