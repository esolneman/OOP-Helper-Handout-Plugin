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

    public static File getCommonAssessmentCriteriaFileDirectory() {
        return RepoLocalStorageDataProvider.getCommonAssessmentCriteriaFileDirectory();
    }

    //TODO Parser from Repo and USER call
    public static File getChecklistData() {
        return RepoLocalStorageDataProvider.getChecklistFile();
    }

    public static File getCodingStylesFileDirectory() {
        return RepoLocalStorageDataProvider.getCodingStylesFile();
    }

    public static File getVariablesDirectory() {
        return RepoLocalStorageDataProvider.getVariablesFile();
    }

    public static File getNotesFile() {
        return  UserLocalStorageDataProvider.getNotesFile();
    }

    public static File getInitNotesHtmlFile() {
        return RepoLocalStorageDataProvider.getNotesInitFile();
    }
}
