package provider;

import java.io.File;

import static environment.Constants.*;

public class UserLocalStorageDataProvider implements UserLocalStorageDataProviderInterface {
    //TODO PROJECT
    public static File getChecklistFile() {
        File checklistFile = new File(RepoLocalStorageDataProvider.getUserProjectDirectory() + LOCAL_STORAGE_FILE + USER_LOCAL_STORAGE_FILE + CHECKLIST_FILE_NAME);
        return checklistFile;
    }
}
