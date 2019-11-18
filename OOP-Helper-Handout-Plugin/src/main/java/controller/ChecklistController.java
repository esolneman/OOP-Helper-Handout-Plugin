package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import javafx.collections.ObservableList;
import objects.Checklist;
import objects.ChecklistTableTask;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import provider.LocalStorageDataProvider;
import provider.ParseChecklistJSON;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

public class ChecklistController {

    public static void saveTableDataInFile(ObservableList<ChecklistTableTask> userData){
        System.out.println("saveTableDataInFile" + userData.toString());
        Checklist updatedChecklist = ParseChecklistJSON.getJsonFromChecklistTableData(userData);
        //https://stackoverflow.com/a/29319491
        try (Writer writer = new FileWriter(LocalStorageDataProvider.getChecklistUserData())) {
            Gson gson = new GsonBuilder().create();
            gson.toJson(updatedChecklist, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
