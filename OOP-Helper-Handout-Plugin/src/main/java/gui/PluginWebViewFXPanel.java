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
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

import java.io.File;

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
            finalWebView1.getEngine().executeScript("toogleCheckedClass()");
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
                System.out.println("W3 Doc: " + webView.getEngine().getDocument());
                System.out.println("W3 Doc uri: " + webView.getEngine().getDocument().getDocumentURI());

                System.out.println("W3 Doc text: " + webView.getEngine().getDocument().getTextContent());
                Element el = (Element) doc.getElementsByTagName("li").item(0);
                //((EventTarget) el).addEventListener("click", listener, false);
            }
        });
        webView.getEngine().load(urlString);
        setScene(new Scene(webView));
    }
}
