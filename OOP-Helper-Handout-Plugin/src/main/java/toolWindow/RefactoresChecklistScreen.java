package toolWindow;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.wm.ToolWindow;
import controller.ChecklistController;
import gui.PluginWebViewFXPanel;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.scene.web.WebView;
import objects.Checklist;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import provider.LocalStorageDataProvider;
import provider.ParseChecklistJSON;
import webView.WebViewController;
import netscape.javascript.JSObject;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.MalformedURLException;

public class RefactoresChecklistScreen extends SimpleToolWindowPanel {
    private PluginWebViewFXPanel checklistContent;
    private ToolWindow checklistToolWindow;
    private JsonObject checklistJson;
    private JsonObject userChecklistJson;
    private String checklistStartPage;
    private static WebView webView;
    private WebViewController webViewController;
    private SimpleToolWindowPanel toolWindowPanel;
    private File file;
    private File userData;
    private ChecklistController checklistController;


    public RefactoresChecklistScreen(ToolWindow toolWindow) {
        super(true, true);
        checklistController = ChecklistController.getInstance();
        webViewController = new WebViewController();

        toolWindowPanel = new SimpleToolWindowPanel(true);
        checklistToolWindow = toolWindow;
        try {
            checklistStartPage = LocalStorageDataProvider.getLocalPredefinedChecklistFile().toURI().toURL().toString();
            System.out.println("checklistStartPage: " + checklistStartPage);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        createContent();
        initToolWindowMenu();
    }

    private void initToolWindowMenu() {
        //http://androhi.hatenablog.com/entry/2015/07/23/233932
        toolWindowPanel.setContent(checklistContent);
    }

    private void createContent() {
        checklistContent = new PluginWebViewFXPanel();
        Platform.setImplicitExit(false);
        Platform.runLater(() -> {
            webView = webViewController.createWebView(checklistStartPage);
            checklistContent.showChecklist(checklistStartPage, webView);
           // checklistController.createTaskList("predefined", checklist, webView);
            initAddTaskButtonListener();
            initDeleteTaskButtonListener();
            initCheckTaskButtonListener();
        });

    }

    private void initCheckTaskButtonListener() {
        System.out.println("initCheckTaskButtonListener");
        JSObject window = (JSObject) webView.getEngine().executeScript("window");
        window.setMember("clickTask", ChecklistController.getInstance());
    }

    private void initDeleteTaskButtonListener() {
        System.out.println("initAddTaskButtonListener");
        JSObject window = (JSObject) webView.getEngine().executeScript("window");
        window.setMember("checklistController", ChecklistController.getInstance());
    }

    private void initAddTaskButtonListener() {
        //https://stackoverflow.com/a/34547416
        //create listener for "add task" button in webView
        System.out.println("initAddTaskButtonListener");
        //noteAddingFrame = NoteAddingFrame.getInstance();
        //noteAddingFrame.setNotesScreen(this);
        //JSObject window = (JSObject) webView.getEngine().executeScript("window");
        //window.setMember("noteAddingFrame", NoteAddingFrame.getInstance());
    }

    public JPanel getContent() {
        return toolWindowPanel;
    }
}