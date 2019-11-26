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

    private static ChecklistController single_instance = null;
    private File checklistStartPage;

    public static ChecklistController getInstance() {
        if (single_instance == null) {
            single_instance = new ChecklistController();
        }
        return single_instance;
    }

    private ChecklistController(){
        System.out.println("ChecklistController" );
        createChecklistFile();
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

    public void createChecklistFile() {
        checklistStartPage = LocalStorageDataProvider.getChecklistStartPageFile();

        System.out.println("createChecklistFile: " + checklistStartPage.getPath());
        try {
            checklistStartPage.getParentFile().mkdirs();
            checklistStartPage.createNewFile();
        } catch (IOException e) {
            //TODO CATACH
            System.out.println(e);
        }

        File predefinedChecklistRepoFile = LocalStorageDataProvider.getRepoPredefinedChecklistFile();
        System.out.println("predefinedChecklistRepoFile: " + predefinedChecklistRepoFile.getPath());

        //File userChecklistRepoFile = LocalStorageDataProvider.();
        BufferedReader inputStream = null;
        BufferedWriter outputStream = null;
        try {
            inputStream = new BufferedReader(new FileReader(
                    predefinedChecklistRepoFile));
            File UIFile = checklistStartPage;
            // if File doesnt exists, then create it
            if (!UIFile.exists()) {
                UIFile.createNewFile();
            }
            FileWriter filewriter = new FileWriter(UIFile.getAbsoluteFile());
            outputStream = new BufferedWriter(filewriter);
            String count;
            while ((count = inputStream.readLine()) != null) {
                outputStream.write(count);
            }
            outputStream.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
