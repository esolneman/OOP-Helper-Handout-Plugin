package provider;

import com.google.gson.*;
import objects.Checklist;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLLIElement;
import org.w3c.dom.html.HTMLUListElement;

import java.util.ArrayList;

public class ParseChecklistJSON {

    private static final String CHECKLIST_JSON = "checklist";
    private static final String TASK_DESCRIPTION_JSON = "taskDescription";
    private static final String ID_JSON = "id";
    private static final String CHECKED_JSON = "checked";
    private static final String IMAGE_CLASS_NAME = "fa fa-check-square";

    //https://stackoverflow.com/a/34510715
    public static Checklist jsonToChecklistParser(JsonObject checklistJson) {
        JsonArray checklist = ((JsonArray) checklistJson.get(CHECKLIST_JSON));
        ArrayList<Checklist.Task> tasks = new ArrayList<>();
        for (JsonElement task : checklist) {
            String taskName = task.getAsJsonObject().get(TASK_DESCRIPTION_JSON).getAsString();
            Boolean checked = task.getAsJsonObject().get(CHECKED_JSON).getAsBoolean();
            String id = task.getAsJsonObject().get(ID_JSON).getAsString();
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
            HTMLElement checkboxImage = (HTMLElement) currentTask.getChildNodes().item(1).getFirstChild();
            String description = currentTask.getFirstChild().getTextContent();
            if (checkboxImage.getClassName().equals(IMAGE_CLASS_NAME)){
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
            task.add(TASK_DESCRIPTION_JSON, gson.toJsonTree(checklist.tasks.get(i).taskDescription));
            task.add(CHECKED_JSON, gson.toJsonTree(checklist.tasks.get(i).checked));
            if(checklist.tasks.get(i).id != null){
                task.add(ID_JSON, gson.toJsonTree(checklist.tasks.get(i).id));
            }
            tasks.add(task);
        }
        return tasks;
    }
}
