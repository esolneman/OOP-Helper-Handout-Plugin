package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import javafx.collections.ObservableList;
import objects.Checklist;
import objects.ChecklistTableTask;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import provider.LocalStorageDataProvider;
import provider.ParseChecklistJSON;

import java.io.*;

public class ChecklistController {

    private static ChecklistController single_instance = null;
    private File checklistStartPage;
    private File userDataChecklistHTMLFile;

    public static ChecklistController getInstance() {
        if (single_instance == null) {
            single_instance = new ChecklistController();
        }
        return single_instance;
    }

    private ChecklistController() {
        System.out.println("ChecklistController");
        createChecklistFiles();
    }

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

    public static void savePredefinedDataInFile(ObservableList<ChecklistTableTask> predefinedData) {
        System.out.println("saveTableDataInFile" + predefinedData.get(0).id);

        JsonObject updatedDataJson = new JsonObject();
        JsonArray tasks;
        tasks = ParseChecklistJSON.getJsonFromChecklistTableData(predefinedData);
        updatedDataJson.add("checklist", tasks);
        System.out.println("SAVEPREDEFINED DATA: " + predefinedData.get(2).id.getValue());
        System.out.println("SAVEPREDEFINED updatedDataJson: " + updatedDataJson.get("checklist").getAsJsonArray().get(2).getAsJsonObject().get("id"));

        //https://crunchify.com/how-to-write-json-object-to-file-in-java/
        try (FileWriter file = new FileWriter(LocalStorageDataProvider.getLocalChecklistPredefinedData())) {
            file.write(updatedDataJson.toString());
            System.out.println("Successfully Copied JSON Object to File...");
            System.out.println("\nJSON Object: " + updatedDataJson);
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
        Checklist checklistLocal = ParseChecklistJSON.predefinedJsonToChecklistParser(localChecklistData);
        Checklist checklistRepo = ParseChecklistJSON.predefinedJsonToChecklistParser(repoChecklistData);

        for (Checklist.Task repoTask : checklistRepo.tasks) {
            String currentRepoTaskID = repoTask.id;
            String currentRepoTaskDescription = repoTask.taskDescription;
            //if task exists update description
            if (checklistLocal.containsID(currentRepoTaskID)) {
                System.out.println("REPO ID EXISTS");
                checklistLocal.getTaskWithId(currentRepoTaskID).setDescription(currentRepoTaskDescription);
                //if task not exists add new task at the end of the list
                //TODO Think about commetn
            } else {
                System.out.println("REPO ID NOT EXISTS");
                Checklist.Task newTask = new Checklist.Task.TasksBuilder(currentRepoTaskDescription, false)
                        .id(currentRepoTaskID).build();
                checklistLocal.tasks.add(newTask);
            }
        }

        //check if local tasks exists still in the repo
        //TODO ERROR ConcurrentModificationException
        for (Checklist.Task localTask : checklistLocal.tasks) {
            if (!checklistRepo.containsID(localTask.id)) {
                System.out.println("LOCAL ID NOT EXISTS");
                checklistLocal.tasks.remove(localTask);
            }
        }
    }

    public void createChecklistFiles() {
        //TODO create in Checklsit controller
        File checklistUserFile = LocalStorageDataProvider.getChecklistUserData();
        CreateFiles.createNewFile(checklistUserFile);
        JsonObject checklistJson = new JsonObject();
        JsonArray tasks = new JsonArray();
        checklistJson.add("checklist", tasks);
        saveJsonObjectInFile(checklistJson, checklistUserFile);

        File localPredefinedChecklistFile = LocalStorageDataProvider.getLocalChecklistPredefinedData();
        File predefinedRepoChecklistFile = LocalStorageDataProvider.getChecklistData();

        //TODO create in Checklsit controller
        //https://stackoverflow.com/a/29965924
        Gson gson = new Gson();
        JsonReader reader = null;
        try {
            reader = new JsonReader(new FileReader(predefinedRepoChecklistFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        JsonObject repoChecklistData = gson.fromJson(reader, JsonObject.class);
        saveJsonObjectInFile(repoChecklistData, localPredefinedChecklistFile);


        checklistStartPage = LocalStorageDataProvider.getLocalPredefinedChecklistFile();
        userDataChecklistHTMLFile = LocalStorageDataProvider.getLocalUserDataChecklistFile();

        System.out.println("createChecklistFile: " + checklistStartPage.getPath());
        CreateFiles.createNewFile(checklistStartPage);
        CreateFiles.createNewFile(userDataChecklistHTMLFile);

        File predefinedChecklistRepoFile = LocalStorageDataProvider.getRepoPredefinedChecklistFile();
        System.out.println("predefinedChecklistRepoFile: " + predefinedChecklistRepoFile.getPath());
        File userChecklistRepoFile = LocalStorageDataProvider.getRepoUserDataChecklistFile();

        CreateFiles.saveRepoFileInLocalFile(predefinedChecklistRepoFile, checklistStartPage);
        CreateFiles.saveRepoFileInLocalFile(userChecklistRepoFile, userDataChecklistHTMLFile);
    }

    //TODO IN CLASS CREATES FILES
    private void saveJsonObjectInFile(JsonObject jsonObject, File file) {
        //https://stackoverflow.com/a/29319491
        try (Writer writer = new FileWriter(file)) {
            Gson gson = new GsonBuilder().create();
            gson.toJson(jsonObject, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createTaskList(String predefined, Checklist checklist) {
        File localPredefinedChecklistFile = LocalStorageDataProvider.getLocalPredefinedChecklistFile();
        System.out.println(localPredefinedChecklistFile.getPath());

        try {
            //TODO maybe call javaScript function direccctly
            Document jsoupDoc = Jsoup.parse(localPredefinedChecklistFile, "UTF-8");
            //W3CDom w3cDom = new W3CDom();
            //org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
            System.out.println(jsoupDoc.html());
           Element taskList = jsoupDoc.getElementById("predefinedTaskList");
           System.out.println("taskLIst: " + taskList.html());
            for (int i = 0; i < checklist.tasks.size(); i++) {
                Element newTask = jsoupDoc.createElement("li");
                newTask.text(checklist.tasks.get(i).taskDescription);
                if(checklist.tasks.get(i).checked){
                    //TODO INSERT STYLE CLASS AVTIVE
                    newTask.addClass("checked");
                }
                taskList.appendChild(newTask);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
