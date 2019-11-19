package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javafx.collections.ObservableList;
import objects.ChecklistTableTask;
import provider.LocalStorageDataProvider;
import provider.ParseChecklistJSON;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class ChecklistController {

    public static void saveUserDataInFile(ObservableList<ChecklistTableTask> userData){
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

    public static void savePredefinedDataInFile(ObservableList<ChecklistTableTask> userData){
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

    public static void comparePredefinedChecklistVersions() {
    }
}
