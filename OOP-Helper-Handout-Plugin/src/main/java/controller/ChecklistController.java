package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import javafx.collections.ObservableList;
import objects.Checklist;
import objects.ChecklistTableTask;
import provider.LocalStorageDataProvider;
import provider.ParseChecklistJSON;

import java.io.*;

public class ChecklistController {

    public static void saveUserDataInFile(ObservableList<ChecklistTableTask> userData) {
        System.out.println("saveTableDataInFile" + userData.toString());

        JsonObject checklistJson = new JsonObject();
        JsonArray tasks;
        tasks = ParseChecklistJSON.getJsonFromChecklistTableData(userData);
        checklistJson.add("checklist", tasks);

        //https://stackoverflow.com/a/29319491
        try (Writer writer = new FileWriter(LocalStorageDataProvider.getChecklistUserData())) {
            Gson gson = new GsonBuilder().create();
            gson.toJson(checklistJson, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void savePredefinedDataInFile(ObservableList<ChecklistTableTask> userData) {
        System.out.println("saveTableDataInFile" + userData.toString());

        JsonObject checklistJson = new JsonObject();
        JsonArray tasks;
        tasks = ParseChecklistJSON.getJsonFromChecklistTableData(userData);
        checklistJson.add("checklist", tasks);

        //https://stackoverflow.com/a/29319491
        try (Writer writer = new FileWriter(LocalStorageDataProvider.getLocalChecklistPredefinedData())) {
            Gson gson = new GsonBuilder().create();
            gson.toJson(checklistJson, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void comparePredefinedChecklistVersions() throws FileNotFoundException {
        //https://stackoverflow.com/a/29965924
        Gson gson = new Gson();
        JsonReader reader = null;
        JsonObject localChecklistData;
        JsonObject repoChecklistData;
        reader = new JsonReader(new FileReader(LocalStorageDataProvider.getLocalChecklistPredefinedData()));
        localChecklistData = gson.fromJson(reader, JsonObject.class);
        reader = new JsonReader(new FileReader(LocalStorageDataProvider.getChecklistData()));
        repoChecklistData = gson.fromJson(reader, JsonObject.class);
        Checklist checklistLocal = ParseChecklistJSON.checklistJSONHandler(localChecklistData);
        Checklist checklistRepo = ParseChecklistJSON.checklistJSONHandler(repoChecklistData);

        checklistRepo.tasks.forEach(repoTask -> {
            String currentRepoTaskID = repoTask.id;
            String currentRepoTaskDescription = repoTask.taskDescription;
            Integer currentPosition = checklistRepo.tasks.indexOf(repoTask);

            //if task exists update description
            if (checklistLocal.containsID()){
                Checklist.Task newTask = checklistLocal.getTaskWithId();
                newTask.setDescription(currentRepoTaskDescription);
                checklistLocal.tasks.add(currentPosition, newTask);
                //if task not exists add new task
            }else {
                Checklist.Task newTask = new Checklist.Task.TasksBuilder(currentRepoTaskDescription, false)
                        .id(currentRepoTaskID).build();
                checklistLocal.tasks.add(currentPosition, newTask);
            }

        });
    }
}
