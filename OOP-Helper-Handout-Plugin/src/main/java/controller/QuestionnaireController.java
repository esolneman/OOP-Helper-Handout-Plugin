package controller;

import com.intellij.openapi.components.ServiceManager;
import provider.LocalStorageDataProvider;
import provider.RepoLocalStorageDataProvider;
import services.ToolWindowServiceInterface;
import toolWindow.HandoutToolWindowFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

public class QuestionnaireController {

    private static QuestionnaireController single_instance = null;
    private File projectCreationDateFile;

    public static QuestionnaireController getInstance() {
        if (single_instance == null) {
            single_instance = new QuestionnaireController();
        }
        return single_instance;
    }

    private QuestionnaireController(){
        projectCreationDateFile = LocalStorageDataProvider.getProjectCreationDateDirectory();

    }

    public void saveProjectCreationDate() {
        projectCreationDateFile.getParentFile().mkdirs();
        try {
            projectCreationDateFile.createNewFile();
        } catch (IOException e) {
            //TODO CATACH
            System.out.println(e);
        }
        System.out.println("saveProjectCreationDate run");

        String creationDate = new Date().toString();
        System.out.println("creationDate: " + creationDate);

        //https://stackoverflow.com/a/1053475

        try (PrintWriter out = new PrintWriter(projectCreationDateFile)) {
            out.println(creationDate);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
