package handoutMenu;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

public class HandoutMenuExport extends AnAction {
    public HandoutMenuExport() {
        super("Export");
    }


    public void actionPerformed(AnActionEvent event) {
        System.out.println("Export Started");

        Project project = event.getProject();
        Messages.showMessageDialog(project, "Hello Export!", "Export", Messages.getInformationIcon());
    }
}