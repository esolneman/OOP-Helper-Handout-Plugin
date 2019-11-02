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


}
