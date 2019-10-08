package controller;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindowFactory;
import listener.OnEventListener;
import provider.HandoutContentDataProviderInterface;
import provider.RepoLocalStorageDataProvider;

import java.io.File;

public class HandoutPluginController implements HandoutPluginControllerInterface, OnEventListener{
    HandoutContentDataProviderInterface handoutDataProvider;
    Project project;
    ToolWindowFactory toolWindowFactory;

    public HandoutPluginController(Project project) {
        //toolWindowFactory =
        RepoLocalStorageDataProvider.setUserProjectDirectory(project);
        handoutDataProvider = ServiceManager.getService(project, HandoutContentDataProviderInterface.class);
        handoutDataProvider.addListener(this);
        updateHandoutContent();
        this.project = project;
    }


    public void updateHandoutContent() {
        handoutDataProvider.updateHandoutData();
    }

    public void onCloningRepositoryEvent(File repoFile) {
        System.out.println("Performing callback after Asynchronous Task");
        System.out.println(repoFile.toString());

        repoFile.setExecutable(false);
        repoFile.setReadable(true);
        repoFile.setWritable(false);
        repoFile.setReadOnly();

        //toolWindowFactory.
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
