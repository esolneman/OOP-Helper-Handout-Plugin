package Actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

public class HelperHandoutActionReset extends AnAction {
    public HelperHandoutActionReset() {
        super("Reset");
    }


    public void actionPerformed(AnActionEvent event) {
        System.out.println("Reset Started");

        Project project = event.getProject();
        Messages.showMessageDialog(project, "Hello Reset!", "Reset", Messages.getInformationIcon());
    }
}
