package toolWindow;

import com.intellij.openapi.wm.ToolWindow;

import javax.swing.*;
import java.io.File;

public class ChecklistScreen {


    private JPanel checklistContent;
    private JButton button1;
    private JTextPane userChecklist;
    private JButton button2;
    private JTextPane repoChecklist;

    public ChecklistScreen(ToolWindow toolWindow) {
        File file = new File("C:/Masterarbeit/TestProjekt/OOP-18WS-CoreDefense-Starter/HelperHandoutPluginContentData/RepoLocalStorage/checklist.md");
        repoChecklist.setText(file.getName());
    }

    public JPanel getContent() {
        System.out.println("Getting Content for checklist");

        return checklistContent;
    }


}
