package services.impl;

import Listener.OnEventListener;
import com.intellij.openapi.project.Project;
import de.ur.mi.pluginhelper.tasks.TaskConfiguration;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import services.AsyncExecutor;
import services.HandoutDataProvider;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HandoutDataProviderImpl implements HandoutDataProvider{
    private List<OnEventListener> listeners = new ArrayList<OnEventListener>();
    private AsyncExecutor asyncExecutor = new AsyncExecutor();
    //private OnEventListener eventListener;

    // TODO: get RepoURL from jar file
    String repoUrl = "https://github.com/esolneman/OOP-Helper-Handout-Template.git";
    String CLONE_DIRECTORY_PATH = "refs/heads/test";
    String CONTENT_FILE_NAME = "/RepoTEST";
    Project project;
    String projectDirectory;
    String contentRepoPath;
    File contentRepoFile;


    public HandoutDataProviderImpl(Project project) {
        this.project = project;
        projectDirectory = project.getBasePath();
        contentRepoPath = projectDirectory + CONTENT_FILE_NAME;
        contentRepoFile = new File (contentRepoPath);
        getRepoUrl();
        System.out.println(contentRepoPath);
    }

    private void getRepoUrl() {
        System.out.println("getRepoUrl");
        System.out.println(TaskConfiguration.loadFrom());
        //repoUrl = TaskConfiguration.loadFrom().getHandoutURL();
    }

    public void updateHandoutData() {
        System.out.println("updateHandoutData");

        //TODO check internet connection first
        //https://stackoverflow.com/a/15571626
        if (!contentRepoFile.exists()) {
            cloneRepository();
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
        //Runnable cloningRunnable
        //asyncExecutor.runAsyncClone();
        Runnable cloneTask = () -> {
            Git clone = null;
            {
                try {
                    clone  = Git.cloneRepository()
                            .setURI(repoUrl)
                            .setDirectory(contentRepoFile)
                            .setBranchesToClone(Arrays.asList(CLONE_DIRECTORY_PATH))
                            .setBranch(CLONE_DIRECTORY_PATH)
                            .call();
                } catch (GitAPIException e) {
                    try {
                        FileUtils.deleteDirectory(contentRepoFile);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    System.out.print(e);
                    //throw e;
                } finally {
                    System.out.println("end cloning branch");
                    clone.close();
                    //check if listener is registered.
                    if (listeners != null) {
                        for(OnEventListener listener : listeners){
                            listener.onCloningRepositoryEvent(contentRepoFile);
                        }
                    }else{
                        System.out.println("event Listener null");
                    }
                }
            }
        };
/*        new Thread(() -> {
            //https://www.vogella.com/tutorials/JGit/article.html#example-for-using-jgit
            Git clone = null;
            {
                try {
                    clone  = Git.cloneRepository()
                            .setURI(repoUrl)
                            .setDirectory(contentRepoFile)
                            .setBranchesToClone(Arrays.asList(CLONE_DIRECTORY_PATH))
                            .setBranch(CLONE_DIRECTORY_PATH)
                            .call();
                } catch (GitAPIException e) {
                    try {
                        FileUtils.deleteDirectory(contentRepoFile);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    System.out.print(e);
                    //throw e;
                } finally {
                    System.out.println("end cloning branch");
                    clone.getRepository().close();
                    //check if listener is registered.
                    if (listeners != null) {
                        for(OnEventListener listener : listeners){
                            listener.onCloningRepositoryEvent(contentRepoFile);
                        }
                    }else{
                        System.out.println("event Listener null");
                    }
                }
            }
        };
        }).start();*/
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
