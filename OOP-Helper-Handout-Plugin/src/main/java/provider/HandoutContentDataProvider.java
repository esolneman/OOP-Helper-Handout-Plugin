package provider;

import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import controller.QuestionnaireController;
import de.ur.mi.pluginhelper.tasks.TaskConfiguration;
import eventHandling.OnGitEventListener;
import org.eclipse.jgit.api.errors.GitAPIException;
import provider.helper.ProgressExecutor;
import provider.helper.DownloadTask;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import static environment.FileConstants.*;
import static environment.Messages.*;

// is Singleton
public class HandoutContentDataProvider implements HandoutContentDataProviderInterface {
    private OnGitEventListener onEventListener;
    private ProgressExecutor progressExecutor = new ProgressExecutor();
    private static HandoutContentDataProvider single_instance = null;

    private String repoUrl;
    private String branchPath;
    private String branchName;
    private Project project;
    private String projectDirectory;
    private String contentRepoPath;
    private File contentRepoFile;
    private File repoLocalData;
    private DownloadTask task;

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
        repoLocalData = new File(RepoLocalStorageDataProvider.getUserProjectDirectory() + LOCAL_STORAGE_FILE + REPO_LOCAL_STORAGE_FILE);
        getRepoUrl();
        task = DownloadTask.getInstance();
    }

    private void getRepoUrl() {
        TaskConfiguration taskConfiguration = TaskConfiguration.loadFrom(project);
        repoUrl = taskConfiguration.getHandoutURL();
        branchPath = taskConfiguration.getBranchPath();
        branchName = taskConfiguration.getBranchName();
    }

    public void updateHandoutData() {
        controlRetrievingContentData();
    }

    private void controlRetrievingContentData() {
        Boolean internetConnection = checkInternetConnection();
        Boolean repoContentDataExists = checkRepoContentDataExists();
        if (internetConnection && !repoContentDataExists) {
            cloneRepository();
        } else if (internetConnection && repoContentDataExists) {
            QuestionnaireController.getInstance().compareDates();
            updateBranch();
        } else if (repoContentDataExists) {
            QuestionnaireController.getInstance().compareDates();
            callListenerNotUpdating(ERROR_MESSAGE_UPDATING_NO_INTERNET, MessageType.ERROR);
        } else {
            callListenerNotUpdating(ERROR_MESSAGE_DOWNLOADING_NO_INTERNET, MessageType.ERROR);
        }
    }

    private boolean checkRepoContentDataExists() {
        //https://stackoverflow.com/a/15571626
        if (!contentRepoFile.exists()) {
            return false;
        } else {
            return true;
        }
    }

    //https://www.it-swarm.net/de/java/wie-pruefe-ich-ob-eine-internetverbindung-java-vorhanden-ist/967034905/
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

    private void cloneRepository() {
        //https://www.vogella.com/tutorials/JGit/article.html#example-for-using-jgit
        Runnable cloneTask = () -> {
            try {
                task.cloneRepository(repoUrl, contentRepoFile, branchPath);
            } catch (IOException e) {
                e.printStackTrace();
                cloneCanceledListener(ERROR_MESSAGE_DOWNLOADING);
            } finally {
                callListener(DOWNLOADING_DATA, NotificationType.INFORMATION);
            }
        };
        progressExecutor.runSynchronousProcess(cloneTask);
    }


    private void updateBranch() {
        progressExecutor = new ProgressExecutor();
        ArrayList<String> commitMessages = task.getLatestCommits(branchName);
        if (commitMessages.size() >= 1) {
            Runnable updateTask = () -> {
                try {
                    task.updateRepository();
                } catch (IOException | GitAPIException e) {
                    cloneCanceledListener(ERROR_MESSAGE_DOWNLOADING);
                    e.printStackTrace();
                }
            };
            progressExecutor.runSynchronousProcess(updateTask);
            callListener(commitMessages);
        }else{
            callListenerNotUpdating( DATA_IS_ACTUAL, MessageType.INFO);
        }
    }

    private void callListener(String message, NotificationType messageType) {
        if (onEventListener != null) {
            onEventListener.onCloningRepositoryEvent(message, messageType);
        }
    }

    private void callListener(ArrayList<String> lastCommitMessages) {
        if (onEventListener != null) {
            onEventListener.onUpdatingRepositoryEvent(lastCommitMessages);
        }
    }

    private void callListenerNotUpdating(String message, MessageType messageType) {
        if (onEventListener != null) {
            onEventListener.onNotUpdatingRepositoryEvent(message, messageType);
        }
    }

    private void cloneCanceledListener(String message) {
        if (onEventListener != null) {
            onEventListener.onCloneCanceledRepositoryEvent(message, MessageType.ERROR, contentRepoFile);
        }
    }
}
