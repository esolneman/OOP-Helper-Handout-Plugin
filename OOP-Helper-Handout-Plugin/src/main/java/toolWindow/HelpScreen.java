package toolWindow;

import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.wm.ToolWindow;
import controller.LoggingWebViewController;
import de.ur.mi.pluginhelper.logger.LogDataType;
import eventHandling.HelpWebViewLinkListener;
import gui.PluginWebViewFXPanel;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.scene.web.WebView;
import provider.LocalStorageDataProvider;
import controller.WebViewController;

import javax.swing.*;
import java.net.MalformedURLException;

//UI for displaying further ressources (information about coding styles and variables,
// shortcuts and the tutorial
public class HelpScreen extends SimpleToolWindowPanel {
    private PluginWebViewFXPanel criteriaContent;
    private ToolWindow handoutToolWindow;
    private static String tutorialDirectory;
    private String startPageDirectory;
    private SimpleToolWindowPanel toolWindowPanel;
    private static WebView webView;
    private static WebViewController webViewController;
    private JPanel panel;
    private LoggingWebViewController loggingWebViewController;
    private static final String SHORTCUTS_ID = "shortcuts";
    private static final String TUTORIAL_ID ="tutorial";
    private static final String CODING_STYLES_ID = "CodingStyles";
    private static final String Variable_ID ="Variable";


    public HelpScreen(ToolWindow toolWindow) {
        super(true, true);
        toolWindowPanel = new SimpleToolWindowPanel(true);
        panel = new JPanel();
        webViewController = new WebViewController();
        handoutToolWindow = toolWindow;
        try {
            startPageDirectory = LocalStorageDataProvider.getHelpStartDirectory().toURI().toURL().toString();
            tutorialDirectory = LocalStorageDataProvider.getTutorialDirectory().toURI().toURL().toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        createContent();
        initToolWindowMenu();
    }

    private void initToolWindowMenu() {
        //http://androhi.hatenablog.com/entry/2015/07/23/233932
        toolWindowPanel.setContent(criteriaContent);
    }

    private void createContent() {
        criteriaContent = new PluginWebViewFXPanel();
        Platform.setImplicitExit(false);
        Platform.runLater(() -> {
            webView = webViewController.createWebView(startPageDirectory);
            criteriaContent.showHandoutWebView(startPageDirectory, webView);
            final WebView currentWebView = webView;
            //only needed to log events
            webView.getEngine().getLoadWorker().stateProperty().addListener((ov, oldState, newState) -> {
                if (newState == Worker.State.SUCCEEDED) {
                    if(webView.getEngine().getDocument() != null) {
                        HelpWebViewLinkListener webViewLinkListener = new HelpWebViewLinkListener(currentWebView, startPageDirectory);
                        webViewLinkListener.createListener();
                        //log key and mouse events, depends on current page in help-tab
                        LogDataType logDataType;
                        if(webView.getEngine().getLocation().contains(SHORTCUTS_ID)){
                            logDataType = LogDataType.HELP_SHORTCUTS;
                        } else if (webView.getEngine().getLocation().contains(TUTORIAL_ID)) {
                            logDataType = LogDataType.HELP_TUTORIAL;
                        } else if (webView.getEngine().getLocation().contains(CODING_STYLES_ID)) {
                            logDataType = LogDataType.HELP_CODING_STYLES;
                        } else if (webView.getEngine().getLocation().contains(Variable_ID)) {
                            logDataType = LogDataType.HELP_VARIABLES;
                        } else {
                            logDataType = LogDataType.HELP_INDEX;
                        }
                        loggingWebViewController = new LoggingWebViewController(webView, logDataType);
                        loggingWebViewController.addLoggingKeyEvents();
                        loggingWebViewController.addLoggingMouseEvents();
                    }
                }
            });

        });
    }

    public static void displayTutorial(){
        webViewController.loadNewURL(tutorialDirectory);
    }

    public JComponent getToolbar() {
        return toolWindowPanel.getToolbar();
    }

    public JPanel getContent() {
        return toolWindowPanel;
    }

    public void updateContent() {
        webViewController.updateWebViewContent();
    }
}
