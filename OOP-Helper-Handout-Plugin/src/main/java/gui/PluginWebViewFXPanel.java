package gui;

import com.sun.webkit.dom.KeyboardEventImpl;
import controller.ChecklistController;
import javafx.concurrent.Worker;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLInputElement;
import provider.LocalStorageDataProvider;

public class PluginWebViewFXPanel extends JFXPanel {

    private static String ADD_TASK_ELEMENT_ID = "addTaskButton";
    private static String NEW_TASK_ELEMENT_ID = "newTaskDescription";

    private static String CLICK_STRING = "click";
    private static String KEYDOWN_STRING = "keydown";
    private static String PREDEFINED_STRING = "predefined";
    private static String USER_DATA_STRING = "userData";

    private static int ENTER_KEY_CODE = 13;

    private  static String FOLDER_NAME = "OOP_Plugin";

    public void showHandoutWebView(String urlString, WebView webView) {
        webView.getEngine().load(urlString);
        webView.getEngine().setJavaScriptEnabled(true);
        setScene(new Scene(webView));
    }


    public void showChecklist(String urlString, WebView webView) {
        webView.getEngine().setJavaScriptEnabled(true);
        final WebView finalWebView1 = webView;
        //https://stackoverflow.com/a/10684168
        webView.getEngine().getLoadWorker().stateProperty().addListener((ov, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                EventListener addTaskButtonListener = ev -> {
                    ChecklistController.getInstance().addTask(finalWebView1);
                };
                EventListener addTaskButtonFocusListener = ev -> {
                    KeyboardEventImpl keyboardEvent = (KeyboardEventImpl) ev;
                    if (keyboardEvent.getKeyCode() == ENTER_KEY_CODE){
                        ChecklistController.getInstance().addTask(finalWebView1);
                    }
                };
                Document doc = finalWebView1.getEngine().getDocument();
                if (doc.getDocumentURI().substring(doc.getDocumentURI().indexOf(FOLDER_NAME)).equals(LocalStorageDataProvider.getLocalUserDataChecklistFile().toURI().toString().substring(LocalStorageDataProvider.getLocalUserDataChecklistFile().toURI().toString().indexOf("OOP_Plugin")))) {
                    ChecklistController.getInstance().createTaskList(USER_DATA_STRING, doc, finalWebView1);
                    Element addTaskButton = doc.getElementById(ADD_TASK_ELEMENT_ID);
                    HTMLInputElement initButton = (HTMLInputElement) doc.getElementById(NEW_TASK_ELEMENT_ID);
                    ((EventTarget) addTaskButton).addEventListener(CLICK_STRING, addTaskButtonListener, false);
                    ((EventTarget) initButton).addEventListener(KEYDOWN_STRING, addTaskButtonFocusListener, false);
                } else {
                    ChecklistController.getInstance().createTaskList(PREDEFINED_STRING, doc, finalWebView1);
                }
            }
        });
        webView.getEngine().load(urlString);
        setScene(new Scene(webView));
    }
}
