package ToolWindow;

import Controller.HandoutPluginController;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.*;
import com.intellij.ui.content.*;
import org.jetbrains.annotations.NotNull;
import services.HandoutDataProvider;


public class HandoutToolWindowFactory implements ToolWindowFactory {
        // Create the tool window content.
        public void createToolWindowContent(Project project, ToolWindow toolWindow) {
            initScreens(toolWindow);
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

                }
            });

        }

    private void initScreens(ToolWindow toolWindow) {
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();

        HandoutContentScreen handoutContentScreen = new HandoutContentScreen(toolWindow);
        ChecklistScreen checklistScreen = new ChecklistScreen(toolWindow);
        ShortcutScreen shortcutScreen = new ShortcutScreen(toolWindow);
        SpecificAssessmentCriteria specificAssessmentCriteria = new SpecificAssessmentCriteria(toolWindow);
        CommonAssessmentCriteria commonAssessmentCriteria = new CommonAssessmentCriteria(toolWindow);


        Content handoutContent = contentFactory.createContent(handoutContentScreen.getContent(), "Handout", false);
        Content checklistContent = contentFactory.createContent(checklistScreen.getContent(), "Checklist", false);
        Content shortcutContent = contentFactory.createContent(shortcutScreen.getContent(), "Shortcut", false);
        Content specificCriteriaContent = contentFactory.createContent(specificAssessmentCriteria.getContent(), "Specific Assessment Criteria", false);
        Content commonAssessmentCriteriaContent = contentFactory.createContent(commonAssessmentCriteria.getContent(), "Common Assessment Criteria", false);


        toolWindow.getContentManager().addContent(handoutContent);
        toolWindow.getContentManager().addContent(checklistContent);
        toolWindow.getContentManager().addContent(shortcutContent);
        toolWindow.getContentManager().addContent(specificCriteriaContent);
        toolWindow.getContentManager().addContent(commonAssessmentCriteriaContent);


    }
}
