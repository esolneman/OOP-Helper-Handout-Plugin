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

    public void setCriteriaScreen(CommonAssessmentCriteriaScreen criteriaScreen){
        this.criteriaScreen = criteriaScreen;
    }
}
