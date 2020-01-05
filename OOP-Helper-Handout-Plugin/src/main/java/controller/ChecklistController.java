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

import javax.print.Doc;
import java.io.*;
import java.util.ArrayList;

import static environment.LoggingMessageConstants.*;

public class ChecklistController {

    private static ChecklistController single_instance = null;
    private File checklistStartPage;
    private File userDataChecklistHTMLFile;
    private static String UNCHECKED_CLASS = "unchecked";
    private static String CHECKED_CLASS = "checked";
    private static String CHECKED_CHECKBOX_CLASS = "fa fa-check-square";
    private static String UNCHECKED_CHECKBOX_CLASS = "fa fa-square";
    private static String USER_DATA_SOURCE = "userData";
    private static String PREDEFINED_DATA_SOURCE = "predefined";
    private static String SPAN_ELEMENT = "SPAN";
    private static String SPAN_ELEMENT_HTML = "span";
    private static String IMAGE_ELEMENT_HTML = "i";

    public static ChecklistController getInstance() {
        if (single_instance == null) {
            single_instance = new ChecklistController();
        }
        return single_instance;
    }

    private ChecklistController() {
    }

    public static void saveDocumentInFile(Document document, String dataSource){
        String taskListId;
        File checklistFile;
        if(dataSource.equals(PREDEFINED_DATA_SOURCE)){
            taskListId = "predefinedTaskList";
            checklistFile = LocalStorageDataProvider.getLocalChecklistPredefinedData();
        }else{
            taskListId = "userDataTaskList";
            checklistFile = LocalStorageDataProvider.getChecklistUserData();
        }
        HTMLUListElement taskList = (HTMLUListElement) document.getElementById(taskListId);
        JsonArray tasks = ParseChecklistJSON.getJsonFromLiElement(taskList);
        JsonObject updatedDataJson = new JsonObject();
        updatedDataJson.add("checklist", tasks);
        FileHandleController.saveJsonObjectInFile(updatedDataJson, checklistFile);
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
            if (checklistLocal.getTaskWithId(currentRepoTaskID) != null) {
                checklistLocal.getTaskWithId(currentRepoTaskID).setDescription(currentRepoTaskDescription);
                //if task not exists add new task at the end of the list
                //TODO Think about commetn
            } else {
                Checklist.Task newTask = new Checklist.Task.TasksBuilder(currentRepoTaskDescription, false)
                        .id(currentRepoTaskID).build();
                checklistLocal.tasks.add(newTask);
            }
        }
        //delete task, if no tasks exist with this ID anymore
        Checklist updatedLocalChecklist = checklistLocal;
        for (Checklist.Task localTask : checklistLocal.tasks) {
            String currentLocalTaskID = localTask.id;
            if (checklistRepo.getTaskWithId(currentLocalTaskID) == null) {
                updatedLocalChecklist.tasks.remove(updatedLocalChecklist.getTaskWithId(currentLocalTaskID));
            }
        }
        saveChecklistInFile(updatedLocalChecklist, LocalStorageDataProvider.getLocalChecklistPredefinedData());
    }

    private static void saveChecklistInFile(Checklist checklist, File file) {
        Gson gson = new Gson();
        JsonObject checklistJson = new JsonObject();
        ArrayList<Checklist.Task> tasksArrayList = checklist.tasks;
        JsonArray tasks = ParseChecklistJSON.getJsonFromChecklist(checklist, tasksArrayList);
        checklistJson.add("checklist", gson.toJsonTree(tasks));
        FileHandleController.saveJsonObjectInFile(checklistJson, file);
    }

    public void createChecklistFiles() {
        Gson gson = new Gson();
        File checklistUserFile = LocalStorageDataProvider.getChecklistUserData();
        FileHandleController.createNewFile(checklistUserFile);
        JsonObject checklistJson = new JsonObject();
        JsonArray tasks = new JsonArray();
        checklistJson.add("checklist", gson.toJsonTree(tasks));
        FileHandleController.saveJsonObjectInFile(checklistJson, checklistUserFile);


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
        FileHandleController.saveJsonObjectInFile(repoChecklistData, localPredefinedChecklistFile);


        checklistStartPage = LocalStorageDataProvider.getLocalPredefinedChecklistFile();
        userDataChecklistHTMLFile = LocalStorageDataProvider.getLocalUserDataChecklistFile();


        FileHandleController.createNewFile(checklistStartPage);
        FileHandleController.createNewFile(userDataChecklistHTMLFile);

        File predefinedChecklistRepoFile = LocalStorageDataProvider.getRepoPredefinedChecklistFile();
        File userChecklistRepoFile = LocalStorageDataProvider.getRepoUserDataChecklistFile();

        FileHandleController.saveRepoFileInLocalFile(predefinedChecklistRepoFile, checklistStartPage);
        FileHandleController.saveRepoFileInLocalFile(userChecklistRepoFile, userDataChecklistHTMLFile);
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
            HTMLLIElement newTask = (HTMLLIElement) checklistDocument.createElement("li");
            taskList.appendChild(newTask);
            String checkBoxIcon;
            if (checklistData.tasks.get(i).checked) {
                checkBoxIcon = CHECKED_CHECKBOX_CLASS;
                newTask.setClassName(CHECKED_CLASS);
            } else {
                checkBoxIcon = UNCHECKED_CHECKBOX_CLASS;
                newTask.setClassName(UNCHECKED_CLASS);
            }
            if (checklistData.tasks.get(i).id != null) {
                newTask.setId(checklistData.tasks.get(i).id);
            }
            if (!checklistSource.equals(PREDEFINED_DATA_SOURCE)) {
                newTask.appendChild(createEditableDescriptionElement(checklistDocument,finalWebView1, checklistData.tasks.get(i).taskDescription,"editableLI","contenteditable", "true"  ));
                newTask.appendChild(createCheckBoxElement(checklistDocument, finalWebView1, checklistSource, checkBoxIcon));
                newTask.appendChild(createCloseElement(checklistDocument, finalWebView1));
            } else {
                newTask.appendChild(createDescriptionElement(checklistDocument, checklistData.tasks.get(i).taskDescription));
                newTask.appendChild(createCheckBoxElement(checklistDocument, finalWebView1, checklistSource, checkBoxIcon));
            }
            taskList.appendChild(newTask);
        }
    }

    //TODO ADD SOURCE W3 PAGE
    public void addTask(WebView webView) {
        Document doc = webView.getEngine().getDocument();
        HTMLUListElement userDataTaskList = (HTMLUListElement) doc.getElementById("userDataTaskList");
        HTMLInputElement newTaskInputField = (HTMLInputElement) doc.getElementById("newTaskDescription");
        String taskDescription = newTaskInputField.getValue();
        // source for detect if string contains only whitespaces: https://stackoverflow.com/a/3247081
        if (taskDescription == null || taskDescription.trim().length() == 0) {
            taskDescription = "Neue Aufgabe";
        }
        HTMLLIElement newTask = (HTMLLIElement) doc.createElement("li");
        newTask.setClassName(UNCHECKED_CLASS);
        newTask.appendChild(createEditableDescriptionElement(doc,webView, taskDescription,"editableLI","contenteditable", "true"  ));
        userDataTaskList.appendChild(newTask);
        doc.getElementById("newTaskDescription").setTextContent("");
        newTask.appendChild(createCheckBoxElement(doc, webView, "userData","fa fa-square"));
        newTask.appendChild(createCloseElement(doc, webView));
        newTaskInputField.setValue("");
        saveDocumentInFile(webView.getEngine().getDocument(), USER_DATA_SOURCE);
        LoggingController.getInstance().saveDataInLogger(LogDataType.CHECKLIST, CHECKLIST_ADD_TASK, taskDescription);
    }

    private HTMLElement createCloseElement(Document doc, WebView webView) {
        HTMLElement span = (HTMLElement) doc.createElement(SPAN_ELEMENT_HTML);
        Text txt = doc.createTextNode("\u00D7");
        span.setClassName("close");
        span.appendChild(txt);
        ((EventTarget) span).addEventListener("click", getCloseButtonListener(webView), false);
        return span;
    }

    private HTMLElement createCheckBoxElement(Document doc, WebView webView, String checklistSource, String checkBoxIcon) {
        HTMLElement checkbox = (HTMLElement) doc.createElement(SPAN_ELEMENT_HTML);
        HTMLElement checkboxImage = (HTMLElement) doc.createElement(IMAGE_ELEMENT_HTML);
        //TODO make constant for class name
        checkbox.appendChild(checkboxImage);
        checkbox.setClassName("checkbox");
        ((EventTarget) checkbox).addEventListener("click", getToggleCheckTaskListener(checklistSource, webView), false);
        checkboxImage.setClassName(checkBoxIcon);
        return checkbox;
    }

    private HTMLDivElement createDescriptionElement(Document doc, String description) {
        HTMLDivElement descriptionElement = createBaseDescription( doc, description);
        return descriptionElement;
    }

    private HTMLDivElement createEditableDescriptionElement(Document doc, WebView webView, String description, String className, String attributeStyle , String attributeValue) {
        HTMLDivElement descriptionElement = createBaseDescription( doc,   description);
        descriptionElement.setClassName(className);
        descriptionElement.setAttribute(attributeStyle, attributeValue);
        ((EventTarget) descriptionElement).addEventListener("focusout", getEditableTaskListener(webView), false);
        return descriptionElement;
    }

    private HTMLDivElement createBaseDescription(Document doc, String description){
        HTMLDivElement descriptionElement = (HTMLDivElement) doc.createElement("div");
        descriptionElement.setTextContent(description);
        return descriptionElement;
    }

    private EventListener getToggleCheckTaskListener(String dataSource, WebView webView) {
        EventListener toggleCheckListener = ev -> {
            //https:stackoverflow.com/a/13966749
            ev.stopPropagation();
            HTMLElement checkbox = (HTMLElement) ev.getTarget();
            if (checkbox.getNodeName().equals(SPAN_ELEMENT)) {
                checkbox = (HTMLElement) checkbox.getFirstChild();
            }
            HTMLLIElement liElement = (HTMLLIElement) checkbox.getParentNode().getParentNode();
            //https://stackoverflow.com/a/20093950d
            LogDataType logDataType;
            if (dataSource.equals(USER_DATA_SOURCE)) {
                logDataType = LogDataType.CHECKLIST_USER;
            } else {
                logDataType = LogDataType.CHECKLIST_PREDEFINED;
            }
            if (checkbox.getClassName().equals(UNCHECKED_CHECKBOX_CLASS)) {
                checkbox.setClassName(CHECKED_CHECKBOX_CLASS);
                liElement.setClassName(CHECKED_CLASS);
                LoggingController.getInstance().saveDataInLogger(logDataType, CHECKLIST_CHECKED, liElement.getTextContent());
            } else {
                checkbox.setClassName(UNCHECKED_CHECKBOX_CLASS);
                liElement.setClassName(UNCHECKED_CLASS);
                LoggingController.getInstance().saveDataInLogger(logDataType, CHECKLIST_UNCHECKED, liElement.getTextContent());
            }
            saveDocumentInFile(webView.getEngine().getDocument(), dataSource);
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
            saveDocumentInFile(webView.getEngine().getDocument(), USER_DATA_SOURCE);
            LoggingController.getInstance().saveDataInLogger(LogDataType.CHECKLIST_USER, CHECKLIST_CLOSE_TASK, (((HTMLElement) eventListener.getTarget()).getParentNode().getTextContent()));
        };
        return closeListener;
    }

    private EventListener getEditableTaskListener(WebView webView) {
        EventListener editTaskListener = ev -> {
            //https:stackoverflow.com/a/13966749
            ev.stopPropagation();
            saveDocumentInFile(webView.getEngine().getDocument(), USER_DATA_SOURCE);
            LoggingController.getInstance().saveDataInLogger(LogDataType.CHECKLIST_USER, CHECKLIST_EDIT_TASK, (((HTMLElement) ev.getTarget()).getParentNode().getTextContent()));
        };
        return editTaskListener;
    }
}
