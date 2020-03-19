package toolWindow;

import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.wm.ToolWindow;
import controller.ChecklistController;
import controller.LoggingWebViewController;
import de.ur.mi.pluginhelper.logger.LogDataType;
import gui.PluginWebViewFXPanel;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.scene.web.WebView;
import provider.LocalStorageDataProvider;
import controller.WebViewController;

import javax.swing.*;
import java.net.MalformedURLException;

//UI for displaying the predefined tasklist and the tasklist to be created by the user himself
public class ChecklistScreen extends SimpleToolWindowPanel {
    private PluginWebViewFXPanel checklistContent;
    private ToolWindow checklistToolWindow;
    private String checklistStartPage;
    private static WebView webView;
    private WebViewController webViewController;
    private SimpleToolWindowPanel toolWindowPanel;
    private ChecklistController checklistController;
    private LoggingWebViewController loggingWebViewController;

    private static String PREDEFINED_ID = "Predefined";


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
            //load html of predefined task list in webview
            webView = webViewController.createWebView(checklistStartPage);
            webView.getEngine().getLoadWorker().stateProperty().addListener((ov, oldState, newState) -> {
                if (newState == Worker.State.SUCCEEDED) {
                    LogDataType logDataType;
                    if(webView.getEngine().getLocation().contains(PREDEFINED_ID)){
                        logDataType = LogDataType.CHECKLIST_PREDEFINED;
                    } else {
                        logDataType = LogDataType.CHECKLIST_USER;
                    }
                    //add controller to log key and mouse events on this screen
                    loggingWebViewController = new LoggingWebViewController(webView, logDataType);
                    loggingWebViewController.addLoggingKeyEvents();
                    loggingWebViewController.addLoggingMouseEvents();
                }
            });
            checklistContent.showChecklist(checklistStartPage, webView);
        });
    }

    public JPanel getContent() {
        return toolWindowPanel;
    }

    public void updateContent() {
        webViewController.updateWebViewContent();
    }
}