package toolWindow;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.wm.ToolWindow;
import environment.HandoutPluginFXPanel;
import provider.RepoLocalStorageDataProvider;
import toolWindow.actions.HandoutDownloadAction;

import javax.swing.*;
import java.io.File;

public class ChecklistScreen extends SimpleToolWindowPanel {
    private HandoutPluginFXPanel handoutContent;
    private ToolWindow handoutToolWindow;
    private JPanel checklistContent;
    private JButton button1;
    private JTextPane userChecklist;
    private JButton button2;
    private JTextPane repoChecklist;

    private SimpleToolWindowPanel toolWindowPanel;


    public ChecklistScreen(ToolWindow toolWindow) {
        super(true, true);
        toolWindowPanel = new SimpleToolWindowPanel(true);
        File file = new File("C:/Masterarbeit/TestProjekt/OOP-18WS-CoreDefense-Starter/HelperHandoutPluginContentData/RepoLocalStorage/checklist.md");
        repoChecklist.setText(file.getName());
        //http://androhi.hatenablog.com/entry/2015/07/23/233932
        toolWindowPanel.setToolbar(createToolbarPanel());
        toolWindowPanel.setContent(checklistContent);
    }

    private JComponent createToolbarPanel() {
        final DefaultActionGroup checklistActionGroup = new DefaultActionGroup();
        //checklistActionGroup.add(ActionManager.getInstance().getAction("Handout.Download"));
        checklistActionGroup.add(ActionManager.getInstance().getAction("Checklist.EditContent"));
        final ActionToolbar checklistActionToolbar = ActionManager.getInstance().createActionToolbar("Checklisttool", checklistActionGroup, true);
        return checklistActionToolbar.getComponent();
    }

    public JPanel getContent() {
        System.out.println("Getting Content for checklist");
        return toolWindowPanel;
    }


}
