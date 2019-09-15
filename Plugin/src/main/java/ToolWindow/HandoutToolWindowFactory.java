package ToolWindow;

import Controller.HandoutPluginController;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.*;
import com.intellij.ui.content.*;
import services.HandoutDataProvider;


public class HandoutToolWindowFactory implements ToolWindowFactory {
        // Create the tool window content.
        public void createToolWindowContent(Project project, ToolWindow toolWindow) {
            //HandoutDataProvider handoutDataProvider = ServiceManager.getService(project, HandoutDataProvider.class);
            //handoutDataProvider.updateHandoutData();
            System.out.println("createToolWindowContent");
            HandoutToolWindow handoutToolWindow = new HandoutToolWindow(toolWindow);
            HandoutPluginController handoutPluginController = new HandoutPluginController(toolWindow, project);
            ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
            Content content = contentFactory.createContent(handoutToolWindow.getContent(), "", false);
            toolWindow.getContentManager().addContent(content);
        }
}
