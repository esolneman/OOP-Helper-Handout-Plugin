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
        Checklist updatedChecklist;
        JsonArray tasks = new JsonArray();
        Gson gson = new GsonBuilder().create();
        ArrayList<Checklist.Task> tasksArrayList = new ArrayList<>();
        Boolean checked;
        for (int i = 0; i < taskList.getChildNodes().getLength(); i++) {
            HTMLLIElement currentTask = (HTMLLIElement) taskList.getChildNodes().item(i);

            System.out.println("currentTask: " + currentTask);
            System.out.println("currentTask: " + currentTask.getChildNodes().getLength());

            System.out.println("currentTask: " + currentTask.getChildNodes().item(0).toString());
            System.out.println("currentTask: " + currentTask.getChildNodes().item(1).toString());


            HTMLElement checkboxImage = (HTMLElement) currentTask.getChildNodes().item(1).getFirstChild();

            String description = currentTask.getFirstChild().getTextContent();
            System.out.println("description: " + description);
          /*  byte[] ptext = description.getBytes();
            description = new String(ptext, UTF_8);*/
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


        updatedChecklist = new Checklist();
        updatedChecklist.setTasks(tasksArrayList);

        for (int i = 0; i < updatedChecklist.tasks.size(); i++) {
            JsonObject task = new JsonObject();
            //task.addProperty("taskDescription", updatedChecklist.tasks.get(i).taskDescription);
            //task.addProperty("checked", updatedChecklist.tasks.get(i).checked);
            task.add("taskDescription", gson.toJsonTree(updatedChecklist.tasks.get(i).taskDescription));
            task.add("checked", gson.toJsonTree(updatedChecklist.tasks.get(i).checked));
            if(updatedChecklist.tasks.get(i).id != null){
                //task.addProperty("id", updatedChecklist.tasks.get(i).id);
                task.add("id", gson.toJsonTree(updatedChecklist.tasks.get(i).id));
            }
            tasks.add(task);

        }
        return tasks;
    }
}
