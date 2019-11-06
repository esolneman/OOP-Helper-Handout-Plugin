package toolWindow;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.components.JBScrollPane;
import environment.HandoutPluginFXPanel;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import objects.SpecificAssessmentCriteria;
import provider.LocalStorageDataProvider;

import javax.swing.*;
import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;

public class AssessmentScreen extends SimpleToolWindowPanel{
    private final SpecificAssessmentCriteria data;
    private HandoutPluginFXPanel handoutContent;
    private ToolWindow handoutToolWindow;
    private static File content;
    private String urlString;
    private TableView table = new TableView();


    private SimpleToolWindowPanel toolWindowPanel;

    public AssessmentScreen (ToolWindow toolWindow){
        super(true, true);
        toolWindowPanel = new SimpleToolWindowPanel(true);
        handoutToolWindow = toolWindow;
        data = LocalStorageDataProvider.getSpecificAssessmentCriteria();
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
        toolWindowPanel.setToolbar(createToolbarPanel());
        toolWindowPanel.setContent(handoutContent);
    }

    private JComponent createToolbarPanel() {
        final DefaultActionGroup handoutActionGroup = new DefaultActionGroup();
        handoutActionGroup.add(ActionManager.getInstance().getAction("Handout.Minimize"));
        handoutActionGroup.add(ActionManager.getInstance().getAction("Handout.Maximize"));
        final ActionToolbar checklistActionToolbar = ActionManager.getInstance().createActionToolbar("SpecificAssessmentTool", handoutActionGroup, true);
        return checklistActionToolbar.getComponent();
    }

    private void createContent() {
        handoutContent = new HandoutPluginFXPanel();
        table.setEditable(true);
        ArrayList<String> headings = new ArrayList<>();
        for (String s : data.getHeadline()) {
            headings.add(s);
        }
        TableColumn vorNameCol = new TableColumn("Vorname");
        TableColumn nachNameCol = new TableColumn("Nachname");
        TableColumn emailCol = new TableColumn("Email");
        TableColumn alterCol = new TableColumn("Alter");
        //table.getColumns().addAll(vorNameCol, nachNameCol, emailCol, alterCol);
        //JScrollPane tableContainer = new JBScrollPane(table);
        handoutContent.showTable();
    }

    public JComponent getToolbar(){
        return toolWindowPanel.getToolbar();
    }

    public JPanel getContent() {
        return toolWindowPanel;
    }

}


