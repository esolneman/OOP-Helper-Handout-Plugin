package Controller;

import Listener.OnEventListener;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import services.HandoutDataProvider;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HandoutPluginController implements HandoutPluginControllerInterface, OnEventListener {
    HandoutDataProvider handoutDataProvider;
    Project project;

    public HandoutPluginController(Project project) {
        handoutDataProvider = ServiceManager.getService(project, HandoutDataProvider.class);
        handoutDataProvider.addListener(this);
        updateHandoutContent();
        this.project = project;
    }

    public void updateHandoutContent() {
        handoutDataProvider.updateHandoutData();
    }

    @Override
    public void onCloningRepositoryEvent(File repoFile) {
        System.out.println("Performing callback after Asynchronous Task");
        /*repoFile.setExecutable(false);
        repoFile.setReadable(true);
        repoFile.setWritable(false);
        repoFile.setReadOnly();
        Path file = Paths.get(repoFile.getAbsolutePath());
        try {
            Files.setAttribute(file, "dos:hidden", true);
        } catch (IOException e) {
            System.out.print(e);
        }
        System.out.println("RepoFile hidden: "+ repoFile.isHidden());*/
    }
}
