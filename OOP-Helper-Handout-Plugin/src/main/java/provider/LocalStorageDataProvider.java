package provider;

import java.io.File;

public class LocalStorageDataProvider {

    public static File getHandoutFileDirectory() {
        return UserLocalStorageDataProvider.getHandoutHtmlFile();
    }

    public static File getInitHandoutFileDirectory() {
        return RepoLocalStorageDataProvider.getHandoutInitFile();
    }

    public static File getSpecificAssessmentCriteriaFileDirectory() {
        return RepoLocalStorageDataProvider.getSpecificAssessmentCriteriaFile();
    }

    public static File getCommonAssessmentCriteriaFileDirectory() {
        return RepoLocalStorageDataProvider.getCommonAssessmentCriteriaFileDirectory();
    }

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

    public static File getTutorialDirectory() { return RepoLocalStorageDataProvider.getTutorialFile();
    }

    public static File getProjectCreationDateDirectory() {
        return UserLocalStorageDataProvider.getProjectCreationDateFile();
    }
}
