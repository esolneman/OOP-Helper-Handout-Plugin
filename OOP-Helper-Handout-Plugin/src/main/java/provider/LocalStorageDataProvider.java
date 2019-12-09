package provider;

import java.io.File;

public class LocalStorageDataProvider implements LocalStorageDataProviderInterface {

    public static File getHandoutFileDirectory() {
        return RepoLocalStorageDataProvider.getHandoutHtmlFile();
    }

    public static File getSpecificAssessmentCriteriaFileDirectory() {
        return RepoLocalStorageDataProvider.getSpecificAssessmentCriteriaFile();
    }

    public static File getCommonAssessmentCriteriaFileDirectory() {
        return RepoLocalStorageDataProvider.getCommonAssessmentCriteriaFileDirectory();
    }

    //TODO Parser from Repo and USER call
    public static File getChecklistData() {
        return RepoLocalStorageDataProvider.getChecklistFile();
    }

    public static File getChecklistUserData() {
        return UserLocalStorageDataProvider.getChecklistFile();
    }

    public static File getLocalChecklistPredefinedData() {
        return UserLocalStorageDataProvider.getPredefinedChecklistFile();
    }

    public static File getNotesFile() {
        return UserLocalStorageDataProvider.getNotesFile();
    }

    public static File getInitNotesHtmlFile() {
        return RepoLocalStorageDataProvider.getNotesInitFile();
    }


    public static File getHelpStartDirectory() { return RepoLocalStorageDataProvider.getHelpStartFile();
    }

    public static File getLocalPredefinedChecklistFile() { return UserLocalStorageDataProvider.getChecklistStartPageFile();}

    public static File getRepoPredefinedChecklistFile() {return RepoLocalStorageDataProvider.getPredefinedChecklist();}

    public static File getRepoUserDataChecklistFile() { return RepoLocalStorageDataProvider.getUserDataChecklist();}

    public static File getLocalUserDataChecklistFile() { return UserLocalStorageDataProvider.getUserDataChecklist();}

}
