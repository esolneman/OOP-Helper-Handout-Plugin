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

    //TODO Combine Methods
    //https://stackoverflow.com/a/34510715
    public static Checklist checklistJSONHandler(JsonObject checklistJson) {
        JsonArray checklist = ((JsonArray) checklistJson.get("checklist"));
        ArrayList<Checklist.Task> tasks = new ArrayList<>();
        for (JsonElement task : checklist) {
            String taskName = task.getAsJsonObject().get("taskDescription").toString();
            Boolean checked = task.getAsJsonObject().get("checked").getAsBoolean();

            //TODO Quelle: Efefective Java Page 13-14 Kapitel 2.2 - Thema 2
            Checklist.Task newTask = new Checklist.Task.TasksBuilder(taskName, checked).build();
            tasks.add(newTask);
        }
        Checklist realChecklist = new Checklist();
        realChecklist.setTasks(tasks);
        return realChecklist;
    }

    public static Checklist predefinedJsonToChecklistParser(JsonObject checklistJson) {
        JsonArray checklist = ((JsonArray) checklistJson.get("checklist"));
        ArrayList<Checklist.Task> tasks = new ArrayList<>();
        for (JsonElement task : checklist) {
            String taskName = task.getAsJsonObject().get("taskDescription").toString();
            Boolean checked = task.getAsJsonObject().get("checked").getAsBoolean();
            String id = task.getAsJsonObject().get("id").toString();
            //TODO Quelle: Efefective Java Page 13-14 Kapitel 2.2 - Thema 2
            Checklist.Task newTask = new Checklist.Task.TasksBuilder(taskName, checked)
                    .id(id).build();
            tasks.add(newTask);
        }
        Checklist realChecklist = new Checklist();
        realChecklist.setTasks(tasks);
        return realChecklist;
    }


    public static JsonArray getJsonFromChecklistTableData(ObservableList<ChecklistTableTask> userData) {
        Checklist updatedChecklist;
        ArrayList<Checklist.Task> tasksArrayList = new ArrayList<>();
        for (int i = 0; i < userData.size(); i++) {
            String description = userData.get(i).taskDescription.getValue();
            Boolean checked = userData.get(i).checked.getValue();
            Checklist.Task newTask;
            if(userData.get(i).id != null){
                System.out.println("DATA HAS ID WHOOOP WHHOPP: " + description);
                String id = userData.get(i).id.getValue();
                newTask = new Checklist.Task.TasksBuilder(description, checked)
                        .id(id)
                        .build();
                System.out.println("getJsonFromChecklistTableData: " + newTask.id);
            }else{
                newTask = new Checklist.Task.TasksBuilder(description, checked).build();
            }
            tasksArrayList.add(newTask);
        }


        updatedChecklist = new Checklist();
        updatedChecklist.setTasks(tasksArrayList);
        JsonObject updatedJson = new JsonObject();
        JsonArray tasks = new JsonArray();
        updatedJson.add("checklist", tasks);

        for (int i = 0; i < updatedChecklist.tasks.size(); i++) {
            JsonObject task = new JsonObject();
            task.addProperty("taskDescription", updatedChecklist.tasks.get(i).taskDescription);
            task.addProperty("checked", updatedChecklist.tasks.get(i).checked);
            if(updatedChecklist.tasks.get(i).id != null){
                task.addProperty("id", updatedChecklist.tasks.get(i).id);
            }
            tasks.add(task);
        }
        return tasks;
    }
}
