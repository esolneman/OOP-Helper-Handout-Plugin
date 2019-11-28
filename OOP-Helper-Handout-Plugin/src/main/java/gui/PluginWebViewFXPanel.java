package gui;

import controller.ChecklistController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import objects.Checklist;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

import java.io.File;
import java.util.ArrayList;

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
                    System.out.println(ev.getTarget());
                    System.out.println("gf");
                    ChecklistController.getInstance().toggleChecked(ev);
                };
                Document doc = finalWebView1.getEngine().getDocument();
                System.out.println("W3 Doc uri: " + webView.getEngine().getDocument().getDocumentURI());
                NodeList taskElements;
                taskElements = doc.getElementsByTagName("li");
                for (int i = 0; i < taskElements.getLength(); i++) {
                    System.out.println("TaskElements: " + i);
                    Element taskElement = (Element) taskElements.item(i);
                    ((EventTarget) taskElement).addEventListener("click", listener, false);
                }
            }
        });
        webView.getEngine().load(urlString);
        setScene(new Scene(webView));
    }
}
