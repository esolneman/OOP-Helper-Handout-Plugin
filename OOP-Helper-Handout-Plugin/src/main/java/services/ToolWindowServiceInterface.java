package services;

import com.intellij.openapi.wm.ToolWindow;
import controller.HandoutPluginController;

public interface ToolWindowServiceInterface {
    ToolWindow getToolWindow();
    void setToolWindow(ToolWindow toolWindow);

    void addListener(HandoutPluginController handoutPluginController);
}
