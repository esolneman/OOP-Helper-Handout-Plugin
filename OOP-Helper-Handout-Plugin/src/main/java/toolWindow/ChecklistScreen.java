package toolWindow;

import com.intellij.openapi.wm.ToolWindow;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebView;

import javax.swing.*;

public class ChecklistScreen {
    private JFXPanel checklistContent;
    private WebView webView;

    public ChecklistScreen(ToolWindow toolWindow) {
        createContent();
    }

    private void createContent() {
        checklistContent = new JFXPanel();
        final String url = "https://www.google.com";
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                webView = new WebView();
                webView.getEngine().load(url);
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
