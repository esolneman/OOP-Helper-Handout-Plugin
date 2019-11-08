package provider;

import objects.SpecificAssessmentCriteria;

import java.io.File;
import java.util.ArrayList;

import static environment.Constants.*;

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
}
