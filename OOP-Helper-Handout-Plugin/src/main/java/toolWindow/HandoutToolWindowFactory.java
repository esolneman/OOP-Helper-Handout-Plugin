package toolWindow;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.*;
import com.intellij.ui.content.*;
import org.jetbrains.annotations.NotNull;


public class HandoutToolWindowFactory implements ToolWindowFactory {
    ToolWindow toolWindow;
    Content handoutContent;
    Content handoutContent2;

    Content checklistContent;
    Content shortcutContent;
    Content specificCriteriaContent;
    Content commonAssessmentCriteriaContent;
    HandoutContentScreen handoutContentScreen;
    Handout2 handoutContentScreen2;

    ChecklistScreen checklistScreen;
    ShortcutScreen shortcutScreen;
    SpecificAssessmentCriteria specificAssessmentCriteria;
    CommonAssessmentCriteriaScreen commonAssessmentCriteriaScreen;
    ContentFactory contentFactory;

    // Create the tool window content.
    public void createToolWindowContent(Project project, ToolWindow toolWindow) {
        this.toolWindow = toolWindow;
        contentFactory = ContentFactory.SERVICE.getInstance();
        initScreens();
        toolWindow.getContentManager().addContentManagerListener(new ContentManagerListener() {
            @Override
            public void contentAdded(@NotNull ContentManagerEvent event) {
            }
            @Override
            public void contentRemoved(@NotNull ContentManagerEvent event) {
            }
            @Override
            public void contentRemoveQuery(@NotNull ContentManagerEvent event) {
            }
            @Override
            public void selectionChanged(@NotNull ContentManagerEvent event) {
                System.out.println("Selection changed to " + event.getContent().getTabName());
                toolWindow.getContentManager().addContent(event.getContent());
            }
        });
    }

    private void initScreens() {
        handoutContentScreen = new HandoutContentScreen(toolWindow);
        handoutContentScreen2 = new Handout2(toolWindow);

        checklistScreen = new ChecklistScreen(toolWindow);
        shortcutScreen = new ShortcutScreen(toolWindow);
        specificAssessmentCriteria = new SpecificAssessmentCriteria(toolWindow);
        commonAssessmentCriteriaScreen = new CommonAssessmentCriteriaScreen(toolWindow);
        updateScreenContent();
    }

    public void updateScreenContent() {
        handoutContent = contentFactory.createContent(handoutContentScreen.getContent(), "HandoutHTML", false);
        handoutContent2 = contentFactory.createContent(handoutContentScreen2.getContent(), "HandoutMD", false);

        checklistContent = contentFactory.createContent(checklistScreen.getContent(), "Checklist", false);
        shortcutContent = contentFactory.createContent(shortcutScreen.getContent(), "Shortcut", false);
        specificCriteriaContent = contentFactory.createContent(specificAssessmentCriteria.getContent(), "Specific Assessment Criteria", false);
        commonAssessmentCriteriaContent = contentFactory.createContent(commonAssessmentCriteriaScreen.getContent(), "Common Assessment Criteria", false);

        toolWindow.getContentManager().addContent(handoutContent);
        toolWindow.getContentManager().addContent(handoutContent2);

        toolWindow.getContentManager().addContent(checklistContent);
        toolWindow.getContentManager().addContent(shortcutContent);
        toolWindow.getContentManager().addContent(specificCriteriaContent);
        toolWindow.getContentManager().addContent(commonAssessmentCriteriaContent);
    }
}
