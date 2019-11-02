package provider;

import com.intellij.openapi.project.Project;
import listener.OnEventListener;
import provider.helper.AsyncExecutor;
import provider.helper.DownloadTask;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
    File repoLocalData;


    public HandoutContentDataProvider(Project project) {
        this.project = project;
        projectDirectory = project.getBasePath();
        contentRepoPath = RepoLocalStorageDataProvider.getUserProjectDirectory() + LOCAL_STORAGE_FILE + REPO_LOCAL_STORAGE_FILE;
        contentRepoFile = new File(contentRepoPath);
        zipFile = new File(RepoLocalStorageDataProvider.getUserProjectDirectory() + LOCAL_STORAGE_FILE + REPO_LOCAL_STORAGE_FILE + "/repo.zip");
        outputDir = new File(RepoLocalStorageDataProvider.getUserProjectDirectory() + LOCAL_STORAGE_FILE + REPO_LOCAL_STORAGE_FILE);
        //TODO: TEMP-File
        tempVersionZipFile = new File(RepoLocalStorageDataProvider.getUserProjectDirectory() + LOCAL_STORAGE_FILE + REPO_LOCAL_STORAGE_FILE + "/temp" + "/repo.zip");
        tempVersionOutputDir = new File(RepoLocalStorageDataProvider.getUserProjectDirectory() + LOCAL_STORAGE_FILE + REPO_LOCAL_STORAGE_FILE + "/temp");
        repoLocalData = new File(RepoLocalStorageDataProvider.getUserProjectDirectory() + LOCAL_STORAGE_FILE + REPO_LOCAL_STORAGE_FILE);
        getRepoUrl();
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
        String branchFolderName = "/OOP-Helper-Handout-Template-test";
        RepoLocalStorageDataProvider.setBranchFolderName(branchFolderName);
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
        Runnable cloneTask = () -> {
            try {
                zipFile.getParentFile().mkdirs();
                zipFile.createNewFile();
                task.run(zipFile);
                outputDir.mkdirs();
                outputDir.createNewFile();
                task.unzipFile(zipFile, outputDir);
            } catch (IOException e) {
                //TODO Notification
                repoLocalData.delete();
                e.printStackTrace();
            } finally {
                callListener();
            }
        };
        asyncExecutor.runAsyncClone(cloneTask);
    }

    private void callListener() {
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

    private void updateBranch(DownloadTask task) {
        Runnable updateTask = () -> {
            if (!tempVersionZipFile.exists()) {
                try {
                    //https://stackoverflow.com/a/6143076
                    tempVersionZipFile.getParentFile().mkdirs();
                    tempVersionZipFile.createNewFile();
                    task.run(tempVersionZipFile);
                    if (!task.compareZipFiles(zipFile, tempVersionZipFile)) {
                        System.out.println("not equal");
                        task.unzipFile(tempVersionZipFile, outputDir);
                        //https://stackoverflow.com/a/17169576
                        Path from = tempVersionZipFile.toPath(); //convert from File to Path
                        Path to = Paths.get(zipFile.getPath()); //convert from String to Path
                        Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
                    }
                } catch (IOException e) {
                    //TODO Notification
                    repoLocalData.delete();
                    e.printStackTrace();
                } finally {
                    //FileUtils.deleteDirectory(transformation.getTransformedApplicationLocation());
                    boolean delete = tempVersionOutputDir.delete();
                    System.out.println(" delete file: " + delete);
                    callListener();
                }
            }
        };
        asyncExecutor.runAsyncClone(updateTask);
    }
}
