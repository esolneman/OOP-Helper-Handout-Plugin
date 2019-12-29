package handoutMenuActions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.openapi.wm.ToolWindowType;
import toolWindow.HelpScreen;

public class HandoutMenuTutorial extends AnAction {
    public HandoutMenuTutorial() {
        super("Tutorial");
    }
    private static int TUTORIAL_ID = 3;

    public void actionPerformed(AnActionEvent event) {
        System.out.println("greetingEvent");

        Project project = event.getProject();
        ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow("Aufgabe (OOP)");
        if(toolWindow.isVisible()) {
            //open HandoutToolWindow and select the correct tab
            toolWindow.getContentManager().setSelectedContent(toolWindow.getContentManager().getContent(TUTORIAL_ID));
        }else{
            toolWindow.activate(() -> {
                toolWindow.show(null);
                //https://intellij-support.jetbrains.com/hc/en-us/community/posts/206383859-How-to-float-tool-window-programatically-
                toolWindow.setType(ToolWindowType.DOCKED, null);
            });
        }
        HelpScreen.displayTutorial();
    }
}