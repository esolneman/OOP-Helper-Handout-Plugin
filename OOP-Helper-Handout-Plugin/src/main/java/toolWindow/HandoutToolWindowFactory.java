package toolWindow;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.*;
import com.intellij.ui.content.*;
import org.jetbrains.annotations.NotNull;


public class HandoutToolWindowFactory implements ToolWindowFactory {
    ToolWindow toolWindow;
    ContentFactory contentFactory;

    Content handoutContent;
    Content checklistContent;
    Content shortcutContent;
    Content specificCriteriaContent;
    Content commonAssessmentCriteriaContent;

    HandoutContentScreen handoutContentScreen;
    ChecklistScreen checklistScreen;
    ShortcutScreen shortcutScreen;
    SpecificAssessmentCriteria specificAssessmentCriteria;
    CommonAssessmentCriteriaScreen commonAssessmentCriteriaScreen;

    // Create the tool window content.
    public void createToolWindowContent(Project project, ToolWindow toolWindow) {
        this.toolWindow = toolWindow;
        contentFactory = ContentFactory.SERVICE.getInstance();
        initScreens();
        toolWindow.getContentManager().addContentManagerListener(new ContentManagerListener() {
            @Override
            public void contentAdded(@NotNull ContentManagerEvent event) {
                System.out.println("contentAdded");
            }

            @Override
            public void contentRemoved(@NotNull ContentManagerEvent event) {
                System.out.println("contentRemoved");
            }

            @Override
            public void contentRemoveQuery(@NotNull ContentManagerEvent event) {
                System.out.println("contentRemoveQuery");
            }

            @Override
            public void selectionChanged(@NotNull ContentManagerEvent event) {
                System.out.println("in: selectionChanged");
                System.out.println("Current Component: \"" + event.getContent().getDisplayName() + "\"");
                System.out.println("Component object: " + event.getContent().getComponent());
                if (event.getContent().getDisplayName() == "Shortcut") {
                    System.out.println("Switching to Shortcut-Screen");
                    ((MyJFXPanel) event.getContent().getComponent()).showContent();
                }
            }
        });
    }

    private void initScreens() {
        handoutContentScreen = new HandoutContentScreen(toolWindow);
        checklistScreen = new ChecklistScreen(toolWindow);
        shortcutScreen = new ShortcutScreen(toolWindow);
        specificAssessmentCriteria = new SpecificAssessmentCriteria(toolWindow);
        commonAssessmentCriteriaScreen = new CommonAssessmentCriteriaScreen(toolWindow);
        addScreenContent();
    }

    public void addScreenContent() {
        handoutContent = contentFactory.createContent(handoutContentScreen.getContent(), "HandoutHTML", false);
        handoutContent.setPreferredFocusableComponent(handoutContentScreen.getContent());
        checklistContent = contentFactory.createContent(checklistScreen.getContent(), "Checklist", false);
        shortcutContent = contentFactory.createContent(shortcutScreen.getContent(), "Shortcut", false);
        specificCriteriaContent = contentFactory.createContent(specificAssessmentCriteria.getContent(), "Specific Assessment Criteria", false);
        commonAssessmentCriteriaContent = contentFactory.createContent(commonAssessmentCriteriaScreen.getContent(), "Common Assessment Criteria", false);




        toolWindow.getContentManager().addContent(handoutContent);
        toolWindow.getContentManager().addContent(checklistContent);
        toolWindow.getContentManager().addContent(shortcutContent);
        toolWindow.getContentManager().addContent(specificCriteriaContent);
        toolWindow.getContentManager().addContent(commonAssessmentCriteriaContent);

    }

    public void updateScreenContent() {
        handoutContentScreen.updateContent();
    }

}
