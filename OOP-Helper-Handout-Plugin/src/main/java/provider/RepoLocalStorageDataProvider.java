package provider;

import com.intellij.openapi.project.Project;

import java.io.File;

import static environment.Constants.*;

public class RepoLocalStorageDataProvider implements RepoLocalStorageDataProviderInterface {

    private static String projectDirectory;
    private static Project handoutProject;
    private static String handoutDataDirectoryPath = null;

    public static void setUserProjectDirectory(Project project){
        projectDirectory = project.getBasePath();
        handoutProject = project;
    }

    public static String getUserProjectDirectory() {
        return projectDirectory;
    }

    public static Project getProject(){
        return handoutProject;
    }

    public static File getHandoutHtmlFile(){
        File handoutFile = new File(getHandoutHtmlString());
        return handoutFile;
    }

    public static String getHandoutHtmlString(){
        //ToDo: implement retrieving branchName
        return handoutDataDirectoryPath;
    }

    public static void setHandoutHtmlString(String BranchFolderName){
        handoutDataDirectoryPath = getUserProjectDirectory() + LOCAL_STORAGE_FILE + REPO_LOCAL_STORAGE_FILE + BranchFolderName + HANDOUT_FILE_NAME;
    }


}
