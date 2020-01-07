package provider;

import com.intellij.openapi.project.Project;

import java.io.File;

import static environment.FileConstants.*;

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

    public static File getHandoutInitFile(){
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

    public static File getCommonAssessmentCriteriaFileDirectory() {
        File specificAssessmentCriteriaFile = new File(getUserProjectDirectory() + LOCAL_STORAGE_FILE + REPO_LOCAL_STORAGE_FILE  + COMMON_INFORMATION_FILE_NAME);
        return specificAssessmentCriteriaFile;
    }
    public static File getChecklistFile() {
        File checklistFile = new File(getUserProjectDirectory() + LOCAL_STORAGE_FILE + REPO_LOCAL_STORAGE_FILE + CHECKLIST_FILE_NAME);
        return checklistFile;
    }

    public static File getNotesInitFile() {
        File notesFile = new File(getUserProjectDirectory() + LOCAL_STORAGE_FILE + REPO_LOCAL_STORAGE_FILE + NOTES_FILE);
        return notesFile;
    }

    public static File getHelpStartFile() {
        File helpStartFile = new File(getUserProjectDirectory() + LOCAL_STORAGE_FILE + REPO_LOCAL_STORAGE_FILE + COMMON_INFORMATION_FILE_NAME);
        return helpStartFile;
    }

    public static File getPredefinedChecklist() {
        File predefinedChecklist = new File(getUserProjectDirectory() + LOCAL_STORAGE_FILE + REPO_LOCAL_STORAGE_FILE + PREDEFINED_CHECKLIST_HTML_FILE);
        return predefinedChecklist;
    }

    public static File getUserDataChecklist() {
        File predefinedChecklist = new File(getUserProjectDirectory() + LOCAL_STORAGE_FILE + REPO_LOCAL_STORAGE_FILE + USER_CHECKLIST_HTML_FILE);
        return predefinedChecklist;
    }

    public static File getTutorialFile() {
        File tutorialFile = new File(getUserProjectDirectory() + LOCAL_STORAGE_FILE + REPO_LOCAL_STORAGE_FILE + TUTORIAL_FILE_NAME);
        return tutorialFile;
    }
}
