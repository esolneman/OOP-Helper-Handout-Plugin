package toolWindow.checklistScreen;

import com.intellij.openapi.wm.ToolWindow;

import javax.swing.*;

public class ChecklistScreen {
    //private JButton hideToolWindowButton;
    private JPanel checklistContent;

    public ChecklistScreen(ToolWindow toolWindow) {
        //hideToolWindowButton.addActionListener(e -> toolWindow.hide(null));
    }
    public JPanel getContent() {
        return checklistContent;
    }
}
