package gui;

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
import org.w3c.dom.html.HTMLInputElement;
import org.w3c.dom.html.HTMLUListElement;
import provider.LocalStorageDataProvider;

import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
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

                System.out.println("HTML        :" + (String) finalWebView1.getEngine().executeScript("document.documentElement.outerHTML"));

                System.out.println("HTML   body     :" + (String) finalWebView1.getEngine().executeScript("document.body.innerHTML"));

                //if (newState != null) {
                System.out.println("DOcument not null");
                System.out.println("newValue: " + newState);

                EventListener addTaskButtonListener = ev -> {
                    ChecklistController.getInstance().addTask(finalWebView1);
                };


                Document doc = finalWebView1.getEngine().getDocument();
                System.out.println("W3 Doc uri: " + doc.getDocumentURI().substring(doc.getDocumentURI().indexOf("C")));
                if (doc.getDocumentURI().substring(doc.getDocumentURI().indexOf("C")).equals(LocalStorageDataProvider.getLocalUserDataChecklistFile().toURI().toString().substring(LocalStorageDataProvider.getLocalUserDataChecklistFile().toURI().toString().indexOf("C")))) {
                    //TODO Load Tasks OR NOT - ONLY HTML
                    //ChecklistController.getInstance().createTaskList("userData", doc, finalWebView1);
                    System.out.println(" USER DATA URL: ");
                    Element addTaskButton = doc.getElementById("addTaskButton");
                    ((EventTarget) addTaskButton).addEventListener("click", addTaskButtonListener, false);
                } else {
                    ChecklistController.getInstance().createTaskList("predefined", doc, finalWebView1);
                    System.out.println("HTML After Create       :" + (String) finalWebView1.getEngine().executeScript("document.documentElement.outerHTML"));
                    System.out.println("HTML inner Create       :" + (String) finalWebView1.getEngine().executeScript("document.body.innerHTML"));

                    System.out.println("Not USER DATA URL: " + LocalStorageDataProvider.getLocalUserDataChecklistFile().toURI());
                }
            } else{
                System.out.println("DOcument  null");
            }
        });
        webView.getEngine().load(urlString);
        setScene(new Scene(webView));
    }
}
