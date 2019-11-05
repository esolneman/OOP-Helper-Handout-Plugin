package eventHandling;

import com.intellij.openapi.wm.ToolWindow;
import toolWindow.HandoutToolWindowFactory;

public interface OnToolWindowCreatedListener {
    void OnToolWindowCreatedEvent(HandoutToolWindowFactory toolWindowFactory);
}
