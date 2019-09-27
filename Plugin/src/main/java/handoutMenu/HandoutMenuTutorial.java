package handoutMenu;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

public class HandoutMenuTutorial extends AnAction {
    public HandoutMenuTutorial() {
        super("Tutorial");
    }


    public void actionPerformed(AnActionEvent event) {
        System.out.println("greetingEvent");

        Project project = event.getProject();
        Messages.showMessageDialog(project, "Hello world!", "Greeting", Messages.getInformationIcon());
    }
}