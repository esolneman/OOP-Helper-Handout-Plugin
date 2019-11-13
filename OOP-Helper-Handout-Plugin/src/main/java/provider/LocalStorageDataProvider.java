package provider;

import java.io.File;

public class LocalStorageDataProvider implements LocalStorageDataProviderInterface {

    public static File getHandoutFileDirectory() {
        return RepoLocalStorageDataProvider.getHandoutHtmlFile();
    }

    public static String getHandoutStringDirectory() {
        return RepoLocalStorageDataProvider.getHandoutHtmlString();
    }

    public static File getSpecificAssessmentCriteriaFileDirectory() {
        return RepoLocalStorageDataProvider.getSpecificAssessmentCriteriaFile();
    }

    public static File getShortcutFileDirectory() {
        return RepoLocalStorageDataProvider.getShortcutFileDirectory();

    }

    //TODO Parser from Repo and USER call
    public static File getChecklistData() {
        return RepoLocalStorageDataProvider.getChecklistFile();
    }

    public static File getChecklistUserData() {
        return UserLocalStorageDataProvider.getChecklistFile();
    }
}
