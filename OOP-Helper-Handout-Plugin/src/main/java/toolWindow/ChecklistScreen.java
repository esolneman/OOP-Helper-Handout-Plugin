package toolWindow;

import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.wm.ToolWindow;
import controller.ChecklistController;
import gui.PluginWebViewFXPanel;
import javafx.application.Platform;
import javafx.scene.web.WebView;
import provider.LocalStorageDataProvider;
import webView.WebViewController;

import javax.swing.*;
import java.net.MalformedURLException;

public class ChecklistScreen extends SimpleToolWindowPanel {
    private PluginWebViewFXPanel checklistContent;
    private ToolWindow checklistToolWindow;
    private String checklistStartPage;
    private static WebView webView;
    private WebViewController webViewController;
    private SimpleToolWindowPanel toolWindowPanel;
    private ChecklistController checklistController;


    public ChecklistScreen(ToolWindow toolWindow) {
        super(true, true);
        checklistController = ChecklistController.getInstance();
        webViewController = new WebViewController();

        toolWindowPanel = new SimpleToolWindowPanel(true);
        checklistToolWindow = toolWindow;
        try {
            checklistStartPage = LocalStorageDataProvider.getLocalPredefinedChecklistFile().toURI().toURL().toString();
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
        });
    }

    public JPanel getContent() {
        return toolWindowPanel;
    }
}