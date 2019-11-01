package provider;

import com.intellij.openapi.project.Project;
import listener.OnEventListener;
import provider.helper.AsyncExecutor;
import provider.helper.DownloadTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
    File tempVersionZipFile;
    File tempVersionOutputDir;



    public HandoutContentDataProvider(Project project) {
        this.project = project;
        projectDirectory = project.getBasePath();
        contentRepoPath = RepoLocalStorageDataProvider.getUserProjectDirectory() + LOCAL_STORAGE_FILE + REPO_LOCAL_STORAGE_FILE;
        contentRepoFile = new File (contentRepoPath);
        zipFile = new File (RepoLocalStorageDataProvider.getUserProjectDirectory() + LOCAL_STORAGE_FILE  + REPO_LOCAL_STORAGE_FILE + "/repo.zip");
        outputDir = new File(RepoLocalStorageDataProvider.getUserProjectDirectory() + LOCAL_STORAGE_FILE  + REPO_LOCAL_STORAGE_FILE + "/repo");
        //TODO: TEMP-File
        tempVersionZipFile = new File(RepoLocalStorageDataProvider.getUserProjectDirectory() + LOCAL_STORAGE_FILE  + REPO_LOCAL_STORAGE_FILE + "/temp" + "/repo.zip");
        tempVersionOutputDir = new File(RepoLocalStorageDataProvider.getUserProjectDirectory() + LOCAL_STORAGE_FILE  + REPO_LOCAL_STORAGE_FILE + "/temp");

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
        if (!zipFile.exists()) {
            System.out.println("repo doesn't exist");
            cloneRepository(task);
        } else {
            System.out.println("repo exist");
            updateBranch(task);
        }






    }
    public void addListener(OnEventListener listener) {
        listeners.add(listener);
    }

    private void cloneRepository(DownloadTask task) {
        System.out.println("start cloning branch");
        //https://www.vogella.com/tutorials/JGit/article.html#example-for-using-jgit
        /**
         * 1. Download handout branch as ZIP
         * 2. Store ZIP in project folder
         * 3. Compare ZIP file with last downloaded file (HASH)
         * 4. If new (HASH is different) unzip to handout folder and overwrite last downloaded file
         */

        Runnable cloneTask = () -> {
            try {
                zipFile.getParentFile().mkdirs();
                zipFile.createNewFile();
                task.run(zipFile);
                //TODO: maybe delete if --> always true?
                if(!outputDir.exists()){
                    outputDir.mkdirs();
                    outputDir.createNewFile();
                    task.unzipFile(zipFile, outputDir);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                System.out.println("end cloning branch");
                if (listeners != null) {
                    System.out.println("listener not null");
                    for (OnEventListener listener : listeners) {
                        System.out.println("listener: " + listener.toString());
                        listener.onCloningRepositoryEvent(outputDir);
                    }
                } else {
                    System.out.println("event Listener null");
                }
            }
        };
        asyncExecutor.runAsyncClone(cloneTask);
    }

    private void updateBranch(DownloadTask task){
        Runnable updateTask = () -> {
        if(!tempVersionZipFile.exists()){
            try {
                tempVersionZipFile.getParentFile().mkdirs();
                tempVersionZipFile.createNewFile();
                task.run(tempVersionZipFile);
                if(!tempVersionOutputDir.exists()){
                    tempVersionOutputDir.getParentFile().mkdirs();
                    tempVersionZipFile.createNewFile();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                System.out.println("end cloning branch");
                if (listeners != null) {
                    System.out.println("listener not null");
                    for (OnEventListener listener : listeners) {
                        System.out.println("listener: " + listener.toString());
                        listener.onCloningRepositoryEvent(outputDir);
                    }
                } else {
                    System.out.println("event Listener null");
                }
            }
        }
        //TODO: hash zips or output files
        hashZipFiles();
        task.unzipFile(tempVersionZipFile, tempVersionOutputDir);
        };
        asyncExecutor.runAsyncClone(updateTask);
    }

    private void hashZipFiles(){

    }

}
