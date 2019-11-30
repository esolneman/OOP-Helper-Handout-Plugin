package gui;

import controller.ChecklistController;
import javafx.concurrent.Worker;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import objects.Checklist;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLInputElement;
import org.w3c.dom.html.HTMLUListElement;
import provider.LocalStorageDataProvider;

import java.net.MalformedURLException;

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
            if (newState == Worker.State.SUCCEEDED) {
                System.out.println("oldValue: " + oldState);
                System.out.println("newValue: " + newState);
                // note next classes are from org.w3c.dom domain
                EventListener listener = ev -> {
                    ChecklistController.getInstance().toggleChecked(ev);
                };


                EventListener addTaskButtonListener = ev -> {
                    Document doc = finalWebView1.getEngine().getDocument();
                    HTMLUListElement userDataTaskList = (HTMLUListElement) doc.getElementById("userDataTaskList");
                    org.w3c.dom.html.HTMLInputElement newTAskInputField = (HTMLInputElement) doc.getElementById("newTaskDescription");
                    ChecklistController.getInstance().addTask(newTAskInputField.getValue(), userDataTaskList, doc);
                };

                Document doc = finalWebView1.getEngine().getDocument();
                System.out.println("W3 Doc uri: " + webView.getEngine().getDocument().getDocumentURI().substring(webView.getEngine().getDocument().getDocumentURI().indexOf("C")));
                NodeList taskElements;
                taskElements = doc.getElementsByTagName("li");
                for (int i = 0; i < taskElements.getLength(); i++) {
                    System.out.println("TaskElements: " + i);
                    Element taskElement = (Element) taskElements.item(i);
                    ((EventTarget) taskElement).addEventListener("click", listener, false);
                }
                System.out.println("getChecklistUserData uri: " + LocalStorageDataProvider.getLocalUserDataChecklistFile().toURI().toString().substring(LocalStorageDataProvider.getLocalUserDataChecklistFile().toURI().toString().indexOf("C")));

                if (webView.getEngine().getDocument().getDocumentURI().substring(webView.getEngine().getDocument().getDocumentURI().indexOf("C")).equals(LocalStorageDataProvider.getLocalUserDataChecklistFile().toURI().toString().substring(LocalStorageDataProvider.getLocalUserDataChecklistFile().toURI().toString().indexOf("C")))){
                    //TODO Load Tasks
                    ChecklistController.getInstance().createTaskList("userData", webView);
                    System.out.println(" USER DATA URL: ");
                    Element addTaskButton = doc.getElementById("addTaskButton");
                    ((EventTarget) addTaskButton).addEventListener("click", addTaskButtonListener, false);
                }else{
                    ChecklistController.getInstance().createTaskList("predefined", webView);
                    System.out.println("Not USER DATA URL: " + LocalStorageDataProvider.getLocalUserDataChecklistFile().toURI());

                }
            }
        });
        webView.getEngine().load(urlString);
        setScene(new Scene(webView));
    }
}
