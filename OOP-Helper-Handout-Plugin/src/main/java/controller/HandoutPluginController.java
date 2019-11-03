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
        checkInternetConnection();
        updateHandoutContent();
        handoutDataProvider.addListener(this);
    }


    public Boolean checkInternetConnection(){
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("ping www.geeksforgeeks.org");
            int x = process.waitFor();
            if (x == 0) {
                System.out.println("Connection Successful, " + "Output was " + x);
                return true;
            }
            else {
                System.out.println("Internet Not Connected, "  + "Output was " + x);
                return false;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
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
