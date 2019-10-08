package provider;

import java.io.File;

public class LocalStorageDataProvider implements LocalStorageDataProviderInterface {

    public static File getHandoutFileDirectory(){
        return RepoLocalStorageDataProvider.getHandoutHtmlFile();
    }

    public static String getHandoutStringDirectory(){
        return RepoLocalStorageDataProvider.getHandoutHtmlString();
    }
}
