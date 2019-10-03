package toolWindow;

import com.intellij.openapi.wm.ToolWindow;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import provider.RepoLocalStorageDataProvider;

import javax.swing.*;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

//import jdk.tools.jlink.internal.Platform;
//import javafx.embed.swing.JFXPanel;

public class HandoutContentScreen {
    private JFXPanel checklistContent;
    private WebView webView;
    public HandoutContentScreen(ToolWindow toolWindow){
        createContent();
    }

    private void createContent() {
        checklistContent = new JFXPanel();
        File content = RepoLocalStorageDataProvider.getHandoutHtmlFormat();
        String file = "C:/Masterarbeit/TestProjekt/OOP-18WS-CoreDefense-Starter/HelperHandoutPluginContentData/RepoLocalStorage/index.html";
        final String url = "file:///C:/Masterarbeit/TestProjekt/OOP-18WS-CoreDefense-Starter/HelperHandoutPluginContentData/RepoLocalStorage/index.html";
        //URL url = getClass().getResource("C:/Masterarbeit/TestProjekt/OOP-18WS-CoreDefense-Starter/HelperHandoutPluginContentData/RepoLocalStorage/index.html");
        System.out.println(url);

        //String url = WebView.class.getResource(file).toExternalForm();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                webView = new WebView();
                webView.getEngine().load(url);
                //webView.getEngine().load(url.toExternalForm());
                checklistContent.setScene(new Scene(webView));
            }
        });
    }

    public JFXPanel getContent() {
        System.out.println("Getting Content for ChecklistScreen");
        if(webView != null) {
            webView.getEngine().reload();
        }
        return checklistContent;
    }

}
