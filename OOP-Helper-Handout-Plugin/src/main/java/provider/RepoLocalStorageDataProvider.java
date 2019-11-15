package provider;

import com.intellij.openapi.project.Project;
import objects.SpecificAssessmentCriteria;

import java.io.File;

import static environment.Constants.*;

public class RepoLocalStorageDataProvider implements RepoLocalStorageDataProviderInterface {

    private static String projectDirectory;
    private static Project handoutProject;

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
        File specificAssessmentCriteriaFile = new File(getUserProjectDirectory() + LOCAL_STORAGE_FILE + REPO_LOCAL_STORAGE_FILE  + SPECIFIC_ASSESSMENT_CRITERIA_FILE_NAME);
        return specificAssessmentCriteriaFile;
    }

    public static String getHandoutHtmlString(){
        String handoutFile = getUserProjectDirectory() + LOCAL_STORAGE_FILE + REPO_LOCAL_STORAGE_FILE  + HANDOUT_FILE_NAME;
        return handoutFile;
    }

    public static String getRepoLocalFile(){
        //File file = new File();
        return getUserProjectDirectory() + LOCAL_STORAGE_FILE + REPO_LOCAL_STORAGE_FILE;
    }


    public static File getShortcutFileDirectory() {
        File shortcutFile = new File(getUserProjectDirectory() + LOCAL_STORAGE_FILE + REPO_LOCAL_STORAGE_FILE + SHORTCUT_FILE_NAME);
        return shortcutFile;
    }


    public static File getCommonAssessmentCriteriaFileDirectory() {
        File specificAssessmentCriteriaFile = new File(getUserProjectDirectory() + LOCAL_STORAGE_FILE + REPO_LOCAL_STORAGE_FILE + branchFolderName + COMMON_ASSESSMENT_CRITERIA_FILE_NAME);
        return specificAssessmentCriteriaFile;
    }
    public static File getChecklistFile() {
        File checklistFile = new File(getUserProjectDirectory() + LOCAL_STORAGE_FILE + REPO_LOCAL_STORAGE_FILE + CHECKLIST_FILE_NAME);
        return checklistFile;
    }
}
