package services;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public interface ContentService {
    static ContentService getInstance(@NotNull Project project) {
        return ServiceManager.getService(project, ContentService.class);
    }

    public void cloneBranch();
}
