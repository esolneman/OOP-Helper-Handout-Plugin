package provider;

import java.io.File;

import static environment.Constants.*;

public class UserLocalStorageDataProvider implements UserLocalStorageDataProviderInterface {
    public static File getNotesFile() {
        File notesFile = new File(RepoLocalStorageDataProvider.getUserProjectDirectory() + LOCAL_STORAGE_FILE + REPO_LOCAL_STORAGE_FILE + NOTES_FILE);
        return notesFile;
    }
}
