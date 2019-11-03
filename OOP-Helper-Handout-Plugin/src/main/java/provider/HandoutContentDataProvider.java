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
    DownloadTask task;


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
        RepoLocalStorageDataProvider.setHandoutHtmlString(branchFolderName);
    }

    public void updateHandoutData() {
        System.out.println("updateHandoutData");
        //TODO: implement Logic
        task = new DownloadTask(repoZipUrl);
        controlRetrievingContentData();
    }

    private void controlRetrievingContentData() {
        Boolean internetConnection = checkInternetConnection();
        Boolean repoContentDataExists = checkRepoContentDataExists();
        if (internetConnection && !repoContentDataExists) {
            System.out.println("Downloading - internet && !repoContentDataExists");
            updateBranch(task);
        } else if (internetConnection && repoContentDataExists) {
            System.out.println("UPDATE - internet && repoContentDataExists");
            cloneRepository(task);
        } else if (repoContentDataExists) {
            System.out.println("Notification - !internet && repoContentDataExists");
            //TODO: Notification
        } else {
            //TODO: Notification
            System.out.println("Notification - !internet && !repoContentDataExists");
        }
    }

    private boolean checkRepoContentDataExists() {
        //https://stackoverflow.com/a/15571626
        if (!zipFile.exists()) {
            System.out.println("repo doesn't exist");
            return false;
        } else {
            System.out.println("repo exist");
            return true;
        }
    }

    //https://www.geeksforgeeks.org/checking-internet-connectivity-using-java/
    public Boolean checkInternetConnection() {
        Process process;
        try {
            process = Runtime.getRuntime().exec("ping www.geeksforgeeks.org");
            int x = process.waitFor();
            if (x == 0) {
                System.out.println("Connection Successful, " + "Output was " + x);
                return true;
            } else {
                System.out.println("Internet Not Connected, " + "Output was " + x);
                return false;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void addListener(OnEventListener listener) {
        listeners.add(listener);
    }

    private void cloneRepository(DownloadTask task) {
        System.out.println("start cloning branch");
        //https://www.vogella.com/tutorials/JGit/article.html#example-for-using-jgit
        Runnable cloneTask = () -> {
            try {
                createFolder(zipFile, true);
                createFolder(outputDir, false);
                task.run(zipFile);
                task.unzipFile(zipFile, outputDir);
            } catch (IOException e) {
                //TODO Notification
                deleteFile(repoLocalData);
                e.printStackTrace();
            } finally {
                callListener();
            }
        };
        asyncExecutor.runAsyncClone(cloneTask);
    }

    private void createFolder(File zipFile, Boolean createParentFolder) throws IOException {
        if(createParentFolder){
            zipFile.getParentFile().mkdirs();
        }else{
            zipFile.mkdirs();
        }
        zipFile.createNewFile();
    }

    private void callListener() {
        System.out.println("end cloning branch");
        if (listeners != null) {
            System.out.println("listener not null");
            for (OnEventListener listener : listeners) {
                listener.onCloningRepositoryEvent(outputDir);
            }
        }
    }

    private void updateBranch(DownloadTask task) {
        Runnable updateTask = () -> {
            try {
                //https://stackoverflow.com/a/6143076
                createFolder(tempVersionZipFile, true);
                task.run(tempVersionZipFile);
                if (!task.compareZipFiles(zipFile, tempVersionZipFile)) {
                    System.out.println("not equal");
                    task.unzipFile(tempVersionZipFile, outputDir);
                    replaceFile(tempVersionZipFile, zipFile);
                }
            } catch (IOException e) {
                //TODO Notification
                repoLocalData.delete();
                e.printStackTrace();
            } finally {
                deleteFile(tempVersionZipFile);
                callListener();
            }
        };
        asyncExecutor.runAsyncClone(updateTask);
    }

    private void replaceFile (File newData, File oldData) throws IOException {
        //https://stackoverflow.com/a/17169576
        Path from = newData.toPath(); //convert from File to Path
        Path to = Paths.get(oldData.getPath()); //convert from String to Path
        Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
    }

    private void deleteFile(File file){
        file.delete();
    }
}
