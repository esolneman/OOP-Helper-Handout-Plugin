package myToolWindow;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.*;
import com.intellij.ui.content.*;


public class ToolWindowTestFactory implements ToolWindowFactory {
        // Create the tool window content.
        public void createToolWindowContent(Project project, ToolWindow toolWindow) {
            myToolWindow.MyToolWindowTest myToolWindow = new myToolWindow.MyToolWindowTest(toolWindow);
            ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
            Content content = contentFactory.createContent(myToolWindow.getContent(), "", false);
            toolWindow.getContentManager().addContent(content);
        }
}
