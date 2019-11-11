package toolWindow;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.actionSystem.Separator;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.wm.ToolWindow;
import environment.HandoutPluginFXPanel;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.web.WebView;
import provider.LocalStorageDataProvider;
import toolWindow.actionGroups.HandoutContentActionGroup;
import webView.WebViewController;

import javax.swing.*;
import java.io.File;
import java.net.MalformedURLException;

public class ShortcutScreen extends SimpleToolWindowPanel implements PluginToolWindowTabsInterface{

    private ToolWindow handoutToolWindow;
    private static File content;
    private String urlString;
    private static WebView webView;
    private WebViewController webViewController;

    private SimpleToolWindowPanel toolWindowPanel;

    private HandoutPluginFXPanel shortcutContent ;

    public ShortcutScreen(ToolWindow toolWindow) {
        super(true, true);
        shortcutContent = new HandoutPluginFXPanel();
        shortcutContent.showContent();
        webViewController = new WebViewController();
        toolWindowPanel = new SimpleToolWindowPanel(true);
        handoutToolWindow = toolWindow;
        content = LocalStorageDataProvider.getShortcutFileDirectory();
        System.out.println("content Shortcut: " + content);

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
        //toolWindowPanel.setToolbar(createToolbarPanel());
        toolWindowPanel.setContent(shortcutContent);
    }

    private void createContent() {
        shortcutContent = new HandoutPluginFXPanel();
        Platform.setImplicitExit(false);
        Platform.runLater(() -> {
            webView = webViewController.createWebView(urlString);;
            shortcutContent.showHandoutWebView(urlString, webView);
        });
    }

    public JComponent getToolbar(){
        return toolWindowPanel.getToolbar();
    }

    public JPanel getContent() {
        return toolWindowPanel;
    }

    public void updateContent() {
        webViewController.updateWebViewContent();
    }

}
