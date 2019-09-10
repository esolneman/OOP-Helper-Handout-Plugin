package services.impl;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import services.HandoutDataProvider;
import com.intellij.openapi.project.Project;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class HandoutDataProviderImpl implements HandoutDataProvider {

    // TODO: get RepoURL from jar file
    String repoUrl = "https://github.com/esolneman/OOP-Helper-Handout-Template.git";
    String CLONE_DIRECTORY_PATH = "refs/heads/test";
    String CONTENT_FILE_NAME = "/RepoTEST";
    Project project;
    String projectDirectory;
    String contentRepoPath;


    public HandoutDataProviderImpl(Project project) {
        this.project = project;
        projectDirectory = project.getBasePath();
        //ModuleRootManager.getInstance(ModuleManager.getInstance(project).getModules()[0]).getContentRoots()[0]
        contentRepoPath = projectDirectory + CONTENT_FILE_NAME;
        System.out.println(contentRepoPath);

    }

    public void updateHandoutData() {
        //TODO check internet connection first
        //https://stackoverflow.com/a/15571626
        if (Files.exists(Paths.get(contentRepoPath))) {
            System.out.println("repo exist");
        } else {
            cloneRepository();
            //updateBranch();
        }
    }

    private void cloneRepository(){
        System.out.println("start cloning branch");
        //https://www.vogella.com/tutorials/JGit/article.html#example-for-using-jgit
        Git git;
        {
            try {
                git = Git.cloneRepository()
                        .setURI(repoUrl)
                        .setDirectory(new File(contentRepoPath))
                        .setBranchesToClone(Arrays.asList(CLONE_DIRECTORY_PATH))
                        .setBranch(CLONE_DIRECTORY_PATH)
                        .call();
            } catch (GitAPIException e) {
                //ToDo: write more into log
                System.out.print(e);
            }
        }
        File handoutFile = new File((contentRepoPath + "handout.md"));
        handoutFile.setReadOnly();
/*        File handoutFile = new File((contentRepoPath + "handout"));
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
