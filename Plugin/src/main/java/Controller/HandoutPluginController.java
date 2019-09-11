package Controller;

import Listener.OnEventListener;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import services.HandoutDataProvider;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HandoutPluginController implements OnEventListener {
    HandoutDataProvider handoutDataProvider;
    Project project;

    public HandoutPluginController(ToolWindow toolWindow, Project project) {
        handoutDataProvider = ServiceManager.getService(project, HandoutDataProvider.class);
        updateHandoutContent();
        this.project = project;
    }

    public void updateHandoutContent() {
        handoutDataProvider.updateHandoutData();

    }

    @Override
    public void onCloningRepositoryEvent(String repoPath) {
        System.out.println("Performing callback after Asynchronous Task");
        //File handoutFile = new File((contentRepoPath + "handout.md"));
            //handoutFile.setReadOnly();
            //File handoutFile = new File((contentRepoPath + "handout"));
            Path file = Paths.get(repoPath);
            File handoutFile = file.toFile();
            System.out.print(handoutFile);
        /*Path file2 = Paths.get("handout.md");
        File handoutFile2 = file2.toFile();
        System.out.print(handoutFile2);*/
            //Path file3 = Paths.get("/RepoTEST/" + "handout.md");
            //File handoutFile3 = file3.toFile();
            handoutFile.setExecutable(false);
            handoutFile.setReadable(true);
            handoutFile.setWritable(false);

/*      File handoutFile = new File((contentRepoPath + "handout"));
        Path file = Paths.get(contentRepoPath + "handout.txt");

        try {
            Files.setAttribute(file, "dos:hidden", true);
        } catch (IOException e) {
            System.out.print(e);
        }*/
    }
}
