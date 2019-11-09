package provider;

import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;
import controller.BalloonPopupController;
import eventHandling.OnGitEventListener;
import eventHandling.OnUpdatingRepositoryEvent;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.errors.GitAPIException;
import provider.helper.AsyncExecutor;
import provider.helper.DownloadTask;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.concurrent.Future;

import static environment.Constants.*;

// is Singleton
public class HandoutContentDataProvider implements HandoutContentDataProviderInterface {
    private OnGitEventListener onEventListener;
    private OnUpdatingRepositoryEvent onUpdatingRepositoryEvent;
    private AsyncExecutor asyncExecutor = new AsyncExecutor();
    private static HandoutContentDataProvider single_instance = null;

    // TODO: get RepoURL from jar file
    private String repoUrl = "https://github.com/esolneman/OOP-Helper-Handout-Template";
    private String repoZipUrl = "https://github.com/esolneman/OOP-Helper-Handout-Template/archive/test.zip";
    //String CLONE_DIRECTORY_PATH = "refs/heads/test";
    //String CONTENT_FILE_NAME = "/HelperHandoutPluginContentData/RepoLocalStorage";
    private String repoFileName;
    private String branchPath;
    private Project project;
    private String projectDirectory;
    private String contentRepoPath;
    private File contentRepoFile;
    private File zipFile;
    private File outputDir;
    private File tempVersionZipFile;
    private File tempVersionOutputDir;
    private File repoLocalData;
    private DownloadTask task;
    private ArrayList<String> lastCommitMessages;

    public static HandoutContentDataProvider getInstance() {
        if (single_instance == null) {
            single_instance = new HandoutContentDataProvider();
        }
        return single_instance;
    }


    private HandoutContentDataProvider() {
        this.project = RepoLocalStorageDataProvider.getProject();
        projectDirectory = project.getBasePath();
        contentRepoPath = RepoLocalStorageDataProvider.getUserProjectDirectory() + LOCAL_STORAGE_FILE + REPO_LOCAL_STORAGE_FILE;
        contentRepoFile = new File(contentRepoPath);
        zipFile = new File(RepoLocalStorageDataProvider.getUserProjectDirectory() + LOCAL_STORAGE_FILE + REPO_LOCAL_STORAGE_FILE);
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
        task = DownloadTask.getInstance();
    }


    //ToDo getRepo Url
    private void getRepoUrl() {
        System.out.println("getRepoUrl");
        //System.out.println(TaskConfiguration.loadFrom());
        //repoUrl = TaskConfiguration.loadFrom().getHandoutURL();


        //String branchFolderName = "/OOP-Helper-Handout-Template-test";
        //RepoLocalStorageDataProvider.setBranchFolderName(branchFolderName);
    }

    public void updateHandoutData() {
        System.out.println("updateHandoutData");
        controlRetrievingContentData();
    }

    private void controlRetrievingContentData() {
        Boolean internetConnection = checkInternetConnection();
        Boolean repoContentDataExists = checkRepoContentDataExists();
        if (internetConnection && !repoContentDataExists) {
            cloneRepository();
        } else if (internetConnection && repoContentDataExists) {
            updateBranch();
        } else if (repoContentDataExists) {
            BalloonPopupController.showNotification(project, "Keine Internetverbindung vorhanden. Handout Daten können momentan nicht aktualisiert werden.", NotificationType.ERROR);
        } else {
            BalloonPopupController.showNotification(project, "Keine Internetverbindung vorhanden. Handout Daten können momentan nciht geladen werden.", NotificationType.ERROR);
        }
    }

    private boolean checkRepoContentDataExists() {
        //https://stackoverflow.com/a/15571626
        //if (!zipFile.exists()) {
        if (!contentRepoFile.exists()) {
            System.out.println("repo doesn't exist");
            return false;
        } else {
            System.out.println("repo exist");
            return true;
        }
    }

    //https://www.geeksforgeeks.org/checking-internet-connectivity-using-java/
    //TODO Ping Github Repo -> is Repo Available
    public Boolean checkInternetConnection() {
        Process process;
        try {
            //TODO was anpingen?
            process = Runtime.getRuntime().exec("ping www.google.de");
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

    public void addListener(OnGitEventListener listener) {
        this.onEventListener = listener;
    }

    @Override
    public void addListener(OnUpdatingRepositoryEvent listener) {
        this.onUpdatingRepositoryEvent = listener;
    }

    //TODO
    private void cloneRepository() {
        System.out.println("start cloning branch");
        //https://www.vogella.com/tutorials/JGit/article.html#example-for-using-jgit
        Runnable cloneTask = () -> {
            try {
                //createFolder(zipFile, true);
                //createFolder(outputDir, false);
                task.run(repoUrl, contentRepoFile, branchPath);
                //task.run(zipFile);
                //task.unzipFile(zipFile, outputDir);
            } catch (IOException e) {
                //TODO Notification
                deleteFile(repoLocalData);
                e.printStackTrace();
                try {
                    FileUtils.deleteDirectory(contentRepoFile);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    System.out.println(ex);
                }
            } finally {
                callListener();
                BalloonPopupController.showNotification(project, "Handout Daten wurden runtergeladen.", NotificationType.INFORMATION);
            }
        };
        asyncExecutor.runAsyncClone(cloneTask);
    }


    private void updateBranch() {
        System.out.println("updateBranch");
        ArrayList<String> commitMessages = task.getLatestCommits();
        System.out.println("updateBranch  commitMessages : " + commitMessages.size());
        if (commitMessages.size() >= 1) {
            System.out.println("commitMessages not empty");
            Runnable updateTask = () -> {
                try {
                    task.updateRepository(repoUrl);
                } catch (IOException | GitAPIException e) {
                    e.printStackTrace();
                }
            };
            Future<String> test = asyncExecutor.runAsyncClone(updateTask);
            while (test.isDone() == false) {
                callListener(commitMessages);
                BalloonPopupController.showNotification(project, "Handout Daten wurden runtergeladen." + commitMessages.toString(), NotificationType.INFORMATION);

                //Sleep for 1 second
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        } else {
            System.out.println("commitMessages empty");
            BalloonPopupController.showNotification(project, "Handout Daten sind bereits auf dem aktuellsten Stand.", NotificationType.INFORMATION);
        }

    }

    private void replaceFile(File newData, File oldData) throws IOException {
        //https://stackoverflow.com/a/17169576
        Path from = newData.toPath(); //convert from File to Path
        Path to = Paths.get(oldData.getPath()); //convert from String to Path
        Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
    }

    private void callListener() {
        System.out.println("end cloning branch");
        if (onEventListener != null) {
            System.out.println("listener not null");
            onEventListener.onCloningRepositoryEvent();
        }
    }

    private void callListener(ArrayList<String> lastCommitMessages) {
        System.out.println("end cloning branch");
        if (onUpdatingRepositoryEvent != null) {
            System.out.println("listener not null");
            onEventListener.onUpdatingRepositoryEvent(lastCommitMessages);
        }
    }

    private void deleteFile(File file) {
        file.delete();
    }
}
