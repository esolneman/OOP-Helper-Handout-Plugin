package controller;

import com.intellij.openapi.components.ServiceManager;
import eventHandling.OnToolWindowCreatedListener;
import provider.RepoLocalStorageDataProvider;
import services.ToolWindowServiceInterface;
import toolWindow.HandoutToolWindowFactory;

//is singleton
public class ToolWindowController implements OnToolWindowCreatedListener{
    private static ToolWindowController single_instance = null;
    private HandoutToolWindowFactory handoutToolWindowFactory;
    private ToolWindowServiceInterface toolWindowService;


    public static ToolWindowController getInstance() {
        if (single_instance == null) {
            single_instance = new ToolWindowController();
        }
        return single_instance;
    }

    private ToolWindowController(){
        //toolWindowService = ServiceManager.getService(RepoLocalStorageDataProvider.getProject(), ToolWindowServiceInterface.class);
        //toolWindowService.addListener(this);
    }

    public void updateContent() {
        if(handoutToolWindowFactory != null){
            handoutToolWindowFactory.updateContent();
        }
    }

    public void OnToolWindowCreatedEvent(HandoutToolWindowFactory toolWindowFactory) {
        handoutToolWindowFactory = toolWindowFactory;
    }
}
