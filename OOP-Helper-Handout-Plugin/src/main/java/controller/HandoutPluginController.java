package controller;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import listener.OnEventListener;
import provider.HandoutContentDataProviderInterface;
import provider.RepoLocalStorageDataProvider;

import java.io.File;
import java.io.IOException;

public class HandoutPluginController implements HandoutPluginControllerInterface, OnEventListener{
    HandoutContentDataProviderInterface handoutDataProvider;
    LinkToHandoutController linkToHandoutController;

    public HandoutPluginController(Project project) {
        RepoLocalStorageDataProvider.setUserProjectDirectory(project);
        handoutDataProvider = ServiceManager.getService(project, HandoutContentDataProviderInterface.class);
        updateHandoutContent();
        handoutDataProvider.addListener(this);
    }

    public void updateHandoutContent() {
        handoutDataProvider.updateHandoutData();
    }

    public void onCloningRepositoryEvent(File repoFile) {
        System.out.println("Performing callback after Asynchronous Task");
        System.out.println("repoFile: " + repoFile);

        System.out.println(repoFile.getAbsolutePath());

        repoFile.setExecutable(false);
        repoFile.setReadable(true);
        repoFile.setWritable(false);
        repoFile.setReadOnly();

        /*Path file = Paths.get(repoFile.getAbsolutePath());
        try {
            Files.setAttribute(file, "dos:hidden", true);
        } catch (IOException e) {
            System.out.print(e);
        }
        System.out.println("RepoFile hidden: "+ repoFile.isHidden());*/
    }

    private void updateContent(){
    }
}
