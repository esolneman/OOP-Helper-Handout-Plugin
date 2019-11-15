package toolWindow.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;
import toolWindow.CommonAssessmentCriteriaScreen;

public class SwitchWebViewUrl extends AnAction {
    private CommonAssessmentCriteriaScreen criteriaScreen;

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        System.out.println("ACTION: " + anActionEvent.getPresentation().getText());
        String eventText = anActionEvent.getPresentation().getText();
        this.criteriaScreen.updateContent(eventText);
    }

    //TODO TRY TO GET TOOLWINDOW VIA https://intellij-support.jetbrains.com/hc/en-us/community/posts/115000534584-Access-toolwindow-from-an-action
    // ToolWindowManager.getInstance(project).getToolWindow(TOOLWINDOW_ID)
    public void setCriteriaScreen(CommonAssessmentCriteriaScreen criteriaScreen){
        this.criteriaScreen = criteriaScreen;
    }
}
