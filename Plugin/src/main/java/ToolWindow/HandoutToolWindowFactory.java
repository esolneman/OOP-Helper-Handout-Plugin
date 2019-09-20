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
            HandoutContentScreen handoutContentScreen = new HandoutContentScreen(toolWindow);
            ChecklistScreen checklistScreen = new ChecklistScreen(toolWindow);
            //HandoutPluginController handoutPluginController = new HandoutPluginController(toolWindow, project);
            ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
            Content handoutContent = contentFactory.createContent(handoutContentScreen.getContent(), "Handout", false);
            Content checklistContent = contentFactory.createContent(checklistScreen.getContent(), "Checklist", false);
            toolWindow.getContentManager().addContent(handoutContent);
            toolWindow.getContentManager().addContent(checklistContent);
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
}
