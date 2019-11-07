package toolWindow;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.wm.ToolWindow;
import environment.HandoutPluginFXPanel;
import javafx.application.Platform;
import javafx.scene.control.TableView;
import javafx.scene.web.WebView;
import objects.SpecificAssessmentCriteria;
import provider.LocalStorageDataProvider;
import webView.WebViewController;

import javax.swing.*;
import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;

public class AssessmentScreen extends SimpleToolWindowPanel{
    //private final SpecificAssessmentCriteria data;
    private HandoutPluginFXPanel assessmentContent;
    private ToolWindow handoutToolWindow;
    private static File content;
    private String urlString;
    private TableView table = new TableView();
    private SimpleToolWindowPanel toolWindowPanel;
    private static WebView webView;
    private WebViewController webViewController;

    public AssessmentScreen (ToolWindow toolWindow){
        super(true, true);
        toolWindowPanel = new SimpleToolWindowPanel(true);
        webViewController = new WebViewController();
        handoutToolWindow = toolWindow;
        //data = LocalStorageDataProvider.getSpecificAssessmentCriteria();
        //content = LocalStorageDataProvider.getSpecificAssessmentCriteria();
        content = LocalStorageDataProvider.getSpecificAssessmentCriteriaFileDirectoryTest();
        try {
            //
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
        toolWindowPanel.setContent(assessmentContent);
    }

    private JComponent createToolbarPanel() {
        final DefaultActionGroup handoutActionGroup = new DefaultActionGroup();
        handoutActionGroup.add(ActionManager.getInstance().getAction("Handout.Minimize"));
        handoutActionGroup.add(ActionManager.getInstance().getAction("Handout.Maximize"));
        final ActionToolbar checklistActionToolbar = ActionManager.getInstance().createActionToolbar("SpecificAssessmentTool", handoutActionGroup, true);
        return checklistActionToolbar.getComponent();
    }

    private void createContent() {
        assessmentContent = new HandoutPluginFXPanel();
        /*table.setEditable(false);
        ArrayList<String> headings = new ArrayList<>();
        for (String s : data.getHeadline()) {
            headings.add(s);
        }

        //JScrollPane tableContainer = new JBScrollPane(table);
        assessmentContent.showTable();*/

        Platform.setImplicitExit(false);
        Platform.runLater(() -> {
            webView = webViewController.createWebView(urlString);;
            assessmentContent.showHandoutWebView(urlString, webView);
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


