package provider;

import java.io.File;
import java.util.ArrayList;

import static environment.Constants.*;

public class LocalStorageDataProvider implements LocalStorageDataProviderInterface {

    public static File getHandoutFileDirectory(){
        return RepoLocalStorageDataProvider.getHandoutHtmlFile();
    }

    public static String getHandoutStringDirectory(){
        return RepoLocalStorageDataProvider.getHandoutHtmlString();
    }

    public static String testFile(){
        String testPath = RepoLocalStorageDataProvider.getUserProjectDirectory() + LOCAL_STORAGE_FILE + REPO_LOCAL_STORAGE_FILE + "/test.html";;
        return testPath;
    }

}
