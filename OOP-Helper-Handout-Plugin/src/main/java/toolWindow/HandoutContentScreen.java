package toolWindow;

import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.wm.ToolWindow;
import controller.DownloadPDFHelper;
import controller.HandoutController;
import controller.LinkToHandoutController;
import gui.PluginWebViewFXPanel;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;
import provider.LocalStorageDataProvider;
import provider.RepoLocalStorageDataProvider;
import webView.WebViewController;

import javax.swing.*;
import java.io.File;
import java.net.MalformedURLException;

public class HandoutContentScreen extends SimpleToolWindowPanel implements PluginToolWindowTabsInterface {
    private PluginWebViewFXPanel handoutContent;
    private ToolWindow handoutToolWindow;
    private static File content;
    private String urlString;
    private static WebView webView;
    private WebViewController webViewController;

    private SimpleToolWindowPanel toolWindowPanel;
    public HandoutContentScreen(ToolWindow toolWindow){
        super(true, true);
        webViewController = new WebViewController();
        toolWindowPanel = new SimpleToolWindowPanel(true);
        handoutToolWindow = toolWindow;
        content = LocalStorageDataProvider.getHandoutFileDirectory();
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
        toolWindowPanel.setContent(handoutContent);
    }

    private void createContent() {
        handoutContent = new PluginWebViewFXPanel();
        Platform.setImplicitExit(false);
        Platform.runLater(() -> {
            webView = webViewController.createHandoutWebView(urlString);
            //https://stackoverflow.com/a/10684168
           webView.getEngine().getLoadWorker().stateProperty().addListener((ov, oldState, newState) -> {
                if (newState == Worker.State.SUCCEEDED) {
                    LinkToHandoutController linkToHandoutController = new LinkToHandoutController(RepoLocalStorageDataProvider.getProject(), this);
                    initDownloadButtonListener();
            }});
            //initDownloadButtonListener();
            handoutContent.showHandoutWebView(urlString, webView);

        });
    }

    //https://stackoverflow.com/a/34547416
    //create listener for "download handout" button in webView
    private void initDownloadButtonListener() {
        JSObject window = (JSObject) webView.getEngine().executeScript("window");
        DownloadPDFHelper.getInstance().setContent(this);
        window.setMember("downloadHtml", DownloadPDFHelper.getInstance());
    }

    public JComponent getToolbar(){
        return toolWindowPanel.getToolbar();
    }

    public JPanel getContent() {
        return toolWindowPanel;
    }

    public void goToLocation(String heading) {
        webViewController.goToLocation(heading, handoutToolWindow, this);
    }

    public void updateContent() {
        HandoutController.getInstance().createHandoutFile();
        webViewController.updateWebViewContent();
    }
}
