package provider;

import com.google.gson.*;
import objects.Checklist;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLLIElement;
import org.w3c.dom.html.HTMLUListElement;

import java.util.ArrayList;

public class ParseChecklistJSON {

    //TODO Combine Methods
    //https://stackoverflow.com/a/34510715
    public static Checklist checklistJSONHandler(JsonObject checklistJson) {
        JsonArray checklist = ((JsonArray) checklistJson.get("checklist"));
        ArrayList<Checklist.Task> tasks = new ArrayList<>();
        for (JsonElement task : checklist) {
            String taskName = task.getAsJsonObject().get("taskDescription").getAsString();
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
            String taskName = task.getAsJsonObject().get("taskDescription").getAsString();
 /*           byte[] ptext = taskName.getBytes();
            taskName = new String(ptext, UTF_8);*/

            Boolean checked = task.getAsJsonObject().get("checked").getAsBoolean();
            String id = task.getAsJsonObject().get("id").getAsString();
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
        ArrayList<Checklist.Task> tasksArrayList = new ArrayList<>();
        Boolean checked;
        for (int i = 0; i < taskList.getChildNodes().getLength(); i++) {
            HTMLLIElement currentTask = (HTMLLIElement) taskList.getChildNodes().item(i);
            System.out.println("Child 1: " + (HTMLElement) currentTask.getChildNodes().item(1).getFirstChild());
            HTMLElement checkboxImage = (HTMLElement) currentTask.getChildNodes().item(1).getFirstChild();
            String description = currentTask.getFirstChild().getTextContent();
            if (checkboxImage.getClassName().equals("fa fa-check-square")){
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
            }else{
                newTask = new Checklist.Task.TasksBuilder(description, checked).build();
            }
            tasksArrayList.add(newTask);
        }
        Checklist updatedChecklist;
        updatedChecklist = new Checklist();
        return getJsonFromChecklist(updatedChecklist, tasksArrayList);
    }


    public static JsonArray getJsonFromChecklist(Checklist checklist, ArrayList<Checklist.Task> tasksArrayList) {
        JsonArray tasks = new JsonArray();
        Gson gson = new GsonBuilder().create();
        checklist.setTasks(tasksArrayList);
        for (int i = 0; i < checklist.tasks.size(); i++) {
            JsonObject task = new JsonObject();
            task.add("taskDescription", gson.toJsonTree(checklist.tasks.get(i).taskDescription));
            task.add("checked", gson.toJsonTree(checklist.tasks.get(i).checked));
            if(checklist.tasks.get(i).id != null){
                //task.addProperty("id", updatedChecklist.tasks.get(i).id);
                task.add("id", gson.toJsonTree(checklist.tasks.get(i).id));
            }
            tasks.add(task);

        }
        return tasks;
    }
}
