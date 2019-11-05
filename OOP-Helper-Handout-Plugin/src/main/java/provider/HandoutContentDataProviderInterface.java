package provider;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import eventHandling.OnEventListener;
import org.jetbrains.annotations.NotNull;

public interface HandoutContentDataProviderInterface {
    static HandoutContentDataProviderInterface getInstance(@NotNull Project project) {
        return ServiceManager.getService(project, HandoutContentDataProviderInterface.class);
    }

    void updateHandoutData();

    void addListener(OnEventListener listener);
    //public void cloneRepository();
    //public void updateRepossitory();
    //void getLocalRepository();
}
