package services;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public interface HandoutDataProvider {
    static HandoutDataProvider getInstance(@NotNull Project project) {
        return ServiceManager.getService(project, HandoutDataProvider.class);
    }

    public void updateHandoutData();
    //public void cloneRepository();
    //public void updateRepossitory();
    //void getLocalRepository();
}
