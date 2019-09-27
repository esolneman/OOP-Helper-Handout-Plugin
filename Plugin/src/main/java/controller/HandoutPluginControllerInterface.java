package controller;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public interface HandoutPluginControllerInterface  {
    static HandoutPluginControllerInterface getInstance(@NotNull Project project) {
        return ServiceManager.getService(project, HandoutPluginControllerInterface.class);
    }

    public void updateHandoutContent ();

}
