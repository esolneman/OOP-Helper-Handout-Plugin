package eventHandling;

import com.intellij.openapi.wm.ToolWindow;

public interface OnToolWindowCreatedListener {
    void OnToolWindowCreatedEvent(ToolWindow toolWindow);
}
