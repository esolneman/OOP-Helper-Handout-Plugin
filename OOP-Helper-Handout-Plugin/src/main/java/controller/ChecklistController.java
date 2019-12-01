package controller;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import javafx.scene.web.WebView;
import objects.Checklist;
import org.w3c.dom.Document;
import org.w3c.dom.Text;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLInputElement;
import org.w3c.dom.html.HTMLLIElement;
import org.w3c.dom.html.HTMLUListElement;
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
        System.out.println("saveTableDataInFile");
        JsonObject checklistJson = new JsonObject();
        JsonArray tasks;

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
        System.out.println("saveTableDataInFile");
        String taskListId = "predefinedTaskList";
        JsonArray tasks;
        HTMLUListElement taskList = (HTMLUListElement) checklistDocument.getElementById(taskListId);
        tasks = ParseChecklistJSON.getJsonFromLiElement(taskList);
        JsonObject updatedDataJson = new JsonObject();
        updatedDataJson.add("checklist", tasks);


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

    public void createTaskList(String checklistSource, Document checklistDocument, WebView finalWebView1) {
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
            Text description = checklistDocument.createTextNode(checklistData.tasks.get(i).taskDescription);
            newTask.appendChild(description);
            taskList.appendChild(newTask);
            newTask.setClassName("");
            //newTask.setTextContent(checklistData.tasks.get(i).taskDescription);
            ((EventTarget) newTask).addEventListener("click", getToggleCheckTaskListener(checklistSource, finalWebView1), false);
            if (checklistData.tasks.get(i).checked) {
                newTask.setClassName("checked");
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
                ((EventTarget) span).addEventListener("click", getCloseButtonListener(finalWebView1), false);
            }
            taskList.appendChild(newTask);
        }
    }

    private void saveDocumentInFile(WebView finalWebView1, File checklistFile) {
        //TODO check source
        //https://stackoverflow.com/a/30258688
        //https://www.baeldung.com/java-write-to-file#write-with-printwriter
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(checklistFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.print(finalWebView1.getEngine().executeScript("document.documentElement.outerHTML"));
        printWriter.close();

        //https://stackoverflow.com/a/52965533
        /*DOMSource domSource = new DOMSource(finalWebView1.getEngine().getDocument());
        StreamResult result = new StreamResult(checklistFile);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.METHOD, "html");
            transformer.transform(domSource, result);
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }*/

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
        newTask.setClassName("");
        Text description = doc.createTextNode(taskDescription);
        newTask.appendChild(description);
        userDataTaskList.appendChild(newTask);
        doc.getElementById("newTaskDescription").setTextContent("");
        HTMLElement span = (HTMLElement) doc.createElement("span");
        Text txt = doc.createTextNode("\u00D7");
        span.setClassName("close");
        span.appendChild(txt);
        newTask.appendChild(span);
        newTask.setAttribute("contentEditable", "true;");
        ((EventTarget) newTask).addEventListener("click", getToggleCheckTaskListener("userData", webView), false);
        ((EventTarget) span).addEventListener("click", getCloseButtonListener(webView), false);
        newTAskInputField.setValue("");
        saveUserDataInFile(doc);
    }

    private EventListener getToggleCheckTaskListener(String dataSource, WebView webView) {
        EventListener toggleCheckListener = ev -> {
            //https:stackoverflow.com/a/13966749
            ev.stopPropagation();
            //https://stackoverflow.com/a/20093950d
            HTMLLIElement task = (HTMLLIElement) ev.getTarget();
            if (task.getClassName().equals("checked")) {
                task.setClassName("");
            } else {
                task.setClassName("checked");
            }
            if (dataSource.equals("predefined")) {
                //saveDocumentInFile(webView, LocalStorageDataProvider.getLocalPredefinedChecklistFile());
                savePredefinedDataInFile(webView.getEngine().getDocument());
            } else {
                //saveDocumentInFile(webView, LocalStorageDataProvider.getLocalUserDataChecklistFile());
                saveUserDataInFile(webView.getEngine().getDocument());
            }

        };
        return toggleCheckListener;
    }

    public EventListener getCloseButtonListener(WebView webView) {
        EventListener closeListener = ev -> {
            https:
//stackoverflow.com/a/13966749
            ev.stopPropagation();
            HTMLElement closeSpan = (HTMLElement) ev.getTarget();
            HTMLLIElement task = (HTMLLIElement) closeSpan.getParentNode();
            task.setAttribute("style", "display:none;");
            //saveDocumentInFile(webView, LocalStorageDataProvider.getLocalUserDataChecklistFile());
            saveUserDataInFile(webView.getEngine().getDocument());

        };
        return closeListener;
    }
}
