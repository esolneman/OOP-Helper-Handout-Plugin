package controller;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import eventHandling.OnEventListener;
import eventHandling.OnToolWindowCreatedListener;
import provider.HandoutContentDataProviderInterface;
import provider.RepoLocalStorageDataProvider;
import services.ToolWindowServiceInterface;
import toolWindow.HandoutToolWindowFactory;

import java.io.File;

public class HandoutPluginController implements HandoutPluginControllerInterface, OnEventListener, OnToolWindowCreatedListener {
    HandoutContentDataProviderInterface handoutDataProvider;
    ToolWindowServiceInterface toolWindowService;

    public HandoutPluginController(Project project) {
        RepoLocalStorageDataProvider.setUserProjectDirectory(project);
        handoutDataProvider = ServiceManager.getService(project, HandoutContentDataProviderInterface.class);
        handoutDataProvider.addListener(this);
        toolWindowService = ServiceManager.getService(project, ToolWindowServiceInterface.class);
        toolWindowService.addListener(this);
        updateHandoutContent();
    }

    public void updateHandoutContent() {
        handoutDataProvider.updateHandoutData();
    }

    //TODO MANAGE UPDATE VIEWS....
    //TODO update Webview and other content of toolWindows
    //TODO enable / disable Actions
    public void onCloningRepositoryEvent(File repoFile) {
        System.out.println("Performing callback after Asynchronous Task");
        System.out.println("repoFile: " + repoFile);
        System.out.println(repoFile.getAbsolutePath());
        repoFile.setExecutable(false);
        repoFile.setReadable(true);
        repoFile.setWritable(false);
        repoFile.setReadOnly();

        //update toolWindow
        //
        /*Path file = Paths.get(repoFile.getAbsolutePath());
        try {
            Files.setAttribute(file, "dos:hidden", true);
        } catch (IOException e) {
            System.out.print(e);
        }
        System.out.println("RepoFile hidden: "+ repoFile.isHidden());*/
    }

    @Override
    public void OnToolWindowCreatedEvent(ToolWindow toolWindow) {

    }
}
