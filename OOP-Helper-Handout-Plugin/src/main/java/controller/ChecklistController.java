package controller;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import de.ur.mi.pluginhelper.logger.LogDataType;
import javafx.scene.web.WebView;
import objects.Checklist;
import org.w3c.dom.Document;
import org.w3c.dom.Text;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.*;
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
        //createChecklistFiles();
    }

    public static void saveUserDataInFile(Document checklistDocument) {
        JsonObject checklistJson = new JsonObject();
        JsonArray tasks;
        //TODO duplicated code
        String taskListId = "userDataTaskList";
        HTMLUListElement taskList = (HTMLUListElement) checklistDocument.getElementById(taskListId);
        tasks = ParseChecklistJSON.getJsonFromLiElement(taskList);
        checklistJson.add("checklist", tasks);
        //https://stackoverflow.com/a/29319491
        try (Writer writer = new FileWriter(LocalStorageDataProvider.getChecklistUserData())) {
            Gson gson = new GsonBuilder().create();
            gson.toJson(checklistJson, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void savePredefinedDataInFile(Document checklistDocument) {
        Gson gson = new GsonBuilder().create();
        String taskListId = "predefinedTaskList";
        JsonArray tasks;
        HTMLUListElement taskList = (HTMLUListElement) checklistDocument.getElementById(taskListId);
        tasks = ParseChecklistJSON.getJsonFromLiElement(taskList);
        JsonObject updatedDataJson = new JsonObject();
        //updatedDataJson.add("checklist", tasks);
        updatedDataJson.add("checklist",gson.toJsonTree(tasks) );
        System.out.println("savePredefinedDataInFile checklist description:" + updatedDataJson.get("checklist").toString());
        //https://stackoverflow.com/a/29319491
        try (Writer writer = new FileWriter(LocalStorageDataProvider.getLocalChecklistPredefinedData())) {
            gson = new GsonBuilder().create();
            gson.toJson(updatedDataJson, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void comparePredefinedChecklistVersions() throws FileNotFoundException {
        //https://stackoverflow.com/a/29965924
        Gson gson = new Gson();
        JsonReader reader;
        JsonObject localChecklistData;
        JsonObject repoChecklistData;
        reader = new JsonReader(new FileReader(LocalStorageDataProvider.getLocalChecklistPredefinedData()));
        localChecklistData = gson.fromJson(reader, JsonObject.class);
        reader = new JsonReader(new FileReader(LocalStorageDataProvider.getChecklistData()));
        repoChecklistData = gson.fromJson(reader, JsonObject.class);
        Checklist checklistLocal = ParseChecklistJSON.predefinedJsonToChecklistParser(localChecklistData);
        Checklist checklistRepo = ParseChecklistJSON.predefinedJsonToChecklistParser(repoChecklistData);

        //TODO THINK AND IMPROVE
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
    }

    public void createChecklistFiles() {
        Gson gson = new Gson();
        File checklistUserFile = LocalStorageDataProvider.getChecklistUserData();
        CreateFiles.createNewFile(checklistUserFile);
        JsonObject checklistJson = new JsonObject();
        JsonArray tasks = new JsonArray();
        //checklistJson.add("checklist", tasks);
        checklistJson.add("checklist",gson.toJsonTree(tasks));
        saveJsonObjectInFile(checklistJson, checklistUserFile);


        File localPredefinedChecklistFile = LocalStorageDataProvider.getLocalChecklistPredefinedData();
        File predefinedRepoChecklistFile = LocalStorageDataProvider.getChecklistData();

        //TODO create in Checklsit controller
        //https://stackoverflow.com/a/29965924
        gson = new Gson();
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


        CreateFiles.createNewFile(checklistStartPage);
        CreateFiles.createNewFile(userDataChecklistHTMLFile);

        File predefinedChecklistRepoFile = LocalStorageDataProvider.getRepoPredefinedChecklistFile();
        File userChecklistRepoFile = LocalStorageDataProvider.getRepoUserDataChecklistFile();

        CreateFiles.saveRepoFileInLocalFile(predefinedChecklistRepoFile, checklistStartPage);
        CreateFiles.saveRepoFileInLocalFile(userChecklistRepoFile, userDataChecklistHTMLFile);
    }

    //TODO IN CLASS CREATES FILES
    private void saveJsonObjectInFile(JsonObject jsonObject, File outputFile) {
        //https://stackoverflow.com/a/29319491
        try (Writer writer = new FileWriter(outputFile)) {
            Gson gson = new GsonBuilder().create();
            gson.toJson(jsonObject, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createTaskList(String checklistSource, Document checklistDocument, WebView finalWebView1){
        File checklistDataFile;
        Checklist checklistData;
        JsonObject checklistJson = null;
        BufferedReader br;
        String taskListId = null;
        // FILE AND CHECKLIST DEPEND ON STRING
        switch (checklistSource) {
            case "predefined":
                taskListId = "predefinedTaskList";
                checklistDataFile = LocalStorageDataProvider.getLocalChecklistPredefinedData();
                //TODO Duplicated Code
                //https://stackoverflow.com/a/34486879
                try {
                    br = new BufferedReader(new FileReader(checklistDataFile));
                    JsonParser parser = new JsonParser();
                    checklistJson = parser.parse(br).getAsJsonObject();
                    System.out.println(checklistJson.toString());

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                checklistData = ParseChecklistJSON.predefinedJsonToChecklistParser(checklistJson);
                break;
            case "userData":
                taskListId = "userDataTaskList";
                checklistDataFile = LocalStorageDataProvider.getChecklistUserData();
                //https://stackoverflow.com/a/34486879
                try {
                    br = new BufferedReader(new FileReader(checklistDataFile));
                    JsonParser parser = new JsonParser();
                    checklistJson = parser.parse(br).getAsJsonObject();
                } catch (FileNotFoundException | IllegalStateException e) {
                    System.out.println("OHOH");
                }
                checklistData = ParseChecklistJSON.checklistJSONHandler(checklistJson);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + checklistSource);
        }
        HTMLUListElement taskList = (HTMLUListElement) checklistDocument.getElementById(taskListId);
        //https://stackoverflow.com/a/3955238
        while (taskList.hasChildNodes()) {
            taskList.removeChild(taskList.getLastChild());
        }
        for (int i = 0; i < checklistData.tasks.size(); i++) {
            //TODO METHOD fro creating LI
            HTMLLIElement newTask = (HTMLLIElement) checklistDocument.createElement("li");

            //Text description =  checklistDocument.createTextNode(checklistData.tasks.get(i).taskDescription);
            HTMLDivElement description = (HTMLDivElement) checklistDocument.createElement("div");
            description.setTextContent(checklistData.tasks.get(i).taskDescription);

            newTask.appendChild(description);
            taskList.appendChild(newTask);

            HTMLElement checkbox = (HTMLElement) checklistDocument.createElement("span");
            HTMLElement checkboxImage = (HTMLElement) checklistDocument.createElement("i");
            //TODO make constant for class name
            checkbox.appendChild(checkboxImage);
            checkbox.setClassName("checkbox");
            newTask.appendChild(checkbox);

            ((EventTarget) checkbox).addEventListener("click", getToggleCheckTaskListener(checklistSource, finalWebView1), false);
            //((EventTarget) description).addEventListener("click", getToggleCheckTaskListener(checklistSource, finalWebView1), false);

            if (checklistData.tasks.get(i).checked) {
                checkboxImage.setClassName("fa fa-check-square");
                newTask.setClassName("checked");
            } else {
                checkboxImage.setClassName("fa fa-square");
                newTask.setClassName("unchecked");
            }
            if (checklistData.tasks.get(i).id != null) {
                newTask.setId(checklistData.tasks.get(i).id);
            }
            if (!checklistSource.equals("predefined")) {
                HTMLElement span = (HTMLElement) checklistDocument.createElement("span");
                Text txt = checklistDocument.createTextNode("\u00D7");
                span.setClassName("close");
                span.appendChild(txt);
                newTask.appendChild(span);
                description.setClassName("editableLI");
                description.setAttribute("contenteditable", "true");
                ((EventTarget) description).addEventListener("focusout", getEditableTaskListener(finalWebView1), false);
                ((EventTarget) span).addEventListener("click", getCloseButtonListener(finalWebView1), false);
            }
            taskList.appendChild(newTask);
        }
    }

    //TODO ADD SOURCE W3 PAGE
    //TODO INOUT MAX COUNT CHARS !!!!
    public void addTask(WebView webView) {
        //TODO MAKE EDITIABLE
        Document doc = webView.getEngine().getDocument();
        HTMLUListElement userDataTaskList = (HTMLUListElement) doc.getElementById("userDataTaskList");
        HTMLInputElement newTAskInputField = (HTMLInputElement) doc.getElementById("newTaskDescription");
        String taskDescription = newTAskInputField.getValue();
        if (taskDescription == null || taskDescription == " " || taskDescription.equals("") || taskDescription.equals(" ")) {
            taskDescription = "Neue Aufgabe";
        }

        HTMLLIElement newTask = (HTMLLIElement) doc.createElement("li");
        newTask.setClassName("unchecked");
        HTMLDivElement description = (HTMLDivElement) doc.createElement("div");
        description.setTextContent(taskDescription);
        newTask.appendChild(description);

        HTMLElement checkbox = (HTMLElement) doc.createElement("span");
        HTMLElement checkboxImage = (HTMLElement) doc.createElement("i");
        //TODO make constant for class name
        checkboxImage.setClassName("fa fa-square");
        checkbox.appendChild(checkboxImage);
/*        Text checkboxText = doc.createTextNode("+");
        checkbox.appendChild(checkboxText);*/
        checkbox.setClassName("checkbox");

        userDataTaskList.appendChild(newTask);
        doc.getElementById("newTaskDescription").setTextContent("");
        HTMLElement span = (HTMLElement) doc.createElement("span");
        Text txt = doc.createTextNode("\u00D7");
        span.setClassName("close");
        span.appendChild(txt);
        newTask.appendChild(description);
        newTask.appendChild(checkbox);
        newTask.appendChild(span);
        description.setClassName("editableLI");
        description.setAttribute("contenteditable", "true");
        ((EventTarget) description).addEventListener("focusout", getEditableTaskListener(webView), false);
        ((EventTarget) checkbox).addEventListener("click", getToggleCheckTaskListener("userData", webView), false);
        ((EventTarget) span).addEventListener("click", getCloseButtonListener(webView), false);
        newTAskInputField.setValue("");

        System.out.println("hmmm: " + webView.getEngine().executeScript("document.body.innerHTML"));
        saveUserDataInFile(webView.getEngine().getDocument());
        LoggingController.getInstance().saveDataInLogger(LogDataType.CHECKLIST, "Checklist add Task", taskDescription);

    }

    private EventListener getToggleCheckTaskListener(String dataSource, WebView webView) {
        EventListener toggleCheckListener = ev -> {
            System.out.println("toggleCheckListener: " + webView.getEngine().executeScript("document.body.innerHTML"));

            //https:stackoverflow.com/a/13966749
            ev.stopPropagation();
            System.out.println("EV TARGET: "+ev.getTarget().toString());
            HTMLElement checkbox = (HTMLElement)ev.getTarget();
            System.out.println("EV TARGET checkbox: "+ checkbox);
            System.out.println("EV TARGET checkbox: "+ checkbox.getChildNodes().getLength());
            System.out.println("EV TARGET checkbox: "+ checkbox.getNodeName());


            if(checkbox.getNodeName().equals("SPAN")) {
                System.out.println("EV TARGET SPAN: ");
                checkbox = (HTMLElement) checkbox.getFirstChild();
            }

            HTMLLIElement liElement = (HTMLLIElement) checkbox.getParentNode().getParentNode();



            //https://stackoverflow.com/a/20093950d
            //TODO CONSTANTS
            if (checkbox.getClassName().equals("fa fa-square")) {
                checkbox.setClassName("fa fa-check-square");
                liElement.setClassName("checked");
                LoggingController.getInstance().saveDataInLogger(LogDataType.CHECKLIST, "Checklist check task", dataSource);
            } else {
                LoggingController.getInstance().saveDataInLogger(LogDataType.CHECKLIST, "Checklist uncheck task", dataSource);
                checkbox.setClassName("fa fa-square");
                liElement.setClassName("unchecked");

            }
            if (dataSource.equals("predefined")) {
                savePredefinedDataInFile(webView.getEngine().getDocument());
            } else {
                saveUserDataInFile(webView.getEngine().getDocument());
            }
        };
        return toggleCheckListener;
    }

    public EventListener getCloseButtonListener(WebView webView) {
        EventListener closeListener = eventListener -> {
            //https:stackoverflow.com/a/13966749
            eventListener.stopPropagation();
            HTMLElement closeSpan = (HTMLElement) eventListener.getTarget();
            HTMLLIElement task = (HTMLLIElement) closeSpan.getParentNode();
            HTMLUListElement taskList = (HTMLUListElement) task.getParentNode();
            taskList.removeChild(task);
            saveUserDataInFile(webView.getEngine().getDocument());
            LoggingController.getInstance().saveDataInLogger(LogDataType.CHECKLIST, "Checklist close task", "?");
        };
        return closeListener;
    }

    private EventListener getEditableTaskListener(WebView webView) {
        EventListener editTaskListener = ev -> {
            System.out.println("getEditableTaskListener");
            //https:stackoverflow.com/a/13966749
            ev.stopPropagation();
            saveUserDataInFile(webView.getEngine().getDocument());
            LoggingController.getInstance().saveDataInLogger(LogDataType.CHECKLIST, "Checklist edit task", "?");

        };
        return editTaskListener;
    }
}
