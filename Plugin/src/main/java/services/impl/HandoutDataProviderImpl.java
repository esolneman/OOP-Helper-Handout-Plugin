package services.impl;

import Listener.OnEventListener;
import com.intellij.util.Url;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.TextProgressMonitor;
import services.HandoutDataProvider;
import com.intellij.openapi.project.Project;
import org.eclipse.jgit.api.Git;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class HandoutDataProviderImpl implements HandoutDataProvider{
    private OnEventListener eventListener;

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
        //ModuleRootManager.getInstance(ModuleManager.getInstance(project).getModules()[0]).getContentRoots()[0]
        contentRepoPath = projectDirectory + CONTENT_FILE_NAME;
        contentRepoFile = new File (contentRepoPath);
        System.out.println(contentRepoPath);

    }

    /*public static void main(String[] args)
    {
        HandoutDataProviderImpl obj = new HandoutDataProviderImpl();
        OnEventListener eventListener = new HandoutDataProviderImpl();
        obj.registerOnEventListener(eventListener);
        obj.updateHandoutData();
    }*/

    // setting the listener
    public void registerOnEventListener(OnEventListener eventListener)
    {
        this.eventListener = eventListener;
    }

    public void updateHandoutData() {
        //TODO check internet connection first
        //https://stackoverflow.com/a/15571626
        if (!contentRepoFile.exists()) {
            cloneRepository();
        } else {
            System.out.println("repo exist");
            //updateBranch() check;
        }
    }

    private void cloneRepository() {
        System.out.println("start cloning branch");

        new Thread(new Runnable() {
            @Override
            public void run() {
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
                        //clone.close();
                        //check if listener is registered.
                        if (eventListener != null) {
                            // invoke the callback method of class A
                            eventListener.onCloningRepositoryEvent(contentRepoPath + "handout.md");
                        }else{
                            System.out.println("event Listener null");

                        }
                    }


                }
            }
        }).start();

    }

    private void hideRepo(){
        //File handoutFile = new File((contentRepoPath + "handout.md"));
        //handoutFile.setReadOnly();
        //File handoutFile = new File((contentRepoPath + "handout"));
        Path file = Paths.get(contentRepoPath + "handout.md");
        File handoutFile = file.toFile();
        System.out.print(handoutFile);
        /*Path file2 = Paths.get("handout.md");
        File handoutFile2 = file2.toFile();
        System.out.print(handoutFile2);*/
        //Path file3 = Paths.get("/RepoTEST/" + "handout.md");
        //File handoutFile3 = file3.toFile();
        handoutFile.setExecutable(false);
        handoutFile.setReadable(true);
        handoutFile.setWritable(false);

/*      File handoutFile = new File((contentRepoPath + "handout"));
        Path file = Paths.get(contentRepoPath + "handout.txt");

        try {
            Files.setAttribute(file, "dos:hidden", true);
        } catch (IOException e) {
            System.out.print(e);
        }*/
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
