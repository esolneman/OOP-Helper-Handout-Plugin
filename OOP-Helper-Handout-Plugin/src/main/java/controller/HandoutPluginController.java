package controller;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import eventHandling.OnGitEventListener;
import eventHandling.OnUpdatingRepositoryEvent;
import provider.HandoutContentDataProviderInterface;
import provider.RepoLocalStorageDataProvider;
import toolWindow.HandoutToolWindowFactory;

import java.io.File;
import java.util.ArrayList;

public class HandoutPluginController implements HandoutPluginControllerInterface, OnGitEventListener {
    HandoutContentDataProviderInterface handoutDataProvider;
    HandoutToolWindowFactory handoutToolWindowFactory;
    ToolWindowController toolWindowController;

    public HandoutPluginController(Project project) {
        RepoLocalStorageDataProvider.setUserProjectDirectory(project);
        handoutDataProvider = ServiceManager.getService(project, HandoutContentDataProviderInterface.class);
        handoutDataProvider.addListener(this);
        toolWindowController = ToolWindowController.getInstance();
        updateHandoutContent();
    }

    public void updateHandoutContent() {
        handoutDataProvider.updateHandoutData();
    }

    //TODO MANAGE UPDATE VIEWS....
    //TODO update Webview and other content of toolWindows
    //TODO enable / disable Actions
    public void onCloningRepositoryEvent() {
        System.out.println("Performing callback after Asynchronous Task");
        File repoFile = new File(RepoLocalStorageDataProvider.getRepoLocalFile());
        System.out.println(repoFile.getAbsolutePath());
        repoFile.setExecutable(false);
        repoFile.setReadable(true);
        repoFile.setWritable(false);
        repoFile.setReadOnly();
        toolWindowController.updateContent();
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
    public void onUpdatingRepositoryEvent(ArrayList<String> commitMessages) {

    }

}
