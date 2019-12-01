package gui;

import com.sun.webkit.dom.KeyboardEventImpl;
import controller.ChecklistController;
import javafx.concurrent.Worker;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLInputElement;
import org.w3c.dom.html.HTMLUListElement;
import provider.LocalStorageDataProvider;

import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class PluginWebViewFXPanel extends JFXPanel {
    public void showHandoutWebView(String urlString, WebView webView) {
        System.out.println("in: showHandoutWebView");
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
        //webView.getEngine().documentProperty().addListener((ov, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                //if (newState != null) {
                EventListener addTaskButtonListener = ev -> {
                    ChecklistController.getInstance().addTask(finalWebView1);
                };


                EventListener addTaskButtonFocusListener = ev -> {
                    System.out.println("in: addTaskButtonFocusListener: " + ev.getType());
                    System.out.println("in: addTaskButtonFocusListener: " + ev.getCurrentTarget());
                    System.out.println("in: addTaskButtonFocusListener: " + ev.toString());
                    KeyboardEventImpl keyboardEvent = (KeyboardEventImpl) ev;
                    if (keyboardEvent.getKeyCode() == 13){
                        System.out.println("in: keyboardEvent: " + keyboardEvent.getKeyCode());
                        ChecklistController.getInstance().addTask(finalWebView1);
                    }
                };


                Document doc = finalWebView1.getEngine().getDocument();
                if (doc.getDocumentURI().substring(doc.getDocumentURI().indexOf("C")).equals(LocalStorageDataProvider.getLocalUserDataChecklistFile().toURI().toString().substring(LocalStorageDataProvider.getLocalUserDataChecklistFile().toURI().toString().indexOf("C")))) {
                    //TODO Load Tasks OR NOT - ONLY HTML
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
