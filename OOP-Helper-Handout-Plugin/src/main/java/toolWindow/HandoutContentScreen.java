package toolWindow;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.wm.ToolWindow;
import controller.LinkToHandoutController;
import environment.HandoutPluginFXPanel;
import javafx.application.Platform;
import javafx.scene.web.WebView;
import org.jetbrains.annotations.NotNull;
import provider.LocalStorageDataProvider;
import provider.RepoLocalStorageDataProvider;
import provider.contentHandler.HandoutContentHandler;
import toolWindow.actionGroups.HandoutContentActionGroup;
import webView.WebViewController;

import javax.swing.*;
import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;

public class HandoutContentScreen extends SimpleToolWindowPanel implements PluginToolWindowTabsInterface {
    private HandoutPluginFXPanel handoutContent;
    private ToolWindow handoutToolWindow;
    private static File content;
    private String urlString;
    private static WebView webView;
    private WebViewController webViewController;

    private SimpleToolWindowPanel toolWindowPanel;

    public HandoutContentScreen(ToolWindow toolWindow){
        super(true, true);
        LinkToHandoutController linkToHandoutController = new LinkToHandoutController(RepoLocalStorageDataProvider.getProject(), this);
        webViewController = new WebViewController();
        toolWindowPanel = new SimpleToolWindowPanel(true);
        handoutToolWindow = toolWindow;
        content = LocalStorageDataProvider.getHandoutFileDirectory();
        System.out.println("content HandoutContentScreen: " + content);

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
        toolWindowPanel.setToolbar(createToolbarPanel());
        toolWindowPanel.setContent(handoutContent);
    }

    private JComponent createToolbarPanel() {
        final DefaultActionGroup handoutActionGroup = new DefaultActionGroup();
        HandoutContentActionGroup handoutContentActionGroup = (HandoutContentActionGroup) ActionManager.getInstance().getAction("Handout.TableOfContents");
        handoutContentActionGroup.setWebViewController(webViewController);
        ArrayList<String> headings = HandoutContentHandler.getNavHeadings();
        handoutContentActionGroup.setHeadings(headings);
        handoutActionGroup.add(handoutContentActionGroup);
        handoutActionGroup.add(new Separator());
        handoutActionGroup.add(ActionManager.getInstance().getAction("Handout.Download"));
        handoutActionGroup.add(new Separator());
        final ActionToolbar checklistActionToolbar = ActionManager.getInstance().createActionToolbar("HandoutTool", handoutActionGroup, true);
        return checklistActionToolbar.getComponent();
    }

    private void createContent() {
        handoutContent = new HandoutPluginFXPanel();
        Platform.setImplicitExit(false);
        Platform.runLater(() -> {
            webView = webViewController.createWebView(urlString);;
            handoutContent.showHandoutWebView(urlString, webView);
        });
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
        webViewController.updateWebViewContent();
    }
}
