package toolWindow;

import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.wm.ToolWindow;
import gui.PluginWebViewFXPanel;
import gui.PluginWebViewWithHeaderFXPanel;
import javafx.application.Platform;
import javafx.scene.web.WebView;
import provider.LocalStorageDataProvider;
import webView.WebViewController;

import javax.swing.*;
import java.io.File;
import java.net.MalformedURLException;

import static environment.Messages.ASSESSMENT_CRITERIA_HEADER;

public class SpecificAssessmentCriteriaScreen extends SimpleToolWindowPanel{
    private PluginWebViewWithHeaderFXPanel assessmentContent;
    private ToolWindow handoutToolWindow;
    private static File content;
    private String urlString;
    private SimpleToolWindowPanel toolWindowPanel;
    private static WebView webView;
    private WebViewController webViewController;

    public SpecificAssessmentCriteriaScreen(ToolWindow toolWindow){
        super(true, true);
        toolWindowPanel = new SimpleToolWindowPanel(true);
        webViewController = new WebViewController();
        handoutToolWindow = toolWindow;
        content = LocalStorageDataProvider.getSpecificAssessmentCriteriaFileDirectory();
        try {
            urlString = content.toURI().toURL().toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        createContent();
        initToolWindowMenu();
    }

    private void initToolWindowMenu() {
        //http://androhi.hatenablog.com/entry/2015/07/23/233932
        toolWindowPanel.setContent(assessmentContent);
    }

    private void createContent() {
        assessmentContent = new PluginWebViewWithHeaderFXPanel();
        Platform.setImplicitExit(false);
        Platform.runLater(() -> {
            webView = webViewController.createWebView(urlString);;
            assessmentContent.showWebView(urlString, webView, ASSESSMENT_CRITERIA_HEADER);
        });
    }

    public JPanel getContent() {
        return toolWindowPanel;
    }

    public void updateContent() {
        webViewController.updateWebViewContent();
    }
}


