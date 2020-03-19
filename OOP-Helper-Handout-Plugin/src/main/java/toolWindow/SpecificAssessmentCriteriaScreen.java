package toolWindow;

import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.wm.ToolWindow;
import controller.LoggingWebViewController;
import de.ur.mi.pluginhelper.logger.LogDataType;
import gui.PluginWebViewFXPanel;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.scene.web.WebView;
import provider.LocalStorageDataProvider;
import controller.WebViewController;

import javax.swing.*;
import java.io.File;
import java.net.MalformedURLException;

//UI for displaying the assessment criteria
public class SpecificAssessmentCriteriaScreen extends SimpleToolWindowPanel{
    private PluginWebViewFXPanel assessmentContent;
    private ToolWindow handoutToolWindow;
    private static File content;
    private String urlString;
    private SimpleToolWindowPanel toolWindowPanel;
    private static WebView webView;
    private WebViewController webViewController;
    private LoggingWebViewController loggingWebViewController;

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
        assessmentContent = new PluginWebViewFXPanel();
        Platform.setImplicitExit(false);
        Platform.runLater(() -> {
            //load html of assessment criteria in webview
            webView = webViewController.createWebView(urlString);
            webView.getEngine().getLoadWorker().stateProperty().addListener((ov, oldState, newState) -> {
                if (newState == Worker.State.SUCCEEDED) {
                    //add controller to log key and mouse events on this screen
                    loggingWebViewController = new LoggingWebViewController(webView, LogDataType.ASSESSMENT_CRITERIA);
                    loggingWebViewController.addLoggingKeyEvents();
                    loggingWebViewController.addLoggingMouseEvents();
                }
            });
            assessmentContent.showHandoutWebView(urlString, webView);
        });
    }

    public JPanel getContent() {
        return toolWindowPanel;
    }

    public void updateContent() {
        webViewController.updateWebViewContent();
    }
}


