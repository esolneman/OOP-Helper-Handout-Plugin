package services;

import com.intellij.openapi.wm.ToolWindow;
import controller.HandoutPluginController;
import eventHandling.OnToolWindowCreatedListener;
import toolWindow.HandoutToolWindowFactory;

public interface ToolWindowServiceInterface {
    //HandoutToolWindowFactory getToolWindowFactory();
    //void setToolWindowFactory(HandoutToolWindowFactory toolWindowFactory);
    void addListener(OnToolWindowCreatedListener listener);
}
