package provider;

import com.intellij.notification.NotificationType;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import controller.QuestionnaireController;
import de.ur.mi.pluginhelper.tasks.TaskConfiguration;
import eventHandling.OnGitEventListener;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.errors.GitAPIException;
import provider.helper.AsyncExecutor;
import provider.helper.DownloadTask;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import static environment.FileConstants.*;

// is Singleton
public class HandoutContentDataProvider implements HandoutContentDataProviderInterface {
    private OnGitEventListener onEventListener;
    private AsyncExecutor asyncExecutor = new AsyncExecutor();
    private static HandoutContentDataProvider single_instance = null;

    // TODO: get RepoURL from jar file
    private String repoUrl;
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

    public static HandoutContentDataProvider getInstance() {
        if (single_instance == null) {
            single_instance = new HandoutContentDataProvider();
        }
        return single_instance;
    }


    private HandoutContentDataProvider() {
        //TODO DELETE UNUSED FILES
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
        task = DownloadTask.getInstance();
    }

    private void getRepoUrl() {
        TaskConfiguration taskConfiguration = TaskConfiguration.loadFrom(project);
        repoUrl = taskConfiguration.getHandoutURL();
        branchPath = taskConfiguration.getBranchPath();
    }

    //TODO CHECK UPDATE FUNCTION
    public void updateHandoutData() {
        System.out.println("updateHandoutData");
        controlRetrievingContentData();
    }

    private void controlRetrievingContentData() {
        System.out.println("ControlRetrievingContentData: " + onEventListener);
        Boolean internetConnection = checkInternetConnection();
        Boolean repoContentDataExists = checkRepoContentDataExists();
        if (internetConnection && !repoContentDataExists) {
            cloneRepository();
        } else if (internetConnection && repoContentDataExists) {
            QuestionnaireController.getInstance().compareDates();
            updateBranch();
        } else if (repoContentDataExists) {
            QuestionnaireController.getInstance().compareDates();
            callListenerNotUpdating("Keine Internetverbindung vorhanden. Handout Daten können momentan nicht aktualisiert werden." , NotificationType.ERROR);
        } else {
            callListenerNotUpdating("Keine Internetverbindung vorhanden. Handout Daten können momentan nicht heruntergeladen werden.", NotificationType.ERROR);
        }
    }

    //TODO refactor false file --> check data
    private boolean checkRepoContentDataExists() {
        //https://stackoverflow.com/a/15571626
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
        try {
            final URL url = new URL("http://www.google.com");
            final URLConnection conn = url.openConnection();
            conn.connect();
            conn.getInputStream().close();
            return true;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            return false;
        }
    }

    public void addListener(OnGitEventListener listener) {
        this.onEventListener = listener;
    }

    //TODO
    private void cloneRepository() {
        System.out.println("start cloning branch");

        //https://www.vogella.com/tutorials/JGit/article.html#example-for-using-jgit
        Runnable cloneTask = () -> {
            try {
                task.run(repoUrl, contentRepoFile, branchPath);
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
                callListener("Handout Daten wurden runtergeladen.", NotificationType.INFORMATION);
            }
        };
        asyncExecutor.runAsyncClone(cloneTask);
    }


    private void updateBranch() {
        System.out.println("updateBranch");
        asyncExecutor = new AsyncExecutor();
        ArrayList<String> commitMessages = task.getLatestCommits();
        System.out.println("updateBranch  commitMessages : " + commitMessages.size());
        //if (commitMessages.size() >= 1) {
            System.out.println("commitMessages not empty");
            //TODO ASK USER IF DOWNLOAD IS OK
            Runnable updateTask = () -> {
                try {
                    task.updateRepository(repoUrl);
                } catch (IOException | GitAPIException e) {
                    e.printStackTrace();
                }
            };
            asyncExecutor.runAsyncClone(updateTask);
            callListener(commitMessages);
    }

    private void callListener(String message, NotificationType messageType) {
        if (onEventListener != null) {
            onEventListener.onCloningRepositoryEvent(message, messageType);
        }
    }

    private void callListener(ArrayList<String> lastCommitMessages) {
        System.out.println("listener call commitmessages");
        if (onEventListener != null) {
            System.out.println("listener call commitmessages not null");
            onEventListener.onUpdatingRepositoryEvent(lastCommitMessages);
        }
    }

    private void callListenerNotUpdating(String message, NotificationType messageType) {
        if (onEventListener != null) {
            onEventListener.onNotUpdatingRepositoryEvent(message, messageType);
        }
    }

    //TODO test that user not delted
    //TODO FILE CONTROLLER
    private void deleteFile(File file) {
        file.delete();
    }
}
