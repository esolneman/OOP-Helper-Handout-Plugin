package services;

import com.intellij.openapi.wm.ToolWindow;

public interface ToolWindowServiceInterface {
    ToolWindow getToolWindow();
    void setToolWindow(ToolWindow toolWindow);
}
