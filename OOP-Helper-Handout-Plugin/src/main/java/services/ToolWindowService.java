package services;

import com.intellij.openapi.wm.ToolWindow;

public class ToolWindowService implements ToolWindowServiceInterface{

    private ToolWindow toolWindow;

    @Override
    public ToolWindow getToolWindow() {
        return toolWindow;
    }

    @Override
    public void setToolWindow(ToolWindow toolWindow) {
        this.toolWindow = toolWindow;
    }


}
