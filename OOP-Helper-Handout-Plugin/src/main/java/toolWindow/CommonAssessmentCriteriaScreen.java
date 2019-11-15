package toolWindow;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.wm.ToolWindow;
import gui.HandoutPluginFXPanel;
import javafx.application.Platform;
import javafx.scene.web.WebView;
import provider.LocalStorageDataProvider;
import toolWindow.actions.SwitchWebViewUrl;
import webView.WebViewController;

import javax.swing.*;
import java.io.File;
import java.net.MalformedURLException;

public class CommonAssessmentCriteriaScreen extends SimpleToolWindowPanel {
    private HandoutPluginFXPanel criteriaContent;
    private ToolWindow handoutToolWindow;
    private String variablesDirectory;
    private String codingstylesDirectory;
    private String shortcutDirectory;
    private SimpleToolWindowPanel toolWindowPanel;
    private static WebView webView;
    private WebViewController webViewController;
    private JTabbedPane tabpane;
    private JPanel panel;


    public CommonAssessmentCriteriaScreen(ToolWindow toolWindow) {
        super(true, true);
        toolWindowPanel = new SimpleToolWindowPanel(true);
        panel = new JPanel();


        webViewController = new WebViewController();
        handoutToolWindow = toolWindow;
        try {
            shortcutDirectory = LocalStorageDataProvider.getShortcutFileDirectory().toURI().toURL().toString();
            System.out.println("SHORTCUT: " + shortcutDirectory);
            codingstylesDirectory = LocalStorageDataProvider.getCodingStylesFileDirectory().toURI().toURL().toString();
            System.out.println("CODING STYLES: " + codingstylesDirectory);
            variablesDirectory = LocalStorageDataProvider.getVariablesDirectory().toURI().toURL().toString();
            System.out.println("Variable: " + variablesDirectory);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        createContent();
        initToolWindowMenu();
    }

    private void initToolWindowMenu() {
        //http://androhi.hatenablog.com/entry/2015/07/23/233932
        toolWindowPanel.setToolbar(createToolbarPanel());
        toolWindowPanel.setContent(criteriaContent);
    }

    private JComponent createToolbarPanel() {
        final DefaultActionGroup criteriaActionGroup = new DefaultActionGroup();

        SwitchWebViewUrl switchWebViewUrl = (SwitchWebViewUrl) ActionManager.getInstance().getAction("CommonInformation.CodingStyles");
        switchWebViewUrl.setCriteriaScreen(this);
        criteriaActionGroup.add(switchWebViewUrl);

        switchWebViewUrl = (SwitchWebViewUrl) ActionManager.getInstance().getAction("CommonInformation.Variables");
        switchWebViewUrl.setCriteriaScreen(this);
        criteriaActionGroup.add(switchWebViewUrl);

         switchWebViewUrl = (SwitchWebViewUrl) ActionManager.getInstance().getAction("CommonInformation.Shortcut");
        switchWebViewUrl.setCriteriaScreen(this);
        criteriaActionGroup.add(switchWebViewUrl);

        final ActionToolbar actionToolbar = ActionManager.getInstance().createActionToolbar("HandoutTool", criteriaActionGroup, true);
        return actionToolbar.getComponent();
    }

    private void createContent() {
        criteriaContent = new HandoutPluginFXPanel();
        Platform.setImplicitExit(false);
        Platform.runLater(() -> {
            webView = webViewController.createWebViewWithListener(codingstylesDirectory);
            criteriaContent.showHandoutWebView(codingstylesDirectory, webView);
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

    //TODO CHANGE NAME
    public void updateContent(String eventText) {
        String url;
        if (eventText.equals("Shortcut")){
            url = shortcutDirectory;
        } else if (eventText.equals("CodingStyles")){
            url = codingstylesDirectory;
        } else{
            url = variablesDirectory;
        }
        webViewController.changeURL(url);
    }

}
