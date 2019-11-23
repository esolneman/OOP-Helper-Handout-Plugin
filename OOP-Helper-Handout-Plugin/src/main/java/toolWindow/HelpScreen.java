package toolWindow;

import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.wm.ToolWindow;
import gui.HandoutPluginFXPanel;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.scene.web.WebView;
import provider.LocalStorageDataProvider;
import webView.WebViewController;

import javax.swing.*;
import java.net.MalformedURLException;

public class HelpScreen extends SimpleToolWindowPanel {
    private HandoutPluginFXPanel criteriaContent;
    private ToolWindow handoutToolWindow;
    private String variablesDirectory;
    private String codingstylesDirectory;
    private String shortcutDirectory;
    private String startPageDirectory;
    private SimpleToolWindowPanel toolWindowPanel;
    private static WebView webView;
    private WebViewController webViewController;
    private JPanel panel;


    public HelpScreen(ToolWindow toolWindow) {
        super(true, true);
        toolWindowPanel = new SimpleToolWindowPanel(true);
        panel = new JPanel();
        webViewController = new WebViewController();
        handoutToolWindow = toolWindow;
        try {
            startPageDirectory = LocalStorageDataProvider.getHelpStartDirectory().toURI().toURL().toString();
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
        criteriaContent = new HandoutPluginFXPanel();
        Platform.setImplicitExit(false);
        Platform.runLater(() -> {
            webView = webViewController.createHelpWebView(startPageDirectory);
/*            webView.getEngine().getLoadWorker().stateProperty().addListener(
                    (ov, oldState, newState) -> {
                        if (newState == Worker.State.SUCCEEDED) {
                            webView.getEngine().executeScript("getNavBar()");
                        }
                    }
            );*/

            //webView.getEngine().load(strpath);

            criteriaContent.showHandoutWebView(startPageDirectory, webView);

        });
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
