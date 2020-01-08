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
    public void showHandoutWebView(String urlString, WebView webView) {
        //Platform.setImplicitExit(false);
        //Platform.runLater(() -> {
        webView.getEngine().load(urlString);
        webView.getEngine().setJavaScriptEnabled(true);
        setScene(new Scene(webView));
        //});
    }


    public void showChecklist(String urlString, WebView webView) {
        System.out.println("in: showChecklist");
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
                    //TODO CONSTANT
                    if (keyboardEvent.getKeyCode() == 13){
                        ChecklistController.getInstance().addTask(finalWebView1);
                    }
                };

                Document doc = finalWebView1.getEngine().getDocument();
                if (doc.getDocumentURI().substring(doc.getDocumentURI().indexOf("OOP_Plugin")).equals(LocalStorageDataProvider.getLocalUserDataChecklistFile().toURI().toString().substring(LocalStorageDataProvider.getLocalUserDataChecklistFile().toURI().toString().indexOf("OOP_Plugin")))) {
                    ChecklistController.getInstance().createTaskList("userData", doc, finalWebView1);
                    Element addTaskButton = doc.getElementById("addTaskButton");
                    HTMLInputElement initButton = (HTMLInputElement) doc.getElementById("newTaskDescription");
                    ((EventTarget) addTaskButton).addEventListener("click", addTaskButtonListener, false);
                    ((EventTarget) initButton).addEventListener("keydown", addTaskButtonFocusListener, false);
                } else {
                    ChecklistController.getInstance().createTaskList("predefined", doc, finalWebView1);
                }
            }
        });
        webView.getEngine().load(urlString);
        setScene(new Scene(webView));
    }
}
