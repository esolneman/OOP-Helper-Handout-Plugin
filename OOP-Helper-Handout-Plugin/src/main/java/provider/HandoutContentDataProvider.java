package provider;

import com.intellij.openapi.project.Project;
import listener.OnEventListener;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import provider.helper.DownloadTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static environment.Constants.*;


public class HandoutContentDataProvider implements HandoutContentDataProviderInterface {
    private List<OnEventListener> listeners = new ArrayList<>();
    private AsyncExecutor asyncExecutor = new AsyncExecutor();
    //private OnEventListener eventListener;

    // TODO: get RepoURL from jar file
    String repoUrl = "https://github.com/esolneman/OOP-Helper-Handout-Template.git";
    String repoZipUrl = "https://github.com/esolneman/OOP-Helper-Handout-Template/archive/test.zip";
    //String CLONE_DIRECTORY_PATH = "refs/heads/test";
    //String CONTENT_FILE_NAME = "/HelperHandoutPluginContentData/RepoLocalStorage";
    String repoFileName;
    String branchPath;
    Project project;
    String projectDirectory;
    String contentRepoPath;
    File contentRepoFile;
    File zipFile;
    File outputDir;
    File tempVersion;


    public HandoutContentDataProvider(Project project) {
        this.project = project;
        projectDirectory = project.getBasePath();
        contentRepoPath = RepoLocalStorageDataProvider.getUserProjectDirectory() + LOCAL_STORAGE_FILE + REPO_LOCAL_STORAGE_FILE;
        contentRepoFile = new File (contentRepoPath);
        zipFile = new File (RepoLocalStorageDataProvider.getUserProjectDirectory() + LOCAL_STORAGE_FILE  + "/repo.zip");
        outputDir = new File(RepoLocalStorageDataProvider.getUserProjectDirectory() + LOCAL_STORAGE_FILE  + "/repo");
        //TODO: TEMP-File
        tempVersion = new File(RepoLocalStorageDataProvider.getUserProjectDirectory() + LOCAL_STORAGE_FILE  + "/temp" + "/repo");
        //getRepoUrl();
        //getBranchName();
        // ToDo: get BranchName
        //TODO get RepoName
        branchPath = REPO_PATH_TO_BRANCH + "test";
        System.out.println(contentRepoPath);
    }

    //ToDo getRepo Url
    private void getRepoUrl() {
        System.out.println("getRepoUrl");
        //System.out.println(TaskConfiguration.loadFrom());
        //repoUrl = TaskConfiguration.loadFrom().getHandoutURL();
    }

    public void updateHandoutData() {
        System.out.println("updateHandoutData");

        //TODO check internet connection first

        //TODO: implement Logic

        DownloadTask task = new DownloadTask(repoZipUrl);


//https://stackoverflow.com/a/15571626
        if (!contentRepoFile.exists()) {
            //cloneRepository();
            task.run(zipFile);
            // TODO: Currently not working, make sure output folder exist before trying to unzip file
            task.unzipFile(zipFile, outputDir);
        } else {
            System.out.println("repo exist");
            //updateBranch() check;
        }






    }
    public void addListener(OnEventListener listener) {
        listeners.add(listener);
    }

    private void cloneRepository() {
        System.out.println("start cloning branch");
        //https://www.vogella.com/tutorials/JGit/article.html#example-for-using-jgit
        /**
         * 1. Download handout branch as ZIP
         * 2. Store ZIP in project folder
         * 3. Compare ZIP file with last downloaded file (HASH)
         * 4. If new (HASH is different) unzip to handout folder and overwrite last downloaded file
         */

        Runnable cloneTask = () -> {
            Git clone = null;
            {
                try {
                    clone = Git.cloneRepository()
                            .setURI(repoUrl)
                            .setDirectory(contentRepoFile)
                            .setBranchesToClone(Arrays.asList(branchPath))
                            .setBranch(branchPath)
                            .call();
                    System.out.println("clone run");

                } catch (GitAPIException e) {
                    try {
                        FileUtils.deleteDirectory(contentRepoFile);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        System.out.println(ex);

                    }
                    System.out.println(e);
                    //throw e;
                } finally {
                    System.out.println("end cloning branch");
                    //clone.close();
                    //clone.getRepository().close();
                    //check if listener is registered.
                    if (listeners != null) {
                        System.out.println("listener not null");

                        for(OnEventListener listener : listeners){
                            System.out.println("listener: " + listener.toString());
                            listener.onCloningRepositoryEvent(contentRepoFile);
                        }
                    }else{
                        System.out.println("event Listener null");
                    }
                }
            }
        };
        asyncExecutor.runAsyncClone(cloneTask);
    }

    private void updateBranch(){}
    private void getLocalRepository(){
/*        FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
        Repository repository = repositoryBuilder.setGitDir(new File("/path/to/repo/.git"))
                .readEnvironment() // scan environment GIT_* variables
                .findGitDir() // scan up the file system tree
                .setMustExist(true)
                .build();*/
    }
}
