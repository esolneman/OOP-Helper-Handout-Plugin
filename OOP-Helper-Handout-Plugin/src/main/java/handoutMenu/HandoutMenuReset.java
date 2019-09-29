package handoutMenu;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

public class HandoutMenuReset extends AnAction {
    public HandoutMenuReset() {
        super("Reset");
    }


    public void actionPerformed(AnActionEvent event) {
        System.out.println("Reset Started");

        Project project = event.getProject();
        Messages.showMessageDialog(project, "Hello Reset!", "Reset", Messages.getInformationIcon());
    }
}
