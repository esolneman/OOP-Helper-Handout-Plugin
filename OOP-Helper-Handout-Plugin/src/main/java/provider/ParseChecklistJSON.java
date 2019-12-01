package provider;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javafx.collections.ObservableList;
import objects.Checklist;
import objects.ChecklistTableTask;
import org.w3c.dom.html.HTMLLIElement;
import org.w3c.dom.html.HTMLUListElement;

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
        System.out.println("predefinedJsonToChecklistParser: " + checklistJson.toString());
        System.out.println("isJsonObject ebene weiter: " + checklist.get(0).isJsonObject());
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

    public static JsonArray getJsonFromLiElement(HTMLUListElement taskList){
        Checklist updatedChecklist;
        JsonArray tasks = new JsonArray();
        ArrayList<Checklist.Task> tasksArrayList = new ArrayList<>();
        Boolean checked;
        for (int i = 0; i < taskList.getChildNodes().getLength(); i++) {
            HTMLLIElement currentTask = (HTMLLIElement) taskList.getChildNodes().item(i);
            String description = currentTask.getTextContent();
            if (currentTask.getClassName().equals("checked")){
                 checked = true;
            } else{
                 checked = false;
            }
            Checklist.Task newTask;
            if(currentTask.getId() != null){
                String id = currentTask.getId();
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
