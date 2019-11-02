package toolWindow;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;

public class HandoutToolWindowFactory implements ToolWindowFactory {
    ToolWindow toolWindow;
    ContentFactory contentFactory;

    Content handoutContent;
    Content checklistContent;
    Content shortcutContent;
    Content specificCriteriaContent;
    Content commonAssessmentCriteriaContent;
    Content commonAssessmentCriteriaContentJavaFXTable;

    HandoutContentScreen handoutContentScreen;
    ChecklistScreen checklistScreen;
    ShortcutScreen shortcutScreen;
    SpecificAssessmentCriteriaScreen specificAssessmentCriteria;
    CommonAssessmentCriteriaScreen commonAssessmentCriteriaScreen;
    AssessmentScreen assessmentScreen;

    // Create the tool window content.
    public void createToolWindowContent(Project project, ToolWindow toolWindow) {
        this.toolWindow = toolWindow;
        contentFactory = ContentFactory.SERVICE.getInstance();
        initScreens();
    }

    private void initScreens() {
        handoutContentScreen = new HandoutContentScreen(toolWindow);
        checklistScreen = new ChecklistScreen(toolWindow);
        shortcutScreen = new ShortcutScreen(toolWindow);
        specificAssessmentCriteria = new SpecificAssessmentCriteriaScreen(toolWindow);
        commonAssessmentCriteriaScreen = new CommonAssessmentCriteriaScreen(toolWindow);
        assessmentScreen = new AssessmentScreen(toolWindow);
        addScreenContent();
    }

    public void addScreenContent() {
        handoutContent = contentFactory.createContent(handoutContentScreen.getContent(), "HandoutHTML", false);
        handoutContent.setPreferredFocusableComponent(handoutContentScreen.getContent());
        checklistContent = contentFactory.createContent(checklistScreen.getContent(), "Checklist", false);
        shortcutContent = contentFactory.createContent(shortcutScreen.getContent(), "Shortcut", false);
        specificCriteriaContent = contentFactory.createContent(specificAssessmentCriteria.getContent(), "Specific Assessment Criteria", false);
        commonAssessmentCriteriaContent = contentFactory.createContent(commonAssessmentCriteriaScreen.getContent(), "Common Assessment Criteria", false);
        commonAssessmentCriteriaContentJavaFXTable = contentFactory.createContent(assessmentScreen.getContent(), "TABLE", false);

        toolWindow.getContentManager().addContent(handoutContent);
        toolWindow.getContentManager().addContent(checklistContent);
        toolWindow.getContentManager().addContent(shortcutContent);
        toolWindow.getContentManager().addContent(specificCriteriaContent);
        toolWindow.getContentManager().addContent(commonAssessmentCriteriaContent);
        toolWindow.getContentManager().addContent(commonAssessmentCriteriaContentJavaFXTable);
        //TODO: Decide which Tab is open when start ide
        toolWindow.getContentManager().setSelectedContent(handoutContent);

    }
}
