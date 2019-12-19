package provider;

import java.io.File;

import static environment.FileConstants.*;

public class UserLocalStorageDataProvider implements UserLocalStorageDataProviderInterface {
    //TODO PROJECT
    public static File getChecklistFile() {
        File checklistFile = new File(RepoLocalStorageDataProvider.getUserProjectDirectory() + LOCAL_STORAGE_FILE + USER_LOCAL_STORAGE_FILE + CHECKLIST_FILE_NAME);
        return checklistFile;
    }

    public static File getNotesFile() {
        File notesFile = new File(RepoLocalStorageDataProvider.getUserProjectDirectory() + LOCAL_STORAGE_FILE + USER_LOCAL_STORAGE_FILE + NOTES_FILE);
        return notesFile;
    }

    public static File getPredefinedChecklistFile() {
        File notesFile = new File(RepoLocalStorageDataProvider.getUserProjectDirectory() + LOCAL_STORAGE_FILE + USER_LOCAL_STORAGE_FILE + PREDEFINED_CHECKLIST_FILE_NAME);
        return notesFile;
    }

    public static File getChecklistStartPageFile() {
        File checklistFile = new File(RepoLocalStorageDataProvider.getUserProjectDirectory() + LOCAL_STORAGE_FILE + USER_LOCAL_STORAGE_FILE + PREDEFINED_CHECKLIST_HTML_FILE);
        return checklistFile;
    }


    public static File getUserDataChecklist() {
        File predefinedChecklist = new File(RepoLocalStorageDataProvider.getUserProjectDirectory() + LOCAL_STORAGE_FILE + USER_LOCAL_STORAGE_FILE + USER_CHECKLIST_HTML_FILE);
        return predefinedChecklist;
    }

    public static File getHandoutHtmlFile() {
        File handoutFile = new File(RepoLocalStorageDataProvider.getUserProjectDirectory() + LOCAL_STORAGE_FILE + USER_LOCAL_STORAGE_FILE + HANDOUT_FILE_NAME);
        return handoutFile;
    }
}
