package provider;

import java.io.File;
import java.util.ArrayList;

public class LocalStorageDataProvider implements LocalStorageDataProviderInterface {

    public static File getHandoutFileDirectory(){
        return RepoLocalStorageDataProvider.getHandoutHtmlFile();
    }

    public static String getHandoutStringDirectory(){
        return RepoLocalStorageDataProvider.getHandoutHtmlString();
    }
}
