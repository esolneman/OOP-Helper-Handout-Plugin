package provider;

import java.io.File;

public class RepoLocalStorageDataProvider implements RepoLocalStorageDataProviderInterface {

    public static File getHandoutHtmlFormat(){
        String file = "C:/Masterarbeit/TestProjekt/OOP-18WS-CoreDefense-Starter/HelperHandoutPluginContentData/RepoLocalStorage/index.html";
        File content = new File(file);
        return content;
    }
}
