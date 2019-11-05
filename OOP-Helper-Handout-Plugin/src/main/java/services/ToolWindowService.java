package services;

import eventHandling.OnToolWindowCreatedListener;
import toolWindow.HandoutToolWindowFactory;

import java.util.ArrayList;
import java.util.List;



//TODO UNUSED DELETE CLASS
public class ToolWindowService implements ToolWindowServiceInterface{
    private List<OnToolWindowCreatedListener> listeners = new ArrayList<>();

    private HandoutToolWindowFactory toolWindowFactory;

    public HandoutToolWindowFactory getToolWindowFactory() {
        return toolWindowFactory;
    }

    public void setToolWindowFactory(HandoutToolWindowFactory toolWindowFactory) {
        this.toolWindowFactory = toolWindowFactory;
        callListener();
    }

    @Override
    public void addListener(OnToolWindowCreatedListener listener) {
        listeners.add(listener);
    }

    private void callListener() {
        System.out.println("toolWindow available");
        if (listeners != null) {
            System.out.println("listener not null");
            for (OnToolWindowCreatedListener listener : listeners) {
                listener.OnToolWindowCreatedEvent(toolWindowFactory);
            }
        }
    }
}
