package toolWindow;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.components.JBTabbedPane;
import gui.HandoutPluginFXPanel;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import provider.LocalStorageDataProvider;
import provider.contentHandler.CommonAssessmentCriteriaContentHandler;
import toolWindow.actionGroups.HandoutContentActionGroup;
import webView.WebViewController;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.MalformedURLException;

public class CommonAssessmentCriteriaScreen extends SimpleToolWindowPanel {
    private HandoutPluginFXPanel criteriaContent;
    private HandoutPluginFXPanel assessmentContentVariables;
    private HandoutPluginFXPanel assessmentContentCodingStyle;
    private HandoutPluginFXPanel assessmentContentShortcuts;
    private ToolWindow handoutToolWindow;
    private static File shortcutContent;
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
        initAndShowGUI();
        initToolWindowMenu();
    }

    private void initToolWindowMenu() {
        //http://androhi.hatenablog.com/entry/2015/07/23/233932
        //toolWindowPanel.setToolbar(createToolbarPanel());
        toolWindowPanel.setContent(panel);
    }

    private JComponent createToolbarPanel() {
        final DefaultActionGroup handoutActionGroup = new DefaultActionGroup();
        HandoutContentActionGroup handoutContentActionGroup = (HandoutContentActionGroup) ActionManager.getInstance().getAction("CommonAssessmentCriteria.TableOfContents");
        handoutContentActionGroup.setWebViewController(webViewController);
        handoutContentActionGroup.setHeadings(CommonAssessmentCriteriaContentHandler.getNavHeadings());
        handoutActionGroup.add(handoutContentActionGroup);
        final ActionToolbar actionToolbar = ActionManager.getInstance().createActionToolbar("HandoutTool", handoutActionGroup, true);
        return actionToolbar.getComponent();
    }

    private void createContent() {

/*        Platform.setImplicitExit(false);
        Platform.runLater(() -> {
            webView = webViewController.createWebView(urlString);
            criteriaContent.showHandoutWebView(urlString, webView);
        });*/

/*       tabpane = new JBTabbedPane(JTabbedPane.TOP,JTabbedPane.SCROLL_TAB_LAYOUT );

        assessmentContentCodingStyle = new HandoutPluginFXPanel();
        assessmentContentVariables = new HandoutPluginFXPanel();
        assessmentContentShortcuts = new HandoutPluginFXPanel();

        Platform.setImplicitExit(false);
        Platform.runLater(() -> {
            webView = webViewController.createWebView(urlString);;
            assessmentContentCodingStyle.showHandoutWebView(urlString, webView);
        });

        Platform.setImplicitExit(false);
        Platform.runLater(() -> {
            webView = webViewController.createWebView(urlString);;
            assessmentContentVariables.showHandoutWebView(urlString, webView);
        });

        Platform.setImplicitExit(false);
        Platform.runLater(() -> {
            webView = webViewController.createWebView(urlString);;
            assessmentContentShortcuts.showHandoutWebView(urlString, webView);
        });


        // Hier werden die JPanels als Registerkarten hinzugefügt
        tabpane.add("Variable", assessmentContentCodingStyle);
        tabpane.add("Coding Styles", assessmentContentVariables);
        tabpane.add("Shortcuts", assessmentContentShortcuts);*/
    }



    private void initAndShowGUI() {
        // This method is invoked on the EDT thread
        panel = new JPanel();
        JTabbedPane jtp = new JBTabbedPane();
        jtp.add("Coding Styles", createTab(codingstylesDirectory));
        jtp.add("Variablen", createTab(variablesDirectory));
        jtp.add("Shortcuts", createTab(shortcutDirectory));
        panel.add(jtp, BorderLayout.CENTER);
    }


    private JFXPanel createTab(String s) {
        criteriaContent = new HandoutPluginFXPanel()
        {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(640, 480);
            }
        };
        Platform.runLater(() -> {
            initFX(criteriaContent, s);
        });
        return criteriaContent;
    }

    private void initFX(JFXPanel criteriaContent, String s) {
        // This method is invoked on the JavaFX thread
        StackPane root = new StackPane();
        Scene scene = new Scene(root);
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        webEngine.load(s);
        root.getChildren().add(webView);
        criteriaContent.setScene(scene);
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
