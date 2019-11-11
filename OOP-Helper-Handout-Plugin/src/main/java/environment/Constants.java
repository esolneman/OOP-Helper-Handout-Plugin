package environment;

public class Constants {

    //prevent instantiation
    private Constants() {
    }

    public static final String LOCAL_STORAGE_FILE = "/HelperHandoutPluginContentData";
    public static final String REPO_LOCAL_STORAGE_FILE = "/RepoLocalStorage";
    public static final String USER_LOCAL_STORAGE_FILE = "/UserLocalStorage";
    public static final String REPO_PATH_TO_BRANCH = "refs/heads/";

    public static final String HANDOUT_FILE_NAME = "/index.html";
    public static final String CHECKLIST_FILE_NAME = "/checklist.json";
    public static final String SHORTCUT_FILE_NAME = "/shortcuts.html";
    public static final String COMMON_ASSESSMENT_CRITERIA_FILE_NAME = "/common-assessment-criteria.md";
    public static final String SPECIFIC_ASSESSMENT_CRITERIA_FILE_NAME = "/specific-assessment-criteria.html";

    public static final String HANDOUT_PDF_FILE_NAME = "/handout.pdf";
    public static final String URL_BEGIN_FOR_FILE = "file:///";

    public static final String LAST_COMMIT_HASH_FILE = "/lastCommit.txt";

}
