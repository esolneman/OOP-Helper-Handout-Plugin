package provider;

import com.intellij.openapi.project.Project;

import java.io.File;

import static environment.Constants.*;

public class RepoLocalStorageDataProvider implements RepoLocalStorageDataProviderInterface {

    private static String projectDirectory;

    public static void setUserProjectDirectory(Project project){
        projectDirectory = project.getBasePath();
    }

    public static String getUserProjectDirectory() {
        return projectDirectory;
    }

    public static File getHandoutHtmlFile(){
        File handoutFile = new File(getHandoutHtmlString());
        return handoutFile;
    }

    public static String getHandoutHtmlString(){
        String handoutDataDirectoryPath = getUserProjectDirectory() + LOCAL_STORAGE_FILE + REPO_LOCAL_STORAGE_FILE + HANDOUT_FILE_NAME;
        return handoutDataDirectoryPath;
    }


}
