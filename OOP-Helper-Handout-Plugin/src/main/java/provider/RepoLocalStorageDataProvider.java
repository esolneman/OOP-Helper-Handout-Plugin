package provider;

import com.intellij.openapi.project.Project;
import objects.SpecificAssessmentCriteria;

import java.io.File;

import static environment.Constants.*;

public class RepoLocalStorageDataProvider implements RepoLocalStorageDataProviderInterface {

    private static String projectDirectory;
    private static Project handoutProject;
    private static String handoutDataDirectoryPath = null;
    private static String branchFolderName;

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

    public static File getSpecificAssessmentCriteriaFile(){
        File specificAssessmentCriteriaFile = new File(getUserProjectDirectory() + LOCAL_STORAGE_FILE + REPO_LOCAL_STORAGE_FILE +  branchFolderName + SPECIFIC_ASSESSMENT_CRITERIA_FILE_NAME);
        return specificAssessmentCriteriaFile;
    }

    public static File getSpecificAssessmentCriteriaFileTest(){
        File specificAssessmentCriteriaFile = new File(getUserProjectDirectory() + LOCAL_STORAGE_FILE + REPO_LOCAL_STORAGE_FILE +  branchFolderName + "/test.html");

        return specificAssessmentCriteriaFile;
    }

    public static String getHandoutHtmlString(){
        String handoutFile = getUserProjectDirectory() + LOCAL_STORAGE_FILE + REPO_LOCAL_STORAGE_FILE +  branchFolderName + HANDOUT_FILE_NAME;
        return handoutFile;
    }

    //TODO REFACTOR
    public static void setBranchFolderName(String branchFolderName){
        RepoLocalStorageDataProvider.branchFolderName = branchFolderName;
    }


}
