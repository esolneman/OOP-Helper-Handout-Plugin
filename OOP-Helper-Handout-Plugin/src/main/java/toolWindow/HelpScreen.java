package toolWindow;

import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.wm.ToolWindow;
import eventHandling.HelpWebViewLinkListener;
import gui.PluginWebViewFXPanel;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.scene.web.WebView;
import provider.LocalStorageDataProvider;
import webView.WebViewController;

import javax.swing.*;
import java.net.MalformedURLException;

public class HelpScreen extends SimpleToolWindowPanel {
    private PluginWebViewFXPanel criteriaContent;
    private ToolWindow handoutToolWindow;
    private String variablesDirectory;
    private String codingstylesDirectory;
    private static String tutorialDirectory;
    private String startPageDirectory;
    private SimpleToolWindowPanel toolWindowPanel;
    private static WebView webView;
    private static WebViewController webViewController;
    private JPanel panel;


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
            webView = webViewController.createHelpWebView(startPageDirectory);
            criteriaContent.showHandoutWebView(startPageDirectory, webView);
            final WebView currentWebView = webView;
            webView.getEngine().getLoadWorker().stateProperty().addListener((ov, oldState, newState) -> {
                if (newState == Worker.State.SUCCEEDED) {
                    if(webView.getEngine().getDocument() != null) {
                        HelpWebViewLinkListener webViewLinkListener = new HelpWebViewLinkListener(currentWebView, startPageDirectory);
                        webViewLinkListener.createListener();
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
